package org.kethereum.ens

import org.kethereum.eip137.ENSName

fun ENSName.isENSDomain() = ENS_SUPPORTED_TLDs.contains(string.substringAfterLast("."))