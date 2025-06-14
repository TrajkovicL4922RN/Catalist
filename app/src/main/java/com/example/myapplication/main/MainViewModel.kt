package com.example.myapplication.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    //hotflow koji ce uvek vracati poslednje stanje, jer nas samo to zanima
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()
    private fun setState(reducer: MainScreenState.()-> MainScreenState) = _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<MainScreenEvent>()
    fun setEvent(event: MainScreenEvent) = viewModelScope.launch { events.emit(event) }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is MainScreenEvent.FilterList -> {
                        handleFilterList(query = it.query)
                    }
                }
            }
        }
    }
    private fun handleFilterList(query: String){
        viewModelScope.launch {
            setState { copy(loading = true, query = query) }
            try {
                val data = repository.filter(query)
                setState { copy(listCats = data) }
            }catch (e: IOException){
                setState { copy(error = "ne moze se ucitati, rip :(") }
            }finally {
                setState { copy(loading = false) }
            }
        }
    }

    init {
        observeEvents()
        viewModelScope.launch {
            try {
                val data = repository.findAll()
                setState { copy(listCats = data) }
            }catch (e: IOException){
                setState { copy(error = "ne moze se ucitati, rip :(") }
            }finally {
                setState { copy(loading = false) }
            }

        }
    }
}