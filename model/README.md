This module contains classes that are used for working with KEthereum

### Installation
[![](https://jitpack.io/v/komputing/kethereum.svg)](https://jitpack.io/#komputing/kethereum)
```groovy
implementation("com.github.komputing.kethereum:model:$KETHEREUM_VERSION")
```


### Usage
#### ECKeyPair
You can use hex string and `HexString` value class from [KHex](https://github.com/komputing/KHex) to create private and public keys:
```kotlin
val privateKey = PrivateKey(HexString("a3926...."))
val publicKey = PublicKey(HexString("0x506...."))

// Or use other constructors and pass ByteArray
val privateKey = PrivateKey(byteArrayOf(0x2E, 0x38, ...))
val publicKey = PrivateKey(byteArrayOf(0x1E, 0x44, ...))
        
// or BigInteger
val privateKey = PrivateKey(BigInteger.valueOf(1234))
val publicKey = PrivateKey(BigInteger.valueOf(5678))
```

And to create an `ECKeyPair` you can call its constructor directly like this
or use extension method on `PrivateKey`:
```kotlin
val keyPair: ECKeyPair = ECKeyPair(privateKey, publicKey)
// or
val keyPair: ECKeyPair = privateKey.toECKeyPair()

// You can use destructuring on ECKeyPair to get keys
val (privateKey, publicKey) = keyPair
```
To generate randomly new `ECKeyPair` you can use
```kotlin
val newKeyPair: ECKeyPair = createEthereumKeyPair()
```

Several modules add useful extensions that you can use to reduce boilerplate
```kotlin
// :crypto module
val address: Address = keyPair.toAddress()
val credentials: Credentials = keyPair.toCredentials()
val compressedPubKey: ByteArray = keyPair.getCompressedPublicKey()
val signedMessage: SignatureData = keyPair.signMessage("hello world".toByteArray()) 

// :eip191 module
val eip191Signature: SignatureData = keyPair.signWithEIP191PersonalSign("hello world".toByteArray())
```
