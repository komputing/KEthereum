# Summary

[ERC-55](https://eips.ethereum.org/EIPS/eip-55): Mixed-case checksum address encoding

# Usage

```kotlin
Address("0xfb6916095ca1df60bb79ce92ce3ea74c37c5d359").withERC55Checksum()
```

-> 0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359

```kotlin
Address("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359").hasValidERC55Checksum()
```

-> true

```kotlin
Address("0xFB6916095ca1df60bB79Ce92cE3Ea74c37c5d359").hasValidERC55Checksum()
```

-> false

```kotlin
Address("0xfb6916095ca1df60bb79ce92ce3ea74c37c5d359").hasValidERC55Checksum()
```

-> false

```kotlin
Address("0xfb6916095ca1df60bb79ce92ce3ea74c37c5d359").hasValidERC55ChecksumOrNoChecksum()
```

-> true

```kotlin
Address("0xFb6916095ca1df60bb79ce92ce3ea74c37c5d359").hasValidERC55ChecksumOrNoChecksum()
```

-> false

# Implementation

```kotlin

package org.kethereum.erc55

import org.kethereum.functions.isValid
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.walleth.khex.toNoPrefixHexString

fun Address.withERC55Checksum() = cleanHex.toLowerCase().toByteArray().keccak().toNoPrefixHexString().let { hexHash ->
    Address(cleanHex.mapIndexed { index, hexChar ->
        when {
            hexChar in '0'..'9' -> hexChar
            hexHash[index] in '0'..'7' -> hexChar.toLowerCase()
            else -> hexChar.toUpperCase()
        }
    }.joinToString(""))
}

private fun Address.hasValidERC55ChecksumAssumingValidAddress() = withERC55Checksum().hex == hex

fun Address.hasValidERC55Checksum() = isValid() && hasValidERC55ChecksumAssumingValidAddress()
fun Address.hasValidERC55ChecksumOrNoChecksum() = isValid() &&
        (hasValidERC55ChecksumAssumingValidAddress() ||
                cleanHex.toLowerCase() == cleanHex ||
                cleanHex.toUpperCase() == cleanHex)
```
