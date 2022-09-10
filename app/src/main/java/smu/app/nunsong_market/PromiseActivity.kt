package smu.app.nunsong_market

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.api.PromiseApi
import smu.app.nunsong_market.databinding.ActivityPromiseBinding
import smu.app.nunsong_market.dto.Promise
import smu.app.nunsong_market.fragment.TimePickerFragment
import smu.app.nunsong_market.util.ServiceGenerator
import java.util.*


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

    private lateinit var timePicker: TimePickerDialog
    private lateinit var datePicker: DatePickerDialog


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

        configTimePicker()
        configDatePicker()
        configPublishBtnClickListener()
    }

    @SuppressLint("SetTextI18n")
    private fun configDatePicker() {
        binding.dateTv.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val year: Int = cldr.get(Calendar.YEAR)
            val month: Int = cldr.get(Calendar.MONTH)
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            // time picker dialog
            datePicker = DatePickerDialog(this,
                { dp, sYear, sMonth, sDate -> binding.dateTv.setText("${sMonth+1}월 ${sDate}일") }, year, month, day
            )
            datePicker.show()

        }
    }

    private fun configTimePicker() {
        binding.timeTv.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)
            // time picker dialog
            timePicker = TimePickerDialog(this,
                { tp, sHour, sMinute -> binding.timeTv.setText("$sHour:$sMinute") }, hour, minutes, true
            )
            timePicker.show()
            
        }
    }

    private fun configPublishBtnClickListener() {
        binding.publishBtn.setOnClickListener {

            date = binding.dateTv.text.toString()
            time = binding.timeTv.text.toString()
            location = binding.locationEt.text.toString()
            memo = binding.memoEt.text.toString()

            promiseApi.postPromise(
                Promise(
                    null,
                    itemId.toLong(),
                    title,
                    myUid,
                    buyerUid,
                    date + time,
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