![](assets/logo_smaller.png)

Mission statement
=================

This is a [Kotlin](https://kotlinlang.org) library for [Ethereum](https://ethereum.org). It is striving towards being 100% Kotlin (the code in the lib is Kotlin anyway - but also trying to not drag in JVM dependencies). This is done in order to enable multi-platform support in the future. Currently this library is mainly used in JVM projects but could this way also target e.g. JavaScript and WebAssembly that are broadly used in the web3 space.
Another core principle of this library is to be as modular as possible. Ethereum has a wide range of use-cases and should not be supported by huge monolithic libraries. With KEthereum you can pick and choose the modules you need and keep the footprint of the library small this way. 

Module overview
===============

| Name                      | Description                                              | Status         | Documentation |
| ------------------------- | -------------------------------------------------------- | -------------- | ------------- |
| abi                       | Application Binary Interface (ABI) for contracts         | beta           |               |
| abi_codegen               | Kotlin code generation from ABIs                         | beta           |               |
| abi_filter                | Functions for filtering ABIs                             | beta           |               |
| blockscout                | BlockScout BlockExplorer helper functions                | beta           |               |
| bloomfilter               | Space-efficient probabilistic data structure             | beta           |               |
| eip137                    | Model and functions for ENS NameHash                     | beta           |               |
| eip155                    | TX signing with Simple replay attack protection          | production     | [example code](https://github.com/goerli/goerli_pusher/blob/master/src/main/kotlin/net/goerli/pusher/Main.kt)                         |
| eip191                    | Functions to sign data via signed data standard          | beta           |               |
| eip712                    | Ethereum typed structured data hashing and signing       | experimental   |               |
| ens                       | ENS (Ethereum Name Service) functionality                | beta           | [README](https://github.com/komputing/KEthereum/blob/master/ens/README.md)                                                      |
| erc1328                   | WalletConnect Standard URI Format                        | beta           |               |
| erc1450                   | ERC-20 extension - e.g. including token minting          | beta           | [example code](https://github.com/goerli/goerli_pusher/blob/master/src/main/kotlin/net/goerli/pusher/Main.kt)                         |
| erc181                    | ENS reverse resolution of addresses                      | beta           |               |
| erc20                     | Contract wrapper for the ERC20 Token standard            | beta           |               |
| erc55                     | Mixed-case checksum address encoding                     | production     | [README](https://github.com/komputing/KEthereum/blob/master/erc55/README.md)                                                    |
| erc634                    | Storage of text records in ENS                           | beta           | [README](https://github.com/komputing/KEthereum/blob/master/erc634/README.md)                                                   |
| erc67                     | Standard URI scheme for transactions                     | beta           |               |
| erc681                    | URL Format for Transaction Requests (successor of ERC67) | beta           |               |
| erc831                    | URI Format for Ethereum (used by 681, 961, ..)           | beta           |               |
| erc961                    | URI standard for tokens                                  | beta           |               |
| etherscan                 | EtherScan BlockExplorer function                         | beta           |               |
| example_cli               | CLI App to demo KEthereum functionality                  | demonstration  | [example code](https://github.com/komputing/KEthereum/blob/master/example_cli/src/main/kotlin/org/kethereum/example_cli/ExampleCLI.kt)|
| extensions_kotlin         | Extension functions for Kotlin types                     | beta           |               |
| extensions_transactions   | Extension functions for the Transaction class            | beta           |               |
| flows                     | Coroutine flows for blocks and transactions              | beta           | [example code](https://github.com/komputing/KEthereum/blob/master/example_cli/src/main/kotlin/org/kethereum/example_cli/ExampleCLI.kt)|
| keccak_shortcut           | Proxy extension function for keccak hashing              | beta           |               |
| keystore                  | Storage for wallet files (e.g. to use the geth keystore) | beta           |               |
| metadata                  | Model and parser for contract metadata                   | beta           |               |
| method_signatures         | Functionality for method signatures (4byte repository,..)| beta           |               |
| model                     | Data-/Inline-Classes and constants                       | beta           |               |
| rlp                       | Recursive Length Prefix (RLP) encoder/decoder            | beta           |               |
| rpc                       | Remote Procedure Calls (RPC) abstraction                 | beta           |               |
| rpc_min3                  | Minimal INCUBED (IN3) RPC                                | experimental   | [README](https://github.com/komputing/KEthereum/blob/master/rpc_min3/README.md)                                                 |
| test_data                 | Data used in KEthereum tests                             | production     | -             |
| types                     | Handling of EVM types (e.g. used code from abi_codegen ) | beta           |               |
| uri_common                | Used by several URI modules (681, 1328, 961, ..)         | beta           |               |
| wallet                    | functions for keys from and to JSON wallet files         | beta           |               |

Projects that use KEthereum
===========================

 * [WallETH](https://walleth.org)
 * [FaucETH](https://fauceth.komputing.org)
 * [goerli pusher](https://github.com/walleth/goerli_pusher) (pushes out TST tokens - good example on how to use KEthereum)
 * [Gnosis Safe Android App](https://github.com/gnosis/safe-android)
 * [kmnid](https://github.com/uport-project/kmnid)
 * [uport android signer](https://github.com/uport-project/uport-android-signer)
 * [KHardwareWallet](https://github.com/walleth/KHardWareWallet)
 * multiple scripts in [ethereum-lists](https://github.com/ethereum-lists)
 * [mobidex](https://github.com/sigillabs/mobidex)
 * [Minerva Wallet](https://minerva.digital)
 * add yours

Links
=====

* [Ethereum](https://ethereum.org/)
* [White paper](https://github.com/ethereum/wiki/wiki/White-Paper)
* [Yellow paper](https://github.com/ethereum/yellowpaper)
* [WallETH](https://walleth.org)
* [Why a Kotlin library and not use web3j](https://github.com/web3j/web3j/issues/124#issuecomment-313088274)

Get it
======

KEthereum is available via jitpack:
[![](https://jitpack.io/v/komputing/kethereum.svg)](https://jitpack.io/#komputing/kethereum)

License
=======

MIT
