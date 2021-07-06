package pm.gnosis.eip712


import org.kethereum.contract.abi.types.*
import org.kethereum.contract.abi.types.model.NamedETHType
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.type_params.BytesTypeParams
import org.kethereum.contract.abi.types.model.types.*
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.model.Address
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.model.HexString
import java.io.InputStream
import java.math.BigDecimal
import java.math.BigInteger

data class DomainWithMessage(val domain: Struct712, val message: Struct712)

class EIP712JsonParser(private val jsonAdapter: EIP712JsonAdapter) {
    fun parseMessage(rawJson: String): DomainWithMessage = parseMessage(jsonAdapter.parse(rawJson))

    fun parseMessage(inputStream: InputStream): DomainWithMessage = parseMessage(jsonAdapter.parse(inputStream))

    private fun parseMessage(adapterResult: EIP712JsonAdapter.Result) =
        DomainWithMessage(
            domain = buildStruct712(
                typeName = EIP712_DOMAIN_TYPE,
                values = adapterResult.domain,
                typeSpec = adapterResult.types
            ),
            message = buildStruct712(
                typeName = adapterResult.primaryType,
                values = adapterResult.message,
                typeSpec = adapterResult.types
            )
        )

    private fun buildStruct712(
        typeName: String,
        values: Map<String, Any>,
        typeSpec: Map<String, List<EIP712JsonAdapter.Parameter>>
    ): Struct712 {
        val params = typeSpec[typeName] ?: throw IllegalArgumentException("TypedDate does not contain type $typeName")
        val innerParams = params.map { typeParam ->
            val type712 = if (typeSpec.contains(typeParam.type)) {
                // Struct
                buildStruct712(
                    typeName = typeParam.type,
                    values = values[typeParam.name] as Map<String, Any>,
                    typeSpec = typeSpec
                )
            } else {
                // Literal
                val rawValue = values[typeParam.name] ?: throw IllegalArgumentException("Could not get value for property ${typeParam.name}")
                if (!NamedETHType(typeParam.type).isETHType()) throw IllegalArgumentException("Property with name ${typeParam.name} has invalid Solidity type ${typeParam.type}")
                if (!NamedETHType(typeParam.type).isSupportedETHType()) throw IllegalArgumentException("Property with name ${typeParam.name} has unsupported Solidity type ${typeParam.type}")
                val ethereumType = when {
                    typeParam.type.startsWith(prefix = "uint") -> readNumber(rawNumber = rawValue, creator = { UIntETHType.ofNativeKotlinType(it,
                        BitsTypeParams(typeParam.type.extractPrefixedNumber("uint", INT_BITS_CONSTRAINT))
                    ) })

                    typeParam.type.startsWith(prefix = "int") -> readNumber(rawNumber = rawValue, creator = { IntETHType.ofNativeKotlinType(it,
                        BitsTypeParams(typeParam.type.extractPrefixedNumber("int", INT_BITS_CONSTRAINT))
                    ) })

                    typeParam.type == "bytes" -> DynamicSizedBytesETHType.ofNativeKotlinType(HexString(rawValue as String).hexToByteArray())
                    typeParam.type == "string" ->  StringETHType.ofString(rawValue.toString())
                    typeParam.type.startsWith(prefix = "bytes") -> BytesETHType.ofNativeKotlinType(HexString(rawValue as String).hexToByteArray(),
                        BytesTypeParams(( typeParam.type.extractPrefixedNumber("bytes", BYTES_COUNT_CONSTRAINT))))
                    typeParam.type == "bool" -> readBool(rawBool = rawValue)
                    typeParam.type == "address" -> readNumber(rawNumber = rawValue, creator = { AddressETHType.ofNativeKotlinType(Address(it.toByteArray().toHexString())) })
                    else -> throw IllegalArgumentException("Unknown literal type ${typeParam.type}")
                }
                Literal712(typeName = typeParam.type, value = ethereumType)
            }
            Struct712Parameter(name = typeParam.name, type = type712)
        }
        return Struct712(typeName = typeName, parameters = innerParams)
    }

    private fun <T> readNumber(rawNumber: Any, creator: (BigInteger) -> T): T =
        when (rawNumber) {
            is Number -> creator(BigDecimal(rawNumber.toString()).exactNumber())
            is String -> {
                if (rawNumber.startsWith(prefix = "0x")) creator(HexString(rawNumber).hexToBigInteger())
                else creator(BigDecimal(rawNumber).exactNumber())
            }
            else -> throw IllegalArgumentException("Value $rawNumber is neither a Number nor String")
        }

    private fun readBool(rawBool: Any): BoolETHType =
        if (rawBool is Boolean) BoolETHType.ofNativeKotlinType(rawBool)
        else if (rawBool.toString().equals("true", ignoreCase = true) || rawBool.toString().equals("false", ignoreCase = true))
            BoolETHType.ofNativeKotlinType(rawBool.toString().equals("true", ignoreCase = true))
        else throw java.lang.IllegalArgumentException("Value $rawBool is not a Boolean")

    private fun BigDecimal.exactNumber() =
        try { toBigIntegerExact() } catch (e:Exception) { throw IllegalArgumentException("Value ${toString()} is a decimal (not supported)") }
}
