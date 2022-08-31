package smu.app.nunsong_market.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import smu.app.nunsong_market.*
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.databinding.FragmentTestBinding
import smu.app.nunsong_market.viewmodel.HomeViewModel


class TestFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: FragmentTestBinding
    private lateinit var adapter: FragmentPagerItemAdapter

    //데이터 배열
    var productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "HomeFragment - onAttach() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // status bar 색변경
        val mWindow = requireActivity().window
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        mWindow.statusBarColor = ContextCompat.getColor(requireActivity(),R.color.main_blue)
        //status bar 글자색 하얗게 만듬
        mWindow.decorView.systemUiVisibility = 0


        Log.d(TAG, "HomeFragment - onCreateView() called")
        binding = FragmentTestBinding.inflate(inflater, container, false)

        adapter = FragmentPagerItemAdapter(fragmentManager, FragmentPagerItems.with(requireContext())
            .add("홈",HomeFragment::class.java)
            .add("의류",MessageFragment::class.java)
            .add("전자기기",HomeFragment::class.java)
            .add("중고도서",MessageFragment::class.java)
            .add("기타",HomeFragment::class.java)
            .create())

        val viewPager = binding.viewpager
        viewPager.adapter = adapter

        val viewPagerTab = binding.viewpagertab
        viewPagerTab.setViewPager(viewPager)

        return binding.root // return layout
    }

    companion object {
        const val TAG: String = "HomeFragment"

        fun newInstance(): TestFragment {
            return TestFragment()
        }
    }
}