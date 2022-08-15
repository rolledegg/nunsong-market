package smu.app.nunsong_market.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.fragment.HomeFragment
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.util.ServiceGenerator.createService

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG: String = "HomeFragment"
    }
    val articleList: MutableLiveData<List<Product>> = MutableLiveData()
    val isLoading = MutableLiveData(false)

    private val productApi by lazy { createService(ProductApi::class.java) }

    fun load() {
        if (isLoading.value == true) return
        isLoading.postValue(true)

        productApi.getProducts()
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