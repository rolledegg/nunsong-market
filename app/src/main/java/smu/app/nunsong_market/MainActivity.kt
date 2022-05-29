package smu.app.nunsong_market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import smu.app.nunsong_market.databinding.ActivityMainBinding
import smu.app.nunsong_market.fragment.MessageFragment
import smu.app.nunsong_market.fragment.HomeFragment
import smu.app.nunsong_market.fragment.MyPageFragment

class MainActivity : AppCompatActivity() {

    // 멤버 변수 선언
    private  lateinit var binding: ActivityMainBinding

    private val  bottomNavigationView: BottomNavigationView by lazy {
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
        supportFragmentManager.beginTransaction().add(R.id.container,homeFragment).commit()


        bottomNavigationView.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.page_home->{
                    supportFragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit()

                    true
                }
                R.id.page_post->{
                    val intent = Intent(this,PublishActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.page_chatting->{
                    supportFragmentManager.beginTransaction().replace(R.id.container,chattingFragment).commit()

                    true
                }
                R.id.page_mypage->{
                    supportFragmentManager.beginTransaction().replace(R.id.container,myPageFragment).commit()
                    true
                }
                else -> false
            }
        }
    }
}