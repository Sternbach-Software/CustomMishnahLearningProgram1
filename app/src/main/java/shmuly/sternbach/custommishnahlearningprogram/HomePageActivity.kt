package shmuly.sternbach.custommishnahlearningprogram

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.util.Log

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar?.title = "Home page"
        findViewById<MaterialButton>(R.id.view_today).setOnClickListener {
            Log.v("","$it clicked")
        }
        findViewById<MaterialButton>(R.id.view_timeline).setOnClickListener {
            Log.v("","$it clicked")
        }
        findViewById<MaterialButton>(R.id.set_program).setOnClickListener {
            Log.v("","$it clicked")
            startActivity(Intent(this, CreateProgramActivity::class.java))
        }
    }
}
