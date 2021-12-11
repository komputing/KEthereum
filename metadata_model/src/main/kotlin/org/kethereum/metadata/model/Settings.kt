package org.kethereum.metadata.model

import com.squareup.moshi.JsonClass
import java.math.BigInteger

@JsonClass(generateAdapter = true)
data class OptimizerSettings(
        val enabled: Boolean?,
        val runs: BigInteger
)

@JsonClass(generateAdapter = true)
data class Settings(
        val compilationTarget: Map<String, String>,
        val evmVersion: String?,
        // TODO add libraries and remappings
        val optimizer: OptimizerSettings

)