package com.example.userdirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import com.example.userdirectory.ui.UserScreen
import com.example.userdirectory.ui.UserViewModel

class MainActivity : ComponentActivity() {

    private val vm: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    val state = vm.ui.collectAsState().value
                    UserScreen(
                        state = state,
                        onQueryChange = vm::onQueryChange,
                        onRefresh = vm::refresh
                    )
                }
            }
        }
    }
}
