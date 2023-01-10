package com.example.bookappkotlin

import android.widget.Filter
import com.example.bookappkotlin.adapter.AdapterCategory
import com.example.bookappkotlin.model.Category

class FilterCategory: Filter {

    // arrayList in which we want to search
    private var filterList: ArrayList<Category>
    // adapter in which filter need to be implement
    private var adaterCategory: AdapterCategory

    constructor(filterList: ArrayList<Category>, adaterCategory: AdapterCategory) : super() {
        this.filterList = filterList
        this.adaterCategory = adaterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        // value should not be null and not empty
        if (constraint != null && constraint.isNotEmpty()) {
            // searched value is nor null not empty
            // change to upper case, or lower case to avoid case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels: ArrayList<Category> = ArrayList()
            for (i in 0 until filterList.size) {
                //validate
                if (filterList[i].name.uppercase().contains(constraint)) {
                    // add to filtered list
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        } else {
            // search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }
        return  results // don't miss it
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        // apply filter change
        adaterCategory.categoryArrayList = results.values as ArrayList<Category>

        // notify change
        adaterCategory.notifyDataSetChanged()
    }


}