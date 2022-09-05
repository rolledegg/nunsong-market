package smu.app.nunsong_market

import TimePickerFragment
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.api.PromiseApi
import smu.app.nunsong_market.databinding.ActivityPromiseBinding
import smu.app.nunsong_market.databinding.ItemSearchWordBinding
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.dto.Promise
import smu.app.nunsong_market.fragment.HomeFragment
import smu.app.nunsong_market.util.ServiceGenerator

class PromiseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPromiseBinding
    private val promiseApi by lazy { ServiceGenerator.createService(PromiseApi::class.java) }

    private var itemId: Int = -1
    private lateinit var myUid: String
    private lateinit var buyerUid: String
    private lateinit var myName: String
    private lateinit var buyerName: String
    private lateinit var title: String
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var location: String
    private lateinit var memo: String

    private companion object {
        private const val TAG = "PROMISE_ACTIVITY"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemId = intent.getIntExtra("itemId", -1)
        buyerName = intent.getStringExtra("sellerName").toString()
        buyerUid = intent.getStringExtra("sellerUid").toString()
        myName = intent.getStringExtra("myName").toString()
        myUid = intent.getStringExtra("myUid").toString()
        title = intent.getStringExtra("title").toString()

        binding.productTitleTv.text = title

        configPublishBtnClickListener()


        /*   binding.getBtn.setOnClickListener {
               promiseApi.getPromise(myUid)
                   .enqueue(object : Callback<List<Promise>> {
                       override fun onResponse(
                           call: Call<List<Promise>>,
                           response: Response<List<Promise>>
                       ) {
                           Log.d(TAG, "onResponse: ..")
                           if (response.isSuccessful.not()) {
                               //예외처리
                               Log.d(TAG, "onResponse: Not success")
                               return
                           }

                           response.body()?.let {
                               it.forEach { promise -> //위에도 it이 있으니 헷갈리니까 변수 명명
                                   Log.d(TAG, promise.toString())
                               }
                           }
                       }

                       override fun onFailure(call: Call<List<Promise>>, t: Throwable) {
                           Log.e(TAG, t.toString())
                       }

                   })

           }

           binding.putBtn.setOnClickListener {
               promiseApi.changePromise(
                   13.toLong(),
                   Promise(null, itemId.toLong(), title, myUid, buyerUid, "일요일 9시", 0, "순헌관", "현금 거래")
               )
                   .enqueue(object : Callback<Promise> {
                       override fun onResponse(call: Call<Promise>, response: Response<Promise>) {
                           Log.d(TAG, "onResponse: ..")
                           if (response.isSuccessful.not()) {
                               //예외처리
                               Log.d(TAG, "onResponse: Not success")
                               return
                           }

                           response.body()?.let {
                               Log.d(TAG, "onResponse: ${it}")
                           }
                       }

                       override fun onFailure(call: Call<Promise>, t: Throwable) {
                           Log.e(TAG, t.toString())
                       }

                   })

           }

           binding.statusBtn.setOnClickListener {
               promiseApi.changePromiseStatus(
                   13.toLong(),
                   Promise(
                       null,
                       itemId.toLong(),
                       title,
                       myUid,
                       buyerUid,
                       "일요일 15시",
                       0,
                       "순헌관",
                       "현금 거래"
                   ),
                   2
               )
                   .enqueue(object : Callback<Promise> {
                       override fun onResponse(call: Call<Promise>, response: Response<Promise>) {
                           Log.d(TAG, "onResponse: ..")
                           if (response.isSuccessful.not()) {
                               //예외처리
                               Log.d(TAG, "onResponse: Not success")
                               return
                           }

                           response.body()?.let {
                               Log.d(TAG, "onResponse: ${it}")
                           }
                       }

                       override fun onFailure(call: Call<Promise>, t: Throwable) {
                           Log.e(TAG, t.toString())
                       }

                   })

           }

           binding.deleteBtn.setOnClickListener {
               promiseApi.deletePromise(12.toLong())
                   .enqueue(object : Callback<Int> {
                       override fun onResponse(call: Call<Int>, response: Response<Int>) {
                           Log.d(TAG, "onResponse: ..")
                           if (response.isSuccessful.not()) {
                               //예외처리
                               Log.d(TAG, "onResponse: Not success")
                               return
                           }

                           response.body()?.let {
                               Log.d(TAG, "onResponse: ${it}")
                           }
                       }

                       override fun onFailure(call: Call<Int>, t: Throwable) {
                           Log.e(TAG, t.toString())
                       }


                   })

           }*/
    }

    private fun configPublishBtnClickListener() {
        binding.publishBtn.setOnClickListener {

            date = ""
            time = ""
            location = binding.locationEt.text.toString()
            memo = binding.memoEt.text.toString()

            promiseApi.postPromise(
                Promise(
                    null,
                    itemId.toLong(),
                    title,
                    myUid,
                    buyerUid,
                    "일요일 12시",
                    0,
                    location,
                    memo
                )
            )
                .enqueue(object : Callback<Promise> {
                    override fun onResponse(call: Call<Promise>, response: Response<Promise>) {
                        Log.d(TAG, "onResponse: ..")
                        if (response.isSuccessful.not()) {
                            //예외처리
                            Log.d(TAG, "onResponse: Not success")
                            return
                        }

                        response.body()?.let {
                            Log.d(TAG, "onResponse: ${it}")
                        }
                    }

                    override fun onFailure(call: Call<Promise>, t: Throwable) {
                        Log.e(TAG, t.toString())
                    }

                })

            this.finish()

        }
    }

    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }
}