package com.example.bookappkotlin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.bookappkotlin.databinding.ActivityMainBinding
import com.example.bookappkotlin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var name = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configFirebase()
        configDialog()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        name = binding.etName.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please input name...", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Please input email...", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email...", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please input password...", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please input confirm password...", Toast.LENGTH_SHORT).show()
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Confirm password doesn't matches with password...", Toast.LENGTH_SHORT).show()
        } else {
            createAccount()
        }
    }

    private fun createAccount() {
        progressDialog.setMessage("Creating account...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failure create account duo to ${e.message}", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
    }

    private fun updateUserInfo() {
        progressDialog.setMessage("Saving user info...")
        var createTime = System.currentTimeMillis()

        var uid = firebaseAuth.uid
        var hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["name"] = name
        hashMap["email"] = email
        hashMap["type"] = "user"
        hashMap["profileImage"] = ""
        hashMap["createTime"] = createTime

        val ref = FirebaseDatabase.getInstance().getReference("Users")
            .child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Saving user info success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardUserActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                Toast.makeText(this, "Failure create account duo to ${e.message}", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
    }

    private fun configFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun configDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
    }
}