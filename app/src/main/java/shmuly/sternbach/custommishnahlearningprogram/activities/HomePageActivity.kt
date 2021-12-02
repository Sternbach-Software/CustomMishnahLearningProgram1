package shmuly.sternbach.custommishnahlearningprogram.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.widget.Toast
import shmuly.sternbach.custommishnahlearningprogram.R

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar?.title = "Home page"
        findViewById<MaterialButton>(R.id.view_today).setOnClickListener {
            if(!programInitialized) Toast.makeText(this, "Please set program first.", Toast.LENGTH_SHORT).show()
            else startActivity(Intent(this, ActivityTodayList::class.java))
        }
        findViewById<MaterialButton>(R.id.view_timeline).setOnClickListener {
            if(!programInitialized) Toast.makeText(this, "Please set program first.", Toast.LENGTH_SHORT).show()
            else startActivity(Intent(this, ActivityTimeline::class.java))
        }
        findViewById<MaterialButton>(R.id.set_program).setOnClickListener {
            startActivity(Intent(this, CreateProgramActivity::class.java))
        }
    }
}
