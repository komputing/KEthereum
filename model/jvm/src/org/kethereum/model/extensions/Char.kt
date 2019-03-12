package org.kethereum.model.extensions

/**
 * Returns the numeric value of the character {@code ch} in the
 * specified radix.
 * <p>
 * If the radix is not in the range {@code MIN_RADIX} &le;
 * {@code radix} &le; {@code MAX_RADIX} or if the
 * value of {@code ch} is not a valid digit in the specified
 * radix, {@code -1} is returned. A character is a valid digit
 * if at least one of the following is true:
 * <ul>
 * <li>The method {@code isDigit} is {@code true} of the character
 *     and the Unicode decimal digit value of the character (or its
 *     single-character decomposition) is less than the specified radix.
 *     In this case the decimal digit value is returned.
 * <li>The character is one of the uppercase Latin letters
 *     {@code 'A'} through {@code 'Z'} and its code is less than
 *     {@code radix + 'A' - 10}.
 *     In this case, {@code ch - 'A' + 10}
 *     is returned.
 * <li>The character is one of the lowercase Latin letters
 *     {@code 'a'} through {@code 'z'} and its code is less than
 *     {@code radix + 'a' - 10}.
 *     In this case, {@code ch - 'a' + 10}
 *     is returned.
 * <li>The character is one of the fullwidth uppercase Latin letters A
 *     ({@code '\u005CuFF21'}) through Z ({@code '\u005CuFF3A'})
 *     and its code is less than
 *     {@code radix + '\u005CuFF21' - 10}.
 *     In this case, {@code ch - '\u005CuFF21' + 10}
 *     is returned.
 * <li>The character is one of the fullwidth lowercase Latin letters a
 *     ({@code '\u005CuFF41'}) through z ({@code '\u005CuFF5A'})
 *     and its code is less than
 *     {@code radix + '\u005CuFF41' - 10}.
 *     In this case, {@code ch - '\u005CuFF41' + 10}
 *     is returned.
 * </ul>
 *
 * @param   radix   the radix.
 * @return  the numeric value represented by the character in the
 *          specified radix.
 */
actual fun Char.digit(radix: Int): Int {
    return Character.digit(this, radix)
}