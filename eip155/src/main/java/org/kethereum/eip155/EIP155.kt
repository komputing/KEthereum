package org.kethereum.eip155

import org.kethereum.model.SignatureData

/*
*
* https://github.com/ethereum/EIPs/blob/master/EIPS/eip-155.md
*
*/


/**
 * extracts the ChainID from SignatureData v
 *
 * @return ChainID or null when not EIP155 signed
 *
 */
fun SignatureData.extractChainID() = if (v < 37) { // not EIP 155 signed
    null
} else {
    (v - 35) / 2
}
