package org.kethereum.rpc.min3.model

data class IN3Node(
        val url: String,
        val address: String,
        val index: Int,
        val deposit: String,
        val props: String,
        val timeout: Int,
        val registerTime: Int,
        val weight: Int
)