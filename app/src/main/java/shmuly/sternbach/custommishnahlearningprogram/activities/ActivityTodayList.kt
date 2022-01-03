package shmuly.sternbach.custommishnahlearningprogram.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomwordssample.ProgramUnitsViewModel
import com.example.android.roomwordssample.WordViewModelFactory
import shmuly.sternbach.custommishnahlearningprogram.ReviewApplication
import shmuly.sternbach.custommishnahlearningprogram.adapters.UnitAdapter
import java.time.LocalDate

class ActivityTodayList: AppCompatActivity() {

    private val programUnitsViewModel: ProgramUnitsViewModel by viewModels {
        WordViewModelFactory((application as ReviewApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = RecyclerView(this)
        setContentView(recyclerView)
        val unitAdapter = UnitAdapter(programUnitsViewModel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = unitAdapter
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        programUnitsViewModel.todaysMaterial.observe(this) { units ->
            // Update the cached copy of the words in the adapter.
            units.let {
                if(it.isEmpty()) Toast.makeText(this, "Nothing to do today.", Toast.LENGTH_SHORT).show()
                unitAdapter.submitList(it)
            }
        }
    }
}