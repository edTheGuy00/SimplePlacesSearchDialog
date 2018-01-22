package com.taskail.googleplacessearchdialog

import android.widget.Filter
import com.google.android.gms.location.places.AutocompletePrediction

/**
 *Created by ed on 1/21/18.
 */
class SimpleSearchFilter : Filter() {

    override fun performFiltering(p0: CharSequence?): FilterResults {
        val results = FilterResults()

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