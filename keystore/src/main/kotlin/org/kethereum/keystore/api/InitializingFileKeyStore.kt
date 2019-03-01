package org.kethereum.keystore.api

import java.io.File

class InitializingFileKeyStore(dir: File) : FileKeyStore(dir) {
    init {
        dir.mkdirs()
    }
}
