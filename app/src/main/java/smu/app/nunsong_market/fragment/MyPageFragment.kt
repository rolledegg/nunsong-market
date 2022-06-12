package smu.app.nunsong_market.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import smu.app.nunsong_market.ArticleActivity
import smu.app.nunsong_market.ArticleListActivity
import smu.app.nunsong_market.LoginActivity
import smu.app.nunsong_market.R
import smu.app.nunsong_market.databinding.ActivityLoginBinding
import smu.app.nunsong_market.databinding.FragmentMyPageBinding


class MyPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var btn: TextView
    private lateinit var emailTv: TextView
    private lateinit var binding: FragmentMyPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater,container,false)
        init()
        // Inflate the layout for this fragment
        binding.myArticleTv.setOnClickListener(){
            Log.d(TAG, "onCreateView: recyclerview clicked")
            val intent = Intent(requireContext(), ArticleListActivity::class.java)
            intent.putExtra("title","내가 쓴 글")
            startActivity(intent)
        }
        return binding.root
    }

    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        btn = binding.logoutTv
        binding.logoutTv.setOnClickListener {
            Log.d(TAG, "onCreate: logout button clicked")
            firebaseAuth.signOut()
            checkUser()

        }
    }

    private fun checkUser() {
        // get current User
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            // user not logged in
            Log.d(TAG, "checkUser: ${firebaseUser?.email}")
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            //finish main activity
            ActivityCompat.finishAffinity(requireActivity())

        }
        else{
            //user logged in
            //get user info
            val email = firebaseUser.email
            binding.emailTv.text = email
        }
    }

    companion object {
        private  const val RC_SIGN_IN = 110
        private  const val TAG = "GOOGLE_SIGN_IN"

        @JvmStatic
        fun newInstance(param1: String, param2: String) {

            }

        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }
}