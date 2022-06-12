package smu.app.nunsong_market.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smu.app.nunsong_market.ArticleActivity
import smu.app.nunsong_market.PublishActivity
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.model.Product
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.FragmentHomeBinding
import smu.app.nunsong_market.model.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var viewModel:HomeViewModel

    private lateinit var binding:FragmentHomeBinding
    private lateinit var adapter: ProductAdapter

    //데이터 배열
    var productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"HomeFragment - onCreate() called")
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.load()

//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://www.noonsongmarket.com:8080")
////            .baseUrl("https://fakestoreapi.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

//        val productApi = retrofit.create(ProductApi::class.java)
//        if(productList.isEmpty()) {
//            Log.d(TAG, "onCreate: first loading")
//            productApi.getProducts()
//                .enqueue(object : Callback<List<Product>> {
//                    override fun onResponse(
//                        call: Call<List<Product>>,
//                        response: Response<List<Product>>
//                    ) {
//                        if (response.isSuccessful.not()) {
//                            //예외처리
//                            Log.d(TAG, "NOT SUCCESS")
//                            return
//                        }
//
//                        response.body()?.let {
//                            it.forEach { product ->
//                                Log.d(TAG, product.toString())
//                                productList.add(product)
//                            }
//                            adapter.submitList(productList)
//                            Log.d(TAG, "onResponse:list size ${productList.size}")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {
//                        Log.e(TAG, t.toString())
//                    }
//
//                })
//        }
//        else{
//            Log.d(TAG, "onCreate: Reloading")
//            productList.clear()
//            productApi.getProducts()
//                .enqueue(object : Callback<List<Product>> {
//                    override fun onResponse(
//                        call: Call<List<Product>>,
//                        response: Response<List<Product>>
//                    ) {
//                        if (response.isSuccessful.not()) {
//                            //예외처리
//                            Log.d(TAG, "NOT SUCCESS")
//                            return
//                        }
//
//                        response.body()?.let {
//                            it.forEach { product ->
//                                Log.d(TAG, product.toString())
//                                productList.add(product)
//                            }
//                            adapter.submitList(productList)
//                            Log.d(TAG, "onResponse:list size ${productList.size}")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {
//                        Log.e(TAG, t.toString())
//                    }
//
//                })
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG,"HomeFragment - onAttach() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"HomeFragment - onCreateView() called")
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        initRecyclerView()
        if(productList.isEmpty()){
            //binding.swipeRefresh.isRefreshing = true
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
        }
        return binding.root // return layout
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.articleList.observe(viewLifecycleOwner){
            adapter.submitList(it)
            binding.swipeRefresh.isRefreshing=false
        }
    }

    fun initRecyclerView(){
        adapter = ProductAdapter(requireActivity(),clickListener= {
            Log.d(TAG, "initBookRecyclerView: recyclerview clicked")
            val intent = Intent(requireContext(), ArticleActivity::class.java)
            startActivity(intent)
        })

        adapter.submitList(this.productList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = adapter
    }


    companion object {
        const val TAG: String = "HomeFragment"

        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }
}