package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.kethereum.methodsignatures.model.TextMethodSignature

class TheSignatureFun {

    @Test
    fun canCalculateSignature() {
        // from ERC-20
        assertThat(TextMethodSignature("transfer(address,uint256)").toHexSignature().hex).isEqualTo("a9059cbb")
    }

    @Test
    fun replacementsOfSynonymsHappen() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignature().hex)
                .isEqualTo(TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex)
    }


    @Test
    fun replacementsOfSynonymsDontHappenWhenUnsafeUsed() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignatureUnsafe().hex)
                .isNotEqualTo(TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex)
    }



}