package com.brookmanholmes.drilltracker.presentation.view.util

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.brookmanholmes.drilltracker.R

class ArrayAdapterNoFilter(context: Context, val items: List<String>)
    : ArrayAdapter<String>(context, R.layout.list_item, items) {

    private val noOpFilter = object : Filter() {
        private val noOpResult = FilterResults()
        override fun performFiltering(constraint: CharSequence?) = noOpResult
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
    }

    override fun getFilter() = noOpFilter


}