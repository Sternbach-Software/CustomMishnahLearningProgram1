package shmuly.sternbach.custommishnahlearningprogram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class StartEndAdapter(
    val startAndEndUnits: MutableList<Pair<Mishnah, Mishnah>>,
    val callbackListener: CallbackListener,
    val fragmentManager: FragmentManager
) : RecyclerView.Adapter<StartEndAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val startUnit = view.findViewById<TextView>(R.id.start_mishnah)
        val endUnit = view.findViewById<TextView>(R.id.end_mishnah)
        init {
            startUnit.setOnClickListener {
                MishnahPickerDialog(callbackListener, adapterPosition, true).show(fragmentManager, "MishnahPicker")
            }
            endUnit.setOnClickListener {
                MishnahPickerDialog(callbackListener, adapterPosition, false).show(fragmentManager, "MishnahPicker")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(
                    parent.context
                )
                .inflate(
                    R.layout.list_item_learn_start_and_end,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = startAndEndUnits[position]
        holder.startUnit.text = item.first.toString()
        holder.endUnit.text = item.second.toString()
    }

    override fun getItemCount(): Int = startAndEndUnits.size

}