package com.example.diplom2.screen.dop_content.lessons_light

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavController
import com.example.diplom2.screen.saveLessonProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLessonScreen(navController: NavController, userId: Long, onComplete: () -> Unit = {}) {
    var step by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var contactName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var contactSaved by remember { mutableStateOf(false) }
    var callActive by remember { mutableStateOf(false) }
    var speakerOn by remember { mutableStateOf(false) }
    var callEnded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по звонкам
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📞 Звонки: как звонить, отвечать и управлять вызовами", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Звонки – одна из главных функций телефона. Вот полное руководство:\n\n" +
                                "📌 **Как позвонить по номеру?**\n" +
                                "1. Откройте приложение «Телефон» (зелёная трубка 📞).\n" +
                                "2. Нажмите на клавиатуру (обычно значок 🔢).\n" +
                                "3. Введите номер телефона (10 или 11 цифр).\n" +
                                "4. Нажмите зелёную трубку 📞.\n" +
                                "5. Чтобы завершить звонок – нажмите красную трубку 🔴.\n\n" +
                                "📌 **Как позвонить из контактов?**\n" +
                                "1. Откройте приложение «Телефон» → вкладка «Контакты».\n" +
                                "2. Найдите нужный контакт (листайте или введите имя в поиске).\n" +
                                "3. Нажмите на контакт, затем на зелёную трубку.\n\n" +
                                "📌 **Как добавить контакт перед звонком?**\n" +
                                "1. Откройте «Телефон» → вкладка «Контакты».\n" +
                                "2. Нажмите на значок «+» или «Добавить контакт».\n" +
                                "3. Введите имя и номер телефона.\n" +
                                "4. Нажмите «Сохранить».\n\n" +
                                "📌 **Как ответить на звонок?**\n" +
                                "• На экране появится входящий вызов.\n" +
                                "• Проведите зелёную трубку вправо, чтобы ответить.\n" +
                                "• Проведите красную трубку влево, чтобы отклонить.\n" +
                                "• Нажмите кнопку громкости (один раз) – звонок замолчит (но не сбросится).\n" +
                                "• Нажмите кнопку питания дважды – звонок сбросится (удобно, если телефон в кармане).\n\n" +
                                "📌 **Как управлять звонком во время разговора?**\n" +
                                "• Громкая связь (динамик) – нажмите на значок динамика 📢.\n" +
                                "• Выключить микрофон (Mute) – нажмите на значок микрофона 🎙️.\n" +
                                "• Добавить участника – нажмите «Добавить вызов» (если поддерживается).\n" +
                                "• Переключиться на другой вызов – если второй входящий, нажмите «Ответить».\n" +
                                "• Завершить – красная трубка 🔴.\n\n" +
                                "📌 **Что делать, если телефон не звонит?**\n" +
                                "• Проверьте, включён ли режим «Не беспокоить» (полумесяц в шторке).\n" +
                                "• Проверьте громкость звонка (кнопки на боку).\n" +
                                "• Проверьте, не включён ли Bluetooth – звонок может уходить в наушники.\n" +
                                "• Перезагрузите телефон.\n\n" +
                                "📌 **Что делать, если не слышно собеседника?**\n" +
                                "• Проверьте громкость разговора (кнопки во время звонка).\n" +
                                "• Проверьте, не закрыли ли вы динамик рукой.\n" +
                                "• Включите/выключите громкую связь.\n" +
                                "• Попробуйте перезвонить.\n\n" +
                                "🎯 **Задание:** пройдите симуляцию звонка, добавляя контакт, отвечая и управляя вызовом.",
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }
            }
            Button(
                onClick = { step = 1 },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Начать симуляцию звонка", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Добавление контакта
    if (step == 1) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📇 ШАГ 1: Добавьте контакт «Мама»", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Чтобы позвонить, нужно добавить контакт. Укажите имя и номер телефона.", fontSize = 14.sp)
                    Icon(Icons.Default.Contacts, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contactName,
                onValueChange = { contactName = it },
                label = { Text("Имя контакта") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it ; errorMessage=null},
                label = { Text("Номер телефона (например, 89001234567)") },
                isError = errorMessage != null && errorMessage!!.contains("телефон"),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )

            if (errorMessage != null && (errorMessage!!.contains("email") || errorMessage!!.contains("телефон"))) {
                Text(errorMessage!!, color = Color(
                    0xFF9B0C3F
                ), fontSize = 12.sp)
            }

            Button(
                onClick = {
                    if (contactNumber.isNotBlank() && !contactNumber.replace(Regex("[^0-9]"), "").matches(Regex("\\d{10,11}"))) {
                        errorMessage = "Введите корректный номер телефона (10-11 цифр)"
                    } else {
                        score += 15
                        errorMessage = null
                        step = 2
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) { Text("Сохранить контакт", color = Color.White) }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Входящий звонок
    if (step == 2) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📞 ШАГ 2: Ответьте на входящий звонок", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Звонит $contactName ($contactNumber). Проведите зелёную трубку вправо, чтобы ответить.", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Имитация экрана звонка
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF8B5A2B).copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Входящий вызов", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("$contactName", fontSize = 16.sp)
                    Text(contactNumber, fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Кнопка "Отклонить"
                        Button(
                            onClick = {
                                score += 5
                                step = 3
                                callActive = false
                                callEnded = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color( 0xFF9B0C3F))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Отклонить")
                        }
                        // Кнопка "Ответить"
                        Button(
                            onClick = {
                                callActive = true
                                score += 15
                                step = 3
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E8058))
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ответить")
                        }
                    }
                }
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 3: Управление звонком
    if (step == 3 && callActive) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎛️ ШАГ 3: Управление звонком", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Вы разговариваете с $contactName. Попробуйте включить громкую связь, затем завершите звонок.", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Разговор с $contactName", fontSize = 16.sp)
                    Text("⏱️ 00:00", fontSize = 20.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Громкая связь
                        Button(
                            onClick = { speakerOn = !speakerOn },
                            colors = ButtonDefaults.buttonColors(containerColor = if (speakerOn) Color(0xFF8B5A2B) else Color.Gray)
                        ) {
                            Icon(if (speakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (speakerOn) "Громкая" else "Громкая")
                        }
                        // Завершить звонок
                        Button(
                            onClick = {
                                callActive = false
                                callEnded = true
                                score += 20
                                step = 4
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color( 0xFF9B0C3F))
                        ) {
                            Icon(Icons.Default.CallEnd, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Завершить")
                        }
                    }
                }
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 4: Если звонок был отклонён
    if (step == 3 && callEnded) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📞 ШАГ 3: Вы отклонили звонок", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Теперь вы знаете, как отклонить вызов. В реальной жизни лучше отвечать или перезванивать позже.", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    step = 4
                    score += 10
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) { Text("Продолжить", color = Color.White) }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ФИНАЛЬНЫЙ ЭКРАН
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFEFE3D3)).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.9f))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎉 Поздравляем! Вы научились пользоваться звонками.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО ЗВОНКАМ:\n" +
                            "• Для быстрого звонка добавьте важные контакты в «Избранное».\n" +
                            "• Громкую связь удобно использовать, когда телефон на столе.\n" +
                            "• Если не хотите отвечать, можно отклонить звонок или нажать кнопку громкости (звонок замолчит).\n" +
                            "• В режиме «Не беспокоить» звонки могут не проходить – проверьте настройки.\n" +
                            "• Если номер не определяется – попросите собеседника перезвонить или отправьте SMS.\n" +
                            "• Для экстренных вызовов (112) можно звонить даже без SIM-карты.\n" +
                            "• История звонков хранится в приложении «Телефон» – можно перезвонить по журналу.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_call", true)
                            onComplete()
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}