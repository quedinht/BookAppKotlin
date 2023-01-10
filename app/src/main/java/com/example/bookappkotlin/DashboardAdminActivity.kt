package com.example.bookappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookappkotlin.adapter.AdapterCategory
import com.example.bookappkotlin.databinding.ActivityDashboardAdminBinding
import com.example.bookappkotlin.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth

    //arraylist to hold categories
    private lateinit var categoriesArrayList: ArrayList<Category>
    // adapter
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories()

        // search
        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                println("aaa")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterCategory.filter.filter(s)
                } catch (e: Exception) {

                }
            }

            override fun afterTextChanged(s: Editable?) {
                println("bbb")
            }
        })

        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        binding.addCategoryButton.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }
    }

    private fun loadCategories() {
        // init arrayList
        categoriesArrayList = ArrayList()

        // get all categories from firebase
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear list before add
                categoriesArrayList.clear()
                for (ds in snapshot.children) {
                    // get data as model
                    val model = ds.getValue(Category::class.java)
                    // add to arraylist
                    println("aaa")
                    categoriesArrayList.add(model!!)
                }
                // setup adapter
                adapterCategory = AdapterCategory(this@DashboardAdminActivity, categoriesArrayList)
                // set adapter to recylerview
                binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this@DashboardAdminActivity, LinearLayoutManager.VERTICAL, false)
                binding.categoryRecyclerView.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun checkUser() {
        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val firebaseUser = firebaseAuth.currentUser!!
            binding.emailTextView.setText(firebaseUser.email)
        }
    }
}