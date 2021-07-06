package org.kethereum.abi

val testMetaDataJSON = """
{"compiler":{"version":"0.5.11+commit.c082d0b4"},"language":"Solidity","output":{"abi":[{"constant":true,"inputs":[],"name":"name","outputs":[{"internalType":"string","name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"internalType":"address","name":"spender","type":"address"},{"internalType":"uint256","name":"value","type":"uint256"}],"name":"approve","outputs":[{"internalType":"bool","name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"internalType":"address","name":"sender","type":"address"},{"internalType":"address","name":"recipient","type":"address"},{"internalType":"uint256","name":"amount","type":"uint256"}],"name":"transferFrom","outputs":[{"internalType":"bool","name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"internalType":"uint8","name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"internalType":"address","name":"spender","type":"address"},{"internalType":"uint256","name":"addedValue","type":"uint256"}],"name":"increaseAllowance","outputs":[{"internalType":"bool","name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"internalType":"address","name":"account","type":"address"}],"name":"balanceOf","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"internalType":"string","name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"internalType":"address","name":"spender","type":"address"},{"internalType":"uint256","name":"subtractedValue","type":"uint256"}],"name":"decreaseAllowance","outputs":[{"internalType":"bool","name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"internalType":"address","name":"recipient","type":"address"},{"internalType":"uint256","name":"amount","type":"uint256"}],"name":"transfer","outputs":[{"internalType":"bool","name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"internalType":"address","name":"owner","type":"address"},{"internalType":"address","name":"spender","type":"address"}],"name":"allowance","outputs":[{"internalType":"uint256","name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"string","name":"name","type":"string"},{"internalType":"string","name":"symbol","type":"string"},{"internalType":"uint8","name":"decimals","type":"uint8"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"from","type":"address"},{"indexed":true,"internalType":"address","name":"to","type":"address"},{"indexed":false,"internalType":"uint256","name":"value","type":"uint256"}],"name":"Transfer","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"owner","type":"address"},{"indexed":true,"internalType":"address","name":"spender","type":"address"},{"indexed":false,"internalType":"uint256","name":"value","type":"uint256"}],"name":"Approval","type":"event"}],"devdoc":{"details":"Implementation of the `IERC20` interface. * This implementation is agnostic to the way tokens are created. This means that a supply mechanism has to be added in a derived contract using `_mint`. For a generic mechanism see `ERC20Mintable`. * *For a detailed writeup see our guide [How to implement supply mechanisms](https://forum.zeppelin.solutions/t/how-to-implement-erc20-supply-mechanisms/226).* * We have followed general OpenZeppelin guidelines: functions revert instead of returning `false` on failure. This behavior is nonetheless conventional and does not conflict with the expectations of ERC20 applications. * Additionally, an `Approval` event is emitted on calls to `transferFrom`. This allows applications to reconstruct the allowance for all accounts just by listening to said events. Other implementations of the EIP may not emit these events, as it isn't required by the specification. * Finally, the non-standard `decreaseAllowance` and `increaseAllowance` functions have been added to mitigate the well-known issues around setting allowances. See `IERC20.approve`.","methods":{"allowance(address,address)":{"details":"See `IERC20.allowance`."},"approve(address,uint256)":{"details":"See `IERC20.approve`.     * Requirements:     * - `spender` cannot be the zero address."},"balanceOf(address)":{"details":"See `IERC20.balanceOf`."},"decimals()":{"details":"Returns the number of decimals used to get its user representation. For example, if `decimals` equals `2`, a balance of `505` tokens should be displayed to a user as `5,05` (`505 / 10 ** 2`).     * Tokens usually opt for a value of 18, imitating the relationship between Ether and Wei.     * > Note that this information is only used for _display_ purposes: it in no way affects any of the arithmetic of the contract, including `IERC20.balanceOf` and `IERC20.transfer`."},"decreaseAllowance(address,uint256)":{"details":"Atomically decreases the allowance granted to `spender` by the caller.     * This is an alternative to `approve` that can be used as a mitigation for problems described in `IERC20.approve`.     * Emits an `Approval` event indicating the updated allowance.     * Requirements:     * - `spender` cannot be the zero address. - `spender` must have allowance for the caller of at least `subtractedValue`."},"increaseAllowance(address,uint256)":{"details":"Atomically increases the allowance granted to `spender` by the caller.     * This is an alternative to `approve` that can be used as a mitigation for problems described in `IERC20.approve`.     * Emits an `Approval` event indicating the updated allowance.     * Requirements:     * - `spender` cannot be the zero address."},"name()":{"details":"Returns the name of the token."},"symbol()":{"details":"Returns the symbol of the token, usually a shorter version of the name."},"totalSupply()":{"details":"See `IERC20.totalSupply`."},"transfer(address,uint256)":{"details":"See `IERC20.transfer`.     * Requirements:     * - `recipient` cannot be the zero address. - the caller must have a balance of at least `amount`."},"transferFrom(address,address,uint256)":{"details":"See `IERC20.transferFrom`.     * Emits an `Approval` event indicating the updated allowance. This is not required by the EIP. See the note at the beginning of `ERC20`;     * Requirements: - `sender` and `recipient` cannot be the zero address. - `sender` must have a balance of at least `value`. - the caller must have allowance for `sender`'s tokens of at least `amount`."}}},"userdoc":{"methods":{}}},"settings":{"compilationTarget":{"browser/BCY.sol":"BitCrystals"},"evmVersion":"petersburg","libraries":{},"optimizer":{"enabled":false,"runs":200},"remappings":[]},"sources":{"browser/BCY.sol":{"keccak256":"0x87fcd652f317a58050817271574e6e9b3441b4a36eb61fddcca75c8cce2257ef","urls":["bzz-raw://e5402b81d8dff67a434606b6dffb82a2571d92dc3b6989f8641420c5f79da6cc","dweb:/ipfs/Qme5nna1HKerk5V7PHNCo8m9mESVQX9eKSvHDyYK9U8z1s"]}},"version":1}
"""

