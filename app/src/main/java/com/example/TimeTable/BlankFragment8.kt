package com.example.TimeTable

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.swipetabexample.databinding.FragmentNewTaskSheetBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalTime


class BlankFragment8(var taskItem: TaskItem?) : DialogFragment() {
    private val channelId = "1"
    private val channelName = "Channel 1"
    private val notificationId = 1



    private var dueTime: LocalTime? = null
    private var dueTime1: LocalTime? = null


    private lateinit var binding: FragmentNewTaskSheetBinding
        private lateinit var taskViewModel: TaskViewModel

        @SuppressLint("MissingPermission")
        @RequiresApi(Build.VERSION_CODES.O)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val activity = requireActivity()

            taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
            binding.save.setOnClickListener {
                saveAction()

            }
            if (taskItem != null) {


                val editable = Editable.Factory.getInstance()
                binding.subjectDialog.text = editable.newEditable(taskItem!!.Subject)
                binding.teacherDialog.text = editable.newEditable(taskItem!!.Teacher)
                binding.roomDialog.text = editable.newEditable(taskItem!!.Room)
                binding.dayDialog.text = editable.newEditable(taskItem!!.Day)
                if (taskItem!!.fTime != null || taskItem!!.tTime != null) {
                    dueTime = taskItem!!.fTime!!
                    dueTime1 = taskItem!!.tTime!!
                    updateTimeButtonText()
                    updateTimeButtonText1()
                }
            }





            taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
            val intent = Intent(requireActivity(), AlarmManagerBroadcast::class.java)
            val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 234, intent, PendingIntent.FLAG_IMMUTABLE)

            binding.save.setOnClickListener {
                saveAction()
                Toast.makeText(activity, "Schedule Updated!!",Toast.LENGTH_LONG).show();

                val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,0,0,pendingIntent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        lightColor = Color.RED
                        enableLights(true)
                    }
                    val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel)
                }

                val notificationBuilder = NotificationCompat.Builder(activity, channelId)
                    .setContentTitle("A Class is Scheduled")
                    .setContentText("You Scheduled a class")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()
                val notificationManager = NotificationManagerCompat.from(activity)
               notificationManager.notify(1,notificationBuilder)

            }
            binding.timePickerButton1.setOnClickListener {
                openTimePicker()
            }
            binding.timePickerButton2.setOnClickListener {
                openTimePicker1()
            }

        }





        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
            return binding.root


        }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openTimePicker() {
        if(dueTime == null)
            dueTime = LocalTime.now()
        if(dueTime1 == null)
            dueTime1 = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            dueTime1 = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime1!!.minute, true)
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
    private fun updateTimeButtonText1() {
        binding.timePickerButton2.text = String.format("%02d:%02d",dueTime1!!.hour,dueTime1!!.minute)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeButtonText() {
        binding.timePickerButton1.text = String.format("%02d:%02d",dueTime!!.hour,dueTime1!!.minute)
    }


    private fun addData(mod :TaskItem) {
        val fout = activity?.getSharedPreferences("file", MODE_PRIVATE)
        val gson1 = Gson()
        val json1 = fout?.getString("myArrayListKey", null)
        if(json1==null){
            lateinit var arrList_User:ArrayList<TaskItem>
            arrList_User.add(mod)
            val updatedJson = gson1.toJson(arrList_User)
            fout?.edit()?.putString("myArrayListKey", updatedJson)?.apply()
        }
        else {
            val type = object : TypeToken<ArrayList<TaskItem>>() {}.type
            val myArrayList: ArrayList<TaskItem> = gson1.fromJson(json1, type)
            myArrayList.add(mod)
            val updatedJson = gson1.toJson(myArrayList)
            fout.edit().putString("myArrayListKey", updatedJson).apply()
        }
    }
    private fun fetchData() {
        val output = activity?.getSharedPreferences("file", MODE_PRIVATE)
        val gsono = Gson()
        val jsono = output?.getString("myArrayListKey", null)
        if(jsono==null){
            return
        }
        else {
            val type = object : TypeToken<ArrayList<TaskItem>>() {}.type
            val myArrayList: ArrayList<TaskItem> = gsono.fromJson(jsono, type)
//            arrList_User = gsono.fromJson(jsono, type)
        }
    }
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
            binding.dayDialog.setText("")
            binding.timePickerButton1.setText("")
            binding.timePickerButton2.setText("")
            dismiss()
        }

    }
class AlarmManagerBroadcast : BroadcastReceiver() {
    //
    override fun onReceive(context: Context, intent: Intent) {
        val mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI).start()
//        Log.d("Hello", "repeating alarm")
//        mp.start()
        Toast.makeText(context, "Alarm Ringing", Toast.LENGTH_LONG).show()
    }
}

