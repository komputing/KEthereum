package pm.gnosis.eip712

import org.kethereum.contract.abi.types.allETHTypes
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.types.AddressETHType
import org.kethereum.contract.abi.types.model.types.BytesETHType
import org.kethereum.contract.abi.types.model.types.DynamicSizedBytesETHType
import org.kethereum.contract.abi.types.model.types.StringETHType
import org.kethereum.keccakshortcut.keccak


const val EIP712_DOMAIN_TYPE = "EIP712Domain"

data class Struct712Parameter(val name: String, val type: Type712)

internal infix fun String.asParameterNameFor(type: Type712) = Struct712Parameter(this, type)

sealed class Type712 {
    abstract val typeName: String
}

class Literal712(override val typeName: String, val value: ETHType<out Any>) : Type712()

class Struct712(override val typeName: String, val parameters: List<Struct712Parameter>) : Type712() {
    fun hashStruct(): ByteArray {
        val encodeParameters = encodeParameters()
        val myTypeHash = typeHash
        return (myTypeHash + encodeParameters).keccak()
    }

    val typeHash by lazy {
        (encodeType().joinToString(separator = "").toByteArray(charset = Charsets.UTF_8)).keccak()
    }

    fun encodeParameters(): ByteArray =
        parameters.map { (_, type) ->
            when (type) {
                is Struct712 -> type.hashStruct()
                is Literal712 -> {
                    val encodeSolidityType = encodeSolidityType(type.value)
                    encodeSolidityType

                }
            }
        }.reduce { acc, bytes -> acc + bytes }

    private fun encodeType(): List<String> {
        val encodedStruct = parameters.joinToString(separator = ",", prefix = "$typeName(", postfix = ")",
            transform = { (name, type) -> "${type.typeName} $name" })
        val structParams = parameters.asSequence().filter { (_, type) -> !allETHTypes.contains(type.typeName) }
            .mapNotNull { (_, type) -> (type as? Struct712)?.encodeType() }
            .flatten().distinct().sorted().toList()
        return listOf(encodedStruct) + structParams
    }
}

fun encodeSolidityType(value: ETHType<out Any>): ByteArray = when (value) {
    is StringETHType -> value.toKotlinType().toByteArray().keccak()
    is BytesETHType -> value.toKotlinType().keccak()
    is DynamicSizedBytesETHType -> value.toKotlinType().keccak()
    else -> value.paddedValue
}

fun typedDataHash(message: Struct712, domain: Struct712): ByteArray =
    (byteArrayOf(0x19, 0x1) + domain.hashStruct() + message.hashStruct()).keccak()