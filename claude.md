# Task Quest - Project Documentation

## 📱 About This Project

**Task Quest** is a gamified task management Android application built with modern Android development practices. It transforms boring to-do lists into an engaging RPG adventure where users earn XP, level up, and unlock achievements by completing tasks.

**Last Updated**: January 2025
**Status**: ✅ Fully Implemented
**Technology**: Kotlin + Jetpack Compose + Room Database

---

## 🎯 Quick Start

### For Developers
- **Entry Point**: `MainActivity.kt`
- **Main Documentation**: See `TUTORIAL.md` for comprehensive guide
- **Specification**: See `TASK_QUEST_SPEC.md` for feature details

### Project Commands
```bash
# Build project
./gradlew build

# Run on device/emulator
./gradlew installDebug

# Run tests
./gradlew test
```

---

## 🏗️ Architecture Overview

### Pattern: MVVM (Model-View-ViewModel)

```
┌─────────────────────────────────────┐
│  VIEW (Jetpack Compose Screens)    │
│  - HomeScreen, TaskListScreen, etc │
└─────────────────────────────────────┘
                ↕
┌─────────────────────────────────────┐
│  VIEWMODEL (Business Logic)        │
│  - HomeViewModel, TaskViewModel     │
└─────────────────────────────────────┘
                ↕
┌─────────────────────────────────────┐
│  MODEL (Repository + Room)          │
│  - TaskRepository, DAOs, Entities   │
└─────────────────────────────────────┘
```

---

## 📂 Project Structure

```
app/src/main/java/com/example/prm392_taskquest/
├── data/
│   ├── local/
│   │   ├── dao/              # Database queries
│   │   ├── entity/           # Data models
│   │   └── database/         # Room database
│   └── repository/           # Data access layer
├── ui/
│   ├── screens/              # UI screens (Composables)
│   ├── viewmodel/            # ViewModels
│   ├── components/           # Reusable UI components
│   ├── navigation/           # Navigation setup
│   └── theme/                # Material3 theme
└── MainActivity.kt           # App entry point
```

---

## 🔑 Key Technologies

| Technology | Purpose | Why Used |
|------------|---------|----------|
| **Kotlin** | Programming Language | Modern, concise, null-safe |
| **Jetpack Compose** | UI Framework | Declarative, less code than XML |
| **Room** | Database | Type-safe SQLite wrapper |
| **StateFlow** | State Management | Reactive state updates |
| **Coroutines** | Async Operations | Simpler than callbacks |
| **Material3** | UI Components | Modern Material Design |
| **Navigation Compose** | Screen Navigation | Type-safe navigation |

---

## 🗄️ Database Schema

### Entities (Tables)

#### 1. Task
```kotlin
- id: Int (Primary Key)
- title: String
- description: String
- dueDate: Long?
- priority: Priority (LOW, MEDIUM, HIGH, URGENT)
- categoryId: Int (Foreign Key)
- projectId: Int? (Foreign Key)
- xpReward: Int
- isCompleted: Boolean
- estimatedMinutes: Int
- isRecurring: Boolean
- recurringType: RecurringType?
```

#### 2. UserProfile
```kotlin
- id: Int (Primary Key, always 1)
- level: Int
- xp: Int
- totalTasksCompleted: Int
- currentStreak: Int
- longestStreak: Int
- characterClass: CharacterClass
```

#### 3. Category
```kotlin
- id: Int (Primary Key)
- name: String
- color: String (Hex)
- icon: String
```

#### 4. Project
```kotlin
- id: Int (Primary Key)
- name: String
- description: String
- deadline: Long?
- isCompleted: Boolean
```

#### 5. Achievement
```kotlin
- id: Int (Primary Key)
- title: String
- description: String
- isUnlocked: Boolean
- unlockedDate: Long?
- type: AchievementType
```

#### 6. DailyQuest
```kotlin
- id: Int (Primary Key)
- title: String
- description: String
- xpReward: Int
- date: Long
- isCompleted: Boolean
- questType: QuestType
```

---

## 🎮 Core Features

### ✅ Implemented Features

#### 1. Task Management
- ✅ Create, Read, Update, Delete tasks
- ✅ Set due date and time (DatePickerDialog)
- ✅ Priority levels (LOW, MEDIUM, HIGH, URGENT)
- ✅ Categories and projects
- ✅ Recurring tasks (DAILY, WEEKLY, MONTHLY)
- ✅ XP reward system
- ✅ Search and filter
- ✅ Sort by date, priority, XP

