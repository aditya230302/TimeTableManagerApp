package com.example.TimeTable

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.swipetabexample.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: MainActivity
): RecyclerView.ViewHolder(binding.root)
{
    @RequiresApi(Build.VERSION_CODES.O)
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
    fun bindTaskItem(taskItem: TaskItem)
    {
        binding.subject.text = taskItem.Subject
        binding.teacher.text = taskItem.Teacher
        binding.room.text = taskItem.Room

        binding.time.text = taskItem.fTime.toString()
        binding.time1.text = taskItem.tTime.toString()



        binding.taskCellContainer.setOnClickListener{
            clickListener.editTaskItem(taskItem)
        }

    }
}