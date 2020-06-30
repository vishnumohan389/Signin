package com.example.googlesignin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var providers: List<AuthUI.IdpConfig>
    private val MY_REQUEST_CODE = 1007


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()

            )

        showSignInOption()

        sign_out.setOnClickListener {
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    sign_out.isEnabled = false
                    showSignInOption()
                }
                .addOnFailureListener{
                    e->Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = IdpResponse.fromResultIntent(data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, " "+ user!!.email, Toast.LENGTH_SHORT).show()
                sign_out.isEnabled = true

            }
        } else{

            Toast.makeText(this, ""+response!!.error!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSignInOption() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(), MY_REQUEST_CODE)
    }



}
