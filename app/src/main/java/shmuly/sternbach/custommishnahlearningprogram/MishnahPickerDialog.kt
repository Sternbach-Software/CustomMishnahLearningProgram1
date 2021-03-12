package shmuly.sternbach.custommishnahlearningprogram


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This is the dialog that will be used for filtering the pages which can filterable shiurim
 * (e.g. Downloads, Favorites, List of speaker's shiurim,etc.)
 * */
private const val TAG = "MishnahPickerDialog"
class MishnahPickerDialog(private val callbackListener: CallbackListener) : DialogFragment() {
    //private lateinit var progressiveFilterExplanationImageButton: ImageButton
    private lateinit var selectButton: Button
    private lateinit var cancelButton: Button
    private lateinit var sederAutoCompleteTextView: AutoCompleteTextView
    private lateinit var masechtaAutoCompleteTextView: AutoCompleteTextView
    private lateinit var recyclerViewAdapter: PerekRecyclerAdapter
    private lateinit var sederBeingDisplayed: String //I have not tested the overall efficiency of storing the UI information in extracted variables (and instead of accessing the View's variables/fields, update the extracted variable and access that when needed), but the actual time it takes to access the extracted variable is much quicker than accessing the view's fields - in a vacuum. I say in a vacuum because in one of the android articles on efficiency, they said that object creation is always more expensive than using CPU because of the effort required in doing garbage collecting. I have not gotten to the testing and profiling phase in the TorahDownloads app to know the answer to this question, so feel free to do your own testing, and bear in mind this idea.
    private lateinit var masechtaBeingDisplayed: String //I have not tested the overall efficiency of storing the UI information in extracted variables (and instead of accessing the View's variables/fields, update the extracted variable and access that when needed), but the actual time it takes to access the extracted variable is much quicker than accessing the view's fields - in a vacuum. I say in a vacuum because in one of the android articles on efficiency, they said that object creation is always more expensive than using CPU because of the effort required in doing garbage collecting. I have not gotten to the testing and profiling phase in the TorahDownloads app to know the answer to this question, so feel free to do your own testing, and bear in mind this idea.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        return inflater.inflate(R.layout.mishna_picker, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //<editor-fold desc="variable initializations">
        masechtaAutoCompleteTextView = view.findViewById(R.id.masechta_autocomplete_text_view)
        sederAutoCompleteTextView = view.findViewById(R.id.seder_autocomplete_text_view)
        selectButton = view.findViewById(R.id.select_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        //</editor-fold>

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerViewAdapter = PerekRecyclerAdapter(List(10) {(it+1).toString()})//listOf(1,2,3...)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        setAdapterAndSetHints(
            sederAutoCompleteTextView,
            Sedarim
        )
        setAdapterAndSetHints(
            masechtaAutoCompleteTextView,
            Masechtot
        )

        updateRecyclerView()

        //<editor-fold desc="listeners">
        sederAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            sederBeingDisplayed = Sedarim[position]
            Log.d(TAG,"Seder currently being displayed: $sederBeingDisplayed")
        }
        masechtaAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            masechtaBeingDisplayed = Masechtot[position]
            Log.d(TAG,"Msechta currently being displayed: $masechtaBeingDisplayed")
        }
        selectButton.setOnClickListener {
            callbackListener.onMishnahPicked(sederBeingDisplayed,masechtaBeingDisplayed, "TODO", "TODO")
            dismiss()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
       //</editor-fold>
    }

    private fun updateRecyclerView() {
    }

    private fun setAdapterAndSetHints(
        dropDownMenu: AutoCompleteTextView,
        filterConditions: List<String>
    ) {
        val hint = filterConditions[0] // set default as first filter criterion
        dropDownMenu.setText(hint, false)
        if(filterConditions === Sedarim /*points to the same object*/) {
            sederBeingDisplayed = hint
            Log.d(TAG, "sederBeingDisplayed = $sederBeingDisplayed")
        } else {
            masechtaBeingDisplayed = hint
            Log.d(TAG, "masechtaBeingDisplayed = $masechtaBeingDisplayed")
        }
        dropDownMenu.setAdapter(context?.let { getDropdownAdapter(it,filterConditions) })
    }

    private fun getDropdownAdapter(
        context: Context,
        list: List<String>
    ) = ArrayAdapter(
        context,
        R.layout.sort_or_filter_dropdown_menu_item,
        list
    )

}
