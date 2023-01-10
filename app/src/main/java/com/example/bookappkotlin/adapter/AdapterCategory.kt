package com.example.bookappkotlin.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.bookappkotlin.FilterCategory
import com.example.bookappkotlin.databinding.RowCategoryBinding
import com.example.bookappkotlin.model.Category
import com.google.firebase.database.FirebaseDatabase

class AdapterCategory: RecyclerView.Adapter<AdapterCategory.HolderCategory>, Filterable {

    private val context: Context
    public var categoryArrayList: ArrayList<Category>
    private lateinit var binding: RowCategoryBinding
    private var filterList: ArrayList<Category>

    private var filter: FilterCategory? = null

    constructor(context: Context, categoryArrayList: ArrayList<Category>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }


    // viewHolder class to hold/init UI views for row_category.xml
    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = binding.nameTextView
        var deleteButton: ImageButton = binding.deleteButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val category = categoryArrayList[position]
        holder.nameTextView.text = category.name

        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Do you want to delete this category?")
                .setPositiveButton("OK") { a, d->
                    deleteCategory(category.id)
                }
                .setNegativeButton("Cancel") { a, d->
                    a.dismiss()
                }
                .show()
        }
    }

    private fun deleteCategory(id: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "Failure duo to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterCategory(filterList, this)
        }
        return  filter as FilterCategory
    }
}