package com.basher01.awj.model

/**
 * Represents an app that the user wants to block
 */
data class BlockedApp(
    val packageName: String,
    val appName: String,
    var isBlocked: Boolean = true
)
