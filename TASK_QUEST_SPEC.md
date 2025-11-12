# Task Quest - Gamified To-Do List App Specification

## 📋 App Overview

**Task Quest** is a gamified task management app that transforms your to-do list into an RPG adventure. Complete tasks to level up your character, earn rewards, and defeat "boss" projects. Combines productivity with game mechanics to make task management engaging and motivating.

**Target Users**: Students, professionals, anyone who struggles with motivation and task completion

**Core Value Proposition**: Make boring tasks fun through gamification while maintaining practical task management features

---

## 🎮 Core Features

### 1. Task Management (Chapters 2, 8)
**Basic CRUD Operations:**
- ✅ Create tasks with title, description, due date, priority
- ✅ View tasks in list/grid format
- ✅ Edit task details
- ✅ Delete tasks
- ✅ Mark tasks as complete
- ✅ Filter by category, priority, due date
- ✅ Search tasks

**Task Properties:**
- Title (required)
- Description (optional)
- Due date/time
- Priority (Low, Medium, High, Urgent)
- Category/Project
- Estimated time
- XP reward value
- Recurring (daily, weekly, monthly)
- Subtasks/checklist

### 2. RPG Character System (Chapters 6, 8)
**Character Stats:**
- Level (starts at 1)
- XP (experience points)
- Total tasks completed
- Current streak (days)
- Character class (Warrior, Mage, Rogue, etc.)
- Avatar/appearance

**Progression:**
- Gain XP for completing tasks
- Level up when reaching XP threshold
- Unlock new features at certain levels
- Earn titles/badges for achievements

**XP Formula:**
```
Base XP = 10
Priority multiplier: Low (1x), Medium (1.5x), High (2x), Urgent (3x)
Time bonus: Completed early (+20%), On time (+0%), Late (-10%)
Streak bonus: +5% per consecutive day (max 50%)
```

### 3. Productivity Calculator (Chapter 3)
**Real-time Statistics:**
- Tasks completed today/week/month
- Productivity score (0-100)
- Average completion time
- Category breakdown (%)
- Time saved calculator
- Streak counter

**Productivity Score Formula:**
```
Score = (Completed Tasks / Total Tasks) × 50 +
        (On-time Completion / Total Completed) × 30 +
        (Current Streak / 30) × 20
```

**Dashboard displays:**
- Today's productivity percentage
- Weekly progress graph
- XP earned today/this week
- Level progress bar
- Streak flame icon with count

### 4. Daily Quest System (Chapter 5)
**Quest Types:**
- Daily Challenge: Random task suggestions
- Quick Quest: 3 randomly selected pending tasks
- Boss Battle: Complete a major project
- Speed Run: Complete 5 tasks within 2 hours
- Perfect Day: Complete all scheduled tasks for today

**Quest Rewards:**
- Bonus XP (50-200)
- Achievement badges
- Loot boxes (cosmetic items)
- Power-ups (2x XP for 1 hour)

**Implementation:**
- Random selection from pending tasks
- Daily reset at midnight
- Progress tracking
- Completion celebration animation

### 5. Canvas Animations (Chapter 6)
**Completion Animations:**
- **Task Cannon**: Shoot completed task at target board
  - Touch and drag to aim
  - Physics-based projectile motion
  - Explosion effect on hit
  - XP particles floating up

- **Level Up Animation**:
  - Character grows/glows
  - Confetti particles
  - Level number animated increase
  - Sound effect (optional)

- **Streak Fire**:
  - Animated flame that grows with streak
  - Changes color at milestones (7, 30, 100 days)
  - Particle effects

**Interactive Elements:**
- Drag tasks to reorder priority
- Swipe to complete/delete
- Pull to refresh with custom animation
- Floating action button with ripple effect

### 6. Note Sketching (Chapter 7)
**Drawing Features:**
- Quick sketch pad for task notes
- Draw diagrams or mind maps
- Annotate screenshots
- Multiple colors (8 presets)
- Brush sizes (Small, Medium, Large)
- Eraser tool
- Save drawings with tasks

**Use Cases:**
- Quick visual brainstorming
- Sketch task steps
- Draw goals/vision board
- Annotate complex tasks

### 7. Categories & Projects (Chapter 8)
**Category System:**
- Predefined: Work, Study, Personal, Health, Finance, Creative
- Custom categories with colors
- Icons for each category
- Category-based filtering

**Project System:**
- Projects contain multiple tasks
- Project progress tracking (%)
- Project deadlines
- "Boss Battle" mode for big projects
- Milestone tracking

---

## 🗄️ Database Schema (Room)

### Entity: Task
```kotlin
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val dueDate: Long? = null,
    val priority: Priority,
    val categoryId: Int,
    val projectId: Int? = null,
    val estimatedMinutes: Int = 30,
    val xpReward: Int = 10,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null,
    val isRecurring: Boolean = false,
    val recurringType: RecurringType? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val sketchPath: String? = null
)

enum class Priority { LOW, MEDIUM, HIGH, URGENT }
enum class RecurringType { DAILY, WEEKLY, MONTHLY }
```

