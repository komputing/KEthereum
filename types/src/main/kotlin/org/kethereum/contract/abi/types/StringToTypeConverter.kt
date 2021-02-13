package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.ContractABITypeDefinition
import org.kethereum.contract.abi.types.model.ETHType
import org.kethereum.contract.abi.types.model.TypeAliases
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.type_params.BytesTypeParams
import org.kethereum.contract.abi.types.model.types.*
import org.kethereum.model.Address
import java.math.BigInteger

fun convertStringToABIType(string: String) = convertStringToABITypeOrNull(string)
        ?: throw IllegalArgumentException("$string is not a supported type")

fun convertStringToABITypeOrNull(string: String) = convertStringToABITypeOrNullNoAliases(TypeAliases[string] ?: string)

internal fun convertStringToABITypeOrNullNoAliases(string: String?) = when {
    string == null -> null
    string == "bool" -> ContractABITypeDefinition(BoolETHType::class, Boolean::class)
    string == "string" -> ContractABITypeDefinition(StringETHType::class, String::class)
    string == "address" -> ContractABITypeDefinition(AddressETHType::class, Address::class)
    string == "bytes" -> ContractABITypeDefinition(DynamicSizedBytesETHType::class, ByteArray::class)

    string.startsWith("int") -> ContractABITypeDefinition(
            IntETHType::class,
            BigInteger::class,
            BitsTypeParams(string.extractPrefixedNumber("int", INT_BITS_CONSTRAINT))
    )
    string.startsWith("uint") -> ContractABITypeDefinition(
            UIntETHType::class,
            BigInteger::class,
            BitsTypeParams(string.extractPrefixedNumber("uint", INT_BITS_CONSTRAINT))
    )
    string.startsWith("bytes") -> ContractABITypeDefinition(
            BytesETHType::class,
            ByteArray::class,
            BytesTypeParams(string.extractPrefixedNumber("bytes", BYTES_COUNT_CONSTRAINT))
    )

    else -> null
}

fun String.extractPrefixedNumber(prefix: String, constraint: (Int) -> Unit) =
        (removePrefix(prefix)
                .toIntOrNull() ?: throw IllegalArgumentException("$this MUST have only a number after $prefix"))
                .also(constraint)

// yes the next part feels a bit WET - but any other method as far as I see would drag in kotlin-reflect which is very heavy - but if anyone knows a better way on how to do this in a DRY style without kotlin-reflect - I am all ears!
fun getETHTypeInstance(typeString: String, valueString: String) = getETHTypeInstanceOrNull(typeString, valueString)
        ?: throw IllegalArgumentException("$typeString is not a supported type")

fun getETHTypeInstanceOrNull(typeString: String, valueString: String) =
        getETHTypeInstanceOrNullNoAliases(TypeAliases[typeString] ?: typeString, valueString)

fun getETHTypeInstanceOrNullNoAliases(typeString: String?, valueString: String): ETHType<*>? = when {
    typeString == null -> null
    typeString == "bool" -> BoolETHType.ofString(valueString)
    typeString == "string" -> StringETHType.ofString(valueString)
    typeString == "address" -> AddressETHType.ofString(valueString)
    typeString == "bytes" -> DynamicSizedBytesETHType.ofString(valueString)
    typeString.startsWith("int") -> IntETHType.ofSting(valueString,
            BitsTypeParams(typeString.extractPrefixedNumber("int", INT_BITS_CONSTRAINT))
    )
    typeString.startsWith("uint") -> UIntETHType.ofSting(valueString,
            BitsTypeParams(typeString.extractPrefixedNumber("uint", INT_BITS_CONSTRAINT))
    )
    typeString.startsWith("bytes") -> BytesETHType.ofString(valueString, BytesTypeParams(typeString.extractPrefixedNumber("bytes", BYTES_COUNT_CONSTRAINT)))
    else -> null
}

fun getETHTypeInstanceOrNull(typeString: String, input: PaginatedByteArray) =
        getETHTypeInstanceOrNullNoAliases(TypeAliases[typeString] ?: typeString, input)

fun getETHTypeInstanceOrNullNoAliases(typeString: String?, input: PaginatedByteArray): ETHType<*>? = when {
    typeString == null -> null
    typeString == "bool" -> BoolETHType.ofPaginatedByteArray(input)
    typeString == "string" -> StringETHType.ofPaginatedByteArray(input)
    typeString == "address" -> AddressETHType.ofPaginatedByteArray(input)
    typeString == "bytes" -> DynamicSizedBytesETHType.ofPaginatedByteArray(input)
    typeString.startsWith("int") -> IntETHType.ofPaginatedByteArray(input,
            BitsTypeParams(typeString.extractPrefixedNumber("int", INT_BITS_CONSTRAINT))
    )
    typeString.startsWith("uint") -> UIntETHType.ofPaginatedByteArray(input,
            BitsTypeParams(typeString.extractPrefixedNumber("uint", INT_BITS_CONSTRAINT))
    )
    typeString.startsWith("bytes") -> BytesETHType.ofPaginatedByteArray(input, BytesTypeParams(typeString.extractPrefixedNumber("bytes", BYTES_COUNT_CONSTRAINT)))
    else -> null
}
