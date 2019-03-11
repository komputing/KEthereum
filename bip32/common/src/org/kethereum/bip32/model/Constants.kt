package org.kethereum.bip32.model

import org.kethereum.model.PRIVATE_KEY_SIZE


internal val BITCOIN_SEED = "Bitcoin seed".toByteArray()
internal const val CHAINCODE_SIZE = PRIVATE_KEY_SIZE
internal const val COMPRESSED_PUBLIC_KEY_SIZE = PRIVATE_KEY_SIZE + 1
internal const val EXTENDED_KEY_SIZE: Int = 78
internal val xprv = byteArrayOf(0x04, 0x88.toByte(), 0xAD.toByte(), 0xE4.toByte())
internal val xpub = byteArrayOf(0x04, 0x88.toByte(), 0xB2.toByte(), 0x1E.toByte())
