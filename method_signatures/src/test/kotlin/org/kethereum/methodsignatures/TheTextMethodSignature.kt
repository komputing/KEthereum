package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.methodsignatures.model.TextMethodSignature

class TheTextMethodSignature {

    @Test
    fun normalizingWorks() {
        assertThat(TextMethodSignature("\ttransfer \t( address,\tuint256 ) ").normalizedSignature).isEqualTo("transfer(address,uint256)")
    }


    @Test
    fun normalizingWorksWithTuples() {
        assertThat(TextMethodSignature("\ttransfer \t( address,\t ( uint256) ) ").normalizedSignature).isEqualTo("transfer(address,(uint256))")
    }

    @Test
    fun normalizingWorksWithArrayOfTuples() {
        assertThat(TextMethodSignature("\ttransfer \t( address,\t ( uint256)[] ) ").normalizedSignature).isEqualTo("transfer(address,(uint256)[])")
    }

    @Test
    fun replacementsOfSynonymsReflectedInNormalizedSignature() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed,byte)").normalizedSignature)
                .isEqualTo("transfer(address,uint256,int256,fixed128x18,ufixed128x18,bytes1)")
    }

    @Test
    fun normalizingWorksForArrays() {
        assertThat(TextMethodSignature("multisetProofType(uint[],address[])").normalizedSignature)
                .isEqualTo("multisetProofType(uint256[],address[])")
    }
    @Test
    fun canGetFunctionName() {
        assertThat(TextMethodSignature(" yolo (address,uint,int,fixed,ufixed)").functionName)
                .isEqualTo("yolo")
    }

}