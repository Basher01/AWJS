package com.basher01.awj

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.basher01.awj.manager.ChallengeManager

/**
 * Full-screen activity shown when user tries to access a blocked app
 */
class BlockedAppActivity : AppCompatActivity() {
    
    private lateinit var challengeManager: ChallengeManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_app)
        
        challengeManager = ChallengeManager(this)
        
        val messageText = findViewById<TextView>(R.id.blockMessage)
        val challengesButton = findViewById<Button>(R.id.viewChallengesButton)
        val closeButton = findViewById<Button>(R.id.closeButton)
        
        messageText.text = "This app is blocked!\n\nComplete your daily challenges to unlock it."
        
        challengesButton.setOnClickListener {
            startActivity(Intent(this, ChallengeActivity::class.java))
            finish()
        }
        
        closeButton.setOnClickListener {
            finish()
        }
    }
    
    override fun onBackPressed() {
        // Prevent back button from bypassing the block
        moveTaskToBack(true)
    }
}
