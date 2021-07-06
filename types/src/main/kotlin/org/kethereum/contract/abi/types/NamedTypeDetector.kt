package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.*
import org.kethereum.contract.abi.types.model.byteRange
import org.kethereum.contract.abi.types.model.intRange
import org.kethereum.contract.abi.types.model.supportedTypesNoParams

fun NamedETHType.isSupportedETHType() = supportedTypesNoParams.contains(name)
        || (name.startsWith("bytes") && name.replace("bytes", "").toInt() in byteRange)
        || (name.startsWith("int") && name.replace("int", "").toInt() in intRange)
        || (name.startsWith("uint") && name.replace("uint", "").toInt() in intRange)

private fun String.isValidFixedParam() = split("x").let {
    it.size == 2 && it.first().toInt() in fixedMRange && it.last().toInt() in fixedNRange
}

fun NamedETHType.isUnSupportedETHType() = unSupportedTypesNoParams.contains(name)
        || (name.startsWith("fixed") && name.replace("fixed", "").isValidFixedParam())
        || (name.startsWith("ufixed") && name.replace("ufixed", "").isValidFixedParam())

fun NamedETHType.isETHType() = isSupportedETHType() || isUnSupportedETHType()