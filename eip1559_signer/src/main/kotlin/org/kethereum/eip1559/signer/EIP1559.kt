package org.kethereum.eip1559.signer

import org.kethereum.crypto.signMessage
import org.kethereum.extensions.transactions.encodeAsEIP1559Tx
import org.kethereum.model.ECKeyPair
import org.kethereum.model.Transaction


/**
 * Signs a transaction via EIP1559
 *
 * @return SignatureData - the signature of the transaction signed with the key
 *
 */

fun Transaction.signViaEIP1559(key: ECKeyPair) = key.signMessage(encodeAsEIP1559Tx())