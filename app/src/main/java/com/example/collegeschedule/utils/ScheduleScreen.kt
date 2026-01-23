package com.example.collegeschedule.ui.schedule
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.example.collegeschedule.data.preferences.FavoritesManager
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
import com.example.collegeschedule.ui.components.GroupDropdown
@Composable
fun ScheduleScreen(preselectedGroup: String? = null) {
    val context = LocalContext.current
    val favoritesManager = remember { FavoritesManager(context) }
    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf(preselectedGroup) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        try {
            groups = RetrofitInstance.api.getGroups()
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }
    LaunchedEffect(preselectedGroup) {
        preselectedGroup?.let {
            selectedGroup = it
        }
    }
    LaunchedEffect(selectedGroup) {
        selectedGroup?.let { group ->
            isFavorite = favoritesManager.isFavorite(group)
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
                    onGroupSelected = { selectedGroup = it },
                    isFavorite = isFavorite,
                    onToggleFavorite = {
                        selectedGroup?.let { group ->
                            if (isFavorite) {
                                favoritesManager.removeFavorite(group)
                            } else {
                                favoritesManager.addFavorite(group)
                            }
                            isFavorite = !isFavorite
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedGroup != null) {
                    ScheduleList(schedule)
                }
            }
        }
    }
}