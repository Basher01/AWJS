package com.basher01.awj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basher01.awj.manager.AppBlockManager
import com.basher01.awj.manager.ChallengeManager
import com.basher01.awj.model.Challenge
import com.basher01.awj.model.ChallengeType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID

class ChallengeActivity : AppCompatActivity() {
    
    private lateinit var challengeManager: ChallengeManager
    private lateinit var appBlockManager: AppBlockManager
    
    private lateinit var challengesRecycler: RecyclerView
    private lateinit var unlockButton: Button
    private lateinit var addChallengeButton: FloatingActionButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        
        challengeManager = ChallengeManager(this)
        appBlockManager = AppBlockManager(this)
        
        initViews()
        updateUI()
    }
    
    private fun initViews() {
        challengesRecycler = findViewById(R.id.challengesRecycler)
        unlockButton = findViewById(R.id.unlockButton)
        addChallengeButton = findViewById(R.id.addChallengeButton)
        
        challengesRecycler.layoutManager = LinearLayoutManager(this)
        
        unlockButton.setOnClickListener {
            attemptUnlock()
        }
        
        addChallengeButton.setOnClickListener {
            showAddChallengeDialog()
        }
    }
    
    private fun updateUI() {
        val challenges = challengeManager.getChallenges()
        val allCompleted = challengeManager.areAllChallengesCompleted()
        
        challengesRecycler.adapter = ChallengesAdapter(challenges) { challenge, completed ->
            if (completed) {
                challengeManager.completeChallenge(challenge.id)
            }
            updateUI()
        }
        
        unlockButton.isEnabled = allCompleted
        unlockButton.text = if (allCompleted) {
            "Unlock Apps for Today"
        } else {
            "Complete All Challenges First"
        }
    }
    
    private fun attemptUnlock() {
        if (challengeManager.areAllChallengesCompleted()) {
            appBlockManager.unlockAppsForToday()
            
            AlertDialog.Builder(this)
                .setTitle("Success!")
                .setMessage("You've completed all challenges! Your apps are now unlocked for today.")
                .setPositiveButton("OK") { _, _ -> finish() }
                .show()
        } else {
            Toast.makeText(this, "Please complete all challenges first", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showAddChallengeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_challenge, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.challengeTitleInput)
        val descInput = dialogView.findViewById<EditText>(R.id.challengeDescInput)
        
        AlertDialog.Builder(this)
            .setTitle("Add New Challenge")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val title = titleInput.text.toString()
                val desc = descInput.text.toString()
                
                if (title.isNotEmpty() && desc.isNotEmpty()) {
                    val challenge = Challenge(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = desc,
                        type = ChallengeType.CUSTOM
                    )
                    challengeManager.addChallenge(challenge)
                    updateUI()
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

class ChallengesAdapter(
    private val challenges: List<Challenge>,
    private val onCompleteChanged: (Challenge, Boolean) -> Unit
) : RecyclerView.Adapter<ChallengesAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.challengeTitle)
        val description: TextView = view.findViewById(R.id.challengeDescription)
        val checkbox: CheckBox = view.findViewById(R.id.challengeCheckbox)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.title.text = challenge.title
        holder.description.text = challenge.description
        holder.checkbox.isChecked = challenge.isCompleted
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            onCompleteChanged(challenge, isChecked)
        }
    }
    
    override fun getItemCount() = challenges.size
}
