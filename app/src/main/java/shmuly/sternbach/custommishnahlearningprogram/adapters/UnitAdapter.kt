package shmuly.sternbach.custommishnahlearningprogram.adapters

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.android.roomwordssample.ProgramUnitsViewModel
//import com.example.android.roomwordssample.WordListAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.l4digital.fastscroll.FastScroller
import shmuly.sternbach.custommishnahlearningprogram.*
import shmuly.sternbach.custommishnahlearningprogram.Utils.getOverviewString
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
        val newMaterial: TextView = v.findViewById(R.id.new_material)
        val reviews: TextView = v.findViewById(R.id.reviews)
        val date: TextView = v.findViewById(R.id.date)
        val skip: Button = v.findViewById(R.id.skip_button)
        val complete: Button = v.findViewById(R.id.complete_button)
        val todo: Button = v.findViewById(R.id.todo_button)
        val completionIndicatorNewMaterial: ImageView = v.findViewById(R.id.completion_indicator_new_material)
        val progressIndicatorNewMaterial: CircularProgressIndicator = v.findViewById(R.id.progress_circular_new_material)
        val progressIndicatorReviews: CircularProgressIndicator = v.findViewById(R.id.progress_circular_reviews)
        val completionIndicatorReviews: ImageView = v.findViewById(R.id.completion_indicator_reviews)
        val listOfUnits: LinearLayout = v.findViewById(R.id.individual_items_ll)
        val showHideButton: ImageButton = v.findViewById(R.id.expand_button)
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
        viewHolder.date.text = formatLocalDate(pair.first)

        viewHolder.newMaterial.text = if (pair.second.isEmpty()) "No new material"
        else getOverviewString(pair.second) /*Brachos 1:1 - Brachos 1:4*/
        viewHolder.reviews.text = if (pair.third.isEmpty()) "No reviews"
        else getOverviewString(pair.third)

        if (viewHolder.listOfUnits.isVisible && !listOfExpanded[position]) {
            viewHolder.listOfUnits.removeAllViews()
            viewHolder.listOfUnits.visibility = View.GONE
        }
        viewHolder.progressIndicatorNewMaterial.max = pair.second.size + pair.third.size
        val selector: (ProgramUnit) -> Int = { (if (it.isCompleted) 1 else 0) }
        viewHolder.progressIndicatorNewMaterial.progress =
            pair.second.sumOf(selector) + pair.third.sumOf(selector)
        val resources = viewHolder.completionIndicatorNewMaterial.resources
        val listOfAllMaterial = pair.second + pair.third
        val lambda = { view: View ->
            val isExpanded = listOfExpanded[position]
            var listPopulatedFromPreviousDrawing = false
            val needToDrawOrRedrawChildren =
                /*need to draw because no children have been drawn before*/
                (!(isExpanded && listOfDrawn[position])).also { if (it) ld("condition 1") } ||
                        /*need to redraw because children already drawn from different viewholder's information*/ (
                        /*only overwrite old children if neccesary (i.e. list is visible)*/ viewHolder.listOfUnits.isVisible.also {
                    if (it) ld(
                        "condition 2"
                    )
                } &&
                        /*there are children/reviews drawn*/ (viewHolder.listOfUnits.childCount != 0).also {
                    if (it) ld(
                        "condition 3"
                    )
                } &&
                        /*there are more or less than this should have*/ (viewHolder.listOfUnits.childCount != pair.second.size + pair.third.size).also {
                    if (it) ld(
                        "condition 4"
                    )
                } ||
                        /*even if there are the same amount, maybe they are not the same children as this one's*/ viewHolder.listOfUnits.children.let {
                    val listOfMaterialStrings = listOfAllMaterial.map { it.material }
                    it.any {
                        it.findViewById<TextView>(R.id.unit_to_review).text !in listOfMaterialStrings
                    }.also { if (it) ld("condition 5") }
                }
                        ).also { listPopulatedFromPreviousDrawing = it }
            if (needToDrawOrRedrawChildren) {
                if (listPopulatedFromPreviousDrawing) viewHolder.listOfUnits.removeAllViews()
                ld("allIndividualUnits=${pair.third}")
                for (index in listOfAllMaterial.indices) {
                    viewHolder.listOfUnits.also {
                        LayoutInflater.from(it.context)
                            .inflate(R.layout.individual_mishnah_review_sub_item, it, false)
                            .apply {
                                val unit = listOfAllMaterial[index]
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
            } else if (!isExpanded) {
                ld("List was not expanded")
                listOfExpanded[position] = true
                viewHolder.listOfUnits.visibility = View.VISIBLE
            } else {
                ld("List was not expanded")
                listOfExpanded[position] = false
                viewHolder.listOfUnits.visibility = View.GONE
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
            viewHolder.completionIndicatorNewMaterial.setImageDrawable(
                getDoneIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
            updateCompletionStatus(pair, CompletionStatus.COMPLETED)
            viewHolder.progressIndicatorNewMaterial.progress = viewHolder.progressIndicatorNewMaterial.max
//            if(pair.first.isAfter(LocalDate.now())){}//move date ahead, remove review from today and add it to the date that you completed TODO
//            completedUnitsFile.appendText("~${pair.first}")
//            setOfCompleted.add(pair.first)
        }
        viewHolder.skip.setOnClickListener {
            viewHolder.completionIndicatorNewMaterial.setImageDrawable(
                getSkippedIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
            updateCompletionStatus(pair, CompletionStatus.SKIPPED)
            viewHolder.progressIndicatorNewMaterial.progress = 0
        }
        viewHolder.todo.setOnClickListener {
            viewHolder.completionIndicatorNewMaterial.setImageDrawable(
                getToDoIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
            updateCompletionStatus(pair, CompletionStatus.TODO)
            viewHolder.progressIndicatorNewMaterial.progress = 0
        }
    }

    private fun updateCompletionStatus(pair: Triple<LocalDate, List<ProgramUnit>, List<ProgramUnit>>, status: Int) {
        for (unit in pair.second + pair.third) {
            unit.completedStatus = status
            viewModel.update(unit)
        }
    }

    /**
     * Returns "Brachos 1:1 - Brachos 1:4"\n...
     * */
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
