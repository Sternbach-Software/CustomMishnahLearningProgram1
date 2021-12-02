package shmuly.sternbach.custommishnahlearningprogram.adapters

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.card.MaterialCardView
import com.l4digital.fastscroll.FastScroller
import shmuly.sternbach.custommishnahlearningprogram.*
import shmuly.sternbach.custommishnahlearningprogram.activities.MutablePair
import shmuly.sternbach.custommishnahlearningprogram.data.ProgramUnit
import timber.log.Timber
import java.time.LocalDate

internal class UserAdapter(
    listOfDateToContentToReview: Map<LocalDate, MutablePair<ProgramUnit<String>?, MutableList<ProgramUnit<String>>>>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>(), FastScroller.SectionIndexer {
    fun ld(message: String) = Timber.d(message)
    val list = listOfDateToContentToReview.toList()
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
        val reviews: TextView = v.findViewById(R.id.reviews)
        val title: TextView = v.findViewById(R.id.title)
        val date: TextView = v.findViewById(R.id.date)
        val skip: Button = v.findViewById(R.id.skip_button)
        val complete: Button = v.findViewById(R.id.complete_button)
        val clear: Button = v.findViewById(R.id.clear_button)
        val todo: Button = v.findViewById(R.id.todo_button)
        val image: ImageView = v.findViewById(R.id.completion_indicator)

        init {
            ld("View holder init")
            (v as MaterialCardView).apply {
                isEnabled = false
//                isDragged = true
            }
        }
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
        viewHolder.title.text = pair.second.first.toString()
        viewHolder.reviews.text = pair.second.second.toSet()
            .joinToString("\n")//TODO for some reason there are doubles and I therefore need to .toSet(). That shouldn't be.
        val resources = viewHolder.image.resources
        var addToComplete = false
        var addToSkip = false
        var addToTODO = false
        viewHolder.complete.setOnClickListener {
            viewHolder.image.setImageDrawable(
                getDoneIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
//            if(pair.first.isAfter(LocalDate.now())){}//move date ahead, remove review from today and add it to the date that you completed
            completedUnitsFile.appendText("~${pair.first}")
//            setOfCompleted.add(pair.first)
        }
        viewHolder.skip.setOnClickListener {
            viewHolder.image.setImageDrawable(
                getSkippedIcon(
                    resources,
                    viewHolder.itemView.context.theme
                )
            )
            skippedUnitsFile.appendText("~${pair.first}")
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

    override fun getItemCount(): Int = list.size
    override fun getSectionText(position: Int): CharSequence {
        return formatLocalDate(list[position].first)
    }
}