package com.example.bookappkotlin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()
        configDialog()

        Handler().postDelayed({
            progressDialog.setMessage("Checking user...")
            progressDialog.show()
            checkUser()
        }, 2000)
    }

    private fun checkUser() {
        if (firebaseAuth.currentUser == null) {
            progressDialog.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val firebaseUser = firebaseAuth.currentUser!!
            val ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        progressDialog.dismiss()
                        val userType = snapshot.child("type").value
                        if (userType == "user") {
                            startActivity(Intent(this@SplashActivity, DashboardUserActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this@SplashActivity, DashboardAdminActivity::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        progressDialog.dismiss()
                        Toast.makeText(this@SplashActivity, "Failure duo to ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun configDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
    }
}