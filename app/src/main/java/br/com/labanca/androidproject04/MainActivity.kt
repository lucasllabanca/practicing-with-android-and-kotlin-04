package br.com.labanca.androidproject04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            this.onSignInResult(res)
        } //this launcher call an activity and awaits for its result to continue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) //this uses the json config from firebase
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) { //user might be authenticated or might not
            val name = user.displayName
            val email = user.email
            setContentView(R.layout.activity_main)
        } else {
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder() //creates the login intent
                .setAvailableProviders(providers) //with google auth provider
                .build()
            signInLauncher.launch(signInIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu) //defines that our resource menu file is the menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_sign_out -> { //case the sign-out id button is clicked
                AuthUI.getInstance() //gets the firebase auth instance
                    .signOut(this)
                    .addOnCompleteListener { //listen to the logout event to finish
                        this.recreate() //recreates this view
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) { //if response ok
            val user = FirebaseAuth.getInstance().currentUser //gets the authenticated user
            setContentView(R.layout.activity_main)
        } else {
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
        }
    }
}
