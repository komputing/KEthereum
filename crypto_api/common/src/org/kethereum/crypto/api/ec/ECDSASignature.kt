package org.kethereum.crypto.api.ec

import org.kethereum.model.number.BigInteger

data class ECDSASignature(val r: BigInteger, val s: BigInteger)
