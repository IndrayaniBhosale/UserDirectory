package com.example.userdirectory.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.userdirectory.data.UserRepository
import com.example.userdirectory.di.ServiceLocator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val query: String = "",
    val users: List<com.example.userdirectory.data.local.UserEntity> = emptyList()
)

class UserViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: UserRepository = ServiceLocator.provideRepository(app)
    private val query = MutableStateFlow("")

    val ui: StateFlow<UiState> =
        query
            .flatMapLatest { q ->
                if (q.isBlank()) repo.usersFlow()
                else repo.searchFlow(q)
            }
            .map { users ->
                UiState(
                    query = query.value,
                    users = users
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    init {
        refresh()
    }

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun refresh() {
        viewModelScope.launch {
            repo.refreshUsers()
        }
    }
}
