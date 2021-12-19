package org.kethereum.eip1559_fee_oracle

/*
Economical Fee Oracle
EIP-1559 transaction fee parameter suggestion algorithm based on the eth_feeHistory API
based on: https://github.com/zsfelfoldi/feehistory
*/

import org.kethereum.eip1559_fee_oracle.model.EIP1559FeeOracleResult
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.rpc.EthereumRPC
import org.komputing.khex.model.HexString
import java.lang.Integer.parseInt
import java.math.BigDecimal
import java.math.BigInteger
import java.math.BigInteger.ZERO
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor

private const val sampleMinPercentile = 10          // sampled percentile range of exponentially weighted baseFee history
private const val sampleMaxPercentile = 30

private const val rewardPercentile = 10             // effective reward value to be selected from each individual block
private const val rewardBlockPercentile = 40        // suggested priority fee to be selected from sorted individual block reward percentiles

private const val maxTimeFactor = 15                // highest timeFactor index in the returned list of suggestion
private const val extraPriorityFeeRatio = 0.25      // extra priority fee offered in case of expected baseFee rise
private const val fallbackPriorityFee = 2000000000L // priority fee offered when there are no recent transactions

fun suggestEIP1559Fees(rpc: EthereumRPC): Map<Int, EIP1559FeeOracleResult> {
    val feeHistory = rpc.getFeeHistory(100)

    val baseFee: Array<BigInteger> = feeHistory!!.baseFeePerGas.map {
        HexString(it).hexToBigInteger()
    }.toTypedArray()

    baseFee[baseFee.size - 1] = (baseFee[baseFee.size - 1].toBigDecimal() * BigDecimal(9 / 8.0)).toBigInteger()

    ((feeHistory.gasUsedRatio.size - 1) downTo 0).forEach { i ->
        if (feeHistory.gasUsedRatio[i] > 0.9) {
            baseFee[i] = baseFee[i + 1]
        }
    }

    val order = (0..feeHistory.gasUsedRatio.size).map { it }.sortedBy { baseFee[it] }

    val priorityFee = suggestPriorityFee(parseInt(feeHistory.oldestBlock.removePrefix("0x"), 16), feeHistory.gasUsedRatio, rpc)

    var maxBaseFee = ZERO
    val result = mutableMapOf<Int, EIP1559FeeOracleResult>()
    (maxTimeFactor downTo 0).forEach { timeFactor ->
        var bf = predictMinBaseFee(baseFee, order, timeFactor.toDouble())
        var t = BigDecimal(priorityFee)
        if (bf > maxBaseFee) {
            maxBaseFee = bf
        } else {
            // If a narrower time window yields a lower base fee suggestion than a wider window then we are probably in a price dip.
            // In this case getting included with a low priority fee is not guaranteed; instead we use the higher base fee suggestion
            // and also offer extra priority fee to increase the chance of getting included in the base fee dip.
            t += BigDecimal(maxBaseFee - bf) * BigDecimal.valueOf(extraPriorityFeeRatio)
            bf = maxBaseFee
        }
        result[timeFactor] = EIP1559FeeOracleResult(bf + t.toBigInteger(), t.toBigInteger())
    }

    return result
}

internal fun predictMinBaseFee(baseFee: Array<BigInteger>, order: List<Int>, timeFactor: Double): BigInteger {
    if (timeFactor < 1e-6) {
        return baseFee.last()
    }
    val pendingWeight = (1 - exp(-1 / timeFactor)) / (1 - exp(-baseFee.size / timeFactor))
    var sumWeight = .0
    var result = ZERO
    var samplingCurveLast = .0
    order.indices.forEach { i ->
        sumWeight += pendingWeight * exp((order[i] - baseFee.size + 1) / timeFactor)
        val samplingCurveValue = samplingCurve(sumWeight * 100.0)
        result += ((samplingCurveValue - samplingCurveLast) * baseFee[order[i]].toDouble()).toBigDecimal().toBigInteger()
        if (samplingCurveValue >= 1) {
            return result
        }
        samplingCurveLast = samplingCurveValue
    }
    return result
}

internal fun suggestPriorityFee(firstBlock: Int, gasUsedRatio: List<Float>, rpc: EthereumRPC): BigInteger {
    var ptr = gasUsedRatio.size - 1
    var needBlocks = 5
    val rewards = mutableListOf<BigInteger>()
    while (needBlocks > 0 && ptr >= 0) {
        val blockCount = maxBlockCount(gasUsedRatio, ptr, needBlocks)
        if (blockCount > 0) {
            // feeHistory API call with reward percentile specified is expensive and therefore is only requested for a few non-full recent blocks.
            val feeHistory = rpc.getFeeHistory(blockCount, "0x" + (firstBlock + ptr).toString(16), rewardPercentile.toString())

            val rewardSize = feeHistory?.reward?.size ?: 0
            (0 until rewardSize).forEach {
                rewards.add(HexString(feeHistory!!.reward!![it].first().removePrefix("0x")).hexToBigInteger())
            }
            if (rewardSize < blockCount) break
            needBlocks -= blockCount
        }
        ptr -= blockCount + 1
    }

    if (rewards.isEmpty()) {
        return BigInteger.valueOf(fallbackPriorityFee)
    }
    rewards.sort()
    return rewards[floor((rewards.size - 1) * rewardBlockPercentile / 100.0).toInt()]
}

internal fun maxBlockCount(gasUsedRatio: List<Float>, _ptr: Int, _needBlocks: Int): Int {
    var blockCount = 0
    var ptr = _ptr
    var needBlocks = _needBlocks
    while (needBlocks > 0 && ptr >= 0) {
        if (gasUsedRatio[ptr] == 0f || gasUsedRatio[ptr] > 0.9) {
            break
        }
        ptr--
        needBlocks--
        blockCount++
    }
    return blockCount
}

internal fun samplingCurve(percentile: Double): Double = when {
    (percentile <= sampleMinPercentile) -> 0.0
    (percentile >= sampleMaxPercentile) -> 1.0
    else -> (1 - cos((percentile - sampleMinPercentile) * 2 * Math.PI / (sampleMaxPercentile - sampleMinPercentile))) / 2
}

