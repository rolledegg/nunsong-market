package smu.app.nunsong_market.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import smu.app.nunsong_market.*
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.databinding.FragmentHomeBinding
import smu.app.nunsong_market.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ProductAdapter

    //데이터 배열
    var productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(AllProductFragment.TAG, "HomeFragment - onCreate() called")
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.load()
        ArticleActivity.from_where= HOME_FRAGMENT

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(AllProductFragment.TAG, "HomeFragment - onAttach() called")
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initRecyclerView()
        initCategory()
        configSearchLayout()
        configPromiseBtn()

        if (productList.isEmpty()) {
            //binding.swipeRefresh.isRefreshing = true
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
            binding.swipeRefresh.isRefreshing = false
        }
        return binding.root // return layout
    }

    private fun configPromiseBtn() {
        binding.promiseIbtn.setOnClickListener {
            Log.d(TAG, "onCreateView: recyclerview clicked")
            val intent = Intent(requireContext(), PromiseListActivity::class.java).apply{
//                putExtra("type",0)
//                putExtra("title", "내가 쓴 글")
            }
            startActivity(intent)
        }
    }

    private fun configSearchLayout() {
        binding.searchEtv.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java).apply {
                // putExtra()
            }
            startActivity(intent)
        }

        binding.searchIbtn.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java).apply {
                // putExtra()
            }
            startActivity(intent)
        }
    }

     private fun initCategory() {
         binding.allLayout.setOnClickListener {
             val intent = Intent(requireContext(), ArticleListActivity::class.java).apply {
                 putExtra("type", 1)
                 putExtra("title", "의류")
                 putExtra("value", "CLOTHES")
             }
             startActivity(intent)
         }
         binding.clothLayout.setOnClickListener() {
             Log.d(TAG, "initCategory: category clicked")
             val intent = Intent(requireContext(), ArticleListActivity::class.java).apply {
                 putExtra("type", 1)
                 putExtra("title", "의류")
                 putExtra("value", "CLOTHES")
             }
             startActivity(intent)
         }
         binding.electronicsLayout.setOnClickListener() {
             Log.d(TAG, "initCategory: category clicked")
             val intent = Intent(requireContext(), ArticleListActivity::class.java).apply {
                 putExtra("type", 1)
                 putExtra("title", "전자기기")
                 putExtra("value", "ELECTRONICS")
             }
             startActivity(intent)
         }
         binding.bookLayout.setOnClickListener() {
             Log.d(TAG, "initCategory: category clicked")
             val intent = Intent(requireContext(), ArticleListActivity::class.java).apply {
                 putExtra("type", 1)
                 putExtra("title", "중고도서")
                 putExtra("value", "BOOKS")
             }
             startActivity(intent)
         }
         binding.etcLayout.setOnClickListener() {
             Log.d(TAG, "initCategory: category clicked")
             val intent = Intent(requireContext(), ArticleListActivity::class.java).apply {
                 putExtra("type", 1)
                 putExtra("title", "기타")
                 putExtra("value", "ETC")
             }
             startActivity(intent)
         }
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.articleList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    fun initRecyclerView() {
        adapter = ProductAdapter(requireActivity())

        adapter.submitList(this.productList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = adapter
    }


    companion object {
        const val TAG: String = "HomeFragment"
        private const val HOME_FRAGMENT = 100

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}