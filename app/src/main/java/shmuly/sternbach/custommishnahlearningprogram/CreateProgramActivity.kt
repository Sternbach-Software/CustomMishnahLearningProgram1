package shmuly.sternbach.custommishnahlearningprogram

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.time.LocalDate
import java.time.Period

lateinit var program: Pair<MutableList<Pair<String, LocalDate>>, MutableList<Pair<String, LocalDate>>>
class CreateProgramActivity : AppCompatActivity(), CallbackListener {
    val list = mutableListOf<Pair<Mishnah, Mishnah>>()
    lateinit var adapter: StartEndAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_program)
        val programFile = File(getExternalFilesDir(null), "program")
        val addFab = findViewById<FloatingActionButton>(R.id.add_fab)
        val doneFab = findViewById<FloatingActionButton>(R.id.done_fab)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = StartEndAdapter(list, this, supportFragmentManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        addFab.setOnClickListener {
            list.add(Mishnah() to Mishnah())
            adapter.notifyItemInserted(list.size - 1)
        }
        doneFab.setOnClickListener {
            Toast.makeText(this, "Program being saved. If there is a program already running, it will be overwritten.", Toast.LENGTH_LONG).show()

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
            program = mishnayos.generateProgram(
                startDate = LocalDate.now(),
                intervalToLearnNewMaterial = Period.of(0, 0, 1),
                4
            )
//        File("Program.txt").apply {
//            if (exists()) delete()
//            createNewFile()
//            appendText("New material: ${program.first}\n")
//            appendText(
//                "Reviews: ${
//                    program.second.sortedBy { it.second }
//                        .mapIndexed { index, pair -> "$index~$pair" }
//                }"
//            )
//        }
            println(LocalDate.parse("2010-10-10").plus(Period.of(1,1,1)))
            programFile.createNewFile()
            programFile.delete()
        }
    }

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