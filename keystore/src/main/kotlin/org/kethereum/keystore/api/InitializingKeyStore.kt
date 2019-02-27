package org.kethereum.keystore.api

import java.io.File

class InitializingKeyStore(dir: File) : FileKeyStore(dir) {
    init {
        dir.mkdirs()
    }
}
