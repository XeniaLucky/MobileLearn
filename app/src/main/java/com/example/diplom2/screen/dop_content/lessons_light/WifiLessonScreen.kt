package com.example.diplom2.screen.dop_content.lessons_light

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiLessonScreen(navController: NavController, userId: Long) {
    var step by remember { mutableIntStateOf(0) }
    var password by remember { mutableStateOf("") }
    var selectedNetwork by remember { mutableStateOf<String?>(null) }
    var wifiEnabled by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if (step == 0) {
        // ТЕОРИЯ – подробное руководство по Wi-Fi
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
                    Text("📶 Wi-Fi: как подключаться и решать проблемы", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8B5A2B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Wi-Fi позволяет выходить в интернет без мобильного трафика. Вот полное руководство:\n\n" +
                                "📌 **Как включить Wi-Fi?**\n" +
                                "• Опустите шторку уведомлений → нажмите на значок Wi-Fi (он должен загореться).\n" +
                                "• Или зайдите в Настройки → Сеть и интернет → Wi-Fi → включите переключатель.\n\n" +
                                "📌 **Как найти доступные сети?**\n" +
                                "• После включения Wi-Fi телефон автоматически покажет список сетей поблизости.\n" +
                                "• Нажмите на название сети, чтобы подключиться.\n" +
                                "• Если сеть не появляется, нажмите «Обновить» или «Поиск».\n\n" +
                                "📌 **Как ввести пароль от Wi-Fi?**\n" +
                                "• Выберите сеть → появится окно ввода пароля.\n" +
                                "• Введите пароль (обычно он на обратной стороне роутера или написан в договоре с провайдером).\n" +
                                "• Нажмите «Подключиться».\n" +
                                "• Обязательно включите опцию «Показывать пароль», чтобы убедиться, что вводите правильно.\n\n" +
                                "📌 **Что делать, если пароль забыт?**\n" +
                                "• Посмотрите на обратной стороне роутера (часто написан на наклейке).\n" +
                                "• Спросите у того, кто настраивал Wi-Fi.\n" +
                                "• Зайдите в настройки роутера (адрес: 192.168.0.1 или 192.168.1.1, логин/пароль admin/admin) → раздел Wi-Fi → показать пароль.\n" +
                                "• На телефонах с сохранённым паролем: Настройки → Wi-Fi → нажмите на сеть → «Поделиться» → сгенерируется QR-код (пароль покажется под ним).\n\n" +
                                "📌 **Почему телефон не видит Wi-Fi?**\n" +
                                "• Проверьте, включён ли Wi-Fi на телефоне.\n" +
                                "• Перезагрузите роутер (выключите на 30 секунд, включите).\n" +
                                "• Убедитесь, что роутер включён и горит индикатор Wi-Fi.\n" +
                                "• Подойдите ближе к роутеру (далеко может не ловить).\n\n" +
                                "📌 **Почему не подключается к Wi-Fi?**\n" +
                                "• Неправильный пароль – проверьте раскладку клавиатуры, заглавные буквы.\n" +
                                "• Отключите и включите Wi-Fi на телефоне.\n" +
                                "• Перезагрузите телефон.\n" +
                                "• Забудьте сеть (нажмите на сеть → «Забыть») и подключитесь заново.\n" +
                                "• Проверьте, не включён ли режим «В самолёте».\n\n" +
                                "📌 **Как узнать пароль от Wi-Fi на телефоне, если уже подключён?**\n" +
                                "• Android: Настройки → Wi-Fi → нажмите на сеть → «Поделиться» → пароль показан под QR-кодом (может потребоваться подтверждение).\n" +
                                "• iPhone: нет прямой возможности, только через роутер или Keychain на Mac.\n\n" +
                                "📌 **Что делать, если Wi-Fi работает, но интернета нет?**\n" +
                                "• Перезагрузите роутер.\n" +
                                "• Проверьте, не отключён ли интернет у провайдера.\n" +
                                "• Попробуйте подключить другое устройство (ноутбук) – если у него тоже нет интернета, проблема у провайдера.\n" +
                                "• На телефоне: Настройки → Wi-Fi → нажмите на сеть → «Изменить сеть» → «Расширенные настройки» → измените DHCP на статический и обратно.\n\n" +
                                "🎯 **Задание:** пройдите симуляцию подключения к Wi-Fi.",
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
                Text("Начать подключение", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 1: Включение Wi-Fi
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
                    Text("📶 ШАГ 1: Включите Wi-Fi", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Опустите шторку уведомлений или зайдите в Настройки → Сеть и интернет → Wi-Fi. Включите переключатель.", fontSize = 14.sp)
                    Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Я включил Wi-Fi", fontSize = 14.sp)
                Switch(checked = wifiEnabled, onCheckedChange = { wifiEnabled = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF8B5A2B)))
            }

            if (wifiEnabled) {
                Text("✅ Wi-Fi включён! Теперь видно список сетей.", color = Color.Green)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { score += 10; step = 2 },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Далее", color = Color.White) }
            } else {
                Text("❌ Включите Wi-Fi, чтобы продолжить", color = Color.Red)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 2: Выбор сети Wi-Fi
    if (step == 2) {
        val networks = listOf("MyHome_WiFi", "Guest_WiFi", "Cafe_Free", "Neighbor_Network")
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
                    Text("📡 ШАГ 2: Выберите свою сеть Wi-Fi", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Нажмите на сеть «MyHome_WiFi» (остальные – чужие, к ним не подключайтесь).", fontSize = 14.sp)
                    Icon(Icons.Default.NetworkWifi, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(networks) { network ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (network == "MyHome_WiFi") {
                                    selectedNetwork = network
                                    score += 15
                                    step = 3
                                    errorMessage = null
                                } else {
                                    errorMessage = "❌ Это чужая сеть! Выберите свою – MyHome_WiFi."
                                    score = (score - 5).coerceAtLeast(0)
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedNetwork == network) Color(0xFFC8E6C9) else Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = Color(0xFF8B5A2B))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(network, fontSize = 16.sp)
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp)
            }
            Text("Очки: $score", fontSize = 14.sp, color = Color(0xFF8B5A2B))
            Spacer(modifier = Modifier.height(16.dp))
        }
        return
    }

    // ШАГ 3: Ввод пароля
    if (step == 3) {
        var showPassword by remember { mutableStateOf(false) }

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
                    Text("🔑 ШАГ 3: Введите пароль от Wi-Fi", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Пароль от сети «MyHome_WiFi»: 12345678 (на самом деле обычно на обратной стороне роутера).", fontSize = 14.sp)
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF8B5A2B))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Пароль") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (showPassword) PasswordVisualTransformation() else PasswordVisualTransformation(),
                isError = errorMessage != null
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Показать пароль", fontSize = 14.sp)
                Switch(checked = showPassword, onCheckedChange = { showPassword = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF8B5A2B)))
            }

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 12.sp)
            }

            Button(
                onClick = {
                    if (password == "12345678") {
                        score += 25
                        step = 4
                    } else {
                        errorMessage = "❌ Неверный пароль. Попробуйте ещё раз."
                        score = (score - 5).coerceAtLeast(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
            ) { Text("Подключиться", color = Color.White) }

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
                Text("🎉 Поздравляем! Вы подключились к Wi-Fi.", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Вы заработали $score очков.", color = Color(0xFF8B5A2B), fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "💡 ИТОГОВЫЕ СОВЕТЫ ПО WI-FI:\n" +
                            "• В общественных местах (кафе, аэропорты) будьте осторожны – не вводите пароли от важных сайтов в открытых сетях.\n" +
                            "• Для безопасности включите VPN, если пользуетесь публичным Wi-Fi.\n" +
                            "• Не подключайтесь к сетям с подозрительными названиями (например, «Free_WiFi» без пароля).\n" +
                            "• Если интернет тормозит, перезагрузите роутер или смените канал в настройках роутера.\n" +
                            "• Пароль от Wi-Fi лучше записать в заметки (защищённые) или сохранить в менеджере паролей.\n" +
                            "• Для гостей можно создать отдельную гостевую сеть в настройках роутера.\n" +
                            "• Если забыли пароль, посмотрите его на обратной стороне роутера или войдите в его настройки через браузер (192.168.0.1).",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            saveLessonProgress(context, userId, "game_wifi", true)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5A2B))
                ) { Text("Завершить", color = Color.White) }
            }
        }
    }
}