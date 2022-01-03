package shmuly.sternbach.custommishnahlearningprogram.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import shmuly.sternbach.custommishnahlearningprogram.*
import shmuly.sternbach.custommishnahlearningprogram.adapters.StartEndAdapter
import shmuly.sternbach.custommishnahlearningprogram.adapters.ld
import shmuly.sternbach.custommishnahlearningprogram.data.Mishnah
//import shmuly.sternbach.custommishnahlearningprogram.data.units.ProgramUnitMaterial
import timber.log.Timber
import java.time.LocalDate
import java.time.Period
var programInitialized = false
//lateinit var program: Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>
//lateinit var program: Map<LocalDate, MutablePair<String?, MutableList<String>>>
class CreateProgramActivity : AppCompatActivity(), CallbackListener {
    val list = mutableListOf<Pair<Mishnah, Mishnah>>()
    lateinit var adapter: StartEndAdapter
    lateinit var startDate: LocalDate
    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_program)
        val addFab = findViewById<FloatingActionButton>(R.id.add_fab)
        val doneFab = findViewById<MaterialButton>(R.id.done)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val oneDay = findViewById<MaterialCheckBox>(R.id.review_1_day)
        val eightDay = findViewById<MaterialCheckBox>(R.id.review_8_days)
        val thirtyEightDay = findViewById<MaterialCheckBox>(R.id.review_38_days)
        val oneTwentyEightDay = findViewById<MaterialCheckBox>(R.id.review_128_days)
        val threeSixtyFourDay = findViewById<MaterialCheckBox>(R.id.review_364_days)
        val parsedText = findViewById<TextView>(R.id.parsed_text)
        val customTimes = findViewById<EditText>(R.id.custom_times)
        val numReviews = findViewById<TextInputEditText>(R.id.num_reviews)
        val dateTextInputLayout = findViewById<TextInputLayout>(R.id.date_text_input_layout)
        val date = findViewById<AutoCompleteTextView>(R.id.date)
        val speed = findViewById<TextInputEditText>(R.id.speed)
        customTimes.addTextChangedListener {
            parsedText.text = customTimesParsed(it.toString()).filter{it.isNotEmpty() && it.isDigitsOnly()}.joinToString(", ")
        }
        adapter = StartEndAdapter(list, this, supportFragmentManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        addFab.setOnClickListener {
            list.add(Mishnah() to Mishnah())
            adapter.notifyItemInserted(list.size - 1)
        }
        val lambda = { _: View ->
            val datePicker = DatePickerDialog(this)
            datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                startDate = LocalDate.of(year, month+1, dayOfMonth)
                date.setText(formatLocalDate(startDate))
            }
            datePicker.show()
        }
        dateTextInputLayout.setEndIconOnClickListener(lambda)
        date.setOnClickListener(lambda)
        doneFab.setOnClickListener {
            Toast.makeText(this, "Program being saved. If there is a program already running, it will be overwritten.", Toast.LENGTH_LONG).show()//TODO edit this messsage when implementing multiple programs

            for(program in list){
                val startIndex = program.first.masechtaIndex    // mishnayos.chapterNames.indexOf("Pe'ah")
                val endIndex = program.second.masechtaIndex + 1 //mishnayos.chapterNames.indexOf("Bikkurim") + 1 //exclusive when used as last index and inclusive when used as first endex, so save myself the math by doing it here
                mishnayos.listOfPrograms.add(
                    mishnayos.listMappedToUnits(
                        mishnayos.chapterNames.subList(startIndex, endIndex),
                        mishnayos.chapterLengths.subList(startIndex, endIndex)
                    )
                )
            }
            if(oneDay.isChecked) mishnayos.reviewIntervals.add(1)
            if(eightDay.isChecked) mishnayos.reviewIntervals.add(8)
            if(thirtyEightDay.isChecked) mishnayos.reviewIntervals.add(38)
            if(thirtyEightDay.isChecked) mishnayos.reviewIntervals.add(38)
            if(oneTwentyEightDay.isChecked) mishnayos.reviewIntervals.add(128)
            if(threeSixtyFourDay.isChecked) mishnayos.reviewIntervals.add(364)
            val customText = customTimes.text.toString()
            if(customText.isNotEmpty()) for(number in customTimesParsed(customText)) if(number.isNotEmpty() && number.isDigitsOnly()) mishnayos.reviewIntervals.add(number.toInt()) //TODO show user how much of their input is being parsed in case they made a mistake
            if(threeSixtyFourDay.isChecked) mishnayos.reviewIntervals.add(364)
            mishnayos.numReviewsPerUnit = numReviews.text.toString().toInt()
            val unDateMappedProgram = mishnayos.generateProgram(
                startDate = startDate,
                intervalToLearnNewMaterial = Period.of(0, 0, 1), //TODO add ability to change
                speed.text.toString().toInt(),
                CONSTANTS.PROGRAM_ID_MISHNAYOS
            )
//            Toast.makeText(this, "Saving program...", Toast.LENGTH_SHORT).show()
            mcoroutineScope.launch(Dispatchers.IO) {
                val list = unDateMappedProgram.first + unDateMappedProgram.second  /*TODO consider avoiding having to combine these by only creating one list in generateProgram(), though I am keeping it this way in case I want to change the mechanics later*/
                ld("Program about to insert: ${list.take(10)}")
                (application as ReviewApplication).repository.also {
                    list.forEach { unit ->
                        it.insert(unit)
                        ld("Unit inserted: $unit")
                    }
                    ld("Program from db: ${it.allTimelines.first()}")
                    ld("Today's material from db: ${it.todaysMaterial.first()}")
                    runOnUiThread {
                        programInitialized = true
                        sharedPreferences.edit(true) { putString("program_created", " ") }
                        Toast.makeText(this@CreateProgramActivity, "Done saving program.", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            }
        }
    }

    private fun customTimesParsed(customText: String) = customText.split("\\s?,\\s?".toRegex())

    override fun onMishnahPicked(
        adapterPosition: Int,
        startSelected: Boolean,
        masechtaString: String,
        masechtaIndex: Int,
        perekString: String,
        perekIndex: Int,
        mishnah: Int
    ) {
        val item = list[adapterPosition]
        if (startSelected) {
            item.first
        } else {
            item.second
        }.also {
            it.masechtaString = masechtaString
            it.masechtaIndex = masechtaIndex
            it.perekString = perekString
            it.perekIndex = perekIndex
            it.mishnah = mishnah
        }
        adapter.notifyItemChanged(adapterPosition)
    }
}
data class MutablePair<A,B>(var first: A, var second: B)
data class MutableTriple<A,B,C>(var first: A, var second: B, var third: C)
data class Quad<A,B,C,D>(val first: A, val second: B, val third: C, val fourth: D)
/**
 *
 * TODO consider making original data structure more suitable to this, like returning a list of reviews for every date instead of adding each review unit to a list of strings
 * @return date to Pair<content for the date (null if no new material and just reviews, reviews>
 * */
/*
fun Pair<MutableList<Pair<ProgramUnitMaterial<String>, LocalDate>>, MutableList<Pair<ProgramUnitMaterial<String>, LocalDate>>>.toDateMap(): Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>> {
//fun Pair<MutableList<Pair<String, LocalDate>>, MutableList<Pair<String, LocalDate>>>.toDateMap(): Map<LocalDate, MutablePair<String?, MutableList<String>>> {
    val content = this.first
    val reviews = this.second
    val map = mutableMapOf<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>()
//    val map = mutableMapOf<LocalDate, MutablePair<String?, MutableList<String>>>()
    for(content1 in content) {
        map[content1.second] = MutablePair(content1.first, mutableListOf())
    }
    for(review in reviews){
        val result = map.putIfAbsent(review.second, MutablePair(null, mutableListOf(review.first)))
        result?.second?.add(review.first) //if already in map, add review to date
    }
    return map.toSortedMap()
}
*/
