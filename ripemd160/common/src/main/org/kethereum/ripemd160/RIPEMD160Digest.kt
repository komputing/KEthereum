package org.kethereum.ripemd160

import java.nio.charset.StandardCharsets.US_ASCII

const val RIPEMD160_DIGEST_LENGTH = 20

fun calculateRIPEMD160fromByteArray(inputArray: ByteArray) = ByteArray(RIPEMD160_DIGEST_LENGTH).apply {
    val digest = RIPEMD160Digest()
    digest.update(inputArray, 0, inputArray.size)

    digest.doFinal(this, 0)
}

fun ByteArray.calculateRIPEMD160() = calculateRIPEMD160fromByteArray(this)

fun String.calculateRIPEMD160() = toByteArray(US_ASCII).calculateRIPEMD160()

/**
 * implementation of RIPEMD see,
 * http://www.esat.kuleuven.ac.be/~bosselae/ripemd160.html
 */
class RIPEMD160Digest {

    private var h0: Int = 0
    private var h1: Int = 0
    private var h2: Int = 0
    private var h3: Int = 0
    private var h4: Int = 0 // IV's

    private val x = IntArray(16)
    private var xOff: Int = 0

    private val xBuf = ByteArray(4)
    private var xBufOff: Int = 0

    private var byteCount: Long = 0

    init {
        reset()
    }

    fun update(input: Byte) {
        xBuf[xBufOff++] = input

        if (xBufOff == xBuf.size) {
            processWord(xBuf, 0)
            xBufOff = 0
        }

        byteCount++
    }

    fun update(input: ByteArray, inputOffset: Int, len: Int) {
        var pos = inputOffset
        var toProcess = len
        //
        // fill the current word
        //
        while (xBufOff != 0 && toProcess > 0) {
            update(input[pos])

            pos++
            toProcess--
        }

        //
        // process whole words.
        //
        while (toProcess > xBuf.size) {
            processWord(input, pos)

            pos += xBuf.size
            toProcess -= xBuf.size
            byteCount += xBuf.size.toLong()
        }

        //
        // load in the remainder.
        //
        while (toProcess > 0) {
            update(input[pos])

            pos++
            toProcess--
        }
    }

    private fun finish() {
        val bitLength = byteCount shl 3

        //
        // add the pad bytes.
        //
        update(128.toByte())

        while (xBufOff != 0) {
            update(0.toByte())
        }

        processLength(bitLength)

        processBlock()
    }

    /**
     * reset the chaining variables to the IV values.
     */
    fun reset() {
        byteCount = 0

        xBufOff = 0
        for (i in xBuf.indices) {
            xBuf[i] = 0
        }

        h0 = 0x67452301
        h1 = -0x10325477
        h2 = -0x67452302
        h3 = 0x10325476
        h4 = -0x3c2d1e10

        xOff = 0

        for (i in x.indices) {
            x[i] = 0
        }
    }

    private fun processWord(inputWord: ByteArray, inputOffset: Int) {
        x[xOff++] = (inputWord[inputOffset].toInt() and 0xff or (inputWord[inputOffset + 1].toInt() and 0xff shl 8)
                or (inputWord[inputOffset + 2].toInt() and 0xff shl 16) or (inputWord[inputOffset + 3].toInt() and 0xff shl 24))

        if (xOff == 16) {
            processBlock()
        }
    }

    private fun processLength(bitLength: Long) {
        if (xOff > 14) {
            processBlock()
        }

        x[14] = (bitLength and -0x1).toInt()
        x[15] = bitLength.ushr(32).toInt()
    }

    private fun unpackWord(word: Int, out: ByteArray, outOffset: Int) {
        out[outOffset] = word.toByte()
        out[outOffset + 1] = word.ushr(8).toByte()
        out[outOffset + 2] = word.ushr(16).toByte()
        out[outOffset + 3] = word.ushr(24).toByte()
    }

    fun doFinal(out: ByteArray, outOffset: Int): Int {
        finish()

        unpackWord(h0, out, outOffset)
        unpackWord(h1, out, outOffset + 4)
        unpackWord(h2, out, outOffset + 8)
        unpackWord(h3, out, outOffset + 12)
        unpackWord(h4, out, outOffset + 16)

        reset()

        return RIPEMD160_DIGEST_LENGTH
    }

    /*
     * rotate int x left n bits.
     */
    private fun rotateLeft(x: Int, n: Int) = x shl n or x.ushr(32 - n)

    /*
     * f1,f2,f3,f4,f5 are the basic RIPEMD160 functions.
     */

    /*
     * rounds 0-15
     */
    private fun f1(x: Int, y: Int, z: Int) = x xor y xor z

    /*
     * rounds 16-31
     */
    private fun f2(x: Int, y: Int, z: Int) = x and y or (x.inv() and z)

