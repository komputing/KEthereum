package org.kethereum.bip39

import org.kethereum.bip32.BIP32
import org.kethereum.bip32.ExtendedKey
import org.kethereum.hashes.sha256
import org.spongycastle.jce.provider.BouncyCastleProvider
import java.security.SecureRandom
import java.security.Security
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.experimental.or

object Mnemonic {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    /**
     * Generates a seed buffer from a mnemonic phrase according to the BIP39 spec.
     * The mnemonic phrase is given as a list of words and the seed can be salted using a password
     */
    fun mnemonicToSeed(phrase: String, password: String = ""): ByteArray {
        return mnemonicToSeed(phrase.split(" ").toTypedArray(), password)
    }

    /**
     * Generates a seed buffer from a mnemonic phrase according to the BIP39 spec.
     * The mnemonic phrase is given as a list of words and the seed can be salted using a password
     */
    fun mnemonicToSeed(words: Array<String>, password: String = ""): ByteArray {
        val pass = words.joinToString(" ")
        val salt = "mnemonic" + password

        val keyFactory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA512")
        val spec = PBEKeySpec(pass.toCharArray(), salt.toByteArray(), 2048, 512)
        val seed = keyFactory.generateSecret(spec).encoded
        return seed
    }

    /**
     * Converts a phrase (list of words) into a [ByteArray] entropy buffer according to the BIP39 spec
     */
    fun mnemonicToEntropy(phrase: String): ByteArray {
        return mnemonicToEntropy(phrase.split(" ").toTypedArray())
    }

    /**
     * Converts a list of words into a [ByteArray] entropy buffer according to the BIP39 spec
     */
    fun mnemonicToEntropy(words: Array<String>): ByteArray {
        if (words.size % 3 > 0)
            throw RuntimeException("Word list size must be multiple of three words.")

        if (words.isEmpty())
            throw RuntimeException("Word list is empty.")

        val numTotalBits = words.size * 11
        val bitArray = BooleanArray(numTotalBits)

        for ((phraseIndex, word) in words.withIndex()) {

            val dictIndex = Collections.binarySearch(ENGLISH, word)
            if (dictIndex < 0)
                throw RuntimeException("word($word) not in known word list")

            // Set the next 11 bits to the value of the index.
            for (bit in 0..10)
                bitArray[phraseIndex * 11 + bit] = dictIndex and (1 shl (10 - bit)) != 0
        }

        val numChecksumBits = numTotalBits / 33
        val numEntropyBits = numTotalBits - numChecksumBits

        val entropy = bitsToBytes(bitArray, numEntropyBits / 8 )

        // Take the digest of the entropy.
        val hash = entropy.sha256()
        val hashBits = bytesToBits(hash)

        // Check all the checksum bits.
        for (i in 0 until numChecksumBits)
            if (bitArray[numEntropyBits + i] != hashBits[i])
                throw RuntimeException("mnemonic checksum does not match")

        return entropy
    }

    fun mnemonicToKey(phrase : String, path : String, saltPhrase: String = "") : ExtendedKey {
        val generatedSeed = Mnemonic.mnemonicToSeed(phrase, saltPhrase)
        return BIP32.generateKey(generatedSeed, path)
    }

    private fun bytesToBits(data: ByteArray): BooleanArray {
        val bits = BooleanArray(data.size * 8)
        for (byteIndex in data.indices)
            for (bitIndex in 0..7) {
                bits[byteIndex * 8 + bitIndex] = (1 shl (7 - bitIndex)) and data[byteIndex].toInt() != 0
            }
        return bits
    }

    private fun bitsToBytes(bits: BooleanArray, len: Int = bits.size / 8): ByteArray {
        val result = ByteArray(len)
        for (byteIndex in result.indices)
            for (bitIndex in 0..7)
                if (bits[byteIndex * 8 + bitIndex]) {
                    result[byteIndex] = result[byteIndex] or (1 shl (7 - bitIndex)).toByte()
                }
        return result
    }

    /**
     * Converts an entropy buffer to a list of words according to the BIP39 spec
     */
    fun entropyToMnemonic(entropy: ByteArray): String {
        if (entropy.size % 4 > 0)
            throw RuntimeException("Entropy not multiple of 32 bits.")

        if (entropy.isEmpty())
            throw RuntimeException("Entropy is empty.")

        val hash = entropy.sha256()
        val hashBits = bytesToBits(hash)

        val entropyBits = bytesToBits(entropy)
        val checksumLengthBits = entropyBits.size / 32

        val concatBits = BooleanArray(entropyBits.size + checksumLengthBits)
        System.arraycopy(entropyBits, 0, concatBits, 0, entropyBits.size)
        System.arraycopy(hashBits, 0, concatBits, entropyBits.size, checksumLengthBits)


        val words = ArrayList<String>().toMutableList()
        val numWords = concatBits.size / 11
        for (i in 0 until numWords) {
            var index = 0
            for (j in 0..10) {
                index = index shl 1
                if (concatBits[i * 11 + j])
                    index = index or 0x01
            }
            words.add(ENGLISH[index])
        }

        return words.joinToString(" ")
    }

    fun generateMnemonic(strength: Int = 128): String {

        val entropyBuffer = ByteArray(strength / 8)
        SecureRandom().nextBytes(entropyBuffer)

        return Mnemonic.entropyToMnemonic(entropyBuffer)
    }

    /**
     * Checks if a list of words is a valid encoding according to the BIP39 spec
     */
    fun validateMnemonic(words: Array<String>): Boolean {
        return try {
            mnemonicToEntropy(words)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun validateMnemonic(phrase: String): Boolean {
        val words = phrase.split(" ").toTypedArray()
        return validateMnemonic(words)
    }

}