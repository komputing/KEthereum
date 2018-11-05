package org.kethereum.cryptoapi.kdf

fun pbkdf2(): PBKDF2 = PBKDF2Impl

fun scrypt(): SCrypt = SCryptImpl
