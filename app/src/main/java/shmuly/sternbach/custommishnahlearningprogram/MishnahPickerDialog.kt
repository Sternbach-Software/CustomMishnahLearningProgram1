package shmuly.sternbach.custommishnahlearningprogram


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment

/**
 * This is the dialog that will be used for filtering the pages which can filterable shiurim
 * (e.g. Downloads, Favorites, List of speaker's shiurim,etc.)
 * */
private const val TAG = "MishnahPickerDialog"
class MishnahPickerDialog(
    private val callbackListener: CallbackListener,
    val adapterPosition: Int,
    val startSelected: Boolean
) : DialogFragment() {
    //private lateinit var progressiveFilterExplanationImageButton: ImageButton
    private lateinit var selectButton: Button
    private lateinit var cancelButton: Button
    private lateinit var masechtaAutoCompleteTextView: AutoCompleteTextView
    private lateinit var perekAutoCompleteTextView: AutoCompleteTextView
    private lateinit var mishnahAutoCompleteTextView: AutoCompleteTextView
//    private lateinit var sederBeingDisplayed: String //I have not tested the overall efficiency of storing the UI information in extracted variables (and instead of accessing the View's variables/fields, update the extracted variable and access that when needed), but the actual time it takes to access the extracted variable is much quicker than accessing the view's fields - in a vacuum. I say in a vacuum because in one of the android articles on efficiency, they said that object creation is always more expensive than using CPU because of the effort required in doing garbage collecting. I have not gotten to the testing and profiling phase in the TorahDownloads app to know the answer to this question, so feel free to do your own testing, and bear in mind this idea.
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
        perekAutoCompleteTextView = view.findViewById(R.id.perek_autocomplete_text_view)
        mishnahAutoCompleteTextView = view.findViewById(R.id.mishnah_autocomplete_text_view)
        selectButton = view.findViewById(R.id.select_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        //</editor-fold>

        setAdapter(
            masechtaAutoCompleteTextView,
            mishnayos.chapterNames
        )

        //<editor-fold desc="listeners">
        var lengthsBeingDisplayed = listOf<Int>()
        var indexOfMasechtaBeingDisplayed = 0
        masechtaAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            indexOfMasechtaBeingDisplayed = position
            masechtaBeingDisplayed = mishnayos.chapterNames[position]
            lengthsBeingDisplayed = mishnayos.chapterLengths[position]
            perekAutoCompleteTextView.isEnabled = true
            setAdapter(
                perekAutoCompleteTextView,
                (1..lengthsBeingDisplayed.size).map { it.toString() }
            )
            Log.d(TAG,"Masechta currently being displayed: $masechtaBeingDisplayed")

        }
        var indexOfPerekBeingDisplayed = 1
        perekAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            indexOfPerekBeingDisplayed = position

            mishnahAutoCompleteTextView.isEnabled = true
            setAdapter(
                mishnahAutoCompleteTextView,
                (1..lengthsBeingDisplayed[position]).map { it.toString() }
            )
            Log.d(TAG,"Masechta currently being displayed: $masechtaBeingDisplayed")

        }
        var mishnaBeingDisplayed = 1
        mishnahAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            mishnaBeingDisplayed = position + 1 //starts with 1
            Log.d(TAG,"Masechta currently being displayed: $masechtaBeingDisplayed")

        }
        selectButton.setOnClickListener {
            Log.d(TAG, "" +
                    "" +
                    "" +
                    "adapterPosition=$adapterPosition\n" +
                    "startSelected=$startSelected\n" +
                    "masechtaBeingDisplayed=$masechtaBeingDisplayed\n" +
                    "indexOfMasechtaBeingDisplayed=$indexOfMasechtaBeingDisplayed\n" +
                    "perekBeingDisplayed=$indexOfPerekBeingDisplayed\n" +
                    "perekBeingDisplayed=$indexOfPerekBeingDisplayed\n" +
                    "mishnaBeingDisplayed=$mishnaBeingDisplayed")
            callbackListener.onMishnahPicked(
                adapterPosition,
                startSelected,
                masechtaBeingDisplayed,
                indexOfMasechtaBeingDisplayed,
                (indexOfPerekBeingDisplayed + 1).toString(),
                indexOfPerekBeingDisplayed,
                mishnaBeingDisplayed
            )
            dismiss()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
       //</editor-fold>
    }

    private fun setAdapter(
        dropDownMenu: AutoCompleteTextView,
        filterConditions: List<String>
    ) {
//        val hint = filterConditions[0] // set default as first filter criterion
//        dropDownMenu.setText(hint, false)
        /*if(filterConditions === Sedarim *//*points to the same object*//*) {
            sederBeingDisplayed = hint
            Log.d(TAG, "sederBeingDisplayed = $sederBeingDisplayed")
        } else {
        */
//            Log.d(TAG, "masechtaBeingDisplayed = $masechtaBeingDisplayed")
//        }
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
