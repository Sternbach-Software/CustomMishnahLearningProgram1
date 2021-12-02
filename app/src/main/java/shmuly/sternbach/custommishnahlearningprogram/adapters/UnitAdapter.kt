package shmuly.sternbach.custommishnahlearningprogram.adapters

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.l4digital.fastscroll.FastScroller
import io.objectbox.Box
import io.objectbox.Property
import shmuly.sternbach.custommishnahlearningprogram.*
import shmuly.sternbach.custommishnahlearningprogram.activities.MutablePair
import shmuly.sternbach.custommishnahlearningprogram.data.molecules.*
import shmuly.sternbach.custommishnahlearningprogram.data.units.*
import timber.log.Timber
import java.time.LocalDate

internal class UnitAdapter(
    listOfDateToContentToReview: Map<LocalDate, MutablePair<ProgramUnitMaterial<String>?, MutableList<ProgramUnitMaterial<String>>>>
) : RecyclerView.Adapter<UnitAdapter.ViewHolder>(), FastScroller.SectionIndexer {
    fun ld(message: String) = Timber.d(message)
    val list = listOfDateToContentToReview.toList()
    val listOfExpanded = MutableList(list.size) { false }
    val listOfDrawn = MutableList(list.size) { false } //whether sub-items were drawn
    var doneIcon: Drawable? = null
    var skippedIcon: Drawable? = null
    var todoIcon: Drawable? = null

    fun getDoneIcon(resources: Resources, theme: Resources.Theme) =
        doneIcon ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_done, theme)
            .also { doneIcon = it }

    fun getSkippedIcon(resources: Resources, theme: Resources.Theme) =
        skippedIcon ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_skipped, theme)
            .also { skippedIcon = it }

    fun getToDoIcon(resources: Resources, theme: Resources.Theme) =
        todoIcon ?: ResourcesCompat.getDrawable(resources, R.drawable.ic_to_do, theme)
            .also { todoIcon = it }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val card = v as MaterialCardView
        val reviews: TextView = v.findViewById(R.id.reviews)
        val title: TextView = v.findViewById(R.id.title)
        val date: TextView = v.findViewById(R.id.date)
        val skip: Button = v.findViewById(R.id.skip_button)
        val complete: Button = v.findViewById(R.id.complete_button)
        val clear: Button = v.findViewById(R.id.clear_button)
        val todo: Button = v.findViewById(R.id.todo_button)
        val image: ImageView = v.findViewById(R.id.completion_indicator)
        val list: LinearLayout = v.findViewById(R.id.individual_items_ll)
        val progressIndicator: CircularProgressIndicator = v.findViewById(R.id.progress_circular)
        val showHideButton: Button = v.findViewById(R.id.expand_button)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.list_item_unit_card, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pair = list[(position)]
        ld("OnBindViewHolder: pair=$pair")
        viewHolder.date.text = formatLocalDate(pair.first)
        viewHolder.title.text =
            if (pair.second.first?.listOfMaterials?.first() == "null") "Review".also { ld("Called == null") } else pair.second.first.toString()
                .also { ld("Called not null, list = ${pair.second.first!!.listOfMaterials}") }
        val toSet = pair.second.second
        viewHolder.progressIndicator.max = toSet.sumBy { it.listOfMaterials.size }
        viewHolder.reviews.text = toSet.joinToString("\n")
        val resources = viewHolder.image.resources
        val lambda = { view: View ->
            if (!listOfExpanded[position] && !listOfDrawn[position]) {
                val allIndividualUnits = toSet.flatMap { it.listOfMaterials }
                ld("allIndividualUnits=$allIndividualUnits")
                for (index in allIndividualUnits.indices) {
                    viewHolder.list.also {
                        LayoutInflater.from(it.context)
                            .inflate(R.layout.individual_mishnah_review_sub_item, it, false).apply {
                                val unit = allIndividualUnits[index]
                                val toggleGroup =
                                    findViewById<MaterialButtonToggleGroup>(R.id.togggle_group)
                                toggleGroup.addOnButtonCheckedListener { group, id, isChecked ->
                                    when (id) {
                                        R.id.todo_button -> {
                                            val toPut = TODOMolecule(
                                                molecule = unit,
                                                date = pair.first
                                            )
                                            ld("Contained: ${containsTODOMolecule(unit, pair.first)}")
                                            if(isChecked) todoMoleculesBox.put(toPut)
                                            else removeTODOMolecule(unit, pair.first)
                                        }
                                        R.id.skip_button -> {
                                            val toPut = SkippedMolecule(
                                                molecule = unit,
                                                date = pair.first
                                            )
                                            ld("Contained: ${containsSkippedMolecule(unit, pair.first)}")
                                            if(isChecked) skippedMoleculesBox.put(toPut)
                                            else removeSkippedMolecule(unit, pair.first)
                                        }
                                        R.id.complete_button -> {

                                            val toPut = CompletedMolecule(
                                                molecule = unit,
                                                date = pair.first
                                            )
                                            ld("Contained: ${containsCompletedMolecule(unit, pair.first)}")
                                            if(isChecked) completedMoleculesBox.put(toPut)
                                            else removeCompletedMolecule(unit, pair.first)
                                        }
                                    }
                                }
                                when {
                                    containsCompletedMolecule(unit, pair.first) -> toggleGroup.check(R.id.complete_button)
                                    containsSkippedMolecule(unit, pair.first) -> toggleGroup.check(R.id.skip_button)
                                    containsTODOMolecule(unit, pair.first) -> toggleGroup.check(R.id.todo_button)
                                }
                                findViewById<TextView>(R.id.unit_to_review).text =
                                    unit.also { ld("allIndividualUnits[index]=$it") }
                                findViewById<Button>(R.id.todo_button)
                                    .also {
                                        if (todoUnitsBox.all.any {
                                                it.containsThisUnit(
                                                    unit,
                                                    pair.first
                                                )
                                            }) it.performClick()
                                    }
                                    .setOnClickListener {
                                        todoUnitsBox.put(
                                            TODOUnit(
                                                programUnitMaterial = ProgramUnitMaterial(
                                                    listOf(
                                                        unit
                                                    )
                                                ),
                                                date = pair.first
                                            )
                                        )
                                    }
                                findViewById<Button>(R.id.skip_button)
                                    .also {
                                        if (skippedUnitsBox.all.any {
                                                it.containsThisUnit(
                                                    unit,
                                                    pair.first
                                                )
                                            }) it.performClick()
                                    }
                                    .setOnClickListener {
                                        skippedUnitsBox.put(
                                            SkippedUnit(
                                                programUnitMaterial = ProgramUnitMaterial(
                                                    listOf(
                                                        unit
                                                    )
                                                ),
                                                date = pair.first
                                            )
                                        )
                                    }
//                            completedBox.query { this.filter { it.containsThisUnit(unit, pair.first) } }.findFirst() != null //TODO is this more efficient than getting all entities and using stdlib to check?
                                findViewById<Button>(R.id.complete_button)
                                    .also {
                                        if (completedUnitsBox.all.any {
                                                it.containsThisUnit(
                                                    unit,
                                                    pair.first
                                                )
                                            }) it.performClick()
                                    }
                                    .setOnClickListener {
                                        completedUnitsBox.put(
                                            CompletedUnit(
                                                programUnitMaterial = ProgramUnitMaterial(
                                                    listOf(
                                                        unit
                                                    )
                                                ),
                                                date = pair.first
                                            )
                                        )
                                    }
                                it.addView(this)
                            }
                        it.visibility = View.VISIBLE
                    }
                }
                listOfExpanded[position] = true
                listOfDrawn[position] = true
            } else if (!listOfExpanded[position]) {
                listOfExpanded[position] = true
                viewHolder.list.visibility = View.VISIBLE
            } else {
                listOfExpanded[position] = false
                viewHolder.list.visibility = View.GONE
            }
        }
        viewHolder.showHideButton.setOnClickListener(lambda)
        viewHolder.card.setOnClickListener(lambda)
        /*viewHolder.card.setOnClickListener {
            if(!viewHolder.alreadyExpanded) {
                val allIndividualUnits = toSet.flatMap { it.listOfMaterials }
                createNewItem.createSubItems(allIndividualUnits.size)
                for(index in allIndividualUnits.indices) {
                    createNewItem.getSubItemView(index).apply {
                        findViewById<TextView>(R.id.unit_to_review).text = allIndividualUnits[index]
                        findViewById<Button>(R.id.todo_button).setOnClickListener {  }
                        findViewById<Button>(R.id.skip_button).setOnClickListener {  }
                        findViewById<Button>(R.id.complete_button).setOnClickListener {  }
                    }
                }
                viewHolder.alreadyExpanded = true
                viewHolder.item.toggleExpanded()
            } else viewHolder.item.toggleExpanded()
        }*/
        viewHolder.complete.setOnClickListener {
            viewHolder.image.setImageDrawable(
                getDoneIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
//            if(pair.first.isAfter(LocalDate.now())){}//move date ahead, remove review from today and add it to the date that you completed
//            completedUnitsFile.appendText("~${pair.first}")
//            setOfCompleted.add(pair.first)
        }
        viewHolder.skip.setOnClickListener {
            viewHolder.image.setImageDrawable(
                getSkippedIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
//            skippedUnitsFile.appendText("~${pair.first}")
//            setOfSkipped.add(pair.first)
        }
        viewHolder.todo.setOnClickListener {
            viewHolder.image.setImageDrawable(
                getToDoIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
            //            skippedUnitsFile.appendText("~${pair.first}")
//            setOfSkipped.add(pair.first)
        }
        viewHolder.clear.setOnClickListener {
            viewHolder.image.setImageDrawable(null)
        }
    }

    private fun containsTODOMolecule(
        unit: String,
        date: LocalDate
    ) = todoMoleculesBox.containsMolecule(
        TODOMolecule_.molecule,
        TODOMolecule_.date,
        unit,
        date
    )

    private fun containsSkippedMolecule(
        unit: String,
        date: LocalDate
    ) = skippedMoleculesBox.containsMolecule(
        SkippedMolecule_.molecule,
        SkippedMolecule_.date,
        unit,
        date
    )

    private fun containsCompletedMolecule(
        unit: String,
        date: LocalDate
    ) = completedMoleculesBox.containsMolecule(
        CompletedMolecule_.molecule,
        CompletedMolecule_.date,
        unit,
        date
    )

    private fun removeCompletedMolecule(
        unit: String,
        date: LocalDate
    ) {
        completedMoleculesBox.removeMolecule(
            CompletedMolecule_.molecule,
            CompletedMolecule_.date,
            unit,
            date
        )
    }

    private fun removeSkippedMolecule(
        unit: String,
        date: LocalDate
    ) {
        skippedMoleculesBox.removeMolecule(
            SkippedMolecule_.molecule,
            SkippedMolecule_.date,
            unit,
            date
        )
    }

    private fun ProgramUnitDated<String>.containsThisUnit(
        unit: String,
        dateToCompare: LocalDate
    ) =
        programUnitMaterial
            ?.listOfMaterials
            ?.contains(unit)
            ?.and(
                date?.isEqual(dateToCompare) == true
            ) == true

    override fun getItemCount(): Int = list.size
    override fun getSectionText(position: Int): CharSequence {
        return formatLocalDate(list[position].first)
    }
}

private fun <T> Box<T>.removeMolecule(property1: Property<T>, property2: Property<T>, unit: String, date: LocalDate) {
    val find = query()
        .apply(property1.equal(unit).and(property2.equal(date.toString())))
        .build()
        .findFirst()
    if(find != null) {
        ld("Find was not null: $find")
        this.remove(find)
    }
}
private fun <T> Box<T>.containsMolecule(property1: Property<T>, property2: Property<T>, unit: String, date: LocalDate): Boolean {
    val find = query()
        .apply(property1.equal(unit).and(property2.equal(date.toString())))
        .build()
        .count()
    ld("Number found: $find")
    return find > 0
}
fun removeTODOMolecule(unit: String, date: LocalDate) = todoMoleculesBox.removeMolecule(
    TODOMolecule_.molecule,
    TODOMolecule_.date,
    unit,
    date
)
fun ld(s: String) {
    Timber.d(s)
}
