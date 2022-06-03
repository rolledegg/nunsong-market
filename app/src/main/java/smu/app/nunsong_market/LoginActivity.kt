package smu.app.nunsong_market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import smu.app.nunsong_market.databinding.ActivityLoginBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var googleSignInClient: GoogleSignInClient? = null

    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure the Google signin
        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() //
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //Google SignIn bun, Click to begin google sign
        binding.googleBtn.setOnClickListener{
            Log.d(TAG,"onCreate: begin Google SignIn clicked")
            val Intent = googleSignInClient?.signInIntent
            startActivityForResult(Intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser !=null){
            //user is logged in
            //start main activity
            Log.d(TAG, "checkUser: already Logged in")
            Log.d(TAG, "checkUser: ${firebaseUser?.email}")
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accoutTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accoutTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                Log.d(TAG, "onActivityResult: Google sign in failed")
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //login succes
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                //get loggedIn user
                val firebaseUser = firebaseAuth.currentUser
                //get User info
                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email
                val id_domain = email.toString().split("@")
                val domain = id_domain[1]


                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid:${uid}")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email:${email}")

                //check if user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser) {
                    // user is new = Account created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: [Account created] ${email}")
                    Toast.makeText(this, "Account created... ${email}", Toast.LENGTH_SHORT).show()
                } else {
                    //existing user - LoggedIn
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: [Existing user] ${email}")
                    Toast.makeText(this, "LoggedIn... ${email}", Toast.LENGTH_SHORT).show()
                }

                //start main activity
                if(domain == "sookmyung.ac.kr"){
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: ${domain}")
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    finish()
                }
                else {
                    firebaseUser.delete()
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: not a sookmyuung email delete account")
                    Toast.makeText(this, "your not a sookmyung student", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { e ->
                //login failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(this, "Loggin Failed due to ${e.message}", Toast.LENGTH_SHORT).show()


            }
    }
}