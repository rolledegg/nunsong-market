package smu.app.nunsong_market

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.ActivityArticleListBinding
import smu.app.nunsong_market.model.Product

class ArticleListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleListBinding
    private lateinit var adapter: ProductAdapter

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
        initRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.noonsongmarket.com:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productApi = retrofit.create(ProductApi::class.java)

        binding.mainTitleTv.text ="내가 쓴 글"

        //TODO: 유저네임 구글에서 가져와서 동적으로 넣어줘야함
        productApi.getMyProducts("계란말이")
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(TAG, "NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        it.forEach { product ->
                            Log.d(TAG, product.toString())
                            myProductList.add(product)
                        }
                        adapter.submitList(myProductList)
                        Log.d(TAG, "onResponse: ${myProductList.isEmpty()}")
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })


        Log.d(TAG, "onCreate: ${myProductList.isEmpty()}")

        if (myProductList.isEmpty()) {
            Log.d(TAG, "onCreate: finish")
            //this.recreate()
//            finish();
            //overridePendingTransition(0, 0);
//            startActivity(getIntent());
            //overridePendingTransition(0, 0);
        }


    }

    fun initRecyclerView() {
        adapter = ProductAdapter(this, clickListener = {
            Log.d(TAG, "initRecyclerView: recyclerview clicked")
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        })

        val product = Product(id =10,title = "title",price = 20000,category = "ETC",coverSmallUrl = ""
            ,description = ",,,",sellerName = "계란말이",status ="판매중", trans="NOCHOICE",date ="2022년 06월 02일 19시 21분 47초")
        myProductList.add(product)
        adapter.submitList(this.myProductList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.articleRecyclerView.adapter = adapter
    }
}