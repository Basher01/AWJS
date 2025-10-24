package com.basher01.awj

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basher01.awj.manager.AppBlockManager
import com.basher01.awj.manager.ChallengeManager
import com.basher01.awj.model.BlockedApp
import com.basher01.awj.model.Challenge
import com.basher01.awj.service.AppMonitorService
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    
    private lateinit var appBlockManager: AppBlockManager
    private lateinit var challengeManager: ChallengeManager
    
    private lateinit var statusText: TextView
    private lateinit var blockedAppsRecycler: RecyclerView
    private lateinit var challengesButton: Button
    private lateinit var startButton: Button
    private lateinit var addAppButton: FloatingActionButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        appBlockManager = AppBlockManager(this)
        challengeManager = ChallengeManager(this)
        
        initViews()
        checkPermissions()
        updateUI()
    }
    
    override fun onResume() {
        super.onResume()
        updateUI()
    }
    
    private fun initViews() {
        statusText = findViewById(R.id.statusText)
        blockedAppsRecycler = findViewById(R.id.blockedAppsRecycler)
        challengesButton = findViewById(R.id.challengesButton)
        startButton = findViewById(R.id.startButton)
        addAppButton = findViewById(R.id.addAppButton)
        
        blockedAppsRecycler.layoutManager = LinearLayoutManager(this)
        
        challengesButton.setOnClickListener {
            startActivity(Intent(this, ChallengeActivity::class.java))
        }
        
        startButton.setOnClickListener {
            toggleMonitoring()
        }
        
        addAppButton.setOnClickListener {
            showAddAppDialog()
        }
    }
    
    private fun updateUI() {
        val isUnlocked = appBlockManager.areAppsUnlockedToday()
        val blockedApps = appBlockManager.getBlockedApps()
        
        statusText.text = if (isUnlocked) {
            "Apps unlocked for today! ✓"
        } else {
            "Complete challenges to unlock apps"
        }
        
        blockedAppsRecycler.adapter = BlockedAppsAdapter(blockedApps) { app ->
            appBlockManager.removeBlockedApp(app.packageName)
            updateUI()
        }
    }
    
    private fun toggleMonitoring() {
        if (!hasUsageStatsPermission()) {
            requestUsageStatsPermission()
            return
        }
        
        if (!hasOverlayPermission()) {
            requestOverlayPermission()
            return
        }
        
        val intent = Intent(this, AppMonitorService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        
        AlertDialog.Builder(this)
            .setTitle("Monitoring Started")
            .setMessage("AWJ is now monitoring your apps. Complete your challenges to unlock blocked apps!")
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showAddAppDialog() {
        val installedApps = packageManager.getInstalledApplications(0)
            .filter { it.packageName != packageName }
            .map { BlockedApp(it.packageName, it.loadLabel(packageManager).toString()) }
            .sortedBy { it.appName }
        
        val appNames = installedApps.map { it.appName }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Select App to Block")
            .setItems(appNames) { _, which ->
                appBlockManager.addBlockedApp(installedApps[which])
                updateUI()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun checkPermissions() {
        if (!hasUsageStatsPermission()) {
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("AWJ needs Usage Access permission to monitor apps.")
                .setPositiveButton("Grant") { _, _ -> requestUsageStatsPermission() }
                .setCancelable(false)
                .show()
        }
        
        if (!hasOverlayPermission()) {
            AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("AWJ needs Overlay permission to block apps.")
                .setPositiveButton("Grant") { _, _ -> requestOverlayPermission() }
                .setCancelable(false)
                .show()
        }
    }
    
    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }
    
    private fun requestUsageStatsPermission() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
    
    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
    
    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }
}

class BlockedAppsAdapter(
    private val apps: List<BlockedApp>,
    private val onRemove: (BlockedApp) -> Unit
) : RecyclerView.Adapter<BlockedAppsAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.appName)
        val removeButton: Button = view.findViewById(R.id.removeButton)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blocked_app, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = apps[position]
        holder.appName.text = app.appName
        holder.removeButton.setOnClickListener { onRemove(app) }
    }
    
    override fun getItemCount() = apps.size
}
