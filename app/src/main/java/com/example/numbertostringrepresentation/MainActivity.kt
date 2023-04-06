package com.example.numbertostringrepresentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.numbertostringrepresentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityBinding.root)
    }
}
