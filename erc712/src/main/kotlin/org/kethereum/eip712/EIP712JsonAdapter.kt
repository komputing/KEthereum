package pm.gnosis.eip712

import java.io.InputStream

interface EIP712JsonAdapter {
    fun parse(typedDataJson: String): Result
    fun parse(inputStream: InputStream): Result

    data class Parameter(val name: String, val type: String)
    data class Result(
        val primaryType: String,
        val domain: Map<String, Any>,
        val message: Map<String, Any>,
        val types: Map<String, List<Parameter>>
    )
}