val testMetaDataJSONWithConstructor ="{\n" +
        "  \"compiler\": {\n" +
        "    \"version\": \"0.5.11+commit.c082d0b4\"\n" +
        "  },\n" +
        "  \"language\": \"Solidity\",\n" +
        "  \"output\": {\n" +
        "    \"abi\": [\n" +
        "      {\n" +
        "        \"constant\": false,\n" +
        "        \"inputs\": [\n" +
        "          {\n" +
        "            \"internalType\": \"address\",\n" +
        "            \"name\": \"to\",\n" +
        "            \"type\": \"address\"\n" +
        "          }\n" +
        "        ],\n" +
        "        \"name\": \"delegate\",\n" +
        "        \"outputs\": [],\n" +
        "        \"payable\": false,\n" +
        "        \"stateMutability\": \"nonpayable\",\n" +
        "        \"type\": \"function\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"constant\": true,\n" +
        "        \"inputs\": [],\n" +
        "        \"name\": \"winningProposal\",\n" +
        "        \"outputs\": [\n" +
        "          {\n" +
        "            \"internalType\": \"uint8\",\n" +
        "            \"name\": \"_winningProposal\",\n" +
        "            \"type\": \"uint8\"\n" +
        "          }\n" +
        "        ],\n" +
        "        \"payable\": false,\n" +
        "        \"stateMutability\": \"view\",\n" +
        "        \"type\": \"function\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"constant\": false,\n" +
        "        \"inputs\": [\n" +
        "          {\n" +
        "            \"internalType\": \"address\",\n" +
        "            \"name\": \"toVoter\",\n" +
        "            \"type\": \"address\"\n" +
        "          }\n" +
        "        ],\n" +
        "        \"name\": \"giveRightToVote\",\n" +
        "        \"outputs\": [],\n" +
        "        \"payable\": false,\n" +
        "        \"stateMutability\": \"nonpayable\",\n" +
        "        \"type\": \"function\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"constant\": false,\n" +
        "        \"inputs\": [\n" +
        "          {\n" +
        "            \"internalType\": \"uint8\",\n" +
        "            \"name\": \"toProposal\",\n" +
        "            \"type\": \"uint8\"\n" +
        "          }\n" +
        "        ],\n" +
        "        \"name\": \"vote\",\n" +
        "        \"outputs\": [],\n" +
        "        \"payable\": false,\n" +
        "        \"stateMutability\": \"nonpayable\",\n" +
        "        \"type\": \"function\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"inputs\": [\n" +
        "          {\n" +
        "            \"internalType\": \"uint8\",\n" +
        "            \"name\": \"_numProposals\",\n" +
        "            \"type\": \"uint8\"\n" +
        "          }\n" +
        "        ],\n" +
        "        \"payable\": false,\n" +
        "        \"stateMutability\": \"nonpayable\",\n" +
        "        \"type\": \"constructor\"\n" +
        "      }\n" +
        "    ],\n" +
        "    \"devdoc\": {\n" +
        "      \"methods\": {}\n" +
        "    },\n" +
        "    \"userdoc\": {\n" +
        "      \"methods\": {\n" +
        "        \"constructor\": \"Create a new ballot with \$(_numProposals) different proposals.\",\n" +
        "        \"delegate(address)\": {\n" +
        "          \"notice\": \"Delegate your vote to the voter \$(to).\"\n" +
        "        },\n" +
        "        \"giveRightToVote(address)\": {\n" +
        "          \"notice\": \"Give \$(toVoter) the right to vote on this ballot. May only be called by \$(chairperson).\"\n" +
        "        },\n" +
        "        \"vote(uint8)\": {\n" +
        "          \"notice\": \"Give a single vote to proposal \$(toProposal).\"\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  },\n" +
        "  \"settings\": {\n" +
        "    \"compilationTarget\": {\n" +
        "      \"browser/ballot.sol\": \"Ballot\"\n" +
        "    },\n" +
        "    \"evmVersion\": \"petersburg\",\n" +
        "    \"libraries\": {},\n" +
        "    \"optimizer\": {\n" +
        "      \"enabled\": false,\n" +
        "      \"runs\": 200\n" +
        "    },\n" +
        "    \"remappings\": []\n" +
        "  },\n" +
        "  \"sources\": {\n" +
        "    \"browser/ballot.sol\": {\n" +
        "      \"keccak256\": \"0x9e1ba6d6e1b62269083d8565872651ef2b53c59cecaffb7e483750e72dc659fb\",\n" +
        "      \"urls\": [\n" +
        "        \"bzz-raw://5766400e5d6d822f2029b827331b354c41e0b61f73440851dd0d06f603dd91e5\",\n" +
        "        \"dweb:/ipfs/Qmdyq9ZmWcaryd1mgGZ4PttRNctLGUSAMpPqufsk6uRMKh\"\n" +
        "      ]\n" +
        "    }\n" +
        "  },\n" +
        "  \"version\": 1\n" +
        "}\n"


