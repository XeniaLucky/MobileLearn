package com.example.diplom2.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import bd.AppDatabase
import coil.compose.AsyncImage
import com.example.diplom2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import repository.UserRepository
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalProfileScreen(
    navController: NavController,
    userId: Long,
    levelPrefix: String,
    accentColor: Color,
    onLogout: () -> Unit  // Добавляем колбэк для выхода
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getInstance(context)
    val userRepo = UserRepository(db.userDao())

    var user by remember { mutableStateOf<table.User?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        isLoading = true
        val loadedUser = withContext(Dispatchers.IO) {
            userRepo.getUser(userId)
        }
        user = loadedUser
        isLoading = false
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                val savedPath = saveImageToAppStorage(context, it)
                if (savedPath != null) {
                    withContext(Dispatchers.IO) {
                        userRepo.updateAvatar(userId, savedPath)
                    }
                    user = user?.copy(avatarPath = savedPath)
                    Toast.makeText(context, "Аватар обновлён", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Ошибка сохранения аватара", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Нужно разрешение на чтение файлов", Toast.LENGTH_SHORT).show()
        }
    }

    fun pickImage() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    imagePickerLauncher.launch("image/*")
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    imagePickerLauncher.launch("image/*")
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else -> imagePickerLauncher.launch("image/*")
        }
    }

    val prefs = context.getSharedPreferences("progress_${levelPrefix}_$userId", Context.MODE_PRIVATE)
    val lessonsCompleted = when (levelPrefix) {
        "light_" -> {
            val gameKeys = listOf("game_power", "game_call", "game_touch", "game_contacts", "game_messages", "game_camera", "game_wifi", "game_apps")
            gameKeys.count { prefs.getBoolean("${it}_completed", false) }
        }
        "medium_" -> {
            val lessonKeys = listOf("game_notifications", "game_memory", "game_battery", "game_safeapps", "game_datatransfer", "game_recovery")
            lessonKeys.count { prefs.getBoolean("${it}_completed", false) }
        }
        "expert_" -> {
            val lessonKeys = listOf("lesson_adb", "lesson_root", "lesson_custom_roms", "lesson_optimization", "lesson_scripts", "lesson_security")
            lessonKeys.count { prefs.getBoolean("${it}_completed", false) }
        }
        else -> 0
    }
    val totalLessons = when (levelPrefix) {
        "light_" -> 8
        "medium_" -> 6
        "expert_" -> 6
        else -> 0
    }
    val gamesPlayed = prefs.getInt("games_played", 0)
    val achievementsCount = prefs.getInt("achievements", 0)
    val streakDays = prefs.getInt("streak_days", 0)

    if (isLoading || user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentUser = user!!
    val displayName = currentUser.name
    val displayEmail = currentUser.email
    val avatarPath = currentUser.avatarPath

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFE3D3))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(100.dp).clickable { pickImage() }) {
            if (avatarPath != null) {
                AsyncImage(
                    model = File(avatarPath),
                    contentDescription = "Аватар",
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.avatarka_light),
                    contentDescription = "Аватар",
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .background(Color(0xFF8B5A2B), CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Сменить аватар",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(displayName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
        Text(
            "Уровень ${levelPrefix.replace("_", "").capitalize()}",
            fontSize = 16.sp,
            color = accentColor,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Статистика", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Пройдено уроков", "$lessonsCompleted/$totalLessons", accentColor)
                    StatItem("Сыграно игр", "$gamesPlayed", accentColor)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Достижений", "$achievementsCount", accentColor)
                    StatItem("Дней подряд", "$streakDays 🔥", accentColor)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth().clickable { navController.navigate("family_plan") },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.family),
                    contentDescription = "Семейный тариф",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Семейный тариф", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                    Text("До 5 человек, скидка 30% →", fontSize = 14.sp, color = Color(0xFF616161))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                editName = displayName
                editEmail = displayEmail
                showEditDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Редактировать профиль", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Выйти из аккаунта", fontSize = 16.sp, color = Color(0xFFD32F2F))
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Редактировать профиль") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Имя") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                userRepo.updateUserData(userId, editName, editEmail, currentUser.avatarPath)
                            }
                            user = user?.copy(name = editName, email = editEmail)
                            showEditDialog = false
                            Toast.makeText(context, "Данные обновлены", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Выход из аккаунта") },
            text = { Text("Вы уверены, что хотите выйти?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Выйти")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
private fun StatItem(label: String, value: String, accentColor: Color) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(label, fontSize = 14.sp, color = Color(0xFF616161))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = accentColor)
    }
}

private suspend fun saveImageToAppStorage(context: Context, uri: Uri): String? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
            val fileName = "avatar_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}