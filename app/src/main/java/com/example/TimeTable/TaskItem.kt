package com.example.TimeTable

import java.time.LocalTime
import java.util.*

class TaskItem(
    var Subject: String,
    var Teacher: String,
    var Room: String,
    var Day:String,
    var tTime: LocalTime?,
    var fTime: LocalTime?,
    var id: UUID = UUID.randomUUID()
)
