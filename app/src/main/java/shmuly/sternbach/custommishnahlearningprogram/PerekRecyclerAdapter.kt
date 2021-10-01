package shmuly.sternbach.custommishnahlearningprogram

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "PerekRecyclerAdapter"

class PerekRecyclerAdapter(val mDataSet: List<String>) :
    RecyclerView.Adapter<PerekRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val perekName: TextView = v.findViewById(R.id.perek_name)
        val scrollViewChildLayout: LinearLayout = v.findViewById(R.id.scroll_view_child_layout)

        init {
            //move this to onBindViewHolder when this class has access to the real data
            for (i in mDataSet) scrollViewChildLayout.addView(Button(scrollViewChildLayout.context).apply {
                text = i
                setOnClickListener {
                    //change color and set as selected mishnah
                }
            })
            //TODO there is some strange bug that if you scroll through the "Perek 1" scroll view and then scroll down to the "Perek 10" scroll view, it will be scrolled to the same place. And even to the extent that if you start scrolling the 1st, and then quickly scroll down, the 10th will still be scrolling as if it was the first one.
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_list_item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d("TorahDownloads",TAG, "Element $position set.")
        viewHolder.perekName.text = "Perek: ${mDataSet[position]}"
    }

    override fun getItemCount(): Int = mDataSet.size
}