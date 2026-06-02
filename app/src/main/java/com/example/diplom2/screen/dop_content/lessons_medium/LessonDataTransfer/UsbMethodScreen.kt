package com.example.diplom2.screen.dop_content.lessons_medium

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TAG = "UsbMethod"

@Composable
fun UsbMethodScreen(
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
        0 -> UsbSubStep0(
            onNext = { addPoints(10); onSubStepChange(1) },
            onWrong = { addPoints(-5) },
            score = localScore
        )
        1 -> UsbSubStep1(
            onNext = { addPoints(10); onSubStepChange(2) },
            score = localScore
        )
        2 -> UsbSubStep2(
            onComplete = {
                addPoints(25)
                onSubStepChange(3)
            },
            score = localScore
        )
        3 -> UsbSubStep3(
            score = localScore,
            onScoreUpdate = { newScore -> localScore = newScore; try { onScoreUpdate(localScore) } catch (e: Exception) { Log.e(TAG, "Ошибка onScoreUpdate: ${e.message}") } },
            onComplete = onComplete
        )
    }
}

// --- USB подшаг 0: подключение кабеля ---
@Composable
private fun UsbSubStep0(onNext: () -> Unit, onWrong: () -> Unit, score: Int) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ШАГ 1: Подключите телефон к компьютеру", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("🔌 Используйте оригинальный USB-кабель", fontSize = 14.sp)
                Text("Подключите телефон к компьютеру → На телефоне появится уведомление", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("⚠️ Если компьютер не видит телефон, попробуйте другой кабель или порт USB", fontSize = 12.sp, color = Color( 0xFF9B0C3F))
            }
        }
        Row {
            Button(onClick = onNext, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
                Text("Подключил", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onWrong, colors = ButtonDefaults.buttonColors(containerColor = Color( 0xFF9B0C3F))) {
                Text("Не подключал", color = Color.White)
            }
        }
        Text("Очки: $score")
    }
}

// --- USB подшаг 1: выбор режима MTP ---
@Composable
private fun UsbSubStep1(onNext: () -> Unit, score: Int) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFC4D7DB)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ШАГ 2: Выберите режим передачи файлов", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("📱 На телефоне нажмите на уведомление «Зарядка через USB»", fontSize = 14.sp)
                Text("Выберите «Передача файлов (MTP)» (НЕ «Только зарядка»!)", fontSize = 14.sp, color = Color(0xFF2C5F6E))
                Spacer(modifier = Modifier.height(12.dp))
                Text("После этого на компьютере откроется папка телефона", fontSize = 12.sp, color = Color.Gray)
            }
        }
        Button(onClick = onNext, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C5F6E))) {
            Text("Выбрал MTP", color = Color.White)
        }
        Text("Очки: $score")
    }
}

