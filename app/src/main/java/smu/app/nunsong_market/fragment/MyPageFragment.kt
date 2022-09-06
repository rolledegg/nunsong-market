package smu.app.nunsong_market.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import smu.app.nunsong_market.*
import smu.app.nunsong_market.databinding.ActivityLoginBinding
import smu.app.nunsong_market.databinding.FragmentMyPageBinding


class MyPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var btn: TextView
    private lateinit var emailTv: TextView
    private lateinit var binding: FragmentMyPageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // status bar 색변경
        val mWindow = requireActivity().window
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        mWindow.statusBarColor = ContextCompat.getColor(requireActivity(),R.color.white)
        // status bar 글자 색 까맣게 만듬
        mWindow.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        binding = FragmentMyPageBinding.inflate(inflater,container,false)
        firebaseAuth = Firebase.auth

        checkUser()

        configMyArticleBtn()
        configMyPromiseBtn()
        configLogoutBtn()
        configSignoutBtn()

        return binding.root
    }

    private fun configMyPromiseBtn() {
        binding.myPromiseTv.setOnClickListener() {
            Log.d(TAG, "onCreateView: recyclerview clicked")
            val intent = Intent(requireContext(), PromiseListActivity::class.java).apply{
//                putExtra("type",0)
//                putExtra("title", "내가 쓴 글")
            }
            startActivity(intent)
        }
    }

    private fun configMyArticleBtn() {
        binding.myArticleTv.setOnClickListener() {
            Log.d(TAG, "onCreateView: recyclerview clicked")
            val intent = Intent(requireContext(), ArticleListActivity::class.java).apply{

            }
            startActivity(intent)
        }
    }

    private fun configLogoutBtn() {
        binding.logoutTv.setOnClickListener {
            Log.d(TAG, "onCreate: logout button clicked")
            // firebase에서 로그아웃
            firebaseAuth.signOut()
            // 구글 계정에서 로그아웃
            // 구글에서 로그아웃 안해주면 다음에 로그인할 때앱에 저장되어있는 이전에 로그인했던 계정으로 자동 선택되어 로그인함
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
            googleSignInClient.signOut()
            checkUser()

        }
    }

    private fun configSignoutBtn() {
        binding.deleteAccountTv.setOnClickListener {
            // 계정 삭제
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
            binding.nicknameTv.text = email!!.split("@")[0].toString()
            binding.emailTv.text = email
        }
    }

    companion object {
        private  const val RC_SIGN_IN = 110
        private  const val TAG = "GOOGLE_SIGN_IN"

        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }
}