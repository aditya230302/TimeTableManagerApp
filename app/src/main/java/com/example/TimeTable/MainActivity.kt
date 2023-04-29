package com.example.TimeTable

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipetabexample.R
import com.example.swipetabexample.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent


class MainActivity : AppCompatActivity() {
    lateinit var subject:TextView
    lateinit var teacher:TextView
    lateinit var room:TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        setRecyclerView()


        var add = findViewById<FloatingActionButton>(R.id.fab)


        var layoutToAdd = findViewById<View>(R.id.activitymain) as LinearLayout


        add.setOnClickListener {

            BlankFragment8(null).show(supportFragmentManager, "dialog")
            Toast.makeText(this,"Enter information about Schedule",Toast.LENGTH_LONG).show()


        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    setContent("Home")
                    true
                }
                R.id.profile ->{
                    setContent("Profile")
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)

                    Toast.makeText(this, "Profile accessed", Toast.LENGTH_LONG).show()

                    true
                }
                else -> false
            }
        }


    }

    private fun setContent(content: String){
        title = content
    }

    private fun setRecyclerView()
    {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }
    fun editTaskItem(taskItem: TaskItem)
    {
        NewTaskSheet(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun completeTaskItem(taskItem: TaskItem)
    {
        taskViewModel.setCompleted(taskItem)
    }
}