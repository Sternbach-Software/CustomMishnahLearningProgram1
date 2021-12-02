package shmuly.sternbach.custommishnahlearningprogram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import shmuly.sternbach.custommishnahlearningprogram.adapters.UnitAdapter
import java.time.LocalDate

class ActivityTodayList: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = RecyclerView(this)
        setContentView(recyclerView)
        val today = LocalDate.now()
        recyclerView.adapter = UnitAdapter(program.filter{it.key.isEqual(today)})
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}