package com.example.collegeschedule.data.dto
data class ScheduleByDateDto(
    val lessonDate: String, // ISO: 2026-01-12
    val weekday: String,
    val lessons: List<LessonDto>
)