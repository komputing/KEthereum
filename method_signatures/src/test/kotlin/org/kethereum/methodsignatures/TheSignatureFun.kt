package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.crypto.test_data.TEST_ADDRESSES
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.model.createTransactionWithDefaults
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString
import java.math.BigInteger

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
    fun replacementsOfSynonymsHappen() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignature().hex)
                .isEqualTo(TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex)
    }

    @Test
    fun replacementsOfSynonymsDontHappenWhenUnsafeUsed() {
        assertThat(TextMethodSignature("transfer(address,uint,int,fixed,ufixed)").toHexSignatureUnsafe().hex)
                .isNotEqualTo(TextMethodSignature("transfer(address,uint256,int256,fixed128x18,ufixed128x18)").toHexSignature().hex)
    }


    @Test
    fun canGetHexSignatureFromTransaction() {
        val tx = createTransactionWithDefaults(from = TEST_ADDRESSES.first(), to = TEST_ADDRESSES.last(), value = BigInteger.ZERO)
        assertThat(tx.copy(input = HexString("a9059cbbdeadbeef").hexToByteArray()).getHexSignature().hex).isEqualTo("a9059cbb")
    }
}