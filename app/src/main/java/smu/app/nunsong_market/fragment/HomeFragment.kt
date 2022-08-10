package smu.app.nunsong_market.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ContextUtils.getActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smu.app.nunsong_market.*
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.model.Product
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.FragmentHomeBinding
import smu.app.nunsong_market.model.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: FragmentHomeBinding
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
        Log.d(TAG, "HomeFragment - onCreateView() called")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initRecyclerView()
        initCategory()

        if (productList.isEmpty()) {
            //binding.swipeRefresh.isRefreshing = true
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
            binding.swipeRefresh.isRefreshing = false
        }
        return binding.root // return layout
    }

    private fun initCategory() {
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

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}