#### 2. RPG System
- ✅ User profile with level and XP
- ✅ Character classes (WARRIOR, MAGE, ROGUE, PALADIN)
- ✅ Streak tracking (current and longest)
- ✅ XP calculation based on priority and time
- ✅ Level progression system
- ✅ Profile customization

#### 3. Gamification
- ✅ Daily quests
- ✅ Achievement system
- ✅ XP bonuses for streaks
- ✅ Task completion rewards
- ✅ Progress tracking

#### 4. Statistics
- ✅ Productivity score (0-100)
- ✅ Tasks completed (today, week, month)
- ✅ On-time completion rate
- ✅ 7-day activity chart
- ✅ Average XP per task
- ✅ Custom canvas visualizations

#### 5. Drawing Feature
- ✅ Canvas for sketching notes
- ✅ Color selection
- ✅ Brush size adjustment
- ✅ Eraser tool
- ✅ Save sketches with tasks

#### 6. UI/UX
- ✅ Material3 design
- ✅ Dark mode support
- ✅ Smooth animations
- ✅ Responsive layouts
- ✅ Live previews (@Preview)
- ✅ Bottom navigation
- ✅ Floating action buttons

---

## 🖥️ Screens

### Main Screens

1. **HomeScreen** - Dashboard
   - User profile card (level, XP, streak)
   - Daily quests
   - Quick access to recent tasks
   - Today's progress indicator

2. **TaskListScreen** - All Tasks
   - Search functionality
   - Filter by priority, category
   - Sort options
   - Swipe actions (complete/delete)

3. **AddEditTaskScreen** - Create/Modify Task
   - Title and description
   - Due date picker ✅ (Fixed bug)
   - Priority selection
   - Estimated time slider
   - XP reward (auto-calculated)
   - Recurring toggle

4. **ProfileScreen** - User Profile
   - Character avatar
   - Level and XP bar
   - Statistics cards
   - Character class selector
   - Achievement showcase

5. **StatisticsScreen** - Analytics
   - Productivity gauge
   - Overview cards
   - 7-day bar chart
   - Detailed stats

6. **AchievementScreen** - Achievements
   - Achievement gallery
   - Unlock status
   - Progress tracking

7. **CategoryScreen** - Manage Categories
   - Create/edit categories
   - Color picker
   - Icon selection

8. **ProjectScreen** - Manage Projects
   - Create/edit projects
   - Set deadlines
   - Track progress

9. **DrawingScreen** - Sketch Canvas
   - Drawing tools
   - Color palette
   - Save/load sketches

---

## 🎨 UI Components with Previews

### Preview Functions Added ✅

All major components now have `@Preview` functions for live preview in Android Studio:

#### HomeScreen.kt
- `UserProfileCardPreview`
- `QuestCardPreview`
- `TaskCardPreview`
- `PriorityBadgePreview`

#### TaskListScreen.kt
- `TaskListItemPreview`
- `PriorityChipPreview`

#### AddEditTaskScreen.kt
- `AddEditTaskScreenPreview`

#### ProfileScreen.kt
- `StatCardPreview`

#### StatisticsScreen.kt
- `ProductivityGaugePreview`
- `SimpleBarChartPreview`
- `StatCardPreview`
- `StatRowPreview`

**How to Use Previews**:
1. Open any screen file
2. Click "Split" or "Design" in top-right
3. See live preview without running app
4. Edit code and see changes instantly

---

## 🐛 Recent Bug Fixes

### ✅ Fixed: Date Picker Not Showing (AddEditTaskScreen)

**Problem**: Users couldn't set due dates when adding tasks

**Solution**: Added DatePickerDialog implementation
```kotlin
if (showDatePicker) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = { showDatePicker = false },
        confirmButton = { TextButton(...) { Text("OK") } }
    ) {
        DatePicker(state = datePickerState)
    }
}
```

**File**: `AddEditTaskScreen.kt:232-257`

---

## 🔄 State Management

### StateFlow Pattern

