package com.taskail.googleplacessearchdialog

import android.widget.Filter
import com.google.android.gms.location.places.AutocompletePrediction
import java.util.ArrayList

/**
 *Created by ed on 1/21/18.
 */
class SimpleSearchFilter(private var resultList: ArrayList<AutocompletePrediction>,
                         private val placesPrediction: GoogleAutoCompletePredictions,
                         private val callbacks: FilterCallbacks) : Filter() {

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
        if (p1 != null && p1.count > 0) {
            // The API returned at least one result, update the data.
            callbacks.publishResults()
        } else {
            // The API did not return any results, invalidate the data set.
            callbacks.noResultsReturned()
        }
    }

    override fun convertResultToString(resultValue: Any?): CharSequence {

        return if (resultValue is AutocompletePrediction) {
            resultValue.getFullText(null)
        } else {
            super.convertResultToString(resultValue)
        }
    }
}