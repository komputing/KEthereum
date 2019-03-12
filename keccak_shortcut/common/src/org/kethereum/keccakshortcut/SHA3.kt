package org.kethereum.keccakshortcut

import kotlinx.io.core.toByteArray
import org.kethereum.model.extensions.copy
import org.kethereum.model.extensions.fillWith
import org.kethereum.model.extensions.parseInt
import org.kethereum.model.number.BigInteger
import org.kethereum.model.number.BigInteger.Companion.ONE
import org.kethereum.model.number.BigInteger.Companion.ZERO
import kotlin.math.max
import kotlin.math.min

// TODO: Delete this class once WallETH SHA3 is multiplatform compliant

private val BIT_65 = ONE.shiftLeft(64)
private val MAX_64_BITS = BIT_65.minus(ONE)

fun String.calculateSHA3(parameter: SHA3Parameter) = toByteArray().calculateSHA3(parameter)

fun ByteArray.calculateSHA3(parameter: SHA3Parameter): ByteArray {
    val uState = IntArray(200)
    val uMessage = convertToUInt(this)

    var blockSize = 0
    var inputOffset = 0

    // Absorbing phase
    while (inputOffset < uMessage.size) {
        blockSize = min(uMessage.size - inputOffset, parameter.rateInBytes)
        for (i in 0 until blockSize) {
            uState[i] = uState[i] xor uMessage[i + inputOffset]
        }

        inputOffset += blockSize

        if (blockSize == parameter.rateInBytes) {
            doF(uState)
            blockSize = 0
        }
    }

    // Padding phase
    uState[blockSize] = uState[blockSize] xor parameter.d
    if (parameter.d and 0x80 != 0 && blockSize == parameter.rateInBytes - 1) {
        doF(uState)
    }

    uState[parameter.rateInBytes - 1] = uState[parameter.rateInBytes - 1] xor 0x80
    doF(uState)

    // Squeezing phase
    val byteResults = mutableListOf<Byte>()
    var tOutputLen = parameter.outputLengthInBytes
    while (tOutputLen > 0) {
        blockSize = min(tOutputLen, parameter.rateInBytes)
        for (i in 0 until blockSize) {
            byteResults.add(uState[i].toByte().toInt().toByte())
        }

        tOutputLen -= blockSize
        if (tOutputLen > 0) {
            doF(uState)
        }
    }

    return byteResults.toByteArray()
}

private fun doF(uState: IntArray) {
    val lState = Array(5) { Array(5) { ZERO } }

    for (i in 0..4) {
        for (j in 0..4) {
            val data = IntArray(8)
            uState.copy(8 * (i + 5 * j), data, 0, data.size)
            lState[i][j] = convertFromLittleEndianTo64(data)
        }
    }
    roundB(lState)

    uState.fillWith(0)
    for (i in 0..4) {
        for (j in 0..4) {
            val data = convertFrom64ToLittleEndian(lState[i][j])
            data.copy(0, uState, 8 * (i + 5 * j), data.size)
        }
    }

}

/**
 * Permutation on the given state.
 *
 * @param state state
 */
private fun roundB(state: Array<Array<BigInteger>>) {
    var lfsrState = 1
    for (round in 0..23) {
        val c = arrayOfNulls<BigInteger>(5)
        val d = arrayOfNulls<BigInteger>(5)

        // θ step
        for (i in 0..4) {
            c[i] = state[i][0].xor(state[i][1]).xor(state[i][2]).xor(state[i][3]).xor(state[i][4])
        }

        for (i in 0..4) {
            d[i] = c[(i + 4) % 5]!!.xor(c[(i + 1) % 5]!!.leftRotate64(1))
        }

        for (i in 0..4) {
            for (j in 0..4) {
                state[i][j] = state[i][j].xor(d[i])
            }
        }

        //ρ and π steps
        var x = 1
        var y = 0
        var current = state[x][y]
        for (i in 0..23) {
            val tX = x
            x = y
            y = (2 * tX + 3 * y) % 5

            val shiftValue = current
            current = state[x][y]

            state[x][y] = shiftValue.leftRotate64Safely((i + 1) * (i + 2) / 2)
        }

        //χ step
        for (j in 0..4) {
            val t = arrayOfNulls<BigInteger>(5)
            for (i in 0..4) {
                t[i] = state[i][j]
            }

            for (i in 0..4) {
                // ~t[(i + 1) % 5]
                val invertVal = t[(i + 1) % 5]!!.xor(MAX_64_BITS)
                // t[i] ^ ((~t[(i + 1) % 5]) & t[(i + 2) % 5])
                state[i][j] = t[i]!!.xor(invertVal.and(t[(i + 2) % 5]))
            }
        }

        //ι step
        for (i in 0..6) {
            lfsrState = (lfsrState shl 1 xor (lfsrState shr 7) * 0x71) % 256
            // pow(2, i) - 1
            val bitPosition = (1 shl i) - 1
            if (lfsrState and 2 != 0) {
                state[0][0] = state[0][0].xor(ONE.shiftLeft(bitPosition))
            }
        }
    }
}


internal fun String.fillWithZero(fillLength: Int) = plus("0".repeat(max(fillLength - length, 0)))

private fun convertToUInt(data: ByteArray) = IntArray(data.size) {
    data[it].toInt() and 0xFF
}

internal fun convertFromLittleEndianTo64(data: IntArray) =
    BigInteger(data.map { it.toString(16) }
        .map { if (it.length == 2) it else "0$it" }
        .reversed()
        .joinToString("")
        , 16)

private fun convertFrom64ToLittleEndian(uLong: BigInteger): IntArray {
    val asHex = uLong.toString(16)
    val asHexPadded = "0".repeat((8 * 2) - asHex.length) + asHex
    return IntArray(8) {
        ((7 - it) * 2).let { pos ->
            asHexPadded.substring(pos, pos + 2).parseInt(16)
        }
    }
}


private fun BigInteger.leftRotate64Safely(rotate: Int) = leftRotate64(rotate % 64)

private fun BigInteger.leftRotate64(rotate: Int) = shiftRight(64 - rotate)
    .add(shiftLeft(rotate))
    .mod(BIT_65)