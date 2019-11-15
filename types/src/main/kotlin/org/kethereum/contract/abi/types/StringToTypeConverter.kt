package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.ContractABITypeDefinition
import org.kethereum.contract.abi.types.model.TypeAliases
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.type_params.BytesTypeParams
import org.kethereum.contract.abi.types.model.types.*
import org.kethereum.model.Address
import java.math.BigInteger

fun convertStringToABIType(string: String) = convertStringToABITypeOrNull(string)
        ?: throw IllegalArgumentException("$string is not a supported type")

fun convertStringToABITypeOrNull(string: String) = (convertStringToABITypeOrNullNoAliases(string)
        ?: convertStringToABITypeOrNullNoAliases(TypeAliases[string]))

private fun convertStringToABITypeOrNullNoAliases(string: String?) = when {
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

private fun String.extractPrefixedNumber(prefix: String, constraint: (Int) -> Unit) =
        (removePrefix(prefix)
                .toIntOrNull() ?: throw IllegalArgumentException("$this MUST have only a number after $prefix"))
                .also(constraint)
