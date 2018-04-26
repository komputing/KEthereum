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
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object Wallet {
    private val UTF_8 = Charset.forName("UTF-8")

    private const val N_LIGHT = 1 shl 12
    private const val P_LIGHT = 6

    private const val N_STANDARD = 1 shl 18
    private const val P_STANDARD = 1

    private const val R = 8
    private const val DKLEN = 32

    private const val CURRENT_VERSION = 3

    private const val CIPHER = "aes-128-ctr"

    @Throws(CipherException::class)
    fun create(password: String, ecKeyPair: ECKeyPair, n: Int, p: Int): WalletFile {

        val salt = generateRandomBytes(32)

        val derivedKey = generateDerivedScryptKey(
                password.toByteArray(UTF_8), salt, n, R, p, DKLEN)

        val encryptKey = Arrays.copyOfRange(derivedKey, 0, 16)
        val iv = generateRandomBytes(16)

        val privateKeyBytes = ecKeyPair.privateKey.toBytesPadded(Keys.PRIVATE_KEY_SIZE)

        val cipherText = performCipherOperation(
                Cipher.ENCRYPT_MODE, iv, encryptKey, privateKeyBytes)

        val mac = generateMac(derivedKey, cipherText)

        return createWalletFile(ecKeyPair, cipherText, iv, salt, mac, n, p)
    }

    @Throws(CipherException::class)
    fun createStandard(password: String, ecKeyPair: ECKeyPair): WalletFile {
        return create(password, ecKeyPair, N_STANDARD, P_STANDARD)
    }

    @Throws(CipherException::class)
    fun createLight(password: String, ecKeyPair: ECKeyPair): WalletFile {
        return create(password, ecKeyPair, N_LIGHT, P_LIGHT)
    }

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

    @Throws(CipherException::class)
    private fun generateDerivedScryptKey(
            password: ByteArray, salt: ByteArray, n: Int, r: Int, p: Int, dkLen: Int): ByteArray {
        return SCrypt.generate(password, salt, n, r, p, dkLen)
    }

    @Throws(CipherException::class)
    private fun generateAes128CtrDerivedKey(
            password: ByteArray, salt: ByteArray, c: Int, prf: String): ByteArray {

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
    private fun performCipherOperation(
            mode: Int, iv: ByteArray, encryptKey: ByteArray, text: ByteArray): ByteArray {

        try {
            val ivParameterSpec = IvParameterSpec(iv)
            val cipher = Cipher.getInstance("AES/CTR/NoPadding")

            val secretKeySpec = SecretKeySpec(encryptKey, "AES")
            cipher.init(mode, secretKeySpec, ivParameterSpec)
            return cipher.doFinal(text)
        } catch (e: NoSuchPaddingException) {
            throw CipherException("Error performing cipher operation", e)
        } catch (e: NoSuchAlgorithmException) {
            throw CipherException("Error performing cipher operation", e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw CipherException("Error performing cipher operation", e)
        } catch (e: InvalidKeyException) {
            throw CipherException("Error performing cipher operation", e)
        } catch (e: BadPaddingException) {
            throw CipherException("Error performing cipher operation", e)
        } catch (e: IllegalBlockSizeException) {
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
    fun decrypt(password: String, walletFile: WalletFile): ECKeyPair {

        validate(walletFile)

        val crypto = walletFile.crypto!!

        val mac = crypto.mac!!.hexToByteArray()
        val iv = crypto.cipherparams!!.iv!!.hexToByteArray()
        val cipherText = crypto.ciphertext!!.hexToByteArray()

        val derivedKey: ByteArray

        val kdfParams = crypto.kdfparams!!
        if (kdfParams is WalletFile.ScryptKdfParams) {
            val dklen = kdfParams.dklen
            val n = kdfParams.n
            val p = kdfParams.p
            val r = kdfParams.r
            val salt = kdfParams.salt!!.hexToByteArray()
            derivedKey = generateDerivedScryptKey(password.toByteArray(UTF_8), salt, n, r, p, dklen)
        } else if (kdfParams is WalletFile.Aes128CtrKdfParams) {
            val c = kdfParams.c
            val prf = kdfParams.prf!!
            val salt = kdfParams.salt!!.hexToByteArray()

            derivedKey = generateAes128CtrDerivedKey(password.toByteArray(UTF_8), salt, c, prf)
        } else {
            throw CipherException("Unable to deserialize params: " + crypto.kdf)
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
    fun validate(walletFile: WalletFile) {
        val crypto = walletFile.crypto!!

        if (walletFile.version != CURRENT_VERSION) {
            throw CipherException("Wallet version is not supported")
        }

        if (!crypto.cipher.equals(CIPHER)) {
            throw CipherException("Wallet cipher is not supported")
        }

        if (!crypto.kdf.equals(WalletFile.AES_128_CTR) && !crypto.kdf.equals(WalletFile.SCRYPT)) {
            throw CipherException("KDF type is not supported")
        }
    }

    fun generateRandomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        secureRandom().nextBytes(bytes)
        return bytes
    }
}