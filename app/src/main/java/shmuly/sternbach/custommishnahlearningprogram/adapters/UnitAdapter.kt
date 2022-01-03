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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.android.roomwordssample.ProgramUnitsViewModel
//import com.example.android.roomwordssample.WordListAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.l4digital.fastscroll.FastScroller
import shmuly.sternbach.custommishnahlearningprogram.*
import shmuly.sternbach.custommishnahlearningprogram.data.CompletionStatus
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import timber.log.Timber
import java.time.LocalDate

internal class UnitAdapter(
    val viewModel: ProgramUnitsViewModel
) : ListAdapter<Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>, UnitAdapter.ViewHolder>(UNITS_COMPARATOR), FastScroller.SectionIndexer {
    fun ld(message: String) = Timber.d(message)
    /*listOfDateToContentToReview: Map<LocalDate, Pair<List<ProgramUnit>/*new material*/, List<ProgramUnit>/*reviews*/>>,
    val intervalSize: Int,
    val repository: WordRepository,*/
    var list = this.currentList
    var listOfExpanded = MutableList(list.size) { false }
    var listOfDrawn = MutableList(list.size) { false } //whether sub-items were drawn

    override fun onCurrentListChanged(
        previousList: MutableList<Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>>,
        currentList: MutableList<Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        listOfExpanded = MutableList(currentList.size) { false }
        listOfDrawn = MutableList(currentList.size) { false }
    }
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
        val pair = getItem(position)
        ld("OnBindViewHolder: pair=$pair")
        viewHolder.date.text = formatLocalDate(pair.first)
        val newMaterialString = if (pair.second.isEmpty()) "Review"
        else getOverviewString(pair.second) /*Brachos 1:1 - Brachos 1:4*/

        viewHolder.title.text = newMaterialString
        viewHolder.progressIndicator.max = pair.second.size + pair.third.size
        viewHolder.reviews.text = getOverviewString(pair.third)
        val selector: (ProgramUnit) -> Int = { (if (it.isCompleted) 1 else 0) }
        viewHolder.progressIndicator.progress = pair.second.sumOf(selector) + pair.third.sumOf(selector)
        val resources = viewHolder.image.resources
        val lambda = { view: View ->
            if (!listOfExpanded[position] && !listOfDrawn[position]) {
                ld("allIndividualUnits=${pair.third}")
                for (index in pair.third.indices) {
                    viewHolder.list.also {
                        LayoutInflater.from(it.context)
                            .inflate(R.layout.individual_mishnah_review_sub_item, it, false)
                            .apply {
                                val unit = pair.third[index]
                                findViewById<TextView>(R.id.unit_to_review).text = unit.material
                                val toggleGroup =
                                    findViewById<MaterialButtonToggleGroup>(R.id.togggle_group)
                                when {
                                    unit.isCompleted -> toggleGroup.check(R.id.complete_button)
                                    unit.isSkipped -> toggleGroup.check(R.id.skip_button)
                                    unit.isTODO -> toggleGroup.check(R.id.todo_button)
                                }
                                toggleGroup.addOnButtonCheckedListener { group, id, isChecked ->
                                    when (id) {
                                        R.id.todo_button -> {
                                            unit.completedStatus =
                                                if (isChecked) CompletionStatus.TODO
                                                else CompletionStatus.NONE
                                        }
                                        R.id.skip_button -> {
                                            unit.completedStatus =
                                                if (isChecked) CompletionStatus.SKIPPED
                                                else CompletionStatus.NONE
                                        }
                                        R.id.complete_button -> {
                                            unit.completedStatus =
                                                if (isChecked) CompletionStatus.COMPLETED
                                                else CompletionStatus.NONE
//                                            viewHolder.progressIndicator.progress++ //TODO not fully implemented
                                        }
                                    }
                                    viewModel.update(unit)
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
            updateCompletionStatus(pair, CompletionStatus.COMPLETED)
            viewHolder.progressIndicator.progress = viewHolder.progressIndicator.max
//            if(pair.first.isAfter(LocalDate.now())){}//move date ahead, remove review from today and add it to the date that you completed TODO
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
            updateCompletionStatus(pair, CompletionStatus.SKIPPED)
            viewHolder.progressIndicator.progress = 0
        }
        viewHolder.todo.setOnClickListener {
            viewHolder.image.setImageDrawable(
                getToDoIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
            updateCompletionStatus(pair, CompletionStatus.TODO)
            viewHolder.progressIndicator.progress = 0
        }
        viewHolder.clear.setOnClickListener {
            viewHolder.image.setImageDrawable(null)
        }
    }

    private fun updateCompletionStatus(pair: Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>, status: Int) {
        for (unit in pair.second + pair.third) {
            unit.completedStatus = status
            viewModel.update(unit)
        }
    }

    private fun getIntervalSize(units: List<ProgramUnit>): Int {
        val firstGroup = units.first().group
        var intervalSize = 0
        for (it in units) { //see how long the longest group is
            if (it.group == firstGroup) intervalSize++ else break
        }
        return intervalSize
    }

    /**
     * Returns "Brachos 1:1 - Brachos 1:4"\n...
     * */
    private fun getOverviewString(units: List<ProgramUnit>): String {
        val stringBuilder = StringBuilder()
        var counter = 0
        var firstUnitInGroup = units[counter++]
        //1,1,1,1,2,2,2,3,3,3,4,5,6
        var previousUnit = firstUnitInGroup
        var thisUnit = units[counter]
        while(counter < units.size){
            if(firstUnitInGroup.group == thisUnit.group) {
                previousUnit = thisUnit
            }
            else {
                stringBuilder.appendLine(firstUnitInGroup.material + " - " + previousUnit.material)
                firstUnitInGroup = thisUnit
                previousUnit = firstUnitInGroup
            }
            thisUnit = units[counter++]
        }
        return stringBuilder.toString()
    }
    override fun getSectionText(position: Int): CharSequence {
        return formatLocalDate(getItem(position).first)
    }
    companion object {
        private val UNITS_COMPARATOR = object : DiffUtil.ItemCallback<Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>>() {
            override fun areItemsTheSame(
                oldItem: Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>,
                newItem: Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>,
                newItem: Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>
            ): Boolean {
                return oldItem.first == newItem.first &&
                        oldItem.second == newItem.second /*if new material is the same, then reviews should be the same also*/
            }
        }
    }
}
fun ld(s: String) {
    Timber.d(s)
}
