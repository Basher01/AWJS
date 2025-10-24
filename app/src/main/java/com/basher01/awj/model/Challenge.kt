package com.basher01.awj.model

/**
 * Represents a productive challenge that users must complete to unlock blocked apps
 */
data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    var isCompleted: Boolean = false,
    val type: ChallengeType = ChallengeType.CUSTOM
)

enum class ChallengeType {
    EXERCISE,      // Physical activity like push-ups, running
    LEARNING,      // Read a book, watch educational content
    PRODUCTIVITY,  // Complete a work task, organize space
    CUSTOM         // User-defined challenge
}
