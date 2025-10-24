package com.basher01.awj.manager

import android.content.Context
import android.content.SharedPreferences
import com.basher01.awj.model.Challenge
import com.basher01.awj.model.ChallengeType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

/**
 * Manages user challenges and completion status
 */
class ChallengeManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("awj_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val KEY_CHALLENGES = "challenges"
        private const val KEY_LAST_RESET_DATE = "last_reset_date"
    }
    
    /**
     * Get all challenges
     */
    fun getChallenges(): List<Challenge> {
        checkAndResetDaily()
        val json = prefs.getString(KEY_CHALLENGES, null) ?: return getDefaultChallenges()
        val type = object : TypeToken<List<Challenge>>() {}.type
        return gson.fromJson(json, type)
    }
    
    /**
     * Save challenges
     */
    fun saveChallenges(challenges: List<Challenge>) {
        val json = gson.toJson(challenges)
        prefs.edit().putString(KEY_CHALLENGES, json).apply()
    }
    
    /**
     * Add a new challenge
     */
    fun addChallenge(challenge: Challenge) {
        val challenges = getChallenges().toMutableList()
        challenges.add(challenge)
        saveChallenges(challenges)
    }
    
    /**
     * Remove a challenge
     */
    fun removeChallenge(challengeId: String) {
        val challenges = getChallenges().toMutableList()
        challenges.removeAll { it.id == challengeId }
        saveChallenges(challenges)
    }
    
    /**
     * Mark a challenge as completed
     */
    fun completeChallenge(challengeId: String) {
        val challenges = getChallenges().toMutableList()
        challenges.find { it.id == challengeId }?.isCompleted = true
        saveChallenges(challenges)
    }
    
    /**
     * Check if all challenges are completed
     */
    fun areAllChallengesCompleted(): Boolean {
        val challenges = getChallenges()
        return challenges.isNotEmpty() && challenges.all { it.isCompleted }
    }
    
    /**
     * Reset all challenges (mark as incomplete)
     */
    fun resetChallenges() {
        val challenges = getChallenges().toMutableList()
        challenges.forEach { it.isCompleted = false }
        saveChallenges(challenges)
    }
    
    /**
     * Check if it's a new day and reset challenges if needed
     */
    private fun checkAndResetDaily() {
        val lastResetDate = prefs.getLong(KEY_LAST_RESET_DATE, 0)
        val currentDay = System.currentTimeMillis() / (24 * 60 * 60 * 1000)
        val lastResetDay = lastResetDate / (24 * 60 * 60 * 1000)
        
        if (currentDay != lastResetDay) {
            resetChallenges()
            prefs.edit().putLong(KEY_LAST_RESET_DATE, System.currentTimeMillis()).apply()
        }
    }
    
    /**
     * Get default challenges for first-time users
     */
    private fun getDefaultChallenges(): List<Challenge> {
        return listOf(
            Challenge(
                id = UUID.randomUUID().toString(),
                title = "Morning Exercise",
                description = "Do 10 push-ups or 5 minutes of stretching",
                type = ChallengeType.EXERCISE
            ),
            Challenge(
                id = UUID.randomUUID().toString(),
                title = "Read Something Educational",
                description = "Read 5 pages of a book or an educational article",
                type = ChallengeType.LEARNING
            ),
            Challenge(
                id = UUID.randomUUID().toString(),
                title = "Complete a Task",
                description = "Complete one item from your to-do list",
                type = ChallengeType.PRODUCTIVITY
            )
        )
    }
}
