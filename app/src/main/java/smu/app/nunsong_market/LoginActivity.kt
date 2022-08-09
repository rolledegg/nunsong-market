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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.api.UserApi
import smu.app.nunsong_market.databinding.ActivityLoginBinding
import smu.app.nunsong_market.model.Product
import smu.app.nunsong_market.model.User
import smu.app.nunsong_market.util.ServiceGenerator
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInOptions:GoogleSignInOptions
    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var mDbRef: DatabaseReference

    private val userApi by lazy { ServiceGenerator.createService(UserApi::class.java) }

    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //configure the Google signin
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() 
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        firebaseAuth = Firebase.auth

        checkUser()
        configSignInBtnClickListener()
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //user is logged in
            //start main activity
            Log.d(TAG, "checkUser: already Logged in")
            Log.d(TAG, "checkUser: ${firebaseUser?.email}")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun configSignInBtnClickListener() {
        binding.googleBtn.setOnClickListener {
            Log.d(TAG, "onCreate: currentUser: ${firebaseAuth.currentUser}")
            Log.d(TAG, "onCreate: begin Google SignIn clicked")
            val Intent = googleSignInClient?.signInIntent
            startActivityForResult(Intent, RC_SIGN_IN)
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
                val name = firebaseUser!!.displayName
                val email = firebaseUser!!.email
                val domain = email.toString().split("@")[1]

                //check if user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser) {
                    // user is new = Account created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: [Account created] ${email}")
                    Toast.makeText(this, "Account created... ${email}", Toast.LENGTH_SHORT).show()

                    signUp(email, uid)
                } else {
                    //existing user - LoggedIn
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: [Existing user] ${email}")
                    Toast.makeText(this, "LoggedIn... ${email}", Toast.LENGTH_SHORT).show()
                }

                //start main activity
                if (domain == "sookmyung.ac.kr") {
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: ${domain}")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    firebaseAuth.signOut()
                    firebaseUser.delete()
                    googleSignInClient!!.signOut()

                    Log.d(
                        TAG,
                        "firebaseAuthWithGoogleAccount: not a sookmyuung email delete account"
                    )
                    Toast.makeText(this, "your not a sookmyung student", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { e ->
                //login failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(this, "Loggin Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun signUp(email: String?, uid: String) {
        if (email == null) {
            Log.d(TAG, "signUp: email is null")
        } else {
            Log.d(TAG, "signUp: ")
            val userName = email.split("@")[0]
            addUserToDatabase(userName,uid,email)

           //  post new user to server
            userApi.postUser(User(userName, uid, email))
                .enqueue(object: Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        Log.d(TAG, "onResponse: ..")
                        if (response.isSuccessful.not()) {
                            //예외처리
                            Log.d(TAG, "onResponse: Not success")
                            return
                        }

                        response.body()?.let {
                            Log.d(TAG, "onResponse: ${it}")
                            Log.d(TAG, "onResponse:" + it.toString())
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e(TAG, t.toString())
                    }

                })
        }
    }

    private fun addUserToDatabase(userName: String, uid: String, email: String) {
        Log.d(TAG, "addUserToDatabase: ")
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("users").child(uid).setValue(User(userName, uid, email))
    }
}