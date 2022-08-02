package smu.app.nunsong_market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.databinding.ActivityArticleListBinding
import smu.app.nunsong_market.databinding.ActivityCategoryArtclesBinding
import smu.app.nunsong_market.model.ArticleListViewModel
import smu.app.nunsong_market.model.CategoryArtclesViewModel
import smu.app.nunsong_market.model.Product

class CategoryArtclesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryArtclesBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: CategoryArtclesViewModel

    var myProductList = ArrayList<Product>()

    companion object {
        const val TAG = "CATEGORY_ARTICLES_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryArtclesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: onCreate()")

        val category = intent.getStringExtra("category").toString()
        Log.d(TAG, "onCreate: $category")

        viewModel = ViewModelProvider(this).get(CategoryArtclesViewModel::class.java)
        viewModel.load(category)
        initRecyclerView()

        intent.getStringExtra("title")
        binding.mainTitleTv.text = intent.getStringExtra("title")
        Log.d(TAG, "onCreate: $title")

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load(category)
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.articleList.observe(this) {
            adapter.submitList(it)
        }
    }

    fun initRecyclerView() {
        adapter = ProductAdapter(this
         /*   , clickListener = {
            Log.d(TAG, "initRecyclerView: recyclerview clicked")
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }*/
        )

        adapter.submitList(this.myProductList)
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.articleRecyclerView.adapter = adapter
        Log.d(TAG, "initRecyclerView: init")
    }
}