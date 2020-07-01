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