```kotlin
// In ViewModel
private val _taskTitle = MutableStateFlow("")
val taskTitle: StateFlow<String> = _taskTitle.asStateFlow()

fun updateTitle(title: String) {
    _taskTitle.value = title
}

// In Composable
@Composable
fun Screen(viewModel: TaskViewModel) {
    val taskTitle by viewModel.taskTitle.collectAsState()

    OutlinedTextField(
        value = taskTitle,
        onValueChange = { viewModel.updateTitle(it) }
    )
}
```

### Benefits
- ✅ Reactive updates (UI auto-refreshes)
- ✅ Survives configuration changes
- ✅ Type-safe
- ✅ Easy to test

---

## 🔐 Data Flow

### Create Task Example

```
User Input (UI)
    ↓
ViewModel.updateTitle()
    ↓
StateFlow updates
    ↓
UI recomposes
    ↓
User clicks Save
    ↓
ViewModel.saveTask()
    ↓
Repository.insertTask()
    ↓
TaskDao.insertTask()
    ↓
Room Database
    ↓
Flow<List<Task>> emits
    ↓
ViewModel collects
    ↓
StateFlow updates
    ↓
UI shows new task
```

---

## 📦 Dependencies

### Core Dependencies (build.gradle.kts)

```kotlin
// Kotlin
implementation("androidx.core:core-ktx:1.17.0")

// Compose
implementation(platform("androidx.compose:compose-bom:2024.09.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")
implementation("androidx.activity:activity-compose:1.11.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
```

---

## 🧪 Testing Approach

### Unit Tests (ViewModel)
```kotlin
@Test
fun `calculateXp returns correct value for high priority task`() {
    val xp = viewModel.calculateXp(Priority.HIGH, 60)
    assertEquals(35, xp) // 25 base + 10 time bonus
}
```

### Database Tests (DAO)
```kotlin
@Test
fun insertAndRetrieveTask() = runTest {
    val task = Task(title = "Test Task", priority = Priority.LOW)
    dao.insertTask(task)

    val tasks = dao.getActiveTasks().first()
    assertEquals(1, tasks.size)
    assertEquals("Test Task", tasks[0].title)
}
```

### UI Tests (Compose)
```kotlin
@Test
fun taskListDisplaysTasks() {
    composeTestRule.setContent {
        TaskListScreen(viewModel = mockViewModel)
    }

    composeTestRule.onNodeWithText("Test Task").assertExists()
}
```

---

## 🎯 Key Algorithms

### 1. XP Calculation
```kotlin
fun calculateXp(priority: Priority, estimatedMinutes: Int, streak: Int): Int {
    val baseXp = when (priority) {
        Priority.LOW -> 10
        Priority.MEDIUM -> 15
        Priority.HIGH -> 25
        Priority.URGENT -> 40
    }

    val timeBonus = (estimatedMinutes / 30) * 5
    val streakBonus = (streak * 0.05 * baseXp).toInt()

    return baseXp + timeBonus + streakBonus
}
```

### 2. Productivity Score
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

    return (completionRate + onTimeRate + streakBonus).toInt().coerceIn(0, 100)
}
```

### 3. Level Up Threshold
```kotlin
fun getRequiredXp(level: Int): Int {
    return 100 + (level - 1) * 50
}

// Level 1: 100 XP
// Level 2: 150 XP
// Level 3: 200 XP
// etc.
```

---

## 🎨 Color Scheme

### Material3 Theme

```kotlin
// Primary Colors
Primary = Color(0xFF6366F1)      // Indigo
Secondary = Color(0xFF8B5CF6)    // Purple
Tertiary = Color(0xFF10B981)     // Green

// Priority Colors
LowPriority = Color(0xFF22C55E)      // Green
MediumPriority = Color(0xFFFBBF24)   // Yellow
HighPriority = Color(0xFFFB923C)     // Orange
UrgentPriority = Color(0xFFEF4444)   // Red

// Status Colors
Success = Color(0xFF10B981)
Warning = Color(0xFFF59E0B)
Error = Color(0xFFEF4444)
```

---

## 🚀 Build & Run

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17 or higher
- Android SDK 34
- Kotlin 1.9.0+

### Build Steps
1. Open project in Android Studio
2. Sync Gradle files
3. Wait for indexing to complete
4. Click Run (▶️) or `Shift + F10`

### Build Variants
- **debug** - Development build with debugging enabled
- **release** - Production build (requires signing)

---

## 📝 Code Style

### Naming Conventions
- **Files**: PascalCase (`TaskViewModel.kt`)
- **Classes**: PascalCase (`TaskViewModel`)
- **Functions**: camelCase (`updateTitle()`)
- **Variables**: camelCase (`taskTitle`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_LEVEL`)
- **Composables**: PascalCase (`TaskCard()`)

