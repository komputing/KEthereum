package org.kethereum.methodsignatures

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.kethereum.abi.model.EthereumFunction
import org.kethereum.abi.model.EthereumNamedType
import org.kethereum.methodsignatures.model.TextMethodSignature
import java.nio.file.Files

class TheFileBackedMethodSignatureStore {

    val storeDir = Files.createTempDirectory("sigtmp").toFile()
    val tested = FileBackedMethodSignatureStore(storeDir)


    @AfterEach
    internal fun cleanup() {
        storeDir.deleteRecursively()
    }

    @Test
    fun initiallyEmpty() {
        assertThat(tested.all()).isEmpty()
    }

    @Test
    fun canInsertOneSignature() {
        assertThat(tested.has("fababbcc")).isFalse()
        tested.upsert("fababbcc", "yolo")
        assertThat(tested.has("fababbcc")).isTrue()
        assertThat(tested.get("fababbcc")).containsExactly(TextMethodSignature("yolo"))

    }

    @Test
    fun canInsertOneSignatureViaABI() {
        val function = createEthereumFun(name = "yolo", inputs = listOf(EthereumNamedType("foo", "bool")))
        val fourByteSignature = function.toTextMethodSignature().toHexSignature().hex

        assertThat(tested.has(fourByteSignature)).isFalse()

        tested.upsert(function)
        assertThat(tested.has(fourByteSignature)).isTrue()
        assertThat(tested.get(fourByteSignature)).containsExactly(TextMethodSignature("yolo(bool)"))

    }

    @Test
    fun canInsertMultipleSignaturesForOneFourByte() {
        tested.upsert("fababbcc", "yolo")
        tested.upsert("fababbcc", "yolo2")
        assertThat(tested.get("fababbcc")).containsExactlyInAnyOrder(TextMethodSignature("yolo"), TextMethodSignature("yolo2"))
    }

    @Test
    fun canInsertMultipleSignaturesForDifferentFourBytes() {
        tested.upsert("fababbc1", "yolo")
        tested.upsert("fababbc2", "yolo2")
        assertThat(tested.get("fababbc1")).containsExactly(TextMethodSignature("yolo"))
        assertThat(tested.get("fababbc2")).containsExactly(TextMethodSignature("yolo2"))
    }

}