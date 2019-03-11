package org.kethereum.crypto.impl.cipher

import org.kethereum.crypto.api.cipher.AESCipher
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESCipherImpl: AESCipher {

    private lateinit var cipher: Cipher

    override fun init(mode: AESCipher.Mode, padding: AESCipher.Padding, operation: AESCipher.Operation, key: ByteArray, iv: ByteArray): AESCipher {
        cipher = Cipher.getInstance("AES/${mode.id}/${padding.id}")
        val ivParameterSpec = IvParameterSpec(iv)

        val secretKeySpec = SecretKeySpec(key, "AES")
        cipher.init(operation.toInt(), secretKeySpec, ivParameterSpec)
        return this
    }

    override fun performOperation(data: ByteArray) =
        cipher.doFinal(data)!!

    private fun AESCipher.Operation.toInt() = when(this) {
        AESCipher.Operation.ENCRYPTION -> Cipher.ENCRYPT_MODE
        AESCipher.Operation.DESCRYPTION -> Cipher.DECRYPT_MODE
    }

}
