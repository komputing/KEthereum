package org.kethereum.crypto.api.kdf

fun pbkdf2(): PBKDF2 = PBKDF2Impl

fun scrypt(): SCrypt = SCryptImpl
