package org.kethereum.erc1577.model

import org.junit.jupiter.params.provider.Arguments

val testVectors = listOf(
        //  ipfs from EIP-1577
        Arguments.of(
                "0xe3010170122029f2d17be6139079dc48696d1f582a8530eb9805b561eda517e22a892c7e3f1f",
                "ipfs://QmRAQB6YaCyidP37UdDnjFY5vQuiBrcqdyoW1CuDgwxkD4"
        ),

        // ipfs from verificat.eth
        Arguments.of(
                "0xe30101701220265eb9a69763b2488122d95e07609b1379dba0703a43b4c421d5467a9eb9d9a4",
                "ipfs://QmQvRyeZHnceAw8dTw3ytAVQZnqYeK61TvFwdnydnmCzY3"
        ),

        // bzz from EIP-1577
        Arguments.of(
                "0xe40101fa011b20d1de9994b4d039f6548d191eb26786769f580809256b4685ef316805265ea162",
                "bzz://d1de9994b4d039f6548d191eb26786769f580809256b4685ef316805265ea162"
        ),

        Arguments.of(
                "0xe5010170000f6170702e756e69737761702e6f7267",
                "ipns://app.uniswap.org"
        )

        ,

        Arguments.of(
                "0xe5010170002e516d4e6d42723474695874775472484b6a79707055794168573146515a4d4a54646e55726b734139686170533475",
                "ipns://QmNmBr4tiXtwTrHKjyppUyAhW1FQZMJTdnUrksA9hapS4u"
        )
)