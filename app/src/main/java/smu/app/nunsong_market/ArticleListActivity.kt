package smu.app.nunsong_market

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.databinding.ActivityArticleListBinding
import smu.app.nunsong_market.viewmodel.ArticleListViewModel
import smu.app.nunsong_market.dto.Product

class ArticleListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleListBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ArticleListViewModel

    //데이터 배열
    var myProductList = ArrayList<Product>()

    companion object {
        private const val TAG = "ARTICLE_LIST_ACTIVITY"
        private const val MY_ARTICLE = 0
        private const val CATEGORY = 1
        private const val SEARCH = 2

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: onCreate()")

        val type = intent.getIntExtra("type",-1)
        val title = intent.getStringExtra("title")
        val value = intent.getStringExtra("value")

        binding.mainTitleTv.text =title

        viewModel = ViewModelProvider(this).get(ArticleListViewModel::class.java)

        load(type, value)

        initRecyclerView()

        Log.d(TAG, "onCreate: ${myProductList.isEmpty()}")

        binding.backBtn.setOnClickListener {
            this.finish()
        }

        binding.swipeRefresh.setOnRefreshListener {
            load(type, value)
            binding.swipeRefresh.isRefreshing=false
        }

        viewModel.articleList.observe(this){
            adapter.submitList(it)
        }

    }

    fun initRecyclerView() {
        adapter = ProductAdapter(this)
        adapter.submitList(this.myProductList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.articleRecyclerView.adapter = adapter
    }

    fun load(type:Int, value:String?){
        when(type){
            MY_ARTICLE -> viewModel.loadMyArticle()
            CATEGORY -> viewModel.laodCategoryArticle(value!!)
            SEARCH -> viewModel.loadSearchArticle(value!!)
        }
    }
}