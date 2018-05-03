package org.kethereum.wallet

import org.kethereum.crypto.ECKeyPair
import org.kethereum.crypto.PRIVATE_KEY_SIZE
import org.kethereum.crypto.SecureRandomUtils.secureRandom
import org.kethereum.crypto.getAddress
import org.kethereum.extensions.toBytesPadded
import org.kethereum.keccakshortcut.keccak
import org.kethereum.wallet.model.*
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

private const val R = 8
private const val DKLEN = 32

private const val CURRENT_VERSION = 3

private const val CIPHER = "aes-128-ctr"

class ScryptConfig(val n: Int, val p: Int)

val LIGHT_SCRYPT_CONFIG = ScryptConfig(1 shl 12, 6)
val STANDARD_SCRYPT_CONFIG = ScryptConfig(1 shl 18, 1)

fun ECKeyPair.createWallet(password: String, config: ScryptConfig): Wallet {

    val mySalt = generateRandomBytes(32)

    val derivedKey = generateDerivedScryptKey(password.toByteArray(UTF_8), ScryptKdfParams(n = config.n, r = R, p = config.p).apply {
        dklen = DKLEN
        salt = mySalt.toNoPrefixHexString()
    })

    val encryptKey = Arrays.copyOfRange(derivedKey, 0, 16)
    val iv = generateRandomBytes(16)

    val privateKeyBytes = privateKey.toBytesPadded(PRIVATE_KEY_SIZE)

    val cipherText = performCipherOperation(Cipher.ENCRYPT_MODE, iv, encryptKey, privateKeyBytes)

    val mac = generateMac(derivedKey, cipherText)

    return createWallet(this, cipherText, iv, mac, ScryptKdfParams(n = config.n,
            p = config.p,
            r = R,
            dklen = DKLEN,
            salt = mySalt.toNoPrefixHexString()))
}

private fun createWallet(ecKeyPair: ECKeyPair,
                         cipherText: ByteArray,
                         iv: ByteArray, mac: ByteArray, scryptKdfParams: ScryptKdfParams) = Wallet(
        address = ecKeyPair.getAddress(),

        crypto = WalletCrypto(
                cipher = CIPHER,
                ciphertext = cipherText.toNoPrefixHexString(),
                kdf = SCRYPT,
                kdfparams = scryptKdfParams,
                cipherparams = CipherParams(iv.toNoPrefixHexString()),

                mac = mac.toNoPrefixHexString()
        ),
        id = UUID.randomUUID().toString(),
        version = CURRENT_VERSION
)

private fun generateDerivedScryptKey(password: ByteArray, kdfParams: ScryptKdfParams) = SCrypt.generate(password, kdfParams.salt?.hexToByteArray(), kdfParams.n, kdfParams.r, kdfParams.p, kdfParams.dklen)

@Throws(CipherException::class)
private fun generateAes128CtrDerivedKey(password: ByteArray, kdfParams: Aes128CtrKdfParams): ByteArray {

    if (kdfParams.prf != "hmac-sha256") {
        throw CipherException("Unsupported prf:${kdfParams.prf}")
    }

    // Java 8 supports this, but you have to convert the password to a character array, see
    // http://stackoverflow.com/a/27928435/3211687

    val gen = PKCS5S2ParametersGenerator(SHA256Digest())
    gen.init(password, kdfParams.salt?.hexToByteArray(), kdfParams.c)
    return (gen.generateDerivedParameters(256) as KeyParameter).key
}

@Throws(CipherException::class)
private fun performCipherOperation(mode: Int, iv: ByteArray, encryptKey: ByteArray, text: ByteArray) = try {
    val ivParameterSpec = IvParameterSpec(iv)
    val cipher = Cipher.getInstance("AES/CTR/NoPadding")

    val secretKeySpec = SecretKeySpec(encryptKey, "AES")
    cipher.init(mode, secretKeySpec, ivParameterSpec)
    cipher.doFinal(text)
} catch (e: Exception) {
    throw CipherException("Error performing cipher operation", e)
}


private fun generateMac(derivedKey: ByteArray, cipherText: ByteArray): ByteArray {
    val result = ByteArray(16 + cipherText.size)

    System.arraycopy(derivedKey, 16, result, 0, 16)
    System.arraycopy(cipherText, 0, result, 16, cipherText.size)

    return result.keccak()
}

@Throws(CipherException::class)
fun Wallet.decrypt(password: String): ECKeyPair {

    validate()

    val mac = crypto.mac.hexToByteArray()
    val iv = crypto.cipherparams.iv.hexToByteArray()
    val cipherText = crypto.ciphertext.hexToByteArray()

    val kdfparams = crypto.kdfparams
    val derivedKey = when (kdfparams) {
        is ScryptKdfParams -> generateDerivedScryptKey(password.toByteArray(UTF_8), kdfparams)
        is Aes128CtrKdfParams -> generateAes128CtrDerivedKey(password.toByteArray(UTF_8), kdfparams)
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
fun Wallet.validate() {
    when {
        version != CURRENT_VERSION
        -> throw CipherException("Wallet version is not supported")

        crypto.cipher != CIPHER
        -> throw CipherException("Wallet cipher is not supported")

        crypto.kdf != AES_128_CTR && crypto.kdf != SCRYPT
        -> throw CipherException("KDF type is not supported")
    }
}


fun generateRandomBytes(size: Int) = ByteArray(size).apply {
    secureRandom().nextBytes(this)
}


fun WalletCryptoForImport.toTypedKdfParamsWalletCrypto() = WalletCrypto(cipher = cipher,
        ciphertext = ciphertext,
        cipherparams = cipherparams,
        kdf = kdf,
        kdfparams = getTypedKdfParams(),
        mac = mac)


fun WalletForImport.getCrypto() = crypto ?: cryptoFromMEW

fun WalletForImport.toTypedWallet() = Wallet(
        address = address,
        crypto = getCrypto()?.toTypedKdfParamsWalletCrypto()!!,
        id = id!!,
        version = version)