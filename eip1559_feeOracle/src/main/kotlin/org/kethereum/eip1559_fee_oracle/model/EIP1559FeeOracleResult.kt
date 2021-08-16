package org.kethereum.eip1559_fee_oracle.model

import java.math.BigInteger

data class EIP1559FeeOracleResult(var maxFeePerGas: BigInteger, var maxPriorityFeePerGas: BigInteger)