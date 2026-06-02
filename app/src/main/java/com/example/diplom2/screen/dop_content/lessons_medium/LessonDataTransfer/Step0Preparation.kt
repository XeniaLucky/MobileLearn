package com.example.diplom2.screen.dop_content.lessons_medium

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TAG = "Step0Preparation"

private fun saveCheckboxState(context: Context, userId: Long, key: String, value: Boolean) {
    try {
        context.getSharedPreferences("lesson_prep_$userId", Context.MODE_PRIVATE)
            .edit().putBoolean(key, value).apply()
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка сохранения $key: ${e.message}")
    }
}

private fun getCheckboxState(context: Context, userId: Long, key: String, default: Boolean): Boolean {
    return try {
        context.getSharedPreferences("lesson_prep_$userId", Context.MODE_PRIVATE)
            .getBoolean(key, default)
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка чтения $key: ${e.message}")
        default
    }
}

@Composable
fun Step0Preparation(
    userId: Long,
    score: Int,
    onScoreUpdate: (Int) -> Unit,
    onNext: (Int) -> Unit
) {
    val context = LocalContext.current
    var oldPhoneCharged by remember { mutableStateOf(getCheckboxState(context, userId, "oldPhoneCharged", false)) }
    var newPhoneCharged by remember { mutableStateOf(getCheckboxState(context, userId, "newPhoneCharged", false)) }
    var wifiReady by remember { mutableStateOf(getCheckboxState(context, userId, "wifiReady", false)) }
    var googleRemembered by remember { mutableStateOf(getCheckboxState(context, userId, "googleRemembered", false)) }

    val allReady = oldPhoneCharged && newPhoneCharged && wifiReady && googleRemembered
    var localScore by remember(score) { mutableStateOf(score) }

    fun addPoints(points: Int) {
        localScore += points
        onScoreUpdate(localScore)
    }

    LaunchedEffect(oldPhoneCharged) { saveCheckboxState(context, userId, "oldPhoneCharged", oldPhoneCharged) }
    LaunchedEffect(newPhoneCharged) { saveCheckboxState(context, userId, "newPhoneCharged", newPhoneCharged) }
    LaunchedEffect(wifiReady) { saveCheckboxState(context, userId, "wifiReady", wifiReady) }
    LaunchedEffect(googleRemembered) { saveCheckboxState(context, userId, "googleRemembered", googleRemembered) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("📲 ПЕРЕЕЗД НА НОВЫЙ ТЕЛЕФОН", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Представьте ситуацию:\n\nВы купили новый смартфон.\nНа старом телефоне остались:\n\n• фотографии семьи 👨‍👩‍👧\n• контакты ☎️\n• WhatsApp 💬\n• приложения 📱\n• пароли 🔐\n• документы 📂\n\nВаша задача — ничего не потерять.", fontSize = 16.sp, lineHeight = 24.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🎮 В ЭТОМ СИМУЛЯТОРЕ ВЫ НАУЧИТЕСЬ:", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    Text("✅ Переносить контакты\n✅ Переносить фотографии\n✅ Переносить приложения\n✅ Делать резервные копии\n✅ Подключать телефоны\n✅ Не терять данные\n✅ Правильно сбрасывать старый телефон", fontSize = 15.sp, lineHeight = 24.sp)
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3CD)), shape = RoundedCornerShape(18.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⚠️ ОЧЕНЬ ВАЖНО!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8A6D3B))
                    Text("Многие люди совершают ошибку:\n\n❌ Сбрасывают старый телефон СЛИШКОМ РАНО\n\nПосле этого:\n• пропадают фото\n• исчезают контакты\n• теряются документы\n\nСегодня мы научимся делать всё ПРАВИЛЬНО.", fontSize = 15.sp, lineHeight = 23.sp)
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.92f)), shape = RoundedCornerShape(18.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🧠 ПОДГОТОВКА К ПЕРЕНОСУ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
                    // Старый телефон
                    Card(modifier = Modifier.fillMaxWidth().clickable { oldPhoneCharged = !oldPhoneCharged; if (oldPhoneCharged) addPoints(5) }, colors = CardDefaults.cardColors(containerColor = if (oldPhoneCharged) Color(0xFFDFF5E1) else Color(0xFFF4F4F4))) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(if (oldPhoneCharged) "✅" else "⬜", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Старый телефон заряжен", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("📌 Почему это важно?\n\nВо время переноса телефон может работать 30–60 минут.\nЕсли он выключится — часть данных может НЕ перенестись.", fontSize = 13.sp, lineHeight = 20.sp, color = Color.DarkGray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Новый телефон
                    Card(modifier = Modifier.fillMaxWidth().clickable { newPhoneCharged = !newPhoneCharged; if (newPhoneCharged) addPoints(5) }, colors = CardDefaults.cardColors(containerColor = if (newPhoneCharged) Color(0xFFDFF5E1) else Color(0xFFF4F4F4))) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(if (newPhoneCharged) "✅" else "⬜", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Новый телефон заряжен", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("📌 Новый телефон должен быть заряжен минимум на 50%.", fontSize = 13.sp, color = Color.DarkGray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Wi-Fi
                    Card(modifier = Modifier.fillMaxWidth().clickable { wifiReady = !wifiReady; if (wifiReady) addPoints(5) }, colors = CardDefaults.cardColors(containerColor = if (wifiReady) Color(0xFFDFF5E1) else Color(0xFFF4F4F4))) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(if (wifiReady) "📶" else "❌", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Wi-Fi подключен", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("📌 Без Wi-Fi:\n• резервная копия может не скачаться\n• фото могут не перенестись\n• приложения не восстановятся", fontSize = 13.sp, lineHeight = 20.sp, color = Color.DarkGray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Google пароль
                    Card(modifier = Modifier.fillMaxWidth().clickable { googleRemembered = !googleRemembered; if (googleRemembered) addPoints(5) }, colors = CardDefaults.cardColors(containerColor = if (googleRemembered) Color(0xFFDFF5E1) else Color(0xFFF4F4F4))) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(if (googleRemembered) "🔐" else "⬜", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Я помню пароль от Google аккаунта", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("📌 Без Google аккаунта:\n• не восстановятся контакты\n• не вернутся приложения\n• не восстановятся пароли", fontSize = 13.sp, lineHeight = 20.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF7FF)), shape = RoundedCornerShape(18.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💡 СОВЕТ ОТ ВИРТУАЛЬНОГО ПОМОЩНИКА", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF2C5F6E))
                    Text("Лучше всего переносить данные вечером дома,\nкогда:\n\n✅ есть Wi-Fi\n✅ есть зарядка\n✅ никто не торопит\n✅ можно спокойно всё проверить", fontSize = 14.sp, lineHeight = 22.sp)
                }
            }
        }
        item {
            Button(onClick = { if (allReady) { addPoints(20); onNext(localScore) } }, enabled = allReady, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                Text(if (allReady) "🚀 НАЧАТЬ ОБУЧЕНИЕ" else "⚠️ Отметьте все пункты", fontSize = 18.sp, color = Color.White)
            }
        }
        item {
            Text(text = "🏆 Очки: $localScore", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
        }
    }
}