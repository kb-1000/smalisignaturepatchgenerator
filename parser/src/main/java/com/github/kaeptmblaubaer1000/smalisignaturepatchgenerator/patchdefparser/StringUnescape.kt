package com.github.kaeptmblaubaer1000.smalisignaturepatchgenerator.patchdefparser


// The following is https://gist.github.com/uklimaschewski/6741769 transformed with j2k, with removed Kotlin errors and no "\f" support.
// Also with added \$ support
/**
 * Unescapes a string that contains standard Java escape sequences.
 *
 *  * **&#92;b &#92;f &#92;n &#92;r &#92;t &#92;" &#92;'** :
 * BS, FF, NL, CR, TAB, double and single quote.
 *  * **&#92;X &#92;XX &#92;XXX** : Octal character
 * specification (0 - 377, 0x00 - 0xFF).
 *  * **&#92;uXXXX** : Hexadecimal based Unicode character.
 *
 *
 * @param st
 * A string optionally containing standard java escape sequences.
 * @return The translated string.
 */
fun unescapeJavaString(st: String): String {

    val sb = StringBuilder(st.length)

    var i = 0
    while (i < st.length) {
        var ch = st[i]
        if (ch == '\\') {
            val nextChar = if (i == st.length - 1)
                '\\'
            else
                st[i + 1]
            // Octal escape?
            if (nextChar >= '0' && nextChar <= '7') {
                var code = "" + nextChar
                i++
                if (i < st.length - 1 && st[i + 1] >= '0'
                        && st[i + 1] <= '7') {
                    code += st[i + 1]
                    i++
                    if (i < st.length - 1 && st[i + 1] >= '0'
                            && st[i + 1] <= '7') {
                        code += st[i + 1]
                        i++
                    }
                }
                sb.append(Integer.parseInt(code, 8).toChar())
                i++
                continue
            }
            if (nextChar == '\\') ch = '\\'
            else if (nextChar == 'b') ch = '\b'
            else if (nextChar == 'n') ch = '\n'
            else if (nextChar == 'r') ch = '\r'
            else if (nextChar == 't') ch = '\t'
            else if (nextChar == '\"') ch = '\"'
            else if (nextChar == '\'') ch = '\''
            else if (nextChar == '$') ch = '$'
            // Hex Unicode: u????
            else if (nextChar == 'u') {
                if (i >= st.length - 5) {
                    ch = 'u'
                    break
                }
                val code = Integer.parseInt(
                        "" + st[i + 2] + st[i + 3]
                                + st[i + 4] + st[i + 5], 16)
                sb.append(Character.toChars(code))
                i += 5
                i++
                continue
            }
            i++
        }
        sb.append(ch)
        i++
    }
    return sb.toString()
}

fun unquoteUnescapeJavaString(st: String) : String {
    return unescapeJavaString(st.slice(1..st.length-2))
}
