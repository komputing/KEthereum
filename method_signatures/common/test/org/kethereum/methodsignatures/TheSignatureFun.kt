package org.kethereum.methodsignatures

import org.kethereum.methodsignatures.model.TextMethodSignature
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TheSignatureFun {

    @Test
    fun canCalculateSignature() {
        assertEquals(TextMethodSignature("transfer(address,uint256)").toHexSignature().hex, "a9059cbb")
    }

    @Test
    fun canCalculateSignatureAndDoesNotGetConfusedByTabsAndSpaces() {
        assertEquals(TextMethodSignature("\ttransfer \t( address,\tuint256 ) ").toHexSignature().hex, "a9059cbb")
    }

    @Test
    fun normalizingWorks() {
        assertEquals(
            TextMethodSignature("\ttransfer \t( address,\tuint256 ) ").normalizedSignature,
            "transfer(address,uint256)"
        )
    }

    @Test
    fun replacementsOfSynonymsHappen() {
        assertEquals(
            TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignature().hex,
            TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex
        )
    }

    @Test
    fun replacementsOfSynonymsReflectedInNormalizedSignature() {
        assertEquals(
            TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").normalizedSignature,
            "transfer(address,uint256,int256,fixed128x18,ufixed128x18)"
        )
    }

    @Test
    fun canGetFunctionName() {
        assertEquals(
            TextMethodSignature(" yolo (address,uint,int,fixed,ufixed)").functionName,
            "yolo"
        )
    }

    @Test
    fun canGetParameters() {
        assertEquals(
            TextMethodSignature(" yolo (address,uint)").parameters,
            listOf("address","uint")
        )
    }

    @Test
    fun canGetNormalizedParameters() {
        assertEquals(
            TextMethodSignature(" yolo (address,uint)").normalizedParameters,
            listOf("address","uint256")
        )
    }


    @Test
    fun replacementsOfSynonymsDontHappenWhenUnsafeUsed() {
        assertNotEquals(
            TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignatureUnsafe().hex,
            TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex
        )
    }
}