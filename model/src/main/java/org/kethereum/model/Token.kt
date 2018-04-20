package org.kethereum.model

data class Token(val symbol: String,
                 val address: Address,
                 val chain: ChainDefinition,
                 val decimals: Int = 18,
                 val type: String? = null,
                 val name: String? = null)