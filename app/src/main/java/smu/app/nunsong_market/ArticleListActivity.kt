package smu.app.nunsong_market

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.ActivityArticleListBinding
import smu.app.nunsong_market.model.ArticleListViewModel
import smu.app.nunsong_market.model.HomeViewModel
import smu.app.nunsong_market.model.Product

class ArticleListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleListBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ArticleListViewModel

    //데이터 배열
    var myProductList = ArrayList<Product>()

    companion object {
        private const val TAG = "ARTICLE_LIST_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: onCreate()")

        // TODO: inten세어 타티틀 받아와서 동적으로 매칭
        viewModel = ViewModelProvider(this).get(ArticleListViewModel::class.java)
        viewModel.load()
        initRecyclerView()

//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://www.noonsongmarket.com:8080")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val productApi = retrofit.create(ProductApi::class.java)

        intent.getStringExtra("title")
        binding.mainTitleTv.text =intent.getStringExtra("title")

        //TODO: 유저네임 구글에서 가져와서 동적으로 넣어줘야함
//        productApi.getMyProducts("계란말이")
//            .enqueue(object : Callback<List<Product>> {
//                override fun onResponse(
//                    call: Call<List<Product>>,
//                    response: Response<List<Product>>
//                ) {
//                    if (response.isSuccessful.not()) {
//                        //예외처리
//                        Log.d(TAG, "NOT SUCCESS")
//                        return
//                    }
//
//                    response.body()?.let {
//                        it.forEach { product ->
//                            Log.d(TAG, product.toString())
//                            myProductList.add(product)
//                        }
//                        adapter.submitList(myProductList)
//                        Log.d(TAG, "onResponse: ${myProductList.isEmpty()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
//                    Log.e(TAG, t.toString())
//                }
//
//            })


        Log.d(TAG, "onCreate: ${myProductList.isEmpty()}")

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
            binding.swipeRefresh.isRefreshing=false
        }
        viewModel.articleList.observe(this){
            adapter.submitList(it)
        }

    }

    fun initRecyclerView() {
        adapter = ProductAdapter(this, clickListener = {
            Log.d(TAG, "initRecyclerView: recyclerview clicked")
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        })

        adapter.submitList(this.myProductList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.articleRecyclerView.adapter = adapter
    }
}