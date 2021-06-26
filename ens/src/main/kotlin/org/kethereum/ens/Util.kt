package org.kethereum.ens

import org.kethereum.eip137.model.ENSName

fun ENSName.isPotentialENSDomain() = string.split(".").filter { it.isNotBlank() }.size > 1