val testDataDevDocNoDetails = """
    {"compiler":{"version":"0.6.12+commit.27d51765"},"language":"Solidity","output":{"abi":[{"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"internalType":"address","name":"previousOwner","type":"address"},{"indexed":true,"internalType":"address","name":"newOwner","type":"address"}],"name":"OwnershipTransferred","type":"event"},{"inputs":[{"internalType":"address","name":"_token","type":"address"},{"internalType":"address","name":"_spender","type":"address"},{"internalType":"uint256","name":"_amount","type":"uint256"}],"name":"approve","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"sender","type":"address"},{"components":[{"internalType":"address","name":"owner","type":"address"},{"internalType":"uint256","name":"number","type":"uint256"}],"internalType":"struct Account.Info","name":"account","type":"tuple"},{"internalType":"bytes","name":"data","type":"bytes"}],"name":"callFunction","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"_solo","type":"address"},{"internalType":"address","name":"_token","type":"address"},{"internalType":"address","name":"_unitroller","type":"address"},{"internalType":"address","name":"_supplyDelegator","type":"address"},{"internalType":"address","name":"_borrowDelegator","type":"address"}],"name":"closePosition","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"_solo","type":"address"},{"internalType":"address","name":"_token","type":"address"},{"internalType":"address","name":"_unitroller","type":"address"},{"internalType":"address","name":"_supplyDelegator","type":"address"},{"internalType":"address","name":"_borrowDelegator","type":"address"},{"internalType":"uint256","name":"_amount","type":"uint256"},{"internalType":"uint256","name":"_ltv","type":"uint256"}],"name":"openPosition","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[],"name":"owner","outputs":[{"internalType":"address","name":"","type":"address"}],"stateMutability":"view","type":"function"},{"inputs":[{"internalType":"uint256","name":"_amount","type":"uint256"},{"internalType":"bool","name":"_redeemType","type":"bool"},{"internalType":"address","name":"_delegator","type":"address"}],"name":"redeem","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[],"name":"renounceOwnership","outputs":[],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"_delegator","type":"address"},{"internalType":"uint256","name":"_amount","type":"uint256"}],"name":"repayBorrow","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"_delegator","type":"address"},{"internalType":"address","name":"_token","type":"address"},{"internalType":"uint256","name":"_amount","type":"uint256"}],"name":"supply","outputs":[{"internalType":"bool","name":"","type":"bool"}],"stateMutability":"nonpayable","type":"function"},{"inputs":[{"internalType":"address","name":"newOwner","type":"address"}],"name":"transferOwnership","outputs":[],"stateMutability":"nonpayable","type":"function"}],"devdoc":{"kind":"dev","methods":{"owner()":{"details":"Returns the address of the current owner."},"renounceOwnership()":{"details":"Leaves the contract without owner. It will not be possible to call `onlyOwner` functions anymore. Can only be called by the current owner. NOTE: Renouncing ownership will leave the contract without an owner, thereby removing any functionality that is only available to the owner."},"transferOwnership(address)":{"details":"Transfers ownership of the contract to a new account (`newOwner`). Can only be called by the current owner."}},"version":1},"userdoc":{"kind":"user","methods":{},"version":1}},"settings":{"compilationTarget":{"browser/DydxFlashloaner.sol":"DydxFlashloaner"},"evmVersion":"istanbul","libraries":{},"metadata":{"bytecodeHash":"ipfs"},"optimizer":{"enabled":false,"runs":200},"remappings":[]},"sources":{"Context.sol":{"keccak256":"0xac3640fab06aaa5fc0af5b6548a8636bc14b78229779b692bae195c699221797","license":"MIT","urls":["bzz-raw://48fcd027633a742c81fcfa81dfa2a0a22cee177971fb32e6f8cb5197fdf951d1","dweb:/ipfs/QmeGieEzcGUKHppbDZ3PySE4yi8gTzcRQ7ayobMobbmwJ3"]},"DydxFlashloanBase.sol":{"keccak256":"0xf6d2430ca1bb74712ba9002fda37b2c9085de937d341519d6a62abe23df34a05","urls":["bzz-raw://a3ac70ba4997e597920d7548520b1c13a5e3ee191e678a225b60bb11d111af80","dweb:/ipfs/QmXN9edu4kN3PXgQSsNqxhUvHukJ6p1L18UMV7sCPDoNbN"]},"ICallee.sol":{"keccak256":"0x0609fadb09f1842a074138bbeb1978ec6a9de25e515df58e0256e4e018e42c34","urls":["bzz-raw://1d5298f3537b5a49be152e883547cd8891f1f9be55f94b24c16e489bd0678c6e","dweb:/ipfs/QmU6Y7VF92UYfjS8NNiUm8N6UxxuRhwWMNjWhPUcNwpuco"]},"IERC20.sol":{"keccak256":"0x95bc363907d2793dbc5ab548e1da77a5d1340e5062a6bcd640f706348e0e2f17","license":"MIT","urls":["bzz-raw://fafea5c7a8c0c42598571c87b01e3c39d87eeb6a93a48da3fcd1afc4a5724bd6","dweb:/ipfs/QmXx8srPSPWKwYKFMySqsBmtRz5h6io8TJrc9dTruJjHEp"]},"ISoloMargin.sol":{"keccak256":"0x1f05c0dc380b7a6623e12c7e7081b071515891a95f75db0e4ed441b460d52b1b","urls":["bzz-raw://8cf257eae86764050be95ef57a2143c14a9f42488114c294e10e9bb7974d4a78","dweb:/ipfs/QmbeFsS9nj418azcZUWFBzP8ULWmco5P4YCsZCaqmayPaQ"]},"Ownable.sol":{"keccak256":"0xa782b2ac837ab09fee4168d5eba6e11f107e7e3df5ca9bf440ebe574962532a3","license":"MIT","urls":["bzz-raw://7abcc24c557506793daf21ba0d1e8547e51926169db9e961e6dc1d88811f4bfd","dweb:/ipfs/QmUfGxY4kUY2az6GW1DJDQ59x98WpBz7p44cv2rHoQZL7A"]},"SafeMath.sol":{"keccak256":"0xaa5974a17de3ff0a3b5132f2d65f957c60450c398b0e6bf252cd8c80c43a4724","license":"MIT","urls":["bzz-raw://6f8f9836ee7504a2010136587fdfb373937cb5ae822f44f7c5110be984c552c8","dweb:/ipfs/QmPsVWWUEiPZ9gmFRaR491L1pYWuGrGKRaBt86j8YcDdgQ"]},"browser/DydxFlashloaner.sol":{"keccak256":"0xd6bca24bc7562bba51ea59731af56fe7f9849e02fe31ff1b3cb2af92f72be89e","urls":["bzz-raw://399bb61cf0b079a9ffe1d4df423bd7f8c995d7dc38365664a970cde3f5c00f2a","dweb:/ipfs/QmYUNcC8GGgxCVNAKH5eJP8m4qzVQpeKtC2RVBCtiLdhB8"]}},"version":1}
""".trimIndent()