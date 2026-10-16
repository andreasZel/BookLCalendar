package com.andreaszel.booklcalendar.lookup

private val entities = mapOf("amp" to "&", "quot" to "\"", "apos" to "'", "lt" to "<", "gt" to ">", "nbsp" to " ")
private val blurb = Regex("(?i)bestsell|a novel|winner|author of|prize|edition|copyright")

fun cleanDescription(raw: String, max: Int = 700): String {
    val unlinked = raw.substringBefore("----------").replace(Regex("""\(\[source]\[\d+]\)"""), "").replace(Regex("""\[\d+]: \S+"""), "").replace(Regex("""\[([^\]]+)]\[\d+]"""), "$1")
    val plain = unlinked.replace(Regex("""(?i)<br\s*/?>|</p>"""), " ").replace(Regex("<[^>]+>"), "")
    val text = decodeEntities(plain).replace(Regex("""\s+"""), " ").trim()
    if (text.length <= max) return text
    val cut = text.substring(0, max)
    val end = Regex("""[.!?](?=\s)""").findAll(cut).lastOrNull()?.range?.last
    if (end != null && end >= max / 2) return cut.substring(0, end + 1)
    return cut.substringBeforeLast(' ').trimEnd(',', ';', ':') + "…"
}

fun normalizeIsbn(raw: String): String? {
    val clean = raw.uppercase().filter { it.isDigit() || it == 'X' }
    return when {
        clean.length == 13 && clean.all { it.isDigit() } && (clean.startsWith("978") || clean.startsWith("979")) -> clean
        clean.length == 10 && clean.dropLast(1).all { it.isDigit() } -> clean
        else -> null
    }
}

fun coverQuery(lines: List<CoverLine>): String {
    val words = lines.filter { line -> line.text.count { it.isLetter() } >= 2 && !blurb.containsMatchIn(line.text) }.sortedByDescending { it.height }.take(3).flatMap { it.text.split(Regex("""\s+""")) }.filter { it.isNotBlank() }
    return words.take(8).joinToString(" ")
}

private fun decodeEntities(text: String) = Regex("&(#x[0-9a-fA-F]+|#\\d+|[a-zA-Z]+);").replace(text) { m ->
    val code = m.groupValues[1]
    val decoded = when {
        code.startsWith("#x") -> code.drop(2).toIntOrNull(16)?.let { String(Character.toChars(it)) }
        code.startsWith("#") -> code.drop(1).toIntOrNull()?.let { String(Character.toChars(it)) }
        else -> entities[code.lowercase()]
    }
    decoded ?: m.value
}

data class CoverLine(val text: String, val height: Int)