    /*
     * rounds 32-47
     */
    private fun f3(x: Int, y: Int, z: Int) = x or y.inv() xor z

    /*
     * rounds 48-63
     */
    private fun f4(x: Int, y: Int, z: Int) = x and z or (y and z.inv())

    /*
     * rounds 64-79
     */
    private fun f5(x: Int, y: Int, z: Int) = x xor (y or z.inv())

    private fun processBlock() {
        var aa = h0
        var a = aa
        var bb = h1
        var b = bb
        var cc = h2
        var c = cc
        var dd = h3
        var d = dd
        var ee = h4
        var e = ee

        //
        // Rounds 1 - 16
        //
        // left
        a = rotateLeft(a + f1(b, c, d) + x[0], 11) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f1(a, b, c) + x[1], 14) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f1(e, a, b) + x[2], 15) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f1(d, e, a) + x[3], 12) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f1(c, d, e) + x[4], 5) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f1(b, c, d) + x[5], 8) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f1(a, b, c) + x[6], 7) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f1(e, a, b) + x[7], 9) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f1(d, e, a) + x[8], 11) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f1(c, d, e) + x[9], 13) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f1(b, c, d) + x[10], 14) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f1(a, b, c) + x[11], 15) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f1(e, a, b) + x[12], 6) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f1(d, e, a) + x[13], 7) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f1(c, d, e) + x[14], 9) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f1(b, c, d) + x[15], 8) + e
        c = rotateLeft(c, 10)

        // right
        aa = rotateLeft(aa + f5(bb, cc, dd) + x[5] + 0x50a28be6, 8) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f5(aa, bb, cc) + x[14] + 0x50a28be6, 9) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f5(ee, aa, bb) + x[7] + 0x50a28be6, 9) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f5(dd, ee, aa) + x[0] + 0x50a28be6, 11) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f5(cc, dd, ee) + x[9] + 0x50a28be6, 13) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f5(bb, cc, dd) + x[2] + 0x50a28be6, 15) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f5(aa, bb, cc) + x[11] + 0x50a28be6, 15) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f5(ee, aa, bb) + x[4] + 0x50a28be6, 5) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f5(dd, ee, aa) + x[13] + 0x50a28be6, 7) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f5(cc, dd, ee) + x[6] + 0x50a28be6, 7) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f5(bb, cc, dd) + x[15] + 0x50a28be6, 8) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f5(aa, bb, cc) + x[8] + 0x50a28be6, 11) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f5(ee, aa, bb) + x[1] + 0x50a28be6, 14) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f5(dd, ee, aa) + x[10] + 0x50a28be6, 14) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f5(cc, dd, ee) + x[3] + 0x50a28be6, 12) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f5(bb, cc, dd) + x[12] + 0x50a28be6, 6) + ee
        cc = rotateLeft(cc, 10)

        //
        // Rounds 16-31
        //
        // left
        e = rotateLeft(e + f2(a, b, c) + x[7] + 0x5a827999, 7) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f2(e, a, b) + x[4] + 0x5a827999, 6) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f2(d, e, a) + x[13] + 0x5a827999, 8) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f2(c, d, e) + x[1] + 0x5a827999, 13) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f2(b, c, d) + x[10] + 0x5a827999, 11) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f2(a, b, c) + x[6] + 0x5a827999, 9) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f2(e, a, b) + x[15] + 0x5a827999, 7) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f2(d, e, a) + x[3] + 0x5a827999, 15) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f2(c, d, e) + x[12] + 0x5a827999, 7) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f2(b, c, d) + x[0] + 0x5a827999, 12) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f2(a, b, c) + x[9] + 0x5a827999, 15) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f2(e, a, b) + x[5] + 0x5a827999, 9) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f2(d, e, a) + x[2] + 0x5a827999, 11) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f2(c, d, e) + x[14] + 0x5a827999, 7) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f2(b, c, d) + x[11] + 0x5a827999, 13) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f2(a, b, c) + x[8] + 0x5a827999, 12) + d
        b = rotateLeft(b, 10)

        // right
        ee = rotateLeft(ee + f4(aa, bb, cc) + x[6] + 0x5c4dd124, 9) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f4(ee, aa, bb) + x[11] + 0x5c4dd124, 13) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f4(dd, ee, aa) + x[3] + 0x5c4dd124, 15) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f4(cc, dd, ee) + x[7] + 0x5c4dd124, 7) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f4(bb, cc, dd) + x[0] + 0x5c4dd124, 12) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f4(aa, bb, cc) + x[13] + 0x5c4dd124, 8) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f4(ee, aa, bb) + x[5] + 0x5c4dd124, 9) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f4(dd, ee, aa) + x[10] + 0x5c4dd124, 11) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f4(cc, dd, ee) + x[14] + 0x5c4dd124, 7) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f4(bb, cc, dd) + x[15] + 0x5c4dd124, 7) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f4(aa, bb, cc) + x[8] + 0x5c4dd124, 12) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f4(ee, aa, bb) + x[12] + 0x5c4dd124, 7) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f4(dd, ee, aa) + x[4] + 0x5c4dd124, 6) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f4(cc, dd, ee) + x[9] + 0x5c4dd124, 15) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f4(bb, cc, dd) + x[1] + 0x5c4dd124, 13) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f4(aa, bb, cc) + x[2] + 0x5c4dd124, 11) + dd
        bb = rotateLeft(bb, 10)

        //
        // Rounds 32-47
        //
        // left
        d = rotateLeft(d + f3(e, a, b) + x[3] + 0x6ed9eba1, 11) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f3(d, e, a) + x[10] + 0x6ed9eba1, 13) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f3(c, d, e) + x[14] + 0x6ed9eba1, 6) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f3(b, c, d) + x[4] + 0x6ed9eba1, 7) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f3(a, b, c) + x[9] + 0x6ed9eba1, 14) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f3(e, a, b) + x[15] + 0x6ed9eba1, 9) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f3(d, e, a) + x[8] + 0x6ed9eba1, 13) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f3(c, d, e) + x[1] + 0x6ed9eba1, 15) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f3(b, c, d) + x[2] + 0x6ed9eba1, 14) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f3(a, b, c) + x[7] + 0x6ed9eba1, 8) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f3(e, a, b) + x[0] + 0x6ed9eba1, 13) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f3(d, e, a) + x[6] + 0x6ed9eba1, 6) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f3(c, d, e) + x[13] + 0x6ed9eba1, 5) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f3(b, c, d) + x[11] + 0x6ed9eba1, 12) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f3(a, b, c) + x[5] + 0x6ed9eba1, 7) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f3(e, a, b) + x[12] + 0x6ed9eba1, 5) + c
        a = rotateLeft(a, 10)

        // right
        dd = rotateLeft(dd + f3(ee, aa, bb) + x[15] + 0x6d703ef3, 9) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f3(dd, ee, aa) + x[5] + 0x6d703ef3, 7) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f3(cc, dd, ee) + x[1] + 0x6d703ef3, 15) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f3(bb, cc, dd) + x[3] + 0x6d703ef3, 11) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f3(aa, bb, cc) + x[7] + 0x6d703ef3, 8) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f3(ee, aa, bb) + x[14] + 0x6d703ef3, 6) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f3(dd, ee, aa) + x[6] + 0x6d703ef3, 6) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f3(cc, dd, ee) + x[9] + 0x6d703ef3, 14) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f3(bb, cc, dd) + x[11] + 0x6d703ef3, 12) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f3(aa, bb, cc) + x[8] + 0x6d703ef3, 13) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f3(ee, aa, bb) + x[12] + 0x6d703ef3, 5) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f3(dd, ee, aa) + x[2] + 0x6d703ef3, 14) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f3(cc, dd, ee) + x[10] + 0x6d703ef3, 13) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f3(bb, cc, dd) + x[0] + 0x6d703ef3, 13) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f3(aa, bb, cc) + x[4] + 0x6d703ef3, 7) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f3(ee, aa, bb) + x[13] + 0x6d703ef3, 5) + cc
        aa = rotateLeft(aa, 10)

        //
        // Rounds 48-63
        //
        // left
        c = rotateLeft(c + f4(d, e, a) + x[1] + -0x70e44324, 11) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f4(c, d, e) + x[9] + -0x70e44324, 12) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f4(b, c, d) + x[11] + -0x70e44324, 14) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f4(a, b, c) + x[10] + -0x70e44324, 15) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f4(e, a, b) + x[0] + -0x70e44324, 14) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f4(d, e, a) + x[8] + -0x70e44324, 15) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f4(c, d, e) + x[12] + -0x70e44324, 9) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f4(b, c, d) + x[4] + -0x70e44324, 8) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f4(a, b, c) + x[13] + -0x70e44324, 9) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f4(e, a, b) + x[3] + -0x70e44324, 14) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f4(d, e, a) + x[7] + -0x70e44324, 5) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f4(c, d, e) + x[15] + -0x70e44324, 6) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f4(b, c, d) + x[14] + -0x70e44324, 8) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f4(a, b, c) + x[5] + -0x70e44324, 6) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f4(e, a, b) + x[6] + -0x70e44324, 5) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f4(d, e, a) + x[2] + -0x70e44324, 12) + b
        e = rotateLeft(e, 10)

        // right
        cc = rotateLeft(cc + f2(dd, ee, aa) + x[8] + 0x7a6d76e9, 15) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f2(cc, dd, ee) + x[6] + 0x7a6d76e9, 5) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f2(bb, cc, dd) + x[4] + 0x7a6d76e9, 8) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f2(aa, bb, cc) + x[1] + 0x7a6d76e9, 11) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f2(ee, aa, bb) + x[3] + 0x7a6d76e9, 14) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f2(dd, ee, aa) + x[11] + 0x7a6d76e9, 14) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f2(cc, dd, ee) + x[15] + 0x7a6d76e9, 6) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f2(bb, cc, dd) + x[0] + 0x7a6d76e9, 14) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f2(aa, bb, cc) + x[5] + 0x7a6d76e9, 6) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f2(ee, aa, bb) + x[12] + 0x7a6d76e9, 9) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f2(dd, ee, aa) + x[2] + 0x7a6d76e9, 12) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f2(cc, dd, ee) + x[13] + 0x7a6d76e9, 9) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f2(bb, cc, dd) + x[9] + 0x7a6d76e9, 12) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f2(aa, bb, cc) + x[7] + 0x7a6d76e9, 5) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f2(ee, aa, bb) + x[10] + 0x7a6d76e9, 15) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f2(dd, ee, aa) + x[14] + 0x7a6d76e9, 8) + bb
        ee = rotateLeft(ee, 10)

        //
        // Rounds 64-79
        //
        // left
        b = rotateLeft(b + f5(c, d, e) + x[4] + -0x56ac02b2, 9) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f5(b, c, d) + x[0] + -0x56ac02b2, 15) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f5(a, b, c) + x[5] + -0x56ac02b2, 5) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f5(e, a, b) + x[9] + -0x56ac02b2, 11) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f5(d, e, a) + x[7] + -0x56ac02b2, 6) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f5(c, d, e) + x[12] + -0x56ac02b2, 8) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f5(b, c, d) + x[2] + -0x56ac02b2, 13) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f5(a, b, c) + x[10] + -0x56ac02b2, 12) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f5(e, a, b) + x[14] + -0x56ac02b2, 5) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f5(d, e, a) + x[1] + -0x56ac02b2, 12) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f5(c, d, e) + x[3] + -0x56ac02b2, 13) + a
        d = rotateLeft(d, 10)
        a = rotateLeft(a + f5(b, c, d) + x[8] + -0x56ac02b2, 14) + e
        c = rotateLeft(c, 10)
        e = rotateLeft(e + f5(a, b, c) + x[11] + -0x56ac02b2, 11) + d
        b = rotateLeft(b, 10)
        d = rotateLeft(d + f5(e, a, b) + x[6] + -0x56ac02b2, 8) + c
        a = rotateLeft(a, 10)
        c = rotateLeft(c + f5(d, e, a) + x[15] + -0x56ac02b2, 5) + b
        e = rotateLeft(e, 10)
        b = rotateLeft(b + f5(c, d, e) + x[13] + -0x56ac02b2, 6) + a
        d = rotateLeft(d, 10)

        // right
        bb = rotateLeft(bb + f1(cc, dd, ee) + x[12], 8) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f1(bb, cc, dd) + x[15], 5) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f1(aa, bb, cc) + x[10], 12) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f1(ee, aa, bb) + x[4], 9) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f1(dd, ee, aa) + x[1], 12) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f1(cc, dd, ee) + x[5], 5) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f1(bb, cc, dd) + x[8], 14) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f1(aa, bb, cc) + x[7], 6) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f1(ee, aa, bb) + x[6], 8) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f1(dd, ee, aa) + x[2], 13) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f1(cc, dd, ee) + x[13], 6) + aa
        dd = rotateLeft(dd, 10)
        aa = rotateLeft(aa + f1(bb, cc, dd) + x[14], 5) + ee
        cc = rotateLeft(cc, 10)
        ee = rotateLeft(ee + f1(aa, bb, cc) + x[0], 15) + dd
        bb = rotateLeft(bb, 10)
        dd = rotateLeft(dd + f1(ee, aa, bb) + x[3], 13) + cc
        aa = rotateLeft(aa, 10)
        cc = rotateLeft(cc + f1(dd, ee, aa) + x[9], 11) + bb
        ee = rotateLeft(ee, 10)
        bb = rotateLeft(bb + f1(cc, dd, ee) + x[11], 11) + aa
        dd = rotateLeft(dd, 10)

        dd += c + h1
        h1 = h2 + d + ee
        h2 = h3 + e + aa
        h3 = h4 + a + bb
        h4 = h0 + b + cc
        h0 = dd

        //
        // reset the offset and clean out the word buffer.
        //
        xOff = 0
        for (i in x.indices) {
            x[i] = 0
        }
    }

}
