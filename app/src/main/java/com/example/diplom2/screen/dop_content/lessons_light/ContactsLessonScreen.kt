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
fun ContactsLessonScreen(navController: NavController, userId: Long) {
    var step by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedContact by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по контактам
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
                    Text("📇 Контакты: как добавлять, искать и управлять", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Контакты – это записная книжка телефона. Вот полное руководство:\n\n" +
                                "📌 **Где хранятся контакты?**\n" +
                                "• В телефоне: Настройки → Контакты → Управление контактами.\n" +
                                "• В Google аккаунте: contacts.google.com (синхронизируются со всеми устройствами).\n" +
                                "• На SIM-карте (старый способ, но тоже работает).\n\n" +
                                "📌 **Как открыть контакты?**\n" +
                                "• Нажмите на значок «Контакты» на главном экране или в меню.\n" +
                                "• Или откройте «Телефон» → вкладка «Контакты».\n\n" +
                                "📌 **Как добавить контакт?**\n" +
                                "1. Откройте приложение «Контакты».\n" +
                                "2. Нажмите на значок «+» или «Добавить контакт».\n" +
                                "3. Введите имя, номер телефона, email, день рождения и т.д.\n" +
                                "4. Нажмите «Сохранить».\n\n" +
                                "📌 **Как редактировать контакт?**\n" +
                                "• Откройте контакт → нажмите на значок карандаша ✏️.\n" +
                                "• Измените нужные поля → нажмите «Сохранить».\n\n" +
                                "📌 **Как удалить контакт?**\n" +
                                "• Откройте контакт → нажмите на три точки ⋮ → «Удалить» → подтвердите.\n" +
                                "• Или зажмите контакт в списке → выберите «Удалить».\n\n" +
                                "📌 **Как искать контакт?**\n" +
                                "• Нажмите на строку поиска (лупа 🔍) в приложении «Контакты».\n" +
                                "• Введите имя или номер телефона.\n\n" +
                                "📌 **Как позвонить или написать из контактов?**\n" +
                                "• Нажмите на контакт → выберите телефон 📞 (звонок) или сообщение 💬.\n" +
                                "• Можно долго нажать на контакт → выберите действие.\n\n" +
                                "📌 **Как добавить фото к контакту?**\n" +
                                "• Откройте контакт → нажмите на значок фото (круг).\n" +
                                "• Выберите «Сделать фото» или «Выбрать из галереи».\n" +
                                "• Обрежьте фото и нажмите «Сохранить».\n\n" +
                                "📌 **Как синхронизировать контакты с Google?**\n" +
                                "• Настройки → Аккаунты → Google → выберите аккаунт.\n" +
                                "• Включите синхронизацию «Контакты».\n" +
                                "• Тогда при смене телефона контакты восстановятся автоматически.\n\n" +
                                "📌 **Что делать, если контакты пропали?**\n" +
                                "• Проверьте, какой аккаунт выбран в приложении «Контакты» (левый верхний угол).\n" +
                                "• Включите синхронизацию (см. выше).\n" +
                                "• Зайдите в contacts.google.com – там есть копия всех контактов.\n" +
                                "• Можно восстановить из резервной копии (если делали).\n\n" +
                                "🎯 **Задание:** пройдите симуляцию добавления, редактирования и удаления контакта.",
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
                Text("Начать работу с контактами", color = Color.White)
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
                    Text("➕ ШАГ 1: Добавьте новый контакт", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Заполните поля: имя, номер телефона, email. Все поля важны.", fontSize = 14.sp)
                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя контакта *") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Номер телефона * (например, 89001234567)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (необязательно)") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )

            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank() && phone.replace(Regex("[^0-9]"), "").length >= 10) {
                        score += 20
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

    // ШАГ 2: Список контактов – выбор только что добавленного
    if (step == 2) {
        val contacts = listOf("Мама", "Папа", "Анна", name)
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
                    Text("📋 ШАГ 2: Найдите только что добавленный контакт", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Нажмите на контакт «$name», чтобы открыть его.", fontSize = 14.sp)
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
                                if (contact == name) {
                                    selectedContact = contact
                                    score += 15
                                    step = 3
                                }
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

    // ШАГ 3: Редактирование контакта
    if (step == 3) {
        var editedName by remember { mutableStateOf(name) }
        var editedPhone by remember { mutableStateOf(phone) }
        var saveSuccess by remember { mutableStateOf(false) }

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
                    Text("✏️ ШАГ 3: Редактирование контакта", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Измените имя контакта на «${name} (подруга)» или добавьте день рождения.", fontSize = 14.sp)
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = editedName,
                onValueChange = { editedName = it },
                label = { Text("Имя контакта") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            OutlinedTextField(
                value = editedPhone,
                onValueChange = { editedPhone = it },
                label = { Text("Номер телефона") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )

            Button(
                onClick = {
                    name = editedName
                    phone = editedPhone
                    saveSuccess = true
                    score += 15
                    step = 4
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) { Text("Сохранить изменения", color = Color.White) }

            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 4: Удаление контакта
    if (step == 4) {
        var deleteConfirmed by remember { mutableStateOf(false) }

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
                    Text("🗑️ ШАГ 4: Удаление контакта", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Удалите контакт «$name». В реальной жизни нажмите на три точки → «Удалить».", fontSize = 14.sp)
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (!deleteConfirmed) {
                Button(
                    onClick = {
                        deleteConfirmed = true
                        score += 20
                        step = 5
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Удалить контакт", color = Color.White) }
            }

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
                Text("🎉 Поздравляем! Вы научились управлять контактами.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО КОНТАКТАМ:\n" +
                            "• Храните контакты в Google аккаунте, а не на SIM-карте – они не потеряются.\n" +
                            "• Добавляйте фото к контактам – так легче искать.\n" +
                            "• Группируйте контакты (например, «Семья», «Работа», «Друзья»).\n" +
                            "• Регулярно синхронизируйте контакты с облаком.\n" +
                            "• Экспортируйте контакты раз в полгода (.vcf файл) на случай потери телефона.\n" +
                            "• Импорт контактов: если у вас есть файл .vcf, откройте его → нажмите «Импортировать».\n" +
                            "• Не храните пароли и важные данные в заметках к контактам – это небезопасно.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_contacts", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}