# Task Quest - Project Tutorial & Defense Guide

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Architecture Pattern](#architecture-pattern)
4. [Project Structure](#project-structure)
5. [Key Technologies Explained](#key-technologies-explained)
6. [Database Design](#database-design)
7. [Core Features Implementation](#core-features-implementation)
8. [Defense Q&A](#defense-qa)

---

## 🎯 Project Overview

**Task Quest** is a gamified task management Android application that transforms productivity into an RPG adventure. Users complete tasks to level up their character, earn XP, and unlock achievements.

### Core Value Proposition
- Makes boring tasks fun through gamification
- Maintains practical task management features
- Motivates users with rewards and progression

---

## 💻 Technology Stack

### 1. **Programming Language**
- **Kotlin** (100%)
  - Modern, concise, null-safe language for Android
  - Officially recommended by Google
  - Better than Java: less boilerplate, coroutines support

### 2. **UI Framework**
- **Jetpack Compose** (Modern UI Toolkit)
  - Declarative UI (describe what you want, not how to build it)
  - Less code than XML layouts
  - Live preview support
  - Easier to maintain and test

### 3. **Architecture Components**
- **ViewModel** - Manages UI state, survives configuration changes
- **StateFlow** - Observable data holder for state management
- **LiveData** alternative with better Kotlin support
- **Navigation Compose** - Type-safe navigation between screens

### 4. **Database**
- **Room Database** (SQLite wrapper)
  - Type-safe database access
  - Compile-time SQL query verification
  - Built-in coroutines support
  - Automatic entity generation

### 5. **Dependency Injection**
- **Manual DI with ViewModels**
  - Repository pattern for data access
  - Singleton database instance

### 6. **Async Programming**
- **Kotlin Coroutines**
  - Simplifies async/background operations
  - Better than callbacks or RxJava
  - suspend functions for async code

---

## 🏗️ Architecture Pattern: MVVM

### MVVM = Model-View-ViewModel

```
┌─────────────────────────────────────────┐
│           VIEW (Composables)            │
│  - UI Components (Screens)              │
│  - Displays data                        │
│  - Handles user input                   │
└─────────────────────────────────────────┘
                    ↕ (observes)
┌─────────────────────────────────────────┐
│           VIEWMODEL                     │
│  - Business logic                       │
│  - State management (StateFlow)         │
│  - Communicates with Repository         │
└─────────────────────────────────────────┘
                    ↕ (calls)
┌─────────────────────────────────────────┐
│           MODEL (Repository + DAO)      │
│  - Data access logic                    │
│  - Database operations                  │
│  - Data entities                        │
└─────────────────────────────────────────┘
```

### Why MVVM?
- **Separation of Concerns**: Each layer has one responsibility
- **Testable**: Can test ViewModel without UI
- **Maintainable**: Easy to modify without breaking other parts
- **Recommended by Google** for Android development

---

## 📁 Project Structure

```
app/src/main/java/com/example/prm392_taskquest/
│
├── 📂 data/                          # MODEL Layer
│   ├── 📂 local/
│   │   ├── 📂 dao/                  # Database Access Objects
│   │   │   ├── TaskDao.kt           # Task queries (CRUD)
│   │   │   ├── CategoryDao.kt       # Category queries
│   │   │   ├── ProjectDao.kt        # Project queries
│   │   │   ├── UserProfileDao.kt    # User profile queries
│   │   │   ├── AchievementDao.kt    # Achievement queries
│   │   │   └── DailyQuestDao.kt     # Daily quest queries
│   │   │
│   │   ├── 📂 entity/               # Database Tables
│   │   │   ├── Task.kt              # Task data model
│   │   │   ├── Category.kt          # Category data model
│   │   │   ├── Project.kt           # Project data model
│   │   │   ├── UserProfile.kt       # User profile & stats
│   │   │   ├── Achievement.kt       # Achievement data
│   │   │   ├── DailyQuest.kt        # Daily quest data
│   │   │   ├── Priority.kt          # Enum: LOW, MEDIUM, HIGH, URGENT
│   │   │   ├── RecurringType.kt     # Enum: DAILY, WEEKLY, MONTHLY
│   │   │   └── CharacterClass.kt    # Enum: WARRIOR, MAGE, ROGUE, PALADIN
│   │   │
│   │   └── 📂 database/
│   │       └── TaskQuestDatabase.kt # Room Database instance
│   │
│   └── 📂 repository/
│       └── TaskRepository.kt        # Data access layer
│
├── 📂 ui/                            # VIEW + VIEWMODEL Layer
│   ├── 📂 screens/                  # UI Screens
│   │   ├── 📂 home/
│   │   │   └── HomeScreen.kt        # Dashboard with stats & quests
│   │   ├── 📂 task/
│   │   │   ├── TaskListScreen.kt    # Task list with filters
│   │   │   └── AddEditTaskScreen.kt # Add/Edit task form
│   │   ├── 📂 profile/
│   │   │   └── ProfileScreen.kt     # User profile & character
│   │   ├── 📂 statistics/
│   │   │   └── StatisticsScreen.kt  # Charts & productivity stats
│   │   ├── 📂 achievement/
│   │   │   └── AchievementScreen.kt # Achievement gallery
│   │   ├── 📂 category/
│   │   │   └── CategoryScreen.kt    # Manage categories
│   │   ├── 📂 project/
│   │   │   └── ProjectScreen.kt     # Manage projects
│   │   └── 📂 drawing/
│   │       └── DrawingScreen.kt     # Sketch canvas for notes
│   │
│   ├── 📂 viewmodel/                # VIEWMODEL Layer
│   │   ├── HomeViewModel.kt         # Home screen logic
│   │   ├── TaskViewModel.kt         # Add/Edit task logic
│   │   ├── TaskListViewModel.kt     # Task list logic
│   │   ├── ProfileViewModel.kt      # Profile logic
│   │   ├── StatisticsViewModel.kt   # Statistics calculations
│   │   ├── AchievementViewModel.kt  # Achievement logic
│   │   ├── CategoryViewModel.kt     # Category management
│   │   └── ProjectViewModel.kt      # Project management
│   │
│   ├── 📂 components/               # Reusable UI Components
│   │   └── FilterChips.kt           # Filter chips for sorting
│   │
│   ├── 📂 navigation/               # Navigation Setup
│   │   ├── Screen.kt                # Screen routes
│   │   └── NavGraph.kt              # Navigation graph
│   │
│   └── 📂 theme/                    # UI Theming
│       ├── Color.kt                 # Color definitions
│       ├── Theme.kt                 # Material3 theme
│       └── Type.kt                  # Typography
│
└── MainActivity.kt                  # App entry point
```

---

## 🔑 Key Technologies Explained

### 1. **Jetpack Compose (UI)**

**What**: Modern declarative UI framework for Android

**Example**:
```kotlin
@Composable
fun TaskCard(task: Task) {
    Card {
        Text(text = task.title)
        Text(text = task.description)
    }
}
```

**Why Better Than XML**:
- Less boilerplate code
- Live preview in Android Studio
- Easier to manage state
- Reactive updates (UI updates when data changes)

---

### 2. **Room Database (Data Persistence)**

**What**: Type-safe SQLite wrapper

**Components**:

#### a) Entity (Table Definition)
```kotlin
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val dueDate: Long?,
    val priority: Priority,
    val xpReward: Int
)
```

#### b) DAO (Database Queries)
```kotlin
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isCompleted = 0")
    fun getActiveTasks(): Flow<List<Task>>

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}
```

#### c) Database Class
```kotlin
@Database(
    entities = [Task::class, Category::class, UserProfile::class],
    version = 1
)
abstract class TaskQuestDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
```

**Why Room**:
- Compile-time verification of SQL queries
- No manual SQL string writing
- Automatic object mapping
- Built-in coroutines support

---

### 3. **MVVM with StateFlow**

#### ViewModel Example:
```kotlin
class TaskViewModel : ViewModel() {
    // State holders
    private val _taskTitle = MutableStateFlow("")
    val taskTitle: StateFlow<String> = _taskTitle.asStateFlow()

    // Update state
    fun updateTitle(title: String) {
        _taskTitle.value = title
    }

    // Business logic
    fun saveTask() {
        viewModelScope.launch {
            repository.insertTask(Task(title = _taskTitle.value))
        }
    }
}
```

#### View (Composable):
```kotlin
@Composable
fun AddEditTaskScreen(viewModel: TaskViewModel) {
    val taskTitle by viewModel.taskTitle.collectAsState()

    OutlinedTextField(
        value = taskTitle,
        onValueChange = { viewModel.updateTitle(it) }
    )

    Button(onClick = { viewModel.saveTask() }) {
        Text("Save")
    }
}
```

**Key Concepts**:
- `MutableStateFlow` - Writable state (private in ViewModel)
- `StateFlow` - Read-only state (exposed to UI)
- `collectAsState()` - Observes state changes in Compose
- `viewModelScope` - Coroutine scope tied to ViewModel lifecycle

---

### 4. **Kotlin Coroutines (Async Operations)**

**What**: Simplifies asynchronous programming

**Example**:
```kotlin
// Instead of callbacks...
viewModelScope.launch {
    val tasks = repository.getTaskById(taskId) // suspend function
    _taskTitle.value = tasks.title
}
```

**Key Functions**:
- `suspend` - Marks a function as async
- `launch` - Starts a coroutine
- `async/await` - For parallel operations
- `Flow` - Stream of data that can emit multiple values

---

## 🗄️ Database Design

### Main Entities & Relationships

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   Task      │──────▶│  Category   │       │   Project   │
│             │  n:1  │             │       │             │
│ - id        │       │ - id        │       │ - id        │
│ - title     │       │ - name      │       │ - name      │
│ - priority  │       │ - color     │       │ - deadline  │
│ - xpReward  │       │ - icon      │       └─────────────┘
│ - dueDate   │       └─────────────┘              ▲
└─────────────┘                                    │
                                                   │n:1
                                                   │
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│UserProfile  │       │ Achievement │       │ DailyQuest  │
│             │       │             │       │             │
│ - level     │       │ - title     │       │ - title     │
│ - xp        │       │ - isUnlocked│       │ - xpReward  │
│ - streak    │       │ - icon      │       │ - date      │
└─────────────┘       └─────────────┘       └─────────────┘
```

### Key Tables

#### 1. **tasks**
- Stores all task information
- Links to categories and projects
- Tracks completion status and dates

#### 2. **user_profile**
- Singleton (only 1 row)
- Stores user level, XP, streaks
- Character class selection

#### 3. **categories**
- Predefined + custom categories
- Color and icon for each

#### 4. **achievements**
- Unlockable rewards
- Progress tracking

---

## ⚙️ Core Features Implementation

### 1. **Task Management (CRUD)**

#### Create Task:
```kotlin
// ViewModel
fun saveTask() {
    viewModelScope.launch {
        val task = Task(
            title = _taskTitle.value,
            priority = _taskPriority.value,
            dueDate = _taskDueDate.value,
            xpReward = calculateXp()
        )
        repository.insertTask(task)
    }
}

// Repository
suspend fun insertTask(task: Task) {
    taskDao.insertTask(task)
}
```

#### Read Tasks:
```kotlin
// DAO
@Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
fun getActiveTasks(): Flow<List<Task>>

// ViewModel
val activeTasks: StateFlow<List<Task>> =
    repository.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
```

#### Update/Delete: Similar pattern

---

### 2. **XP & Leveling System**

#### XP Calculation:
```kotlin
fun calculateXp(task: Task): Int {
    val baseXp = when (task.priority) {
        Priority.LOW -> 10
        Priority.MEDIUM -> 15
        Priority.HIGH -> 25
        Priority.URGENT -> 40
    }

    // Time bonus
    val timeBonus = (task.estimatedMinutes / 30) * 5

    // Streak bonus
    val streakBonus = (currentStreak * 0.05).toInt()

    return baseXp + timeBonus + streakBonus
}
```

#### Level Up:
```kotlin
fun completeTask(task: Task) {
    viewModelScope.launch {
        // Mark task complete
        repository.updateTask(task.copy(isCompleted = true))

        // Award XP
        val profile = repository.getUserProfile()
        val newXp = profile.xp + task.xpReward
        val requiredXp = 100 + (profile.level - 1) * 50

        // Level up if enough XP
        if (newXp >= requiredXp) {
            repository.updateProfile(
                profile.copy(
                    level = profile.level + 1,
                    xp = newXp - requiredXp
                )
            )
        } else {
            repository.updateProfile(profile.copy(xp = newXp))
        }
    }
}
```

---

### 3. **Statistics & Productivity Calculator**

#### Productivity Score Formula:
```kotlin
fun calculateProductivityScore(
    completedTasks: Int,
    totalTasks: Int,
    onTimeCompletions: Int,
    currentStreak: Int
): Int {
    val completionRate = (completedTasks.toFloat() / totalTasks) * 50
    val onTimeRate = (onTimeCompletions.toFloat() / completedTasks) * 30
    val streakBonus = (currentStreak.toFloat() / 30) * 20

    return (completionRate + onTimeRate + streakBonus).toInt()
}
```

---

### 4. **Canvas Drawing (Sketches)**

#### Drawing with Compose Canvas:
```kotlin
@Composable
fun DrawingCanvas() {
    var paths by remember { mutableStateOf(listOf<Path>()) }
    var currentPath by remember { mutableStateOf(Path()) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { currentPath = Path().apply { moveTo(it.x, it.y) } },
                    onDrag = { change, _ -> currentPath.lineTo(change.position.x, change.position.y) },
                    onDragEnd = { paths = paths + currentPath }
                )
            }
    ) {
        paths.forEach { path ->
            drawPath(path, Color.Black, style = Stroke(width = 5.dp.toPx()))
        }
        drawPath(currentPath, Color.Black, style = Stroke(width = 5.dp.toPx()))
    }
}
```

---

### 5. **Date Picker Dialog**

#### Material3 DatePicker:
```kotlin
@Composable
fun DatePickerExample() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }

    if (showDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDate = datePickerState.selectedDateMillis
                    showDialog = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
```

---

## 🛡️ Defense Q&A

### Architecture Questions

**Q: Why did you choose MVVM?**
- **A**: MVVM separates UI from business logic, making the app more testable and maintainable. It's the recommended architecture by Google for Android apps. The ViewModel survives configuration changes (like screen rotation), and StateFlow provides reactive data updates to the UI.

**Q: What is the difference between ViewModel and Repository?**
- **A**:
  - **ViewModel**: Holds UI state and business logic, communicates with Repository
  - **Repository**: Single source of truth for data, handles data operations from database/network
  - This separation allows for better testing and reusability

**Q: Why use StateFlow instead of LiveData?**
- **A**: StateFlow is better for Kotlin/Compose:
  - Better Kotlin coroutines integration
  - More predictable with initial state
  - Type-safe
  - Works well with Compose's `collectAsState()`

---

### Database Questions

**Q: Why Room instead of raw SQLite?**
- **A**: Room provides:
  - Compile-time SQL verification (catches errors early)
  - Automatic object mapping (no manual parsing)
  - Built-in coroutines support
  - Less boilerplate code
  - Type safety

**Q: What is a DAO?**
- **A**: Data Access Object - an interface that defines database operations (queries, inserts, updates, deletes). Room generates the implementation automatically.

**Q: How do you handle database migrations?**
- **A**: Room supports migrations through `Migration` objects that define how to upgrade from one database version to another. For development, we can use `fallbackToDestructiveMigration()`.

---

### UI Questions

**Q: Why Jetpack Compose instead of XML?**
- **A**: Compose is:
  - Less code (no findViewById, no XML layouts)
  - Declarative (describe what, not how)
  - Reactive (auto updates when state changes)
  - Modern and recommended by Google
  - Easier to preview and test

**Q: What is @Composable?**
- **A**: An annotation that marks a function as a Composable - a UI component that describes part of the UI. Composables can be reused and composed together to build complex UIs.

**Q: How does state management work in Compose?**
- **A**:
  - `remember` - Preserves state across recompositions
  - `mutableStateOf` - Creates observable state
  - When state changes, Compose automatically recomposes affected UI
  - `collectAsState()` converts Flow/StateFlow to Compose State

---

### Features Questions

**Q: How does the XP system work?**
- **A**: Tasks have base XP based on priority. Bonuses are added for:
  - Estimated time (longer tasks = more XP)
  - Streak days (continuous activity)
  - Early completion
  - User levels up when XP reaches threshold (100 + (level-1) * 50)

**Q: How are daily quests generated?**
- **A**: Daily quests are randomly selected from:
  - Pending tasks (Quick Quest)
  - Task count goals (Complete 3 tasks)
  - Category-specific challenges
  - Refreshes at midnight using background worker

**Q: How does productivity calculation work?**
- **A**: Formula combines:
  - Task completion rate (50%)
  - On-time completion rate (30%)
  - Current streak bonus (20%)
  - Result is 0-100 score

---

### Technical Questions

**Q: What is a coroutine?**
- **A**: Lightweight thread for async operations. Uses `suspend` functions that can pause and resume without blocking. Better than callbacks - easier to read and maintain.

**Q: What is Flow?**
- **A**: A stream that emits values over time (like RxJava Observable). Used for:
  - Database queries that update automatically
  - Real-time data streams
  - Asynchronous sequences

**Q: How do you handle configuration changes?**
- **A**: ViewModel survives configuration changes (screen rotation). UI state is preserved in ViewModel, so the UI can be recreated without losing data.

**Q: What is Dependency Injection?**
- **A**: Design pattern where dependencies are provided from outside rather than created inside. In this project:
  - Database instance is singleton
  - Repository receives DAOs as dependencies
  - ViewModel receives Repository as dependency

---

### Testing Questions

**Q: How would you test this app?**
- **A**:
  - **Unit Tests**: Test ViewModels and Repository logic
  - **Instrumented Tests**: Test DAOs with in-memory database
  - **UI Tests**: Test Composables with Compose testing framework
  - **Preview Tests**: Use `@Preview` for visual testing

**Q: Can you preview Compose UIs?**
- **A**: Yes! Use `@Preview` annotation:
```kotlin
@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskCard(task = Task(...))
}
```
Shows in Android Studio without running the app.

---

## 🎓 Key Concepts Summary

### Must Know:
1. **MVVM Architecture** - View, ViewModel, Model separation
2. **Jetpack Compose** - Declarative UI with @Composable
3. **Room Database** - Entity, DAO, Database class
4. **StateFlow** - Observable state management
5. **Coroutines** - Async operations with suspend functions
6. **Material3** - Modern UI components and theming

### Design Patterns Used:
- **Repository Pattern** - Single source of truth for data
- **Observer Pattern** - StateFlow/Flow for reactive updates
- **Singleton Pattern** - Database instance
- **Factory Pattern** - ViewModel creation

### Android Components:
- **Activity** - Entry point (MainActivity)
- **ViewModel** - Survives configuration changes
- **Compose Navigation** - Screen navigation
- **Material Design 3** - UI components and theming

---

## 📚 Learning Resources

### Official Documentation:
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [MVVM Architecture](https://developer.android.com/topic/architecture)

### Key Gradle Dependencies:
```kotlin
// Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")
```

---

## ✅ Project Strengths (For Defense)

1. **Modern Tech Stack** - Uses latest Android recommended practices
2. **Clean Architecture** - MVVM with clear separation of concerns
3. **Type Safety** - Kotlin + Room compile-time checks
4. **Reactive UI** - Compose + StateFlow for automatic updates
5. **Scalable** - Easy to add new features (new DAOs, screens)
6. **Gamification** - Unique approach to productivity
7. **User Experience** - Smooth animations, live previews
8. **Database Design** - Normalized, efficient queries

---

## 🎯 Conclusion

This project demonstrates:
- ✅ Modern Android development (Compose, Kotlin, Room)
- ✅ Clean architecture principles (MVVM)
- ✅ Async programming (Coroutines, Flow)
- ✅ Database design and management
- ✅ UI/UX design with Material3
- ✅ Complex feature implementation (gamification)
- ✅ State management patterns

**You're ready to defend your project!** 🚀