### Composable Organization
```kotlin
@Composable
fun ScreenName(
    viewModel: ViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    // 1. State collection
    val state by viewModel.state.collectAsState()

    // 2. UI structure (Scaffold)
    Scaffold(
        topBar = { TopAppBar(...) },
        floatingActionButton = { FloatingActionButton(...) }
    ) { paddingValues ->
        // 3. Content
        Content(...)
    }
}

// 4. Helper composables
@Composable
private fun Content(...) { }

// 5. Preview functions
@Preview
@Composable
fun ScreenNamePreview() { }
```

---

## 🔧 Configuration

### Database Configuration
- **Name**: `task_quest_database`
- **Version**: 1
- **Entities**: 6 (Task, UserProfile, Category, Project, Achievement, DailyQuest)

### Navigation Routes
```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
    object EditTask : Screen("edit_task/{taskId}")
    object Profile : Screen("profile")
    object Statistics : Screen("statistics")
    object Achievements : Screen("achievements")
    object Categories : Screen("categories")
    object Projects : Screen("projects")
    object Drawing : Screen("drawing/{taskId}")
}
```

---

## 📊 Performance Considerations

### Database
- ✅ Indexed columns (id, categoryId, projectId)
- ✅ Flow for reactive queries (no manual refresh)
- ✅ Suspend functions for non-blocking operations
- ✅ Transaction support for bulk operations

### UI
- ✅ LazyColumn for efficient scrolling
- ✅ Key in items() for list optimization
- ✅ remember for state preservation
- ✅ derivedStateOf for computed values
- ✅ Stable data classes for recomposition optimization

---

## 🐞 Known Issues

### None at the moment ✅

All major features implemented and bugs fixed!

---

## 🔮 Future Enhancements

### Potential Features
- [ ] Cloud sync (Firebase)
- [ ] Widget support
- [ ] Pomodoro timer
- [ ] Social features (leaderboard)
- [ ] Advanced animations (Lottie)
- [ ] Voice input for tasks
- [ ] Calendar integration
- [ ] Dark mode toggle (currently follows system)

---

## 📖 Documentation Files

1. **TUTORIAL.md** - Comprehensive learning guide for understanding and defending the project
2. **TASK_QUEST_SPEC.md** - Original project specification with all features
3. **IMPLEMENTATION_STATUS.md** - Implementation checklist
4. **claude.md** (this file) - Project documentation and quick reference

---

## 👥 Team & Contact

**Project Type**: Academic Project (Mobile Programming Course)
**Institution**: FPT University
**Course**: PRM392 - Mobile Programming

---

## 📄 License

This is an academic project for educational purposes.

---

## ✅ Checklist for Project Defense

### Preparation
- [ ] Read TUTORIAL.md thoroughly
- [ ] Understand MVVM architecture
- [ ] Know what each technology does (Compose, Room, StateFlow)
- [ ] Understand data flow (UI → ViewModel → Repository → Database)
- [ ] Can explain XP calculation and leveling system
- [ ] Know how DatePicker was implemented
- [ ] Understand StateFlow vs LiveData difference
- [ ] Can explain why MVVM over other patterns

### Demo Preparation
- [ ] Test all features before presenting
- [ ] Prepare sample tasks to show
- [ ] Show date picker working
- [ ] Show previews in Android Studio
- [ ] Demonstrate XP earning and leveling
- [ ] Show database using Device File Explorer
- [ ] Highlight code quality (clean, organized)

### Questions You Should Be Ready For
1. Why did you choose Kotlin over Java?
2. What is MVVM and why did you use it?
3. How does Room database work?
4. What is Jetpack Compose?
5. How do you handle async operations?
6. Explain the XP calculation algorithm
7. How do you manage UI state?
8. What is the purpose of ViewModel?
9. How would you add a new feature?
10. What would you improve if you had more time?

---

**Last Updated**: January 2025
**Status**: ✅ Production Ready

---

For detailed technical explanations and defense preparation, **see TUTORIAL.md**! 📚
