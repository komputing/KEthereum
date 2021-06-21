package org.kethereum.eip1559.detector

import org.kethereum.model.Transaction
import org.kethereum.rpc.model.BlockInformation

fun Transaction.isEIP1559() = gasPrice == null && maxPriorityFeePerGas != null && maxFeePerGas != null

fun BlockInformation.isEIP1559() = baseFeePerGas != null