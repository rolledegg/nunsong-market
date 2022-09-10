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

    val acceptedPromList: MutableLiveData<List<Promise>> = MutableLiveData()
    val requestedPromList: MutableLiveData<List<Promise>> = MutableLiveData()
    val requestPromList: MutableLiveData<List<Promise>> = MutableLiveData()

    val isPromiseLoading = MutableLiveData(false)

    val isAcceptedPromOpen = MutableLiveData(true)
    val isRequestedPromOpen = MutableLiveData(false)
    val isRequestPromOpen = MutableLiveData(false)

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
                        val acceptedProm= arrayListOf<Promise>()
                        val requestedProm= arrayListOf<Promise>()
                        val requestProm= arrayListOf<Promise>()
                        it.forEach { promise ->
                            //위에도 it이 있으니 헷갈리니까 변수 명명
                            if (promise.status == 2) acceptedProm.add(promise)
                            else if (promise.status == 0 && promise.receiverUid == firebaseAuth.uid!!) requestedProm.add(promise)
                            else if ((promise.status == 0 && promise.senderUid == firebaseAuth.uid!!) || (promise.status == 1))
                                requestProm.add(promise)
                        }
                        val empty = arrayListOf<Promise>()
                        // 초기화를 위해 빈 리스트 넣기
                        // 무한으로 덧붙여지는 걸 방지하기 위해
                        acceptedPromList.postValue(empty)
                        acceptedPromList.postValue(acceptedProm)
                        Log.d(TAG, "acceptedPromList: $acceptedProm")

                        requestedPromList.postValue(empty)
                        requestedPromList.postValue(requestedProm)
                        Log.d(TAG, "requestedPromList: $requestedProm")

                        requestPromList.postValue(empty)
                        requestPromList.postValue(requestProm)
                        Log.d(TAG, "requestPromList: $requestProm")

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

    fun clickRequestedProm() {
        if (isRequestedPromOpen.value == true) isRequestedPromOpen.postValue(false)
        else isRequestedPromOpen.postValue(true)
    }

    fun clickRequestProm() {
        if (isRequestPromOpen.value == true) isRequestPromOpen.postValue(false)
        else isRequestPromOpen.postValue(true)
    }

}