package com.example.collegeschedule.data.dto
data class LessonDto(
    val lessonNumber: Int,
    val time: String,
    val subject: String,
    val teacher: String,
    val teacherPosition: String,
    val classroom: String,
    val building: String,
    val address: String,
    val groupParts: Map<LessonGroupPart, LessonPartDto?>
)