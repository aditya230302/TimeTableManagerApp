package com.example.TimeTable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.swipetabexample.R

import com.example.swipetabexample.databinding.ActivityProfileBinding

import com.google.android.material.bottomnavigation.BottomNavigationView

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    setContent("Home")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"You have accessed the Time-Table Manager",Toast.LENGTH_LONG).show()

                    true
                }
                R.id.profile ->{
                    setContent("Profile")

                    true
                }
                else -> false
            }
        }
    }
    private fun setContent(content: String){
        title = content
    }
}