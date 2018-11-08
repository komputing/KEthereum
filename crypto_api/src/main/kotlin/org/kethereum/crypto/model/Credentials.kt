package org.kethereum.crypto.model

import org.kethereum.model.Address

data class Credentials(val ecKeyPair: ECKeyPair?, val address: Address)
