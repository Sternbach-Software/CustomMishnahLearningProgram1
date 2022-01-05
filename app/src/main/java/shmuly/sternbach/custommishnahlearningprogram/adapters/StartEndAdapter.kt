package shmuly.sternbach.custommishnahlearningprogram.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import shmuly.sternbach.custommishnahlearningprogram.CallbackListener
import shmuly.sternbach.custommishnahlearningprogram.MishnahPickerDialog
import shmuly.sternbach.custommishnahlearningprogram.R
import shmuly.sternbach.custommishnahlearningprogram.data.Mishnah

class StartEndAdapter(
    val startAndEndUnits: MutableList<Mishnah>,
    val callbackListener: CallbackListener,
    val fragmentManager: FragmentManager
) : RecyclerView.Adapter<StartEndAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val unit = view.findViewById<TextView>(R.id.unit)
        val label = view.findViewById<TextView>(R.id.label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_learn_start_and_end, parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isStart = position % 2 != 0
        holder.label.text = if (isStart) "Start:" else "End:"
        holder.unit.setOnClickListener {
            MishnahPickerDialog(callbackListener, position, isStart).show(
                fragmentManager,
                "MishnahPicker"
            )
        }
        holder.unit.text = startAndEndUnits[position].toString()
    }

    override fun getItemCount(): Int = startAndEndUnits.size

}