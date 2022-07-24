package smu.app.nunsong_market.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smu.app.nunsong_market.R
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.fragment.HomeFragment

class CategoryArtclesViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG: String = "ARTICLE_LIST"
    }
    val articleList: MutableLiveData<List<Product>> = MutableLiveData()
    val isLoading = MutableLiveData(false)

    val retrofit = Retrofit.Builder()
        .baseUrl(application.getString(R.string.baseUrl))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productApi = retrofit.create(ProductApi::class.java)

    fun load(category: String) {
        if (isLoading.value == true) return
        isLoading.postValue(true)

        productApi.getProductsByCategory(category)
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(HomeFragment.TAG, "NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d(HomeFragment.TAG, it.toString())
                        it.forEach { product -> //위에도 it이 있으니 헷갈리니까 변수 명명
                            Log.d(HomeFragment.TAG, product.toString())
                        }
                        val array = arrayListOf<Product>()
                        // 초기화를 위해 빈 리스트 넣기
                        // 무한으로 덧붙여지는 걸 방지하기 위해
                        articleList.postValue(array)
                        articleList.postValue(it)
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e(HomeFragment.TAG, t.toString())
                }

            })
        isLoading.postValue(false)
    }
}