// --- USB подшаг 2: симулятор настройки нового телефона (рефакторинг) ---
@Composable
private fun UsbSubStep2(
    onComplete: () -> Unit,
    score: Int
) {
    // Состояния каждого шага
    var wifiConnected by remember { mutableStateOf(false) }
    var googleLogged by remember { mutableStateOf(false) }
    var backupSelected by remember { mutableStateOf(false) }
    var appsSelected by remember { mutableStateOf(false) }
    var restoreStarted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC4D7DB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { HeaderTitle() }
        item { GameDescription() }
        item {
            WifiStep(
                wifiConnected = wifiConnected,
                onWifiConnected = { wifiConnected = true }
            )
        }
        item {
            GoogleAccountStep(
                googleLogged = googleLogged,
                wifiConnected = wifiConnected,
                onGoogleLogged = { googleLogged = true }
            )
        }
        item {
            BackupSelectionStep(
                backupSelected = backupSelected,
                googleLogged = googleLogged,
                onBackupSelected = { backupSelected = true }
            )
        }
        item {
            AppsSelectionStep(
                appsSelected = appsSelected,
                backupSelected = backupSelected,
                onAppsSelected = { appsSelected = true }
            )
        }
        item {
            RestoreStep(
                restoreStarted = restoreStarted,
                appsSelected = appsSelected,
                onRestoreStarted = { restoreStarted = true }
            )
        }
        if (restoreStarted) {
            item {
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("✅ ЗАВЕРШИТЬ ПЕРЕНОС ДАННЫХ", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            Text(
                "🏆 Очки: $score",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C5F6E),
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- Вспомогательные компоненты ---

@Composable
private fun HeaderTitle() {
    Text(
        "📱 СИМУЛЯТОР НАСТРОЙКИ НОВОГО ТЕЛЕФОНА",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2C5F6E),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun GameDescription() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.95f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("🎮 ИГРОВОЕ ЗАДАНИЕ", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Представьте:\nВы только что купили новый смартфон.\n\n" +
                        "На старом телефоне остались:\n" +
                        "• контакты\n• WhatsApp\n• фотографии\n• банковские приложения\n" +
                        "• заметки\n• пароли\n• музыка\n• Telegram\n\n" +
                        "Ваша задача — правильно восстановить всё это на новом телефоне.",
                fontSize = 15.sp, lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("⚠️ ВАЖНО ПОНИМАТЬ", fontWeight = FontWeight.Bold, color = Color( 0xFF9B0C3F), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "❗ Если сделать что-то неправильно:\n\n" +
                        "• можно потерять фото\n• контакты могут исчезнуть\n" +
                        "• WhatsApp может удалиться вместе с перепиской\n" +
                        "• приложения придётся устанавливать вручную\n\n" +
                        "Поэтому действуем очень внимательно.",
                fontSize = 15.sp, lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun WifiStep(wifiConnected: Boolean, onWifiConnected: () -> Unit) {
    StepCard(
        title = "📶 ШАГ 1 — ПОДКЛЮЧИТЕ WI-FI",
        content = {
            Text(
                "Когда вы впервые включаете новый телефон, Android спрашивает:\n" +
                        "«Подключиться к Wi-Fi?» Это ОБЯЗАТЕЛЬНО.\n\n" +
                        "Без интернета телефон НЕ сможет:\n" +
                        "• скачать резервную копию\n• загрузить приложения\n" +
                        "• восстановить контакты\n• войти в Google аккаунт",
                fontSize = 15.sp, lineHeight = 23.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("💡 ПОДСКАЗКА ДЛЯ ПЕНСИОНЕРОВ", fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Text(
                "Wi-Fi — это домашний интернет. Обычно название сети написано на роутере.\n\n" +
                        "Например:\n• TP-Link_1234\n• MGTS_5G\n• KeeneticHome\n\n" +
                        "Пароль тоже написан на роутере.",
                fontSize = 14.sp, lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onWifiConnected,
                colors = ButtonDefaults.buttonColors(containerColor = if (wifiConnected) Color(0xFF4CAF50) else Color(0xFF2C5F6E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (wifiConnected) "✅ Wi-Fi подключён" else "📶 Подключить Wi-Fi", color = Color.White)
            }
        }
    )
}

@Composable
private fun GoogleAccountStep(googleLogged: Boolean, wifiConnected: Boolean, onGoogleLogged: () -> Unit) {
    StepCard(
        title = "🔐 ШАГ 2 — ВОЙДИТЕ В GOOGLE АККАУНТ",
        content = {
            Text(
                "Теперь телефон спросит: «Войти в Google аккаунт?»\n\n" +
                        "⚠️ Нужно войти ИМЕННО В ТОТ аккаунт, который был на старом телефоне.\n" +
                        "Иначе резервная копия не найдётся.",
                fontSize = 15.sp, lineHeight = 23.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("📍 ГДЕ ВВОДИТЬ ПОЧТУ?", fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Text(
                "Обычно появляется экран:\n\nGoogle\nВведите адрес электронной почты\n\nПример: ivan@gmail.com",
                fontSize = 14.sp, lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("⚠️ ЧАСТЫЕ ОШИБКИ", fontWeight = FontWeight.Bold, color = Color( 0xFF9B0C3F))
            Text(
                "❌ Люди забывают пароль\n❌ Вводят другую почту\n❌ Пропускают этот шаг\n\n" +
                        "Тогда данные НЕ восстановятся.",
                fontSize = 14.sp, lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onGoogleLogged,
                enabled = wifiConnected,
                colors = ButtonDefaults.buttonColors(containerColor = if (googleLogged) Color(0xFF4CAF50) else Color(0xFF2C5F6E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (googleLogged) "✅ Аккаунт подключён" else "🔐 Войти в Google", color = Color.White)
            }
        }
    )
}

@Composable
private fun BackupSelectionStep(backupSelected: Boolean, googleLogged: Boolean, onBackupSelected: () -> Unit) {
    StepCard(
        title = "💾 ШАГ 3 — ВЫБЕРИТЕ РЕЗЕРВНУЮ КОПИЮ",
        content = {
            Text(
                "После входа телефон покажет список резервных копий.\n\n" +
                        "Например:\nSamsung A52\nРезервная копия: вчера\n2.4 ГБ\n\n" +
                        "Нужно выбрать САМУЮ СВЕЖУЮ копию.",
                fontSize = 15.sp, lineHeight = 23.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("💡 ЧТО ТАКОЕ РЕЗЕРВНАЯ КОПИЯ?", fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Text(
                "Это сохранённая копия телефона в интернете.\n\nGoogle хранит:\n" +
                        "• контакты\n• SMS\n• настройки\n• приложения\n• Wi-Fi пароли\n• историю звонков",
                fontSize = 14.sp, lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onBackupSelected,
                enabled = googleLogged,
                colors = ButtonDefaults.buttonColors(containerColor = if (backupSelected) Color(0xFF4CAF50) else Color(0xFF2C5F6E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (backupSelected) "✅ Копия выбрана" else "💾 Выбрать резервную копию", color = Color.White)
            }
        }
    )
}

@Composable
private fun AppsSelectionStep(appsSelected: Boolean, backupSelected: Boolean, onAppsSelected: () -> Unit) {
    StepCard(
        title = "📲 ШАГ 4 — ВЫБЕРИТЕ ПРИЛОЖЕНИЯ",
        content = {
            Text(
                "Телефон предложит восстановить приложения.\n\n" +
                        "Обычно появляется список:\n\n☑ WhatsApp\n☑ Telegram\n☑ Сбербанк\n" +
                        "☑ YouTube\n☑ Госуслуги\n\nЛучше оставить ВСЕ галочки.",
                fontSize = 15.sp, lineHeight = 23.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("⚠️ ОЧЕНЬ ВАЖНО", fontWeight = FontWeight.Bold, color = Color( 0xFF9B0C3F))
            Text(
                "Некоторые банковские приложения могут НЕ восстановиться автоматически.\n\n" +
                        "Например:\n• Сбер\n• Тинькофф\n\nИх иногда нужно устанавливать вручную.",
                fontSize = 14.sp, lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onAppsSelected,
                enabled = backupSelected,
                colors = ButtonDefaults.buttonColors(containerColor = if (appsSelected) Color(0xFF4CAF50) else Color(0xFF2C5F6E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (appsSelected) "✅ Приложения выбраны" else "📲 Выбрать приложения", color = Color.White)
            }
        }
    )
}

@Composable
private fun RestoreStep(restoreStarted: Boolean, appsSelected: Boolean, onRestoreStarted: () -> Unit) {
    StepCard(
        title = "🚀 ШАГ 5 — ЗАПУСК ВОССТАНОВЛЕНИЯ",
        content = {
            Text(
                "Теперь нажмите: «ВОССТАНОВИТЬ» или «НАЧАТЬ КОПИРОВАНИЕ»\n\n" +
                        "Телефон начнёт:\n• скачивать приложения\n• восстанавливать контакты\n" +
                        "• переносить настройки\n• загружать фото",
                fontSize = 15.sp, lineHeight = 23.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text("⏳ СКОЛЬКО ЭТО ДЛИТСЯ?", fontWeight = FontWeight.Bold, color = Color(0xFF2C5F6E))
            Text(
                "Может длиться:\n\n• 10 минут\n• 30 минут\n• несколько часов\n\n" +
                        "Это нормально.\n\n⚠️ НЕ выключайте телефон!",
                fontSize = 14.sp, lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onRestoreStarted,
                enabled = appsSelected,
                colors = ButtonDefaults.buttonColors(containerColor = if (restoreStarted) Color(0xFF4CAF50) else Color(0xFF2C5F6E)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (restoreStarted) "✅ Восстановление началось" else "🚀 Начать восстановление", color = Color.White)
            }
        }
    )
}

@Composable
private fun StepCard(title: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF2C5F6E))
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

// --- USB подшаг 3: финальная проверка данных ---
@Composable
private fun UsbSubStep3(
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

    // Вспомогательная функция для обновления счёта
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
                "🧠 ФИНАЛЬНАЯ ПРОВЕРКА НОВОГО ТЕЛЕФОНА",
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
                        "🎮 СЕЙЧАС ВЫ — ТЕХНИЧЕСКИЙ СПЕЦИАЛИСТ!\n\n" +
                                "После переноса данных нельзя сразу радоваться.\n\n" +
                                "Нужно ОБЯЗАТЕЛЬНО проверить, что всё действительно перенеслось.",
                        fontSize = 16.sp,
                        lineHeight = 25.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Проверка контактов
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
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "⚠️ ЧАСТАЯ ПРОБЛЕМА",
                        fontWeight = FontWeight.Bold,
                        color = Color( 0xFF9B0C3F)
                    )
                    Text(
                        "Иногда контакты сохранены НЕ в Google, а в памяти SIM-карты.\n\n" +
                                "Тогда часть номеров может пропасть.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "💡 СОВЕТ",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Text(
                        "Лучше хранить контакты в Google аккаунте. Тогда они никогда не потеряются.",
                        fontSize = 14.sp
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

        // Проверка фото
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
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "⚠️ ВАЖНО",
                        fontWeight = FontWeight.Bold,
                        color = Color( 0xFF9B0C3F)
                    )
                    Text(
                        "Иногда фото загружаются НЕ сразу.\n\n" +
                                "Если интернет медленный — подождите.\n\n" +
                                "Google Фото может загружать изображения несколько часов.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "💡 ЛАЙФХАК",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Text(
                        "Если фото не появились:\n\n" +
                                "Google Фото → нажмите на аватар → «Включить синхронизацию»",
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

        // Проверка WhatsApp
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
                        "⚠️ WhatsApp — самое важное приложение для многих пенсионеров.\n\n" +
                                "Там:\n" +
                                "• переписки\n" +
                                "• семейные фото\n" +
                                "• голосовые сообщения\n" +
                                "• документы",
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
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "⚠️ ОЧЕНЬ ВАЖНО",
                        fontWeight = FontWeight.Bold,
                        color = Color( 0xFF9B0C3F)
                    )
                    Text(
                        "НЕ нажимайте «Пропустить».\n\n" +
                                "Иначе переписка может удалиться навсегда.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "💡 СОВЕТ",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C5F6E)
                    )
                    Text(
                        "Перед переносом на старом телефоне лучше сделать:\n\n" +
                                "WhatsApp → Настройки → Чаты → Резервная копия",
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

        // Проверка паролей
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

        // Финальная кнопка
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
                    if (allChecked) "🏆 ЗАВЕРШИТЬ ПЕРЕНОС ДАННЫХ" else "⚠️ Проверьте все пункты",
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