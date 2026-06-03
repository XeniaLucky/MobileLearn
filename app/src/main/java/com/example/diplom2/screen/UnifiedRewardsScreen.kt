package com.example.diplom2.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import kotlinx.coroutines.launch

@Composable
fun UnifiedRewardsScreen(accentColor: Color, userId: Long) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var achievementsWithStatus by remember { mutableStateOf<List<Pair<Achievement, Boolean>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        val all = AchievementManager.getAllAchievementsWithStatus(context, userId)
        achievementsWithStatus = all
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val unlockedCount = achievementsWithStatus.count { it.second }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Достижения",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            "Разблокировано $unlockedCount из ${achievementsWithStatus.size}",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(achievementsWithStatus) { (achievement, isUnlocked) ->
                Card(
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) Color(0xFFFFD700) else Color(0xFF1A1A2E).copy(alpha = 0.8f)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(achievement.icon, fontSize = 48.sp)
                        Text(
                            achievement.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) Color.Black else Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            achievement.description,
                            fontSize = 12.sp,
                            color = if (isUnlocked) Color.DarkGray else Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                        if (!isUnlocked) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🔒 Не разблокировано", fontSize = 10.sp, color = Color(0xFF9B0C3F))
                        }
                    }
                }
            }
        }
    }
}