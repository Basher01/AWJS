package com.basher01.awj.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.basher01.awj.BlockedAppActivity
import com.basher01.awj.R
import com.basher01.awj.manager.AppBlockManager

/**
 * Background service that monitors running apps and blocks them if necessary
 */
class AppMonitorService : Service() {
    
    private lateinit var appBlockManager: AppBlockManager
    private val handler = Handler(Looper.getMainLooper())
    private var monitoringRunnable: Runnable? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "app_monitor_channel"
        private const val CHECK_INTERVAL = 2000L // Check every 2 seconds
    }
    
    override fun onCreate() {
        super.onCreate()
        appBlockManager = AppBlockManager(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startMonitoring()
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
    }
    
    private fun startMonitoring() {
        monitoringRunnable = object : Runnable {
            override fun run() {
                checkForegroundApp()
                handler.postDelayed(this, CHECK_INTERVAL)
            }
        }
        handler.post(monitoringRunnable!!)
    }
    
    private fun stopMonitoring() {
        monitoringRunnable?.let { handler.removeCallbacks(it) }
    }
    
    private fun checkForegroundApp() {
        val currentApp = getForegroundApp()
        if (currentApp != null && currentApp != packageName) {
            if (appBlockManager.isAppBlocked(currentApp)) {
                showBlockScreen()
            }
        }
    }
    
    private fun getForegroundApp(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - 1000 * 10, // Last 10 seconds
            time
        )
        
        if (stats.isNullOrEmpty()) return null
        
        val sortedStats = stats.sortedByDescending { it.lastTimeUsed }
        return sortedStats.firstOrNull()?.packageName
    }
    
    private fun showBlockScreen() {
        val intent = Intent(this, BlockedAppActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Monitoring",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitors running apps to enforce blocking"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AWJ Active")
            .setContentText("Monitoring apps to help you stay productive")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
