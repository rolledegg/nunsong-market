package smu.app.nunsong_market

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.ActivityPublishBinding
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.util.RealPathUtil
import smu.app.nunsong_market.util.ServiceGenerator
import java.io.File

class PublishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublishBinding
    private lateinit var path: String
    private var imageFile: File? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sellerUid: String
    private lateinit var sellerName: String
    private var mode: Int = -1
    private var context = this

    private val categorySpinner: Spinner by lazy {
        binding.categorySpinner
    }
    private val statusSpinner: Spinner by lazy {
        binding.statusSpinner
    }
    private val exitBtn: ImageButton by lazy {
        binding.exitBtn
    }

    //retrofit configure
    private val productApi by lazy { ServiceGenerator.createService(ProductApi::class.java) }

    companion object {
        private const val TAG = "PUBLISH_ACTIVITY"
        private const val PUBLISH_MODE = 0
        private const val EDIT_MODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: 사진 선택 안해도 보낼 수 있어야함
        binding = ActivityPublishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth

        mode = intent.getIntExtra("mode", -1)


        configDiscriptionEdtFocusChangeListener()
        configCategorySpinner()


        if (mode == EDIT_MODE) {
            val itemId = intent.getIntExtra("id", -1)
            configStatusSpinner()
            uiUpdate(itemId)
            configEditBtnClickLisener(itemId)
        }

        configCameraBtnClickLisener()
        configPublishBtnClickLisener()

        // exit btn
        exitBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            this.finish()
        }
    }


    private fun uiUpdate(itemId: Int) {
        binding.mainTv.text = intent.getStringExtra("title")
        binding.statusSpinner.visibility = View.VISIBLE
        binding.publishBtn.visibility = View.INVISIBLE
        binding.editBtn.visibility = View.VISIBLE

        setArticleInfo(itemId)
    }

    private fun setArticleInfo(itemId: Int) {
        productApi.getProductById(itemId)
            .enqueue(object : Callback<Product> {
                override fun onResponse(
                    call: Call<Product>,
                    response: Response<Product>
                ) {
                    Log.d(TAG, "onResponse: ..")
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(TAG, "onResponse: Not success")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG, "onResponse: ${it}")
                        Log.d(TAG, "onResponse:" + it.toString())

                        sellerName = it.sellerName
                        sellerUid = it.sellerUid
//                        //set article
                        binding.productPreviewIv.isVisible = true
                        if (it.coverSmallUrl != null) {
                            Glide
                                .with(binding.productPreviewIv.context)
                                .load(it.coverSmallUrl)
                                .into(binding.productPreviewIv)
                        }
                        binding.titleEt.setText(it.title)
                        binding.priceEt.setText(it.price.toString())
                        binding.discriptionEt.setText(it.description)

                        when(it.category){
                            //순서: "의류", "전자기기", "중고도서", "기타"
                            "CLOTHES" -> binding.categorySpinner.setSelection(0)
                            "ELECTRONICS" -> binding.categorySpinner.setSelection(1)
                            "BOOKS" -> binding.categorySpinner.setSelection(2)
                            "ETC" -> binding.categorySpinner.setSelection(3)
                        }

                        when(it.status){
                            //순서: "판매중", "거래완료"
                            "판매중"-> binding.statusSpinner.setSelection(0)
                            "거래완료"-> binding.statusSpinner.setSelection(1)
                        }
                    }

                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })

    }

    private fun configEditBtnClickLisener(itemId: Int) {
        binding.editBtn.setOnClickListener {
            val title = binding.titleEt.text.toString()
            val price = binding.priceEt.text.toString()
            val category = korToEng(binding.categorySpinner.selectedItem.toString())
            val description = binding.discriptionEt.text.toString()
            val sellerName = firebaseAuth.currentUser!!.email.toString().split("@")[0]
            val sellerUid = firebaseAuth.currentUser!!.uid
            val status = binding.statusSpinner.selectedItem

            if (price.equals("") || description.equals("") || title.equals("")) {
                Toast.makeText(this, " 작성하지 않은 부분이 있습니다.", Toast.LENGTH_SHORT).show()
            } else {
//                val multipleImage: MultipartBody.Part
//                if (imageFile != null) {
                val rImage = RequestBody.create(MediaType.parse("image/*"), imageFile)
                val multipleImage =
                    MultipartBody.Part.createFormData("images", imageFile?.name, rImage)
//                }
                val rTitle = RequestBody.create(MediaType.parse("text/plain"), title)
                val rPrice = RequestBody.create(MediaType.parse("text/plain"), price)
                val rCategory =
                    RequestBody.create(MediaType.parse("text/plain"), category.toString())
                val rDescripton = RequestBody.create(MediaType.parse("text/plain"), description)
                val rSellerName = RequestBody.create(MediaType.parse("text/plain"), sellerName)
                val rSellerUid = RequestBody.create(MediaType.parse("text/plain"), sellerUid)
                val rStatus = RequestBody.create(MediaType.parse("text/plain"), status.toString())
                val rTrans = RequestBody.create(MediaType.parse("text/plain"), "")


                productApi.editProductImage(
                    itemId,
                    rTitle,
                    rPrice,
                    rCategory,
                    rDescripton,
                    rSellerName,
                    rSellerUid,
                    rStatus,
                    rTrans,
                    multipleImage
                )
                    .enqueue(object : Callback<Product> {
                        override fun onResponse(
                            call: Call<Product>,
                            response: Response<Product>
                        ) {
                            Log.d(TAG, "onResponse: ..")
                            if (response.isSuccessful.not()) {
                                //예외처리
                                Log.d(TAG, "onResponse: Not success")
                                return
                            }

                            response.body()?.let {
                                Log.d(TAG, "onResponse: ${it}")
                                Log.d(TAG, "onResponse:" + it.toString())
                            }
                            // 게시글이 등록된 뒤에 종료
                            val intent = Intent(context, ArticleActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                putExtra("id", itemId)
                                putExtra("sellerName",sellerName)
                                putExtra("sellerUid",sellerUid)
                            }
                            startActivity(intent)
                            context.finish()
                        }

                        override fun onFailure(call: Call<Product>, t: Throwable) {
                            Log.e(TAG, t.toString())
                        }

                    })
            }

        }
    }

    private fun configCategorySpinner() {
        //category spinner
        val categoryItems = arrayOf("의류", "전자기기", "중고도서", "기타")

        //TODO: Add spinner hint
        val categoryAdapter = ArrayAdapter(this, R.layout.item_spinner, categoryItems)
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

    private fun configStatusSpinner() {
        //category spinner
        val statusItems = arrayOf("판매중", "거래완료")

        //TODO: Add spinner hint
        val statusAdapter = ArrayAdapter(this, R.layout.item_spinner, statusItems)
        statusSpinner.adapter = statusAdapter
        statusSpinner.onItemLongClickListener = object : AdapterView.OnItemSelectedListener,
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

        categorySpinner.setSelection(0)
    }

    private fun configDiscriptionEdtFocusChangeListener() {
        binding.discriptionEt.setOnFocusChangeListener { view, b ->
            // addOnLayoutChangeListener 적용안하면 키보드떄문에 가려진 작은 레이아웃안에서 가장 마지막으로 가는게 아니라
            // resize된 레이아웃의 마지막으로 가서 맨 밑까지 안내려감
            binding.scrollView.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                if (bottom < oldBottom) {
                    binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }
            }
        }
    }

    private fun configPublishBtnClickLisener() {
        binding.publishBtn.setOnClickListener {
            val title = binding.titleEt.text.toString()
            val price = binding.priceEt.text.toString()
            val category = korToEng(binding.categorySpinner.selectedItem.toString())
            val description = binding.discriptionEt.text.toString()
            val sellerName = firebaseAuth.currentUser!!.email.toString().split("@")[0]
            val sellerUid = firebaseAuth.currentUser!!.uid
            // TODO: RequestBody 만들 때 파일이 null이면 에러뜸
            val rImage = RequestBody.create(MediaType.parse("image/*"), imageFile)
            Log.d(TAG, "configPublishBtnClickLisener: $title/$price/$description")

            if (price.equals("") || description.equals("") || title.equals("")) {
                Toast.makeText(this, " 작성하지 않은 부분이 있습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val multipleImage =
                    MultipartBody.Part.createFormData("images", imageFile?.name, rImage)
                val rTitle = RequestBody.create(MediaType.parse("text/plain"), title)
                val rPrice = RequestBody.create(MediaType.parse("text/plain"), price)
                val rCategory =
                    RequestBody.create(MediaType.parse("text/plain"), category.toString())
                val rDescripton = RequestBody.create(MediaType.parse("text/plain"), description)
                val rSellerName = RequestBody.create(MediaType.parse("text/plain"), sellerName)
                val rSellerUid = RequestBody.create(MediaType.parse("text/plain"), sellerUid)
                val rStatus = RequestBody.create(MediaType.parse("text/plain"), "판매중")
                val rTrans = RequestBody.create(MediaType.parse("text/plain"), "")
//                val multipleImage:MultipartBody.Part?
//                if (imageFile != null){
//                    val rImage = RequestBody.create(MediaType.parse("image/*"), imageFile)
//                    multipleImage =
//                        MultipartBody.Part.createFormData("images", imageFile?.name, rImage)
//
//                } else {multipleImage = null}

                productApi.postProductImage(
                    multipleImage,
                    rTitle,
                    rPrice,
                    rCategory,
                    rDescripton,
                    rSellerName,
                    rSellerUid,
                    rStatus,
                    rTrans
                )
                    .enqueue(object : Callback<Product> {
                        override fun onResponse(
                            call: Call<Product>,
                            response: Response<Product>
                        ) {
                            Log.d(TAG, "onResponse: ..")
                            if (response.isSuccessful.not()) {
                                //예외처리
                                Log.d(TAG, "onResponse: Not success")
                                return
                            }

                            response.body()?.let {
                                Log.d(TAG, "onResponse: ${it}")
                                Log.d(TAG, "onResponse:" + it.toString())
                            }
                            // 게시글이 등록된 뒤에 종료
                            val intent = Intent(context, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                            startActivity(intent)

                            context.finish()
                        }

                        override fun onFailure(call: Call<Product>, t: Throwable) {
                            Log.e(TAG, t.toString())
                        }

                    })
                /*val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                this.finish()*/
            }

        }
    }

    private fun configCameraBtnClickLisener() {
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

    fun korToEng(category: String):String{
        when(category){
            "의류" -> return "CLOTHES"
            "전자기기" -> return "ELECTRONICS"
            "중고도서" -> return "BOOKS"
            else -> return "ETC"
        }
    }
}