package shmuly.sternbach.custommishnahlearningprogram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.l4digital.fastscroll.FastScrollView
import shmuly.sternbach.custommishnahlearningprogram.R
import shmuly.sternbach.custommishnahlearningprogram.adapters.UserAdapter

class ActivityTimeline: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mfast_scroller)
        val recyclerView = findViewById<FastScrollView>(R.id.mfast_scroller)
        recyclerView.setAdapter(UserAdapter(program))
        recyclerView.setLayoutManager(LinearLayoutManager(this))
    }
}
/*package shmuly.sternbach.custommishnahlearningprogram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityTimeline: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fast_scroller)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = UserAdapter(program)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}*/