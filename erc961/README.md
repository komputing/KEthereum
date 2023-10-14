This module provides and implements methods for working with [EIP-961](https://github.com/ethereum/EIPs/pull/961)

### Installation
[![](https://jitpack.io/v/komputing/kethereum.svg)](https://jitpack.io/#komputing/kethereum)
```groovy
implementation("com.github.komputing.kethereum:model:$KETHEREUM_VERSION")
implementation("com.github.komputing.kethereum:erc961:$KETHEREUM_VERSION")
```

### Usage
#### Generating EIP-961 token URL
```kotlin
val address = Address(0x00AB42)
val chainId = ChainId(4)
val erc20Token = Token("TST", address, chainId)
val eip651Url: String = erc20Token.generateURL()

println(eip651Url) // ethereum:token_info-0x00AB42@4?symbol=TST
```

#### Working with EIP-961 token URL 
```kotlin
val exampleUrl = "ethereum:token_info-0x00AB42@4?symbol=TST"

println(isEthereumTokenURI(exampleUri)) // true
val tokenFromUrl: Token = parseTokenFromEthereumURI(exampleUrl) 
println(tokenFromUrl) // Token(symbol="TST", address=0x00AB42, chainId=4, decimals=18)

// also there are extension methods for EthereumURI class
val uri = EthereumURI(exampleUrl)
println(uri.isTokenURI()) // true
println(uri.parseToken()) // Token(symbol="TST", address=0x00AB42, chainId=4, decimals=18)
```
