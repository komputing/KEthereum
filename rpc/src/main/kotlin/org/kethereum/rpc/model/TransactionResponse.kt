package org.kethereum.rpc.model

import org.kethereum.rpc.model.rpc.TransactionRPC

internal data class TransactionResponse(val result: TransactionRPC) : BaseResponse()