### Entity: Category
```kotlin
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val color: String, // Hex color
    val icon: String,  // Icon name
    val taskCount: Int = 0
)
```

### Entity: Project
```kotlin
@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val deadline: Long? = null,
    val isCompleted: Boolean = false,
    val color: String
)
```

### Entity: UserProfile
```kotlin
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val level: Int = 1,
    val xp: Int = 0,
    val totalTasksCompleted: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: Long = System.currentTimeMillis(),
    val characterClass: CharacterClass = CharacterClass.WARRIOR,
    val avatarId: Int = 1
)

enum class CharacterClass { WARRIOR, MAGE, ROGUE, PALADIN }
```

### Entity: Achievement
```kotlin
@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null,
    val requiredCount: Int,
    val currentCount: Int = 0,
    val type: AchievementType
)

enum class AchievementType {
    TASKS_COMPLETED,
    STREAK,
    LEVEL,
    CATEGORY,
    PERFECT_WEEK
}
```

### Entity: DailyQuest
```kotlin
@Entity(tableName = "daily_quests")
data class DailyQuest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val xpReward: Int,
    val date: Long,
    val isCompleted: Boolean = false,
    val questType: QuestType
)

enum class QuestType {
    DAILY_CHALLENGE,
    QUICK_QUEST,
    BOSS_BATTLE,
    SPEED_RUN,
    PERFECT_DAY
}
```

### DAOs

**TaskDao:**
```kotlin
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getActiveTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedDate DESC")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE categoryId = :categoryId")
    fun getTasksByCategory(categoryId: Int): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId")
    fun getTasksByProject(projectId: Int): Flow<List<Task>>

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1 AND completedDate >= :startDate")
    suspend fun getCompletedTasksCount(startDate: Long): Int
}
```

---

## 📱 Screen Designs

### 1. Home/Dashboard Screen
**Layout:**
```
┌─────────────────────────────┐
│  ⚔️ Task Quest    [⚙️] [👤]  │
├─────────────────────────────┤
│ ┌───────────────────────┐   │
│ │  🔥 Streak: 7 days    │   │
│ │  ⭐ Level 5 (XP: 450/500) │
│ │  ━━━━━━━━━━━━━━━━━━  90%│
│ └───────────────────────┘   │
│                             │
│ Today's Quest 🎯            │
│ ┌───────────────────────┐   │
│ │ Complete 3 tasks      │   │
│ │ Reward: 50 XP         │   │
│ │ Progress: █████░░ 2/3 │   │
│ └───────────────────────┘   │
│                             │
│ 📋 My Tasks (5)       [+]   │
│ ┌───────────────────────┐   │
│ │ ☐ Finish homework     │   │
│ │   🎓 Study | Due: 3PM │   │
│ └───────────────────────┘   │
│ ┌───────────────────────┐   │
│ │ ☐ Workout 30 min      │   │
│ │   💪 Health | Due: 6PM│   │
│ └───────────────────────┘   │
│                             │
│ 📊 Today: 3/5 tasks (60%)   │
└─────────────────────────────┘
```

**Components:**
- Character avatar with level
- XP progress bar
- Streak fire indicator
- Daily quest card
- Task list with swipe actions
- FAB for adding tasks
- Bottom navigation (Home, Tasks, Stats, Profile)

### 2. Task List Screen
**Features:**
- Filter chips (All, Today, Upcoming, Completed)
- Sort options (Due date, Priority, XP)
- Category filter dropdown
- Search bar
- Swipe right to complete (green)
- Swipe left to delete (red)
- Long press for quick edit

### 3. Add/Edit Task Screen
**Form Fields:**
- Title (required)
- Description (multiline)
- Due date picker (calendar)
- Time picker
- Priority selector (radio buttons with colors)
- Category dropdown
- Project dropdown (optional)
- Estimated time slider (15m - 4h)
- XP reward (auto-calculated, editable)
- Recurring toggle + type
- Sketch button (opens drawing canvas)

### 4. Statistics Screen
**Sections:**
- Productivity score gauge (0-100)
- XP earned chart (last 7 days)
- Tasks completed chart
- Category breakdown (pie chart)
- Achievements grid (locked/unlocked)
- Streak calendar heat map
- Best performing categories

### 5. Profile Screen
**Content:**
- Character avatar (customizable)
- Level and title
- Total XP earned
- Total tasks completed
- Current/longest streak
- Character class selector
- Achievements showcase (top 3)
- Settings button

### 6. Task Completion Animation Screen
**Canvas:**
- Blue sky background
- Target board on right side
- Cannon on left (angle adjustable)
- Task title as "cannonball"
- Shoot to complete
- Explosion + XP particles
- Return to task list

### 7. Drawing/Sketch Screen
**Tools:**
- Canvas (white background)
- Color palette (8 colors)
- Brush size slider
- Eraser toggle
- Clear all button
- Save button
- Cancel button

---

## 🎯 Implementation Phases

### Phase 1: Foundation (Week 1)
- ✅ Setup project with all dependencies
- ✅ Create database entities and DAOs
- ✅ Implement basic navigation
- ✅ Create home screen UI
- ✅ Add/edit/delete tasks (basic CRUD)

