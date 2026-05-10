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
fun AppsLessonScreen(navController: NavController, userId: Long) {
    var step by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedApp by remember { mutableStateOf<String?>(null) }
    var installConfirmed by remember { mutableStateOf(false) }
    var appOpened by remember { mutableStateOf(false) }
    var appDeleted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по приложениям
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
                    Text("📱 Приложения: установка, открытие, удаление", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Приложения – это программы, которые расширяют возможности телефона. Вот полное руководство:\n\n" +
                                "📌 **Где найти приложения?**\n" +
                                "• Google Play Маркет – значок 🎒 на главном экране или в меню.\n" +
                                "• Galaxy Store (для Samsung), AppGallery (для Huawei).\n" +
                                "• Встроенные приложения уже установлены (камера, контакты, телефон).\n\n" +
                                "🔍 **Как найти нужное приложение?**\n" +
                                "1. Откройте Google Play Маркет.\n" +
                                "2. Нажмите на строку поиска (вверху).\n" +
                                "3. Введите название (например, «Учитель» или «Калькулятор»).\n" +
                                "4. Нажмите на значок лупы 🔍 на клавиатуре.\n" +
                                "5. Выберите приложение из списка.\n\n" +
                                "⬇️ **Как установить приложение?**\n" +
                                "1. На странице приложения нажмите кнопку «Установить».\n" +
                                "2. Подтвердите разрешения (если приложение их запрашивает).\n" +
                                "3. Дождитесь окончания загрузки и установки.\n" +
                                "4. Нажмите «Открыть» или найдите иконку приложения на главном экране.\n" +
                                "5️⃣ **Совет:** устанавливайте приложения только из официального магазина!\n\n" +
                                "📂 **Как найти установленное приложение?**\n" +
                                "• На главном экране – листайте влево/вправо.\n" +
                                "• В меню всех приложений – проведите вверх по главному экрану.\n" +
                                "• Используйте поиск: проведите вниз по экрану и введите название.\n\n" +
                                "🚀 **Как открыть приложение?**\n" +
                                "• Просто нажмите на его иконку один раз (короткое касание).\n" +
                                "• Приложение запустится. Чтобы закрыть – нажмите «Домой» или свайпните вверх снизу.\n\n" +
                                "🗑️ **Как удалить приложение?**\n" +
                                "• Зажмите иконку приложения (долгое нажатие, пока не появится меню).\n" +
                                "• Нажмите «Удалить» или значок корзины 🗑️.\n" +
                                "• Подтвердите удаление.\n" +
                                "• Системные приложения (камера, настройки) удалить нельзя.\n\n" +
                                "⚠️ **Что делать, если приложение не открывается или зависает?**\n" +
                                "• Перезагрузите телефон.\n" +
                                "• Очистите кэш приложения (Настройки → Приложения → выбрать приложение → Очистить кэш).\n" +
                                "• Удалите и установите заново.\n" +
                                "• Проверьте обновления (Play Маркет → Мои приложения).\n\n" +
                                "🎯 **Задание:** пройдите симуляцию установки, открытия и удаления приложения.",
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
                Text("Начать обучение", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Поиск приложения в Play Маркет
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
                    Text("🔍 ШАГ 1: Найдите приложение «Учитель» в Play Маркет", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Откройте Google Play Маркет (значок 🎒). Нажмите на строку поиска и введите «Учитель».", fontSize = 14.sp)
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Введите «Учитель» для поиска") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            if (searchQuery.equals("Учитель", ignoreCase = true)) {
                Text("✅ Отлично! Приложение найдено.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 15; step = 2 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Введите слово «Учитель» в поле поиска", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Установка приложения
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
                    Text("⬇️ ШАГ 2: Установите приложение", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("На странице приложения нажмите кнопку «Установить». Подтвердите разрешения (если спросит).", fontSize = 14.sp)
                    Icon(Icons.Default.GetApp, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я нажал «Установить» и подтвердил разрешения", fontSize = 14.sp)
                Switch(checked = installConfirmed, onCheckedChange = { installConfirmed = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF8B5A2B)))
            }

            if (installConfirmed) {
                Text("✅ Приложение установлено! Теперь его можно открыть.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 20; step = 3 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Подтвердите, что вы установили приложение", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 3: Открытие приложения
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
                    Text("🚀 ШАГ 3: Откройте приложение", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Найдите иконку приложения «Учитель» на главном экране или в меню. Нажмите на неё один раз.", fontSize = 14.sp)
                    Icon(Icons.Default.Apps, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я открыл приложение «Учитель»", fontSize = 14.sp)
                Switch(checked = appOpened, onCheckedChange = { appOpened = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF8B5A2B)))
            }

            if (appOpened) {
                Text("✅ Приложение успешно открыто!", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 15; step = 4 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Откройте приложение, чтобы продолжить", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 4: Удаление приложения
    if (step == 4) {
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
                    Text("🗑️ ШАГ 4: Удалите приложение", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Зажмите иконку приложения (долгое нажатие, пока не появится меню). Нажмите «Удалить» и подтвердите.", fontSize = 14.sp)
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я удалил приложение «Учитель»", fontSize = 14.sp)
                Switch(checked = appDeleted, onCheckedChange = { appDeleted = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF8B5A2B)))
            }

            if (appDeleted) {
                Text("✅ Приложение удалено! Теперь экран стал чище.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 25; step = 5 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            } else {
                Text("❌ Удалите приложение, чтобы продолжить", color = Color.Red)
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
                Text("🎉 Поздравляем! Вы научились управлять приложениями.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО ПРИЛОЖЕНИЯМ:\n" +
                            "• Устанавливайте приложения только из официального магазина.\n" +
                            "• Если приложение зависает – очистите кэш или переустановите.\n" +
                            "• Удаляйте неиспользуемые приложения – они занимают память.\n" +
                            "• Следите за обновлениями (Play Маркет → Мои приложения → Обновить всё).\n" +
                            "• Для игр и тяжёлых приложений нужна свободная память.\n" +
                            "• Иконка приложения на главном экране означает, что оно готово к использованию.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_apps", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}