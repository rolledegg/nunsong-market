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
import smu.app.nunsong_market.util.TimeUtil
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

        binding.exitBtn.setOnClickListener { this.finish() }

        Log.d(
            TAG, "onCreate: ${TimeUtil.formateTime("1:1")}/${TimeUtil.formateTime("13:1")}" +
                    "/${TimeUtil.formateTime("3:32")}/${TimeUtil.formateTime("19:44")}"
        )

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
            datePicker = DatePickerDialog(
                this,
                { dp, sYear, sMonth, sDate ->
                    date = sYear.toString()

                    if (sMonth+1 < 10) date = date + "-0" + (sMonth+ 1)
                    else date = date + "-" + (sMonth+ 1)

                    if (sDate < 10) date = date + "-0" + sDate
                    else date = date + "-" + sDate

                    binding.dateTv.setText("${sYear}년 ${sMonth + 1}월 ${sDate}일")
                }, year, month, day
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
            timePicker = TimePickerDialog(
                this,
                { tp, sHour, sMinute ->
                    if (sHour < 10) time = "0" + sHour
                    else time = sHour.toString()

                    if (sMinute < 10) time = time + ":0" + sMinute
                    else time = time + ":" + sMinute

                    binding.timeTv.setText(TimeUtil.formateTime(time))
                }, hour, minutes, true
            )
            timePicker.show()

        }
    }

    private fun configPublishBtnClickListener() {
        binding.publishBtn.setOnClickListener {

            location = binding.locationEt.text.toString()
            memo = binding.memoEt.text.toString()

            promiseApi.postPromise(
                Promise(
                    null,
                    itemId.toLong(),
                    title,
                    myUid,
                    buyerUid,
                    date +"T"+time,
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