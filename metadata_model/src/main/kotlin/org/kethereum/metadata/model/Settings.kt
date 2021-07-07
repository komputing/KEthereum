package org.kethereum.metadata.model

data class OptimizerSettings(
        val enabled: Boolean,
        val runs: Int

)

data class Settings(
        val compilationTarget: Map<String, String>,
        val evmVersion: String?,
        // TODO add libraries and remappings
        val optimizer: OptimizerSettings

)