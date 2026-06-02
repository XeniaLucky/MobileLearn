package com.example.diplom2.screen.dop_content.lessons_medium

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TAG = "GoogleMethod"

@Composable
fun GoogleMethodScreen(
    userId: Long,
    subStep: Int,
    onSubStepChange: (Int) -> Unit,
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onComplete: () -> Unit
) {
    var localScore by remember(score) { mutableStateOf(score) }
    fun addPoints(p: Int) {
        localScore += p
        try { onScoreUpdate(localScore) } catch (e: Exception) { Log.e(TAG, "Ошибка onScoreUpdate: ${e.message}") }
    }

    when (subStep) {
        0 -> GoogleSubStep0(
            onNext = { addPoints(15); onSubStepChange(1) },
            onWrong = { addPoints(-5) },
            score = localScore
        )
        1 -> GoogleSubStep1(
            onNext = { addPoints(10); onSubStepChange(2) },
            score = localScore
        )
        2 -> GoogleSubStep2(
            onNext = { addPoints(10); onSubStepChange(3) },
            score = localScore
        )
        3 -> GoogleSubStep3(
            score = localScore,
            onScoreUpdate = { newScore -> localScore = newScore; try { onScoreUpdate(localScore) } catch (e: Exception) { Log.e(TAG, "Ошибка onScoreUpdate: ${e.message}") } },
            onNext = { onSubStepChange(4) }
        )
        4 -> GoogleFinalCheckScreen(
            score = localScore,
            onScoreUpdate = { newScore -> localScore = newScore; try { onScoreUpdate(localScore) } catch (e: Exception) { Log.e(TAG, "Ошибка onScoreUpdate: ${e.message}") } },
            onComplete = onComplete
        )
    }
}

