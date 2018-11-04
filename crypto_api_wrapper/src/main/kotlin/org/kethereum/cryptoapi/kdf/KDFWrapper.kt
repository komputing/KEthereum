package org.kethereum.cryptoapi.kdf

fun pkbkdf2(): PBKDF2 = PBKDF2Impl

fun scrypt(): SCrypt = SCryptImpl
