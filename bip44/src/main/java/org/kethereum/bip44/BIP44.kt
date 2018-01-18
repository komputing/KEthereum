package org.kethereum.bip44

/*
BIP44 as in https://github.com/bitcoin/bips/blob/master/bip-0044.mediawiki
 */

data class BIP44Element(val hardened: Boolean, val number: Int)
data class BIP44(val path: List<BIP44Element>) {
    companion object {

        const val HARDENING_FLAG = 0x80000000.toInt()
        fun isHardened(element: Int): Boolean  = (element and HARDENING_FLAG != 0)

        fun fromPath(path: String): BIP44 {
            if (!path.trim().startsWith("m/")) {
                throw (IllegalArgumentException("Must start with m/"))
            }
            val cleanPath = path.replace("m/", "").replace(" ", "")
            return BIP44(cleanPath.split("/")
                    .filter { it.isNotEmpty() }
                    .map {
                        BIP44Element(
                                hardened = it.contains("'"),
                                number = it.replace("'", "").toIntOrNull() ?:
                                        throw IllegalArgumentException("not a number " + it)
                        )
                    })
        }

    }

    override fun equals(other: Any?) = path == path
    override fun hashCode() = path.hashCode()

    fun toIntList() = path.map { if (it.hardened) it.number or HARDENING_FLAG else it.number }
    override fun toString() = "m/" + path.joinToString("/") {
        if (it.hardened) "${it.number}'" else "${it.number}"
    }

    fun increment()
            = BIP44(path.subList(0, path.size - 1) +
            path.last().let { BIP44Element(it.hardened, it.number + 1) })
}