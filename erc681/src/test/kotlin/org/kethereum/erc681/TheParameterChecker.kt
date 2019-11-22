package org.kethereum.erc681

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TheParameterChecker {

    @Test
    fun typeCheckerReturnsNullForValidBoolType() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?bool=true").findIllegalParamType()).isNull()
    }


    @Test
    fun typeCheckerReturnsNullForValidAliasType() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?uint=2").findIllegalParamType()).isNull()
    }

    @Test
    fun typeCheckerReturnsNullForValidTypes() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?bool=true&uint=2").findIllegalParamType()).isNull()
    }

    @Test
    fun typeCheckerReturnsNullForNoParameters() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill").findIllegalParamType()).isNull()
    }

    @Test
    fun typeCheckerReturnsInvalidType() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?no=true").findIllegalParamType()?.first).isEqualTo("no")
    }
    @Test
    fun typeCheckerReturnsInvalidTypeOnSecondPos() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?bool=true&yolo=true").findIllegalParamType()?.first).isEqualTo("yolo")
    }



    @Test
    fun valueCheckerReturnsNullForLegalValue() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?bool=true").findIllegalParamValue()?.first).isNull()
    }


    @Test
    fun valueCheckerReturnsNullForLegalValues() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?uint=2").findIllegalParamValue()?.first).isNull()
    }


    @Test
    fun valueCheckerReturnsInvalidType() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?bool=yolo").findIllegalParamValue()?.second).isEqualTo("yolo")
    }

    @Test
    fun valueCheckerReturnsInvalidUINTType() {
        Assertions.assertThat(parseERC681("ethereum:0x00AB42@23/ownerKill?uint=yolo2").findIllegalParamValue()?.second).isEqualTo("yolo2")
    }

}