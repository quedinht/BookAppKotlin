package com.example.bookappkotlin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookappkotlin.databinding.ActivityAddCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)



        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.submitCategoryButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        category = binding.categoryNameEditText.text.toString().trim()
        if (category.isEmpty()) {
            Toast.makeText(this, "Please input category...", Toast.LENGTH_SHORT).show()
        } else {
            progressDialog.setMessage("Please wait...")
            progressDialog.show()

            var timeCreate = System.currentTimeMillis()
            var uid = FirebaseAuth.getInstance().uid!!
            var hashMap = HashMap<String, Any>()
            hashMap["id"] = timeCreate.toString()
            hashMap["name"] = category
            hashMap["uid"] = uid
            hashMap["timeCreate"] = timeCreate

            val  ref = FirebaseDatabase.getInstance().getReference("Categories")
            ref.child("$timeCreate")
                .setValue(hashMap)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Added category success...", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failure create category duo to ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}