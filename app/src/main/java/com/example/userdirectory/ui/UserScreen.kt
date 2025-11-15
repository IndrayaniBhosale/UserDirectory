package com.example.userdirectory.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    state: UiState,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Directory") },
                actions = {
                    TextButton(onClick = onRefresh) { Text("Refresh") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // 🔥 FIXED TEXTFIELD — no more reverse typing
            OutlinedTextField(
                value = state.query,
                onValueChange = { onQueryChange(it) },
                label = { Text("Search by name or email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn {
                items(state.users) { user ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "${user.id}. ${user.name}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(user.email)
                            Text(user.phone)
                        }
                    }
                }
            }
        }
    }
}
