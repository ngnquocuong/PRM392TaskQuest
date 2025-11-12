package com.example.prm392_taskquest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.CharacterClass
import com.example.prm392_taskquest.data.local.entity.UserProfile
import com.example.prm392_taskquest.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskQuestDatabase.getDatabase(application)
    private val repository = TaskRepository(
        database.taskDao(),
        database.categoryDao(),
        database.projectDao(),
        database.userProfileDao(),
        database.achievementDao(),
        database.dailyQuestDao()
    )

    val userProfile: StateFlow<UserProfile?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateCharacterClass(characterClass: CharacterClass) {
        viewModelScope.launch {
            val profile = repository.getUserProfileSync()
            profile?.let {
                repository.updateUserProfile(it.copy(characterClass = characterClass))
            }
        }
    }

    fun updateAvatar(avatarId: Int) {
        viewModelScope.launch {
            val profile = repository.getUserProfileSync()
            profile?.let {
                repository.updateUserProfile(it.copy(avatarId = avatarId))
            }
        }
    }

    fun getCharacterClassDescription(characterClass: CharacterClass): String {
        return when (characterClass) {
            CharacterClass.WARRIOR -> "Masters of strength and endurance. Warriors gain bonus XP for completing high-priority tasks."
            CharacterClass.MAGE -> "Scholars of wisdom and intellect. Mages gain bonus XP for completing study-related tasks."
            CharacterClass.ROGUE -> "Experts in agility and speed. Rogues gain bonus XP for completing tasks quickly."
            CharacterClass.PALADIN -> "Champions of balance and righteousness. Paladins gain bonus XP for maintaining streaks."
        }
    }

    fun getCharacterClassBonus(characterClass: CharacterClass): String {
        return when (characterClass) {
            CharacterClass.WARRIOR -> "+20% XP on High/Urgent priority tasks"
            CharacterClass.MAGE -> "+15% XP on Study category tasks"
            CharacterClass.ROGUE -> "+25% XP for early completions"
            CharacterClass.PALADIN -> "+10% XP per streak day (max 50%)"
        }
    }
}
