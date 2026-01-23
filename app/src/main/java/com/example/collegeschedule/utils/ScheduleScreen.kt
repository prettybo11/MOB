package com.example.collegeschedule.ui.schedule
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.utils.getWeekDateRange
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeshedule.data.dto.GroupDto
import com.example.collegeshedule.ui.components.GroupDropdown
@Composable
fun ScheduleScreen() {
    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        try {
            groups = RetrofitInstance.api.getGroups()
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }
    LaunchedEffect(selectedGroup) {
        selectedGroup?.let { group ->
            val (start, end) = getWeekDateRange()
            try {
                schedule = RetrofitInstance.api.getSchedule(group, start, end)
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        when {
            loading -> CircularProgressIndicator()
            error != null -> Text("Ошибка: $error")
            else -> {
                GroupDropdown(
                    groups = groups,
                    selectedGroup = selectedGroup,
                    onGroupSelected = { selectedGroup = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedGroup != null) {
                    ScheduleList(schedule)
                }
            }
        }
    }
}