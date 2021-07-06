package org.kethereum.contract.abi.types

import org.kethereum.contract.abi.types.model.*
import org.kethereum.contract.abi.types.model.byteRange
import org.kethereum.contract.abi.types.model.intRange
import org.kethereum.contract.abi.types.model.supportedTypesNoParams

val allSupportedETHTypes by lazy {
    byteRange.map { "bytes$it" } +
            intRange.map { "int$it" } +
            intRange.map { "uint$it" } +
            supportedTypesNoParams
}

val allFixedTypeParams =
    fixedMRange.map { m -> m to fixedNRange.map { n -> n.toString() } }.map { pair -> pair.second.map { pair.first.toString() + "x" + it } }.flatten()

val allFixedTypes = allFixedTypeParams.map { "fixed$it" }
val allUFixedTypes = allFixedTypeParams.map { "ufixed$it" }

val allUnsupportedETHTypes by lazy {
    allFixedTypes + allUFixedTypes + unSupportedTypesNoParams
}

val allETHTypes by lazy {
    allSupportedETHTypes + allUnsupportedETHTypes
}