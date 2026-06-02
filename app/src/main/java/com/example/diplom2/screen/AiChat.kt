package com.example.diplom2.screen

import com.example.diplom2.AI.ChatMessage
import com.example.diplom2.AI.ChatViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    navController: NavController,
    accentColor: Color
) {

    val viewModel: ChatViewModel = viewModel()

    var messageText by remember {
        mutableStateOf("")
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("AI Помощник")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = accentColor,
                    titleContentColor = Color.White
                )
            )
        },

        containerColor = Color(0xFFC4D7DB)

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),

                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(viewModel.messages) { message ->

                    MessageBubble(message)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = messageText,

                    onValueChange = {
                        messageText = it
                    },

                    modifier = Modifier.weight(1f),

                    placeholder = {
                        Text("Введите вопрос...")
                    },

                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {

                        if (messageText.isNotBlank()) {

                            viewModel.sendMessage(messageText)

                            messageText = ""
                        }
                    },

                    containerColor = accentColor
                ) {

                    Icon(
                        Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {

    val isUser = message.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement =
        if (isUser)
            Arrangement.End
        else
            Arrangement.Start
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),

            colors = CardDefaults.cardColors(
                containerColor =
                if (isUser)
                    Color(0xFF8B5A2B)
                else
                    Color.White
            )
        ) {

            Text(
                text = message.content,

                modifier = Modifier.padding(12.dp),

                color =
                if (isUser)
                    Color.White
                else
                    Color.Black
            )
        }
    }
}