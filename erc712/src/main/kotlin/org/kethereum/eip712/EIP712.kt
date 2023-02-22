package pm.gnosis.eip712

import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.types.BytesETHType
import org.kethereum.contract.abi.types.model.types.DynamicSizedBytesETHType
import org.kethereum.contract.abi.types.model.types.StringETHType
import org.kethereum.keccakshortcut.keccak


const val EIP712_DOMAIN_TYPE = "EIP712Domain"

data class Struct712Parameter(val name: String, val type: Type712)

internal infix fun String.asParameterNameFor(type: Type712) = Struct712Parameter(this, type)

sealed class Type712 {
    abstract val typeName: String

    abstract fun encode(): ByteArray
}

class Literal712(override val typeName: String, val value: ETHType<out Any>) : Type712() {

    override fun encode(): ByteArray {
        return when (value) {
            is StringETHType -> value.toKotlinType().toByteArray().keccak()
            is BytesETHType -> value.toKotlinType().keccak()
            is DynamicSizedBytesETHType -> value.toKotlinType().keccak()
            else -> value.paddedValue
        }
    }
}

class Struct712(override val typeName: String, val parameters: List<Struct712Parameter>) : Type712() {

    fun hashStruct(): ByteArray {
        return (typeHash + encode()).keccak()
    }

    val typeHash by lazy {
        (encodeType().joinToString(separator = "").toByteArray(charset = Charsets.UTF_8)).keccak()
    }

    override fun encode(): ByteArray =
        parameters.map { (_, type) ->
            encodeItem(type)
        }.reduce { acc, bytes -> acc + bytes }

    private fun encodeType(): List<String> {
        val encodedStruct = parameters.joinToString(separator = ",", prefix = "$typeName(", postfix = ")",
            transform = { (name, type) -> "${type.typeName} $name" })
        val structParams = parameters.asSequence()
            .mapNotNull { (_, type) -> (type as? Struct712)?.encodeType() }
            .flatten().distinct().sorted().toList()
        return listOf(encodedStruct) + structParams
    }
}

class Array712(override val typeName: String, val items: List<Type712>) : Type712() {

    override fun encode(): ByteArray =
        items.map {
            encodeItem(it)
        }.reduce { acc, bytes -> acc + bytes }.keccak()
}

private fun encodeItem(item : Type712): ByteArray {
    return when (item) {
        is Struct712 -> item.hashStruct()
        is Literal712 -> item.encode()
        is Array712 -> item.encode()
    }
}

fun typedDataHash(message: Struct712, domain: Struct712): ByteArray =
    (byteArrayOf(0x19, 0x1) + domain.hashStruct() + message.hashStruct()).keccak()