package com.andreaszel.booklcalendar.suggest

import kotlinx.coroutines.delay

// Placeholder until a real vision/text model is wired in
class StubSuggestionService(private val latencyMs: Long = 500) : SuggestionService {
    private val fromCover = listOf(
        Suggestion("Piranesi", "A man lives alone in an endless house of statues and tides, keeping careful journals — until messages start to appear."),
        Suggestion("The Little Prince", "A stranded pilot meets a small prince from a faraway asteroid and learns what matters is invisible to the eye."),
    )

    private val known = mapOf(
        "piranesi" to fromCover[0].description,
        "the little prince" to fromCover[1].description,
        "dune" to "A young heir is thrown into a desert planet's politics, prophecy and the fight over its most precious resource.",
    )

    override suspend fun fromCover(coverPath: String, attempt: Int): Suggestion {
        delay(latencyMs)
        return fromCover[Math.floorMod(attempt, fromCover.size)]
    }

    override suspend fun describe(title: String): String {
        delay(latencyMs)
        return known[title.trim().lowercase()] ?: "A short summary of “${title.trim()}” will appear here once suggestions are connected."
    }
}
