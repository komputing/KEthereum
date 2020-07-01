package org.kethereum.keystore.api

import org.kethereum.model.Address
import org.kethereum.model.ECKeyPair
import org.kethereum.wallet.LIGHT_SCRYPT_CONFIG
import org.kethereum.wallet.STANDARD_SCRYPT_CONFIG
import org.kethereum.wallet.generateWalletFile
import org.kethereum.wallet.loadKeysFromWalletFile
import org.kethereum.wallet.model.ScryptConfig
import java.io.File


open class FileKeyStore(private val dir: File) : KeyStore {

    private val fileMap by lazy {
        mutableMapOf<Address, File>().apply {
            rebuildList()
        }
    }

    private fun MutableMap<Address, File>.rebuildList() {
        dir.listFiles()?.filter { !it.isDirectory }?.forEach {
            put(Address(it.name.split("--").last().removeSuffix(".json")), it)
        }
    }

    override fun getAddresses() = fileMap.keys

    override fun hasKeyForForAddress(address: Address) = fileMap.containsKey(address)

    override fun getKeyForAddress(address: Address, password: String) =
            fileMap[address]?.loadKeysFromWalletFile(password)

    override fun deleteKey(address: Address) = (fileMap[address]?.delete() ?: false).also {
        fileMap.remove(address)
    }

    private fun addKey(key: ECKeyPair, password: String, scryptConfig: ScryptConfig) =
            key.generateWalletFile(password, dir, scryptConfig).wallet.address?.let {
                fileMap.rebuildList()
                Address(it)
            }

    override fun addKey(key: ECKeyPair, password: String, light: Boolean) =
            addKey(key, password, if (light) LIGHT_SCRYPT_CONFIG else STANDARD_SCRYPT_CONFIG)

}
