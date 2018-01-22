package com.taskail.googleplacessearchdialog

import android.widget.Filter
import com.google.android.gms.location.places.AutocompletePrediction
import java.util.ArrayList

/**
 *Created by ed on 1/21/18.
 */
class SimpleSearchFilter(private var resultList: ArrayList<AutocompletePrediction>,
                         private val placesPrediction: GoogleAutoCompletePredictions) : Filter() {

    override fun performFiltering(p0: CharSequence?): FilterResults {
        val results = FilterResults()

        if (p0 != null) {
            // Query the autocomplete API for the (constraint) search string.

            if (placesPrediction.getAutocompletePredictions(p0) != null) {
                resultList = placesPrediction.getAutocompletePredictions(p0)!!
                // The API successfully returned results.
                results.values = resultList
                results.count = resultList.size
            }
        }

        return results
    }

    override fun publishResults(p0: CharSequence?, p1: FilterResults?) {

    }

    override fun convertResultToString(resultValue: Any?): CharSequence {

        return if (resultValue is AutocompletePrediction) {
            resultValue.getFullText(null)
        } else {
            super.convertResultToString(resultValue)
        }
    }
}