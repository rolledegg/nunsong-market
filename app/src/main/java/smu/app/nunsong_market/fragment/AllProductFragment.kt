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
import smu.app.nunsong_market.*
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.databinding.FragmentAllProductBinding
import smu.app.nunsong_market.dto.Product

import smu.app.nunsong_market.viewmodel.HomeViewModel


class AllProductFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: FragmentAllProductBinding
    private lateinit var adapter: ProductAdapter

    //데이터 배열
    var productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.load()

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
        binding = FragmentAllProductBinding.inflate(inflater, container, false)
        initRecyclerView()
//        initCategory()
        configSearchLayout()

        if (productList.isEmpty()) {
            //binding.swipeRefresh.isRefreshing = true
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
            binding.swipeRefresh.isRefreshing = false
        }
        return binding.root // return layout
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

   /* private fun initCategory() {
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
    }*/

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

        fun newInstance(): AllProductFragment {
            return AllProductFragment()
        }
    }
}