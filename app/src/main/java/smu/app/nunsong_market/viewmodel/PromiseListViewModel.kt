package smu.app.nunsong_market.viewmodel

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
import smu.app.nunsong_market.api.PromiseApi
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.dto.Promise
import smu.app.nunsong_market.util.ServiceGenerator

class PromiseListViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG: String = "PROMISE_LIST"
    }

    val articleList: MutableLiveData<List<Promise>> = MutableLiveData()
    val isPromiseLoading = MutableLiveData(false)

    val isAcceptedPromOpen = MutableLiveData(true)
    val isRequestedPromOpen = MutableLiveData(false)
    val isrejectedPromOpen = MutableLiveData(false)

    private lateinit var firebaseAuth: FirebaseAuth
    private val promiseApi by lazy { ServiceGenerator.createService(PromiseApi::class.java) }

    fun loadPromise() {
        if (isPromiseLoading.value == true) return
        isPromiseLoading.postValue(true)

        firebaseAuth = Firebase.auth

        promiseApi.getPromise(firebaseAuth.uid!!)
            .enqueue(object : Callback<List<Promise>> {
                override fun onResponse(
                    call: Call<List<Promise>>,
                    response: Response<List<Promise>>
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
                        val empty = arrayListOf<Promise>()
                        // 초기화를 위해 빈 리스트 넣기
                        // 무한으로 덧붙여지는 걸 방지하기 위해
                        articleList.postValue(empty)
                        articleList.postValue(it)
                    }
                }

                override fun onFailure(call: Call<List<Promise>>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
        isPromiseLoading.postValue(false)
    }

    fun clickAcceptedProm() {
        if (isAcceptedPromOpen.value == true) isAcceptedPromOpen.postValue(false)
        else isAcceptedPromOpen.postValue(true)
    }

    fun deleteProm(promiseId: Long) {
        promiseApi.deletePromise(promiseId)
            .enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    Log.d(PromiseListViewModel.TAG, "onResponse: ..")
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(PromiseListViewModel.TAG, "onResponse: Not success")
                        return
                    }

                    response.body()?.let {
                        Log.d(PromiseListViewModel.TAG, "onResponse: ${it}")
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.e(PromiseListViewModel.TAG, t.toString())
                }


            })

    }

    fun changeStatus(promiseId: Long, status: Int) {
        promiseApi.changePromiseStatus(
            promiseId,
            Promise(
                null,
                0,
                "",
                "",
                "",
                "",
                1,
                "",
                ""
            ),
            status
        )
            .enqueue(object : Callback<Promise> {
                override fun onResponse(call: Call<Promise>, response: Response<Promise>) {
                    Log.d(PromiseListViewModel.TAG, "onResponse: ..")
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(PromiseListViewModel.TAG, "onResponse: Not success")
                        return
                    }

                    response.body()?.let {
                        Log.d(PromiseListViewModel.TAG, "onResponse: ${it}")
                    }
                }

                override fun onFailure(call: Call<Promise>, t: Throwable) {
                    Log.e(PromiseListViewModel.TAG, t.toString())
                }

            })


    }
}