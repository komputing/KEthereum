package org.kethereum.model.random

import java.util.UUID

actual object UUID {
    actual fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
}