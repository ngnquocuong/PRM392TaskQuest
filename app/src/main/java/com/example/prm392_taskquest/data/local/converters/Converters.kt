package com.example.prm392_taskquest.data.local.converters

import androidx.room.TypeConverter
import com.example.prm392_taskquest.data.local.entity.*

class Converters {
    @TypeConverter
    fun fromPriority(value: Priority): String {
        return value.name
    }

    @TypeConverter
    fun toPriority(value: String): Priority {
        return Priority.valueOf(value)
    }

    @TypeConverter
    fun fromRecurringType(value: RecurringType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toRecurringType(value: String?): RecurringType? {
        return value?.let { RecurringType.valueOf(it) }
    }

    @TypeConverter
    fun fromCharacterClass(value: CharacterClass): String {
        return value.name
    }

    @TypeConverter
    fun toCharacterClass(value: String): CharacterClass {
        return CharacterClass.valueOf(value)
    }

    @TypeConverter
    fun fromAchievementType(value: AchievementType): String {
        return value.name
    }

    @TypeConverter
    fun toAchievementType(value: String): AchievementType {
        return AchievementType.valueOf(value)
    }

    @TypeConverter
    fun fromQuestType(value: QuestType): String {
        return value.name
    }

    @TypeConverter
    fun toQuestType(value: String): QuestType {
        return QuestType.valueOf(value)
    }
}
