package com.basher01.awj.manager

import android.content.Context
import android.content.SharedPreferences
import com.basher01.awj.model.BlockedApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Manages the list of blocked apps and their blocking state
 */
class AppBlockManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("awj_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val KEY_BLOCKED_APPS = "blocked_apps"
        private const val KEY_APPS_UNLOCKED_TODAY = "apps_unlocked_today"
        private const val KEY_LAST_UNLOCK_DATE = "last_unlock_date"
    }
    
    /**
     * Get all blocked apps
     */
    fun getBlockedApps(): List<BlockedApp> {
        val json = prefs.getString(KEY_BLOCKED_APPS, null) ?: return emptyList()
        val type = object : TypeToken<List<BlockedApp>>() {}.type
        return gson.fromJson(json, type)
    }
    
    /**
     * Save blocked apps
     */
    fun saveBlockedApps(apps: List<BlockedApp>) {
        val json = gson.toJson(apps)
        prefs.edit().putString(KEY_BLOCKED_APPS, json).apply()
    }
    
    /**
     * Add an app to the blocked list
     */
    fun addBlockedApp(app: BlockedApp) {
        val apps = getBlockedApps().toMutableList()
        if (!apps.any { it.packageName == app.packageName }) {
            apps.add(app)
            saveBlockedApps(apps)
        }
    }
    
    /**
     * Remove an app from the blocked list
     */
    fun removeBlockedApp(packageName: String) {
        val apps = getBlockedApps().toMutableList()
        apps.removeAll { it.packageName == packageName }
        saveBlockedApps(apps)
    }
    
    /**
     * Check if an app is blocked
     */
    fun isAppBlocked(packageName: String): Boolean {
        if (!areAppsUnlockedToday()) {
            return getBlockedApps().any { it.packageName == packageName }
        }
        return false
    }
    
    /**
     * Unlock all apps for today after challenges are completed
     */
    fun unlockAppsForToday() {
        prefs.edit()
            .putBoolean(KEY_APPS_UNLOCKED_TODAY, true)
            .putLong(KEY_LAST_UNLOCK_DATE, System.currentTimeMillis())
            .apply()
    }
    
    /**
     * Check if apps are unlocked for today
     */
    fun areAppsUnlockedToday(): Boolean {
        val unlocked = prefs.getBoolean(KEY_APPS_UNLOCKED_TODAY, false)
        val lastUnlockDate = prefs.getLong(KEY_LAST_UNLOCK_DATE, 0)
        
        // Check if it's still the same day
        val currentDay = System.currentTimeMillis() / (24 * 60 * 60 * 1000)
        val lastUnlockDay = lastUnlockDate / (24 * 60 * 60 * 1000)
        
        if (currentDay != lastUnlockDay) {
            // New day, reset unlock status
            prefs.edit().putBoolean(KEY_APPS_UNLOCKED_TODAY, false).apply()
            return false
        }
        
        return unlocked
    }
    
    /**
     * Reset daily unlock status (for testing or manual reset)
     */
    fun resetDailyUnlock() {
        prefs.edit()
            .putBoolean(KEY_APPS_UNLOCKED_TODAY, false)
            .remove(KEY_LAST_UNLOCK_DATE)
            .apply()
    }
}
