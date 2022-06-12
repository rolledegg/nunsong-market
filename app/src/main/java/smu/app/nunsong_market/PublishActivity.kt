package smu.app.nunsong_market

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.ULocale
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.ActivityPublishBinding
import smu.app.nunsong_market.fragment.HomeFragment
import smu.app.nunsong_market.fragment.MyPageFragment
import smu.app.nunsong_market.model.Product
import smu.app.nunsong_market.util.RealPathUtil
import java.io.File
import java.util.jar.Manifest

class PublishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublishBinding
    private lateinit var path: String
    private lateinit var imageFile: File
//    private lateinit var  title: EditText
//    private lateinit var  price: EditText
//    private lateinit var  description: EditText
//    private lateinit var  category: Spinner

    private val categorySpinner: Spinner by lazy {
        binding.categorySpinner
    }
    private val exitBtn: ImageButton by lazy {
        binding.exitBtn
    }

    companion object {
        private const val TAG = "PUBLISH_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: onCreate")

        //retrofit configure
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.noonsongmarket.com:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productApi = retrofit.create(ProductApi::class.java)

        //image view click
        binding.cameraBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(intent, 10)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }


        // TODO: 등록완료하면 홈 액티비티로 돌아가기 (네비게이션 바도 홈으로 클릭외어있어야함)
        binding.publishBtn.setOnClickListener {
            val title = binding.titleEt.text.toString()
            val price = binding.priceEt.text.toString().toInt()
            val category = binding.categorySpinner.selectedItem
            val description = binding.discriptionEt.text.toString()
            Log.d(
                TAG,
                "onCreate: ${title is String} / ${price} / ${category is String} / ${category.toString() is String} / ${description}"
            )
            Log.d(TAG, "onCreate: button clicked ")



            Product(
                id = 10,
                title = title,
                price = price,
                category = category.toString(),
                coverSmallUrl = path,
                description = description,
                sellerName = "계란말이",
                status = "판매중",
                trans = "NOCHOICE",
                date = ""
            )
            val requestImage = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val mutipleImage =
                MultipartBody.Part.createFormData("images", imageFile.name, requestImage)
            val requestTitle = RequestBody.create(MediaType.parse("text/plain"), title)
            val requestPrice = RequestBody.create(MediaType.parse("text/plain"), price.toString())
            val requestCategory =
                RequestBody.create(MediaType.parse("text/plain"), category.toString())
            val requestDescripton = RequestBody.create(MediaType.parse("text/plain"), description)
            val requestSellerName = RequestBody.create(MediaType.parse("text/plain"), "계란말이")
            val requestStatus = RequestBody.create(MediaType.parse("text/plain"), "판매중")
            val requestTrans = RequestBody.create(MediaType.parse("text/plain"), "NOCHOICE")
            /**
            productApi.postImage(mutipleImage,requestTitle,requestPrice,requestCategory,
            requestDescripton,requestSellerName,requestStatus,requestTrans)
             **/
            productApi.postProduct(
                Product(
                    id = 1,
                    title = title,
                    price = price,
                    category = category.toString(),
                    coverSmallUrl = path,
                    description = description,
                    sellerName = "계란말이",
                    status = "판매중",
                    trans = "NOCHOICE",
                    date = ""
                )
            )
                .enqueue(object : Callback<Product> {
                    override fun onResponse(
                        call: Call<Product>,
                        response: Response<Product>
                    ) {
                        if (response.isSuccessful.not()) {
                            //예외처리
                            Log.d(TAG, "onResponse: Not success")
                            return
                        }

                        response.body()?.let {
                            Log.d(TAG, "onResponse: ${it}")
                            Log.d(TAG, "onResponse:" + it.toString())
                        }
                    }

                    override fun onFailure(call: Call<Product>, t: Throwable) {
                        Log.e(TAG, t.toString())
                    }

                })

            this.finish()
        }


        // exit btn
        exitBtn.setOnClickListener {
            this.finish()
        }

        //category spinner
        val categorItems = arrayOf("CLOTHES", "ELECTRONICS", "BOOKS", "ETC")

        //TODO: Add spinner hint
        val categoryAdapter = ArrayAdapter(this, R.layout.item_category, categorItems)
        categorySpinner.adapter = categoryAdapter
        categorySpinner.onItemLongClickListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemLongClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("Not yet implemented")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ): Boolean {
                TODO("Not yet implemented")
            }

        }

        categorySpinner.setSelection(3)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10) {
            val uri: Uri? = data!!.data
            val context = this
            path = RealPathUtil.getRealPath(context, uri)
            Log.d(TAG, "onActivityResult: ${path}")
            val bitmap = BitmapFactory.decodeFile(path)
            binding.productPreviewIv.setImageBitmap(bitmap)
            binding.productPreviewIv.isVisible = true
            imageFile = File(path)

        }
    }
}