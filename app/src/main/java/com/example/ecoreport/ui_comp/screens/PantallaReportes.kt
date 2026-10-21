package com.example.ecoreport.ui_comp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.model.EstadoReporte
import com.example.ecoreport.viewmodel.FiltroReportes

@Composable
fun PantallaReportes(
    reportes: List<ReporteEntity>,
    filtro: FiltroReportes,
    aplicarFiltros: (EstadoReporte?, String) -> Unit,
    seleccionarReporte: (ReporteEntity) -> Unit,
    volver: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Reportes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        BarraFiltros(
            filtro = filtro,
            aplicarFiltros = aplicarFiltros
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (reportes.isEmpty()) {

            Text(
                text = if (filtro == FiltroReportes()) {
                    "No existen reportes registrados."
                } else {
                    "No hay reportes que coincidan con los filtros."
                }
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = reportes,
                    key = { reporte ->
                        reporte.id
                    }
                ) { reporte ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                seleccionarReporte(reporte)
                            },

                        shape = RoundedCornerShape(16.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = reporte.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Categoría: ${reporte.categoria}"
                            )

                            Text(
                                text = "Ubicación: ${reporte.ubicacion}"
                            )

                            Text(
                                text = "Estado: ${reporte.estado}"
                            )

                            Text(
                                text = "Fecha: ${reporte.fecha}"
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Presiona para ver el detalle",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "VOLVER"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraFiltros(
    filtro: FiltroReportes,
    aplicarFiltros: (EstadoReporte?, String) -> Unit
) {

    var estado by rememberSaveable(filtro) {
        mutableStateOf(filtro.estado)
    }

    var categoria by rememberSaveable(filtro) {
        mutableStateOf(filtro.categoria.orEmpty())
    }

    var menuAbierto by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ExposedDropdownMenuBox(
            expanded = menuAbierto,
            onExpandedChange = {
                menuAbierto = it
            },
            modifier = Modifier.weight(1f)
        ) {

            OutlinedTextField(
                value = estado?.name ?: "TODOS",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = {
                    Text("Estado")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menuAbierto
                    )
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = {
                    menuAbierto = false
                }
            ) {

                DropdownMenuItem(
                    text = {
                        Text("TODOS")
                    },
                    onClick = {
                        estado = null
                        menuAbierto = false
                    }
                )

                EstadoReporte.entries.forEach { opcion ->

                    DropdownMenuItem(
                        text = {
                            Text(opcion.name)
                        },
                        onClick = {
                            estado = opcion
                            menuAbierto = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = categoria,
            onValueChange = {
                categoria = it
            },
            singleLine = true,
            label = {
                Text("Categoría")
            },
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                aplicarFiltros(estado, categoria)
            }
        ) {

            Text(
                text = "Buscar"
            )
        }
    }
}
