package org.kethereum.contract.abi.types

import org.junit.jupiter.api.Test
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.types.UIntETHType
import java.math.BigInteger.ONE
import java.math.BigInteger.valueOf
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class TheAllTypes {

    @Test
    fun allTypesAreKnown() {
       allETHTypes.forEach {
           assertNotNull(convertStringToABITypeOrNullNoAliases(it))
       }
    }


}