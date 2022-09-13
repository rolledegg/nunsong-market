package smu.app.nunsong_market

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import smu.app.nunsong_market.databinding.ActivityMainBinding
import smu.app.nunsong_market.fragment.HomeFragment
import smu.app.nunsong_market.fragment.MessageFragment
import smu.app.nunsong_market.fragment.MyPageFragment
import smu.app.nunsong_market.fragment.TestFragment


class MainActivity : AppCompatActivity() {

    // 멤버 변수 선언
    private lateinit var binding: ActivityMainBinding

    private val bottomNavigationView: BottomNavigationView by lazy {
        binding.bottomNavigation
    }
    private val homeFragment: HomeFragment by lazy {
        HomeFragment.newInstance()
    }
    private val chattingFragment: MessageFragment by lazy {
        MessageFragment.newInstance()
    }
    private val myPageFragment: MyPageFragment by lazy {
        MyPageFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //for test
//        val intent = Intent(this,LoginActivity::class.java)
//        startActivity(intent)

        // default fragment

        //TODO: load() 함수를 만들어거나 intent flag를 통해서 startActivity를 하면 아예 액티비티가 초기화 되도록 해야함
        // 왜냐하면 작성 액티비지에서 돌아왔을 때 버튼 초기화가 안되고 프레그먼트도 홈 프레그먼트가 안띄워져있음.
        supportFragmentManager.beginTransaction().add(R.id.container, homeFragment).commit()


        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment)
                        .commit()

                    true
                }
                R.id.page_post -> {
                    //이렇게 하면 탭바는 돌어와도 안바뀌고, 다른 페이지에서 작성탭 클릭하면 홈 프레그먼트로 변경된 뒤에 액티비티가 띄워지는게 보임
                    //supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()
                    val intent = Intent(this, PublishActivity::class.java).apply {
                        putExtra("mode", 0)
                    }
                    startActivity(intent)
                    true
                }
                R.id.page_chatting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, chattingFragment).commit()

                    true
                }
                R.id.page_mypage -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, myPageFragment).commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG", "onActivityResult: ...")
        this.recreate()

    }
}