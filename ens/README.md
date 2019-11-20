What is it?
===========

This module provides support for the [Ethereum Name Service (ENS)](https://ens.domains). For this it uses several other ENS-related KEthereum modules modules (e.g. [EIP137](../eip137) to generate the NameHash or [ERC634](../erc634) to access text records).

How to use it?
==============

The only mandatory constructor parameter is the rpc that is used to connect to the Ethereum chain. By default the contract at 0x314159265dd8dbb310642f98f50c066173c1259b on them main chain will be used. So make sure your RPC is on the same chain.

```kotlin
val ens = ENS(rpc)
```

If you want to use another contract you can pass it in like this:

```kotlin
val ens = ENS(rpc ,Address("0x112234455c3a32fd11230c42e7bccd4a84e02010"))
```

to use the contract deployed on görli.

You can then resolve data. E.g. to get the Ethereum address associated with some ens name you can do:

```kotlin
val address = ens.getAddress(ENSName("vitalik.eth"))
```

Be aware that ens domains do not need to end in ".eth" - the following will also resolve:

```kotlin
val address = ens.getAddress(ENSName("kevins.xyz"))
```

If you want to find out if an ens name is a potential ENS domain (e.g. to prevent hitting your RPC with futile requests) you can use the following extension function:

```kotlin
val willBeTrue = ENSName("test.eth").isPotentialENSDomain()
val willBeFalse = ENSName("test.yo").isPotentialENSDomain()
```

to get the github username for a name you can do:

```kotlin
val address = ens.getGithubUserName(ENSName("ligi.ethereum.eth"))
```
