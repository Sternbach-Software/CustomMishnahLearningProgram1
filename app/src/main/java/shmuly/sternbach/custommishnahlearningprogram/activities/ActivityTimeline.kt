package shmuly.sternbach.custommishnahlearningprogram.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.roomwordssample.ProgramUnitsViewModel
import com.example.android.roomwordssample.WordViewModelFactory
import com.l4digital.fastscroll.FastScrollView
import shmuly.sternbach.custommishnahlearningprogram.R
import shmuly.sternbach.custommishnahlearningprogram.ReviewApplication
import shmuly.sternbach.custommishnahlearningprogram.adapters.UnitAdapter

class ActivityTimeline: AppCompatActivity() {

    private val programUnitsViewModel: ProgramUnitsViewModel by viewModels {
        WordViewModelFactory((application as ReviewApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mfast_scroller)
        val recyclerView = findViewById<FastScrollView>(R.id.mfast_scroller)
        val unitAdapter = UnitAdapter(programUnitsViewModel)
        recyclerView.setAdapter(unitAdapter)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        programUnitsViewModel.allTimelines.observe(this) { units ->
            // Update the cached copy of the units in the adapter.
            units.let {
                unitAdapter.submitList(it)
            }
        }
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
        recyclerView.adapter = UnitAdapter(program)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}*/