### Phase 2: Task Management (Week 2)
- ✅ Task list with filtering
- ✅ Search functionality
- ✅ Categories and projects
- ✅ Swipe gestures
- ✅ Task completion logic
- ✅ Due date notifications (optional)

### Phase 3: RPG System (Week 3)
- ✅ User profile setup
- ✅ XP calculation and awarding
- ✅ Level progression system
- ✅ Streak tracking
- ✅ Profile screen UI

### Phase 4: Gamification (Week 4)
- ✅ Daily quest generation
- ✅ Achievement system
- ✅ Character customization
- ✅ Level up celebrations
- ✅ Quest completion UI

### Phase 5: Statistics & Calculator (Week 5)
- ✅ Productivity calculator
- ✅ Charts and graphs
- ✅ Statistics screen
- ✅ Dashboard widgets
- ✅ Time tracking

### Phase 6: Canvas Animations (Week 6)
- ✅ Task cannon game
- ✅ Completion animations
- ✅ Particle effects
- ✅ Level up animation
- ✅ Streak fire animation

### Phase 7: Drawing Feature (Week 7)
- ✅ Drawing canvas implementation
- ✅ Save/load sketches
- ✅ Attach to tasks
- ✅ Gallery view for sketches

### Phase 8: Polish & Advanced (Week 8)
- ✅ Dark mode support
- ✅ Animations polish
- ✅ Performance optimization
- ✅ Bug fixes
- ✅ User onboarding tutorial

---

## 🔧 Technical Requirements

### Dependencies
```kotlin
// Core
implementation("androidx.core:core-ktx:1.17.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
implementation("androidx.activity:activity-compose:1.11.0")

// Compose
implementation(platform("androidx.compose:compose-bom:2024.09.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

// Charts (optional - for statistics)
implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
// or use Compose-based charting library

// Work Manager (for notifications)
implementation("androidx.work:work-runtime-ktx:2.9.0")
```

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **Database**: Room with SQLite
- **Navigation**: Navigation Compose
- **State Management**: StateFlow + Compose State
- **Async**: Kotlin Coroutines

---

## 🎨 Design Guidelines

### Color Scheme
```kotlin
// Light Theme
Primary = Color(0xFF6366F1)      // Indigo
Secondary = Color(0xFF8B5CF6)    // Purple
Success = Color(0xFF10B981)      // Green
Warning = Color(0xFFF59E0B)      // Amber
Error = Color(0xFFEF4444)        // Red

// Priority Colors
LowPriority = Color(0xFF22C55E)
MediumPriority = Color(0xFFFBBF24)
HighPriority = Color(0xFFFB923C)
UrgentPriority = Color(0xFFEF4444)
```

### Typography
- Headings: Bold, 24sp
- Body: Regular, 16sp
- Caption: Regular, 14sp
- Button: Medium, 16sp

### Icons
Use Material Icons Extended for consistency

---

## 🌟 Advanced Features (Optional)

### 1. Social Features
- Share achievements on social media
- Leaderboard with friends
- Team challenges
- Task delegation

### 2. Widgets
- Home screen widget showing today's tasks
- Character XP progress widget
- Streak counter widget

### 3. Cloud Sync
- Firebase integration
- Cross-device synchronization
- Backup and restore

### 4. AI Features
- Smart task suggestions
- Optimal scheduling
- Productivity insights
- Task difficulty estimation

### 5. Integrations
- Calendar integration
- Google Tasks sync
- Pomodoro timer
- Focus mode (block distractions)

---

## 📝 Notes

### Key Concepts from Each Chapter

**Chapter 1 (Introduction):**
- Basic Android app structure
- Activities and lifecycle
- Gradle configuration

**Chapter 2 (Welcome App):**
- Event handling (button clicks, input)
- Toast notifications
- Basic UI components

**Chapter 3 (Tip Calculator):**
- Real-time calculations
- Number formatting
- Slider controls
- Live data updates

**Chapter 5 (Flag Quiz):**
- Random selection logic
- Quiz/game mechanics
- Score tracking
- Progress indicators

**Chapter 6 (Cannon Game):**
- Canvas drawing
- Physics simulation
- Game loop (LaunchedEffect)
- Touch/gesture handling
- Collision detection

**Chapter 7 (Doodle App):**
- Path drawing
- Touch gestures (drag)
- Color and brush selection
- Save/load drawings

**Chapter 8 (Address Book):**
- Room database
- CRUD operations
- ViewModel
- StateFlow
- Form validation

---

## ✅ Success Criteria

A successful implementation should:
1. ✅ Store and retrieve tasks persistently
2. ✅ Calculate and award XP correctly
3. ✅ Track user level and progression
4. ✅ Generate daily quests automatically
5. ✅ Display real-time productivity statistics
6. ✅ Show smooth completion animations
7. ✅ Allow drawing and saving sketches
8. ✅ Maintain streak counting accurately
9. ✅ Be intuitive and fun to use
10. ✅ Demonstrate all chapter concepts

---

**Good luck with your implementation! 🚀**
