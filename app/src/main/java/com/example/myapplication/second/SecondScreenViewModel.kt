package com.example.myapplication.second

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SecondScreenViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: String = checkNotNull(savedStateHandle.get<String>("id"))

    private val _state = MutableStateFlow(SecondScreenState())
    val state = _state.asStateFlow()

    private fun setState(reducer: SecondScreenState.() -> SecondScreenState) =
        _state.getAndUpdate(reducer)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            setState { copy(loading = true, error = null) } // Resetuj error i postavi loading

            try {
                val data = withContext(Dispatchers.IO) {
                    repository.findById(id)
                }
                setState { copy(cat = data, loading = false) }
            } catch (e: IOException) {
                setState { copy(error = "Ne može se učitati", loading = false) }
            } catch (e: Exception) {
                setState { copy(error = "Greška: ${e.message}", loading = false) }
            }
        }
    }

    // Funkcija za retry
    fun retry() {
        loadData()
    }
}