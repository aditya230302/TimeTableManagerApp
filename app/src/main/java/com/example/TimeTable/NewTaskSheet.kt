package com.example.TimeTable

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.swipetabexample.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalTime
import java.util.*

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null
    private var dueTime1: LocalTime? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()


        if (taskItem != null)
        {
            val editable = Editable.Factory.getInstance()
            binding.subjectDialog.text= editable.newEditable(taskItem!!.Subject)
            binding.teacherDialog.text = editable.newEditable(taskItem!!.Teacher)
            binding.roomDialog.text= editable.newEditable(taskItem!!.Room)
            binding.dayDialog.text=editable.newEditable(taskItem!!.Day)
            if(taskItem!!.fTime != null||taskItem!!.tTime!=null){
                dueTime = taskItem!!.fTime!!
                dueTime1 = taskItem!!.tTime!!
                updateTimeButtonText()
                updateTimeButtonText1()
            }

        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.save.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton1.setOnClickListener {
            openTimePicker()
        }
        binding.timePickerButton2.setOnClickListener {
            openTimePicker1()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openTimePicker() {
        if(dueTime == null)
            dueTime = LocalTime.now()

        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openTimePicker1() {
        if(dueTime1 == null)
            dueTime1 = LocalTime.now()

        val listener1 = TimePickerDialog.OnTimeSetListener{ _, selectedHour1, selectedMinute1 ->
            dueTime1 = LocalTime.of(selectedHour1, selectedMinute1)
            updateTimeButtonText1()
        }
        val dialog1 = TimePickerDialog(activity, listener1, dueTime1!!.hour, dueTime1!!.minute, true)
        dialog1.setTitle("Task Due")
        dialog1.show()


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeButtonText() {
        binding.timePickerButton1.text = String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeButtonText1() {
        binding.timePickerButton2.text = String.format("%02d:%02d",dueTime1!!.hour,dueTime1!!.minute)
    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveAction()
    {
        val subject = binding.subjectDialog.text.toString()
        val teacher = binding.teacherDialog.text.toString()
        val room = binding.roomDialog.text.toString()
        val day = binding.dayDialog.text.toString()
        if(taskItem == null)
        {
            val newTask = TaskItem(subject, teacher,room,day,dueTime1,dueTime)
            taskViewModel.addTaskItem(newTask)
        }
        else
        {
            taskViewModel.updateTaskItem(taskItem!!.id,subject, teacher,room,day,dueTime1,dueTime)
        }
        binding.subjectDialog.setText("")
        binding.teacherDialog.setText("")
        binding.roomDialog.setText("")
        binding.timePickerButton1.setText("")
        binding.timePickerButton2.setText("")
        dismiss()
    }


}








