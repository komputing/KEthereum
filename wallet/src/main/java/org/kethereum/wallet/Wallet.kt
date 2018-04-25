package org.kethereum.wallet

import org.kethereum.crypto.ECKeyPair
import org.kethereum.crypto.Keys
import org.kethereum.crypto.SecureRandomUtils.secureRandom
import org.kethereum.extensions.toBytesPadded
import org.kethereum.keccakshortcut.keccak
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.spongycastle.crypto.generators.SCrypt
import org.spongycastle.crypto.params.KeyParameter
import org.walleth.khex.hexToByteArray
import org.walleth.khex.toNoPrefixHexString
import java.nio.charset.Charset
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private val UTF_8 = Charset.forName("UTF-8")

private const val N_LIGHT = 1 shl 12
private const val P_LIGHT = 6

private const val N_STANDARD = 1 shl 18
private const val P_STANDARD = 1

private const val R = 8
private const val DKLEN = 32

private const val CURRENT_VERSION = 3

private const val CIPHER = "aes-128-ctr"

fun ECKeyPair.createWalletFile(password: String, n: Int, p: Int): WalletFile {

    val salt = generateRandomBytes(32)

    val derivedKey = generateDerivedScryptKey(
            password.toByteArray(UTF_8), salt, n, R, p, DKLEN)

    val encryptKey = Arrays.copyOfRange(derivedKey, 0, 16)
    val iv = generateRandomBytes(16)

    val privateKeyBytes = privateKey.toBytesPadded(Keys.PRIVATE_KEY_SIZE)

    val cipherText = performCipherOperation(
            Cipher.ENCRYPT_MODE, iv, encryptKey, privateKeyBytes)

    val mac = generateMac(derivedKey, cipherText)

    return createWalletFile(this, cipherText, iv, salt, mac, n, p)
}

@Throws(CipherException::class)
fun ECKeyPair.createStandard(password: String) = createWalletFile(password, N_STANDARD, P_STANDARD)

@Throws(CipherException::class)
fun ECKeyPair.createLight(password: String) = createWalletFile(password, N_LIGHT, P_LIGHT)

private fun createWalletFile(
        ecKeyPair: ECKeyPair, cipherText: ByteArray, iv: ByteArray, salt: ByteArray, mac: ByteArray,
        n: Int, p: Int): WalletFile {

    val walletFile = WalletFile()
    walletFile.address = Keys.getAddress(ecKeyPair)

    val crypto = WalletFile.Crypto()
    crypto.cipher = CIPHER
    crypto.ciphertext = cipherText.toNoPrefixHexString()
    walletFile.crypto = crypto

    val cipherParams = WalletFile.CipherParams()
    cipherParams.iv = iv.toNoPrefixHexString()
    crypto.cipherparams = cipherParams

    crypto.kdf = WalletFile.SCRYPT
    val kdfParams = WalletFile.ScryptKdfParams()
    kdfParams.dklen = DKLEN
    kdfParams.n = n
    kdfParams.p = p
    kdfParams.r = R
    kdfParams.salt = salt.toNoPrefixHexString()
    crypto.kdfparams = kdfParams

    crypto.mac = mac.toNoPrefixHexString()
    walletFile.crypto = crypto
    walletFile.id = UUID.randomUUID().toString()
    walletFile.version = CURRENT_VERSION

    return walletFile
}

private fun generateDerivedScryptKey(password: ByteArray, salt: ByteArray, n: Int, r: Int, p: Int, dkLen: Int) = SCrypt.generate(password, salt, n, r, p, dkLen)

@Throws(CipherException::class)
private fun generateAes128CtrDerivedKey(password: ByteArray, salt: ByteArray, c: Int, prf: String): ByteArray {

    if (prf != "hmac-sha256") {
        throw CipherException("Unsupported prf:$prf")
    }

    // Java 8 supports this, but you have to convert the password to a character array, see
    // http://stackoverflow.com/a/27928435/3211687

    val gen = PKCS5S2ParametersGenerator(SHA256Digest())
    gen.init(password, salt, c)
    return (gen.generateDerivedParameters(256) as KeyParameter).key
}

@Throws(CipherException::class)
private fun performCipherOperation(mode: Int, iv: ByteArray, encryptKey: ByteArray, text: ByteArray): ByteArray {

    try {
        val ivParameterSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/CTR/NoPadding")

        val secretKeySpec = SecretKeySpec(encryptKey, "AES")
        cipher.init(mode, secretKeySpec, ivParameterSpec)
        return cipher.doFinal(text)
    } catch (e: Exception) {
        throw CipherException("Error performing cipher operation", e)
    }
}

private fun generateMac(derivedKey: ByteArray, cipherText: ByteArray): ByteArray {
    val result = ByteArray(16 + cipherText.size)

    System.arraycopy(derivedKey, 16, result, 0, 16)
    System.arraycopy(cipherText, 0, result, 16, cipherText.size)

    return result.keccak()
}

@Throws(CipherException::class)
fun WalletFile.decrypt(password: String): ECKeyPair {

    validate()

    val crypto = crypto!!

    val mac = crypto.mac!!.hexToByteArray()
    val iv = crypto.cipherparams!!.iv!!.hexToByteArray()
    val cipherText = crypto.ciphertext!!.hexToByteArray()

    val derivedKey: ByteArray

    val kdfParams = crypto.kdfparams!!
    when (kdfParams) {
        is WalletFile.ScryptKdfParams -> {
            val dklen = kdfParams.dklen
            val n = kdfParams.n
            val p = kdfParams.p
            val r = kdfParams.r
            val salt = kdfParams.salt!!.hexToByteArray()
            derivedKey = generateDerivedScryptKey(password.toByteArray(UTF_8), salt, n, r, p, dklen)
        }
        is WalletFile.Aes128CtrKdfParams -> {
            val c = kdfParams.c
            val prf = kdfParams.prf!!
            val salt = kdfParams.salt!!.hexToByteArray()

            derivedKey = generateAes128CtrDerivedKey(password.toByteArray(UTF_8), salt, c, prf)
        }
        else -> throw CipherException("Unable to deserialize params: " + crypto.kdf)
    }

    val derivedMac = generateMac(derivedKey, cipherText)

    if (!Arrays.equals(derivedMac, mac)) {
        throw CipherException("Invalid password provided")
    }

    val encryptKey = Arrays.copyOfRange(derivedKey, 0, 16)
    val privateKey = performCipherOperation(Cipher.DECRYPT_MODE, iv, encryptKey, cipherText)
    return ECKeyPair.create(privateKey)
}

@Throws(CipherException::class)
fun WalletFile.validate() {
    when {
        version != CURRENT_VERSION
        -> throw CipherException("Wallet version is not supported")

        !crypto!!.cipher.equals(CIPHER)
        -> throw CipherException("Wallet cipher is not supported")

        !crypto!!.kdf.equals(WalletFile.AES_128_CTR) && !crypto!!.kdf.equals(WalletFile.SCRYPT)
        -> throw CipherException("KDF type is not supported")
    }
}


fun generateRandomBytes(size: Int) = ByteArray(size).apply {
    secureRandom().nextBytes(this)
}