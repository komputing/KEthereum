package org.kethereum.bip44

/*
BIP44 as in https://github.com/bitcoin/bips/blob/master/bip-0044.mediawiki
 */

const val BIP44_HARDENING_FLAG = 0x80000000.toInt()

private fun getEnsuredCleanPath(path: String): String {
    if (!path.trim().startsWith("m/")) {
        throw (IllegalArgumentException("Must start with m/"))
    }
    return path.replace("m/", "").replace(" ", "")
}

data class BIP44Element(val hardened: Boolean, val number: Int) {
    val numberWithHardeningFlag = if (hardened) number or BIP44_HARDENING_FLAG else number
}

data class BIP44(val path: List<BIP44Element>) {
    constructor(path: String) : this(getEnsuredCleanPath(path).split("/")
            .asSequence()
            .filter { it.isNotEmpty() }
            .map {
                BIP44Element(
                        hardened = it.contains("'"),
                        number = it.replace("'", "").toIntOrNull()
                                ?: throw IllegalArgumentException("not a number $it")
                )
            }
            .toList())

    override fun equals(other: Any?) = (other as? BIP44)?.path == path
    override fun hashCode() = path.hashCode()
    override fun toString() = "m/" + path.joinToString("/") {
        if (it.hardened) "${it.number}'" else "${it.number}"
    }

    fun increment() = BIP44(path.subList(0, path.size - 1) +
            path.last().let { BIP44Element(it.hardened, it.number + 1) })

}

