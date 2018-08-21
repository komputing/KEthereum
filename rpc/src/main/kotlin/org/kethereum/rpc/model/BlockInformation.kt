package org.kethereum.rpc.model

import org.kethereum.model.SignedTransaction

data class BlockInformation(val transactions: List<SignedTransaction>)