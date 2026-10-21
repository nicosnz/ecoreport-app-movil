package com.example.ecoreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.model.EstadoReporte
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FiltroReportes(
    val estado: EstadoReporte? = null,
    val categoria: String? = null
)

class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {

    private val _filtro = MutableStateFlow(FiltroReportes())

    val filtro: StateFlow<FiltroReportes> = _filtro.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val reportes:
    StateFlow<List<ReporteEntity>> =
        _filtro
            .flatMapLatest {
                filtro ->
                repository.obtenerFiltrados(
                    estado = filtro.estado?.name,
                    categoria = filtro.categoria
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted
                    .WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)
        }
    }

    fun aplicarFiltros(estado: EstadoReporte?, categoria: String) {
        _filtro.value = FiltroReportes(
            estado = estado,
            categoria = categoria.trim().ifBlank { null }
        )
    }
}
