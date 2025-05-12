package com.example.mypharmacy.viewmodel.state


import com.example.mypharmacy.model.domain.Log
import java.time.LocalDateTime

data class LogState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val logs: List<Log> = emptyList(),
    val selectedLog: Log? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val searchQuery: String? = null
)