@Composable
private fun GoogleSubStep0(onNext: () -> Unit, onWrong: () -> Unit, score: Int) {
    var batteryChecked by remember { mutableStateOf(false) }
    var wifiChecked by remember { mutableStateOf(false) }
    var internetChecked by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D7DB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "🎮 МИССИЯ: Переезд на новый телефон",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Представьте: вы купили новый телефон 📱✨\n\n" +
                                "Но на старом остались:\n\n" +
                                "• контакты ☎️\n" +
                                "• фотографии 📸\n" +
                                "• WhatsApp 💬\n" +
                                "• приложения 🎮\n" +
                                "• пароли 🔐\n" +
                                "• заметки 📝\n\n" +
                                "Сейчас мы научимся переносить ВСЁ правильно и безопасно.",
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "📌 ЧТО НУЖНО ПЕРЕД НАЧАЛОМ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = batteryChecked,
                            onCheckedChange = { batteryChecked = it }
                        )
                        Column {
                            Text(
                                "🔋 Заряд телефона минимум 60%",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Если телефон выключится во время переноса — часть данных может потеряться.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = wifiChecked,
                            onCheckedChange = { wifiChecked = it }
                        )
                        Column {
                            Text(
                                "📶 Подключён Wi-Fi",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Фото и приложения могут весить десятки гигабайт. Через мобильный интернет это дорого и медленно.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = internetChecked,
                            onCheckedChange = { internetChecked = it }
                        )
                        Column {
                            Text(
                                "🌍 Интернет работает",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Google не сможет сохранить резервную копию без интернета.",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "💡 ВАЖНО ЗНАТЬ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Google аккаунт — это как сейф в интернете.\n\n" +
                                "Телефон сохраняет туда:\n\n" +
                                "✅ Контакты\n" +
                                "✅ Фото\n" +
                                "✅ Приложения\n" +
                                "✅ Пароли Wi-Fi\n" +
                                "✅ Настройки\n\n" +
                                "А потом новый телефон скачивает всё обратно.",
                        lineHeight = 24.sp
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (batteryChecked && wifiChecked && internetChecked) onNext()
                    else onWrong()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
            ) {
                Text("🚀 Начать перенос", color = Color.White, fontSize = 18.sp)
            }
        }

        item {
            Text(
                "🏆 Очки: $score",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C5F6E),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GoogleSubStep1(onNext: () -> Unit, score: Int) {
    var clickedSettings by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D7DB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "📱 ШАГ 1 — ОТКРОЙТЕ НАСТРОЙКИ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Сейчас мы будем делать резервную копию.\n\nДля этого нужно открыть настройки телефона.",
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        "👇 НАЖМИТЕ НА ИКОНКУ НАСТРОЕК",
                        color = Color( 0xFF9B0C3F),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        modifier = Modifier
                            .size(140.dp)
                            .clickable { clickedSettings = true },
                        shape = RoundedCornerShape(30.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (clickedSettings) Color(0xFFB2DFDB) else Color.White
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = null,
                                modifier = Modifier.size(90.dp),
                                tint = Color(0xFF2C5F6E)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("⚙️ Настройки", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (clickedSettings) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            "✅ Отлично!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "На настоящем телефоне настройки могут находиться:\n\n" +
                                    "• на главном экране\n" +
                                    "• в меню приложений\n" +
                                    "• в шторке сверху\n\n" +
                                    "Иногда значок выглядит как шестерёнка ⚙️",
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onNext,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                        ) {
                            Text("Дальше", color = Color.White)
                        }
                    }
                }
            }
        }

        item {
            Text(
                "🏆 Очки: $score",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C5F6E),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GoogleSubStep2(onNext: () -> Unit, score: Int) {
    var clickedGoogle by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D7DB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "🌍 ШАГ 2 — НАЙДИТЕ GOOGLE",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Теперь нужно открыть раздел Google.\n\nИменно там находится резервное копирование.",
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "👇 Нажмите на Google",
                        color = Color( 0xFF9B0C3F),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Storage, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Память")
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { clickedGoogle = true },
                    colors = CardDefaults.cardColors(
                        containerColor = if (clickedGoogle) Color(0xFFC8E6C9) else Color.White
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌍", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Google", fontWeight = FontWeight.Bold)
                            Text(
                                "Аккаунт, резервные копии",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Экран")
                    }
                }
            }
        }

        if (clickedGoogle) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            "✅ Отлично!",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "На некоторых телефонах раздел Google может называться:\n\n" +
                                    "• Google\n" +
                                    "• Аккаунты Google\n" +
                                    "• Google Services\n\n" +
                                    "Иногда он находится почти в самом низу настроек.",
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onNext,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))
                        ) {
                            Text("Продолжить", color = Color.White)
                        }
                    }
                }
            }
        }

        item {
            Text(
                "🏆 Очки: $score",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C5F6E),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun GoogleSubStep3(score: Int, onScoreUpdate: (Int) -> Unit, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D7DB))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "✅ Резервная копия создана!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C5F6E)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Теперь переходим к проверке перенесённых данных.",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Проверить данные", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun GoogleFinalCheckScreen(
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onComplete: () -> Unit
) {
    var contactsChecked by remember { mutableStateOf(false) }
    var photosChecked by remember { mutableStateOf(false) }
    var whatsappChecked by remember { mutableStateOf(false) }
    var passwordsChecked by remember { mutableStateOf(false) }

    var localScore by remember(score) { mutableStateOf(score) }
    val allChecked = contactsChecked && photosChecked && whatsappChecked && passwordsChecked

    fun updateScore(newScore: Int) {
        localScore = newScore
        onScoreUpdate(newScore)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D7DB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "🧠 ФИНАЛЬНАЯ ПРОВЕРКА (Google перенос)",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C5F6E),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "👥 ПРОВЕРКА КОНТАКТОВ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "📍 Откройте приложение:\n\n«Контакты»\n\nОбычно значок выглядит как: 👤 или 📒",
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "🔍 ЧТО ПРОВЕРЯЕМ?",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Text(
                        "• есть ли номера родственников\n" +
                                "• сохранились ли имена\n" +
                                "• появились ли новые контакты\n" +
                                "• нет ли пустых номеров",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (!contactsChecked) {
                                contactsChecked = true
                                updateScore(localScore + 10)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (contactsChecked) Color(0xFF4CAF50) else Color(0xFF2C5F6E)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (contactsChecked) "✅ Контакты проверены" else "👥 Проверить контакты",
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "🖼️ ПРОВЕРКА ФОТО И ВИДЕО",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "📍 Откройте:\n\n• Галерею\nили\n• Google Фото",
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "🎯 ЧТО ИЩЕМ?",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Text(
                        "• старые семейные фото\n" +
                                "• видео из WhatsApp\n" +
                                "• скриншоты\n" +
                                "• фото документов\n" +
                                "• папку Camera",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (!photosChecked) {
                                photosChecked = true
                                updateScore(localScore + 10)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (photosChecked) Color(0xFF4CAF50) else Color(0xFF2C5F6E)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (photosChecked) "✅ Фото проверены" else "🖼️ Проверить фото",
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "💬 ПРОВЕРКА WHATSAPP",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "⚠️ WhatsApp — самое важное приложение.\n\n" +
                                "Там:\n• переписки\n• семейные фото\n• голосовые сообщения\n• документы",
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "📍 ЧТО ДЕЛАТЬ?",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Text(
                        "1. Откройте WhatsApp\n" +
                                "2. Введите номер телефона\n" +
                                "3. Получите SMS\n" +
                                "4. Нажмите: «Восстановить резервную копию»",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (!whatsappChecked) {
                                whatsappChecked = true
                                updateScore(localScore + 15)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (whatsappChecked) Color(0xFF4CAF50) else Color(0xFF2C5F6E)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (whatsappChecked) "✅ WhatsApp восстановлен" else "💬 Проверить WhatsApp",
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "🔐 ПРОВЕРКА ПАРОЛЕЙ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Google может переносить:\n\n" +
                                "• пароли сайтов\n" +
                                "• Wi-Fi пароли\n" +
                                "• логины приложений",
                        fontSize = 15.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "📍 КАК ПРОВЕРИТЬ?",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Text(
                        "Попробуйте открыть:\n\n" +
                                "• YouTube\n" +
                                "• Gmail\n" +
                                "• Госуслуги\n\n" +
                                "Если аккаунт уже вошёл автоматически — всё хорошо.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (!passwordsChecked) {
                                passwordsChecked = true
                                updateScore(localScore + 10)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (passwordsChecked) Color(0xFF4CAF50) else Color(0xFF2C5F6E)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (passwordsChecked) "✅ Пароли работают" else "🔐 Проверить пароли",
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (allChecked) {
                        updateScore(localScore + 50)
                        onComplete()
                    }
                },
                enabled = allChecked,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (allChecked) Color(0xFF4CAF50) else Color.Gray
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    if (allChecked) "🏆 ЗАВЕРШИТЬ ПЕРЕНОС" else "⚠️ Проверьте все пункты",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Text(
                "🏆 Очки: $localScore",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C5F6E),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}