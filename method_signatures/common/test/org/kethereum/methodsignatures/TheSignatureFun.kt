package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.methodsignatures.model.TextMethodSignature

class TheSignatureFun {

    @Test
    fun canCalculateSignature() {
        assertThat(TextMethodSignature("transfer(address,uint256)").toHexSignature().hex).isEqualTo("a9059cbb")
    }

    @Test
    fun canCalculateSignatureAndDoesNotGetConfusedByTabsAndSpaces() {
        assertThat(TextMethodSignature("\ttransfer \t( address,\tuint256 ) ").toHexSignature().hex).isEqualTo("a9059cbb")
    }

    @Test
    fun normalizingWorks() {
        assertThat(TextMethodSignature("\ttransfer \t( address,\tuint256 ) ").normalizedSignature).isEqualTo("transfer(address,uint256)")
    }

    @Test
    fun replacementsOfSynonymsHappen() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignature().hex)
                .isEqualTo(TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex)
    }

    @Test
    fun replacementsOfSynonymsReflectedInNormalizedSignature() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").normalizedSignature)
                .isEqualTo("transfer(address,uint256,int256,fixed128x18,ufixed128x18)")
    }

    @Test
    fun canGetFunctionName() {
        assertThat(TextMethodSignature(" yolo (address,uint,int,fixed,ufixed)").functionName)
                .isEqualTo("yolo")
    }

    @Test
    fun canGetParameters() {
        assertThat(TextMethodSignature(" yolo (address,uint)").parameters)
                .isEqualTo(listOf("address","uint"))
    }

    @Test
    fun canGetNormalizedParameters() {
        assertThat(TextMethodSignature(" yolo (address,uint)").normalizedParameters)
                .isEqualTo(listOf("address","uint256"))
    }


    @Test
    fun replacementsOfSynonymsDontHappenWhenUnsafeUsed() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignatureUnsafe().hex)
                .isNotEqualTo(TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex)
    }



}