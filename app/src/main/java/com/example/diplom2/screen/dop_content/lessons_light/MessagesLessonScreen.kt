package com.example.diplom2.screen.dop_content.lessons_light

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.rotate
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
fun MessagesLessonScreen(navController: NavController, userId: Long) {
    var step by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var contactName by remember { mutableStateOf("") }
    var selectedContact by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по сообщениям
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
                    Text("💬 Сообщения: как отправлять, читать и управлять", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Сообщения – один из главных способов общения. Вот полное руководство:\n\n" +
                                "📌 **Какие бывают приложения для сообщений?**\n" +
                                "• SMS/MMS – стандартные текстовые сообщения (зелёный значок).\n" +
                                "• WhatsApp – самый популярный мессенджер (зелёная трубка в кружке).\n" +
                                "• Telegram – чаты, каналы, боты (синий бумажный самолётик).\n" +
                                "• Viber – фиолетовый значок.\n" +
                                "• Социальные сети (VK, Одноклассники, Facebook) – внутри них тоже есть сообщения.\n\n" +
                                "📌 **Как отправить SMS?**\n" +
                                "1. Откройте приложение «Сообщения» (обычно зелёный значок).\n" +
                                "2. Нажмите на значок «+» или «Новое сообщение».\n" +
                                "3. Введите номер телефона или выберите контакт из списка.\n" +
                                "4. Напишите текст.\n" +
                                "5. Нажмите на значок отправки (бумажный самолётик ➤ или стрелка).\n\n" +
                                "📌 **Как отправить сообщение через мессенджер (WhatsApp, Telegram)?**\n" +
                                "• Откройте мессенджер → нажмите на значок «+» или «Новый чат».\n" +
                                "• Выберите контакт (или введите номер телефона).\n" +
                                "• Напишите сообщение → нажмите «Отправить».\n" +
                                "• Можно также отправить фото, видео, голосовые сообщения, стикеры.\n\n" +
                                "📌 **Как прикрепить фото или файл?**\n" +
                                "• Нажмите на значок скрепки 📎 или значок фото 📷.\n" +
                                "• Выберите: камера (снять сейчас), галерея (выбрать из фото), документ, местоположение.\n" +
                                "• Подтвердите выбор → отправьте.\n\n" +
                                "📌 **Как удалить сообщение?**\n" +
                                "• Зажмите сообщение → выберите «Удалить» → подтвердите.\n" +
                                "• В WhatsApp и Telegram можно удалить «Для всех» (если не прошло много времени).\n\n" +
                                "📌 **Как прочитать сообщение, если его не видно?**\n" +
                                "• Откройте приложение «Сообщения» или мессенджер.\n" +
                                "• Проверьте вкладки: «Чаты», «Диалоги», «Все сообщения».\n" +
                                "• Используйте поиск (лупа 🔍) – введите часть текста или имя контакта.\n" +
                                "• Проверьте папку «Спам» или «Заблокированные» (для мессенджеров).\n\n" +
                                "📌 **Что делать, если сообщение не отправляется?**\n" +
                                "• Проверьте интернет (для мессенджеров) или сигнал сети (для SMS).\n" +
                                "• Убедитесь, что номер телефона правильный.\n" +
                                "• Перезагрузите телефон.\n" +
                                "• Проверьте, не заблокирован ли контакт.\n" +
                                "• Для SMS: проверьте баланс на счёте.\n\n" +
                                "📌 **Как сохранить важное сообщение?**\n" +
                                "• Сделайте скриншот (кнопка питания + громкость вниз).\n" +
                                "• Скопируйте текст (зажмите сообщение → «Копировать» → вставьте в заметки).\n" +
                                "• В некоторых мессенджерах есть функция «Избранное» или «Закрепить».\n\n" +
                                "🎯 **Задание:** пройдите симуляцию отправки сообщения.",
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
                Text("Начать отправку сообщения", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Выбор контакта
    if (step == 1) {
        val contacts = listOf("Мама", "Папа", "Анна", "Друг")
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
                    Text("📱 ШАГ 1: Выберите контакт", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Кому вы хотите написать? Нажмите на контакт.", fontSize = 14.sp)
                    Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(contacts.size) { index ->
                    val contact = contacts[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedContact = contact
                                score += 10
                                step = 2
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedContact == contact) Color(0xFFC8E6C9) else Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF8B5A2B))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(contact, fontSize = 16.sp)
                        }
                    }
                }
            }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Написание сообщения
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
                    Text("✏️ ШАГ 2: Напишите сообщение для $selectedContact", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Введите текст: «Привет, как дела?» или любое другое приветствие.", fontSize = 14.sp)
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Сообщение") },
                leadingIcon = { Icon(Icons.Default.Message, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (message.isNotBlank()) {
                        score += 20
                        step = 3
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) { Text("Далее", color = Color.White) }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 3: Отправка сообщения
    if (step == 3) {
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
                    Text("📤 ШАГ 3: Отправьте сообщение", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Проверьте текст и нажмите «Отправить».", fontSize = 14.sp)
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Превью сообщения
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC4D7DB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Кому: $selectedContact", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Сообщение: \"$message\"", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    score += 15
                    step = 4
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) { Text("Отправить", color = Color.White) }

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
                Text("🎉 Поздравляем! Вы научились отправлять сообщения.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО СООБЩЕНИЯМ:\n" +
                            "• Для важных сообщений используйте мессенджеры с доставкой (две галочки).\n" +
                            "• Голосовые сообщения удобны, когда не хочется печатать.\n" +
                            "• Не открывайте ссылки из незнакомых номеров – это может быть фишинг.\n" +
                            "• Чаты можно закреплять вверху, чтобы не потерять.\n" +
                            "• Удаляйте старые чаты, чтобы не засорять память.\n" +
                            "• В Telegram и WhatsApp можно создать группу для семьи или коллег.\n" +
                            "• Скопировать сообщение: зажмите → «Копировать» → вставьте в нужное место.\n" +
                            "• Чтобы отправить одно сообщение нескольким людям, используйте функцию «рассылка» (обычно через меню).",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_messages", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}