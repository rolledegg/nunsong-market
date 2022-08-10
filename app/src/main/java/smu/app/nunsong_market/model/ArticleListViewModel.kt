package smu.app.nunsong_market.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.util.ServiceGenerator

class ArticleListViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG: String = "ARTICLE_LIST"
    }

    val articleList: MutableLiveData<List<Product>> = MutableLiveData()
    val isMyArticleLoading = MutableLiveData(false)
    val isCategoryLoading = MutableLiveData(false)
    val isSearchLoading = MutableLiveData(false)

    private lateinit var firebaseAuth: FirebaseAuth
    private val productApi by lazy { ServiceGenerator.createService(ProductApi::class.java) }

    fun loadMyArticle() {
        if (isMyArticleLoading.value == true) return
        isMyArticleLoading.postValue(true)

        firebaseAuth = Firebase.auth
        val sellerName = firebaseAuth.currentUser!!.email.toString().split("@")[0]

        productApi.getMyProducts(sellerName)
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
                        Log.d(TAG, it.toString())
                        it.forEach { product -> //위에도 it이 있으니 헷갈리니까 변수 명명
                            Log.d(TAG, product.toString())
                        }
                        val array = arrayListOf<Product>()
                        // 초기화를 위해 빈 리스트 넣기
                        // 무한으로 덧붙여지는 걸 방지하기 위해
                        articleList.postValue(array)
                        articleList.postValue(it)
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
        isMyArticleLoading.postValue(false)
    }

    fun laodCategoryArticle(value: String) {
        if (isCategoryLoading.value == true) return
        isCategoryLoading.postValue(true)

        productApi.getProductsByCategory(value)
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
                        Log.d(TAG, it.toString())
                        it.forEach { product -> //위에도 it이 있으니 헷갈리니까 변수 명명
                            Log.d(TAG, product.toString())
                        }
                        val array = arrayListOf<Product>()
                        // 초기화를 위해 빈 리스트 넣기
                        // 무한으로 덧붙여지는 걸 방지하기 위해
                        articleList.postValue(array)
                        articleList.postValue(it)
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
        isCategoryLoading.postValue(false)

    }

    fun loadSearchArticle(value: String) {

    }
}