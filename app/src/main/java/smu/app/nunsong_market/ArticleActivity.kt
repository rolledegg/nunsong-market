package smu.app.nunsong_market

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.databinding.ActivityArticleBinding
import smu.app.nunsong_market.dto.Keyword
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.util.BuildConfig
import smu.app.nunsong_market.util.ServiceGenerator
import kotlin.math.log


class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var productWebView: WebView
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth
    private lateinit var sellerUid: String
    private lateinit var sellerName: String
    private var context = this

    private val productApi by lazy { ServiceGenerator.createService(ProductApi::class.java) }


    companion object {
        private const val TAG = "ARTICLE_ACTIVITY"
        private const val HOME_FRAGMENT = 100
        private const val MYPAGE_FRAGMENT = 101
        var from_where: Int = 0
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        productWebView = binding.productDetailWv
        progressBar = binding.webviewProgressBar

        val settings: WebSettings = productWebView.getSettings()
        settings.domStorageEnabled = true

        var itemId = intent.getIntExtra("id", -1)
        sellerName = intent.getStringExtra("sellerName").toString()
        sellerUid = intent.getStringExtra("sellerUid").toString()
        Log.d(TAG, "onCreate: id: $itemId name: $sellerName uid: $sellerUid ")

        binding.sellerTv.text = sellerName

        if (itemId == -1) {
            Toast.makeText(this, "Can't get item id", Toast.LENGTH_SHORT)
        } else {
            val loadUrl =
                BuildConfig.PRODUCT_URL + itemId.toString() + "?uid=" + mAuth.currentUser?.uid
            Log.d(TAG, "onCreate: $loadUrl")

            productWebView.apply {
                settings.javaScriptEnabled = true
                webChromeClient = MyWebChromeClient()
                webViewClient = WebViewClient()
                webChromeClient = webChromeClient()
                loadUrl(loadUrl)
            }
            productWebView.addJavascriptInterface(WebAppInterface(this), "android")
        }

        //본인의 게시글일 때
        if (sellerUid == mAuth.currentUser?.uid) {
            binding.chattingBtn.isVisible = false
            binding.editBtn.isVisible = true
            binding.deleteBtn.isVisible = true
        }

        configExitBtn()
        configChattingBtnClickListener(itemId)
        configEditBtnClickListener(itemId)
        configDeleteBtnClickListener(itemId)
    }


    private fun configExitBtn() {
        binding.backBtn.setOnClickListener {
            backToRightActivity()
        }
    }

    private fun configDeleteBtnClickListener(itemId: Int) {
        binding.deleteBtn.setOnClickListener {
            productApi.deleteArticle(itemId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
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

                        when (from_where) {
                            HOME_FRAGMENT -> {
                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                }
                                startActivity(intent)
                            }
                            MYPAGE_FRAGMENT -> {
                                val intent =
                                    Intent(context, ArticleListActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        putExtra("type", 0)
                                        putExtra("title", "내가 쓴 글")
                                    }
                                startActivity(intent)
                            }
                        }

                        context.finish()
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e(TAG, t.toString())
                    }

                })

        }

    }

    private fun configEditBtnClickListener(itemId: Int) {
        binding.editBtn.setOnClickListener {
            val intent = Intent(this, PublishActivity::class.java).apply {
                putExtra("mode", 1)
                putExtra("title", "게시물 수정")
                putExtra("id", itemId)
            }
            startActivity(intent)
        }
    }

    private fun configChattingBtnClickListener(itemId: Int) {
        binding.chattingBtn.setOnClickListener {
            val intent = Intent(this, ChattingActivity::class.java).apply {
                putExtra("id", itemId)
                putExtra("sellerName", sellerName)
                putExtra("sellerUid", sellerUid)
            }
            startActivity(intent)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            backToRightActivity()
            return true
        }
        return false
    }

    private fun backToRightActivity(){
            when (from_where) {
                HOME_FRAGMENT -> {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                }
                MYPAGE_FRAGMENT -> {
                    val intent =
                        Intent(context, ArticleListActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            putExtra("type", 0)
                            putExtra("title", "내가 쓴 글")
                        }
                    startActivity(intent)
                }
            }

            context.finish()
    }

    inner class webChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            // 0~100의 정수로 로딩 정도를 알려준다.
            progressBar.progress = newProgress
        }
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.isVisible = true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.isVisible = false
//            productWebView.loadUrl("javascript:getAndroidUser('" + sellerUid + "')")
            productWebView.evaluateJavascript("javascript:getAndroidUser('" + sellerUid + "')") {
                Log.d(TAG, "onPageFinished: call function")
                Log.d(TAG, "onPageFinished: $it")
            }
        }


    }

    inner class MyWebChromeClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            return super.onConsoleMessage(consoleMessage)
            Log.d(
                TAG,
                "${consoleMessage?.message()},${consoleMessage?.lineNumber()}, ${consoleMessage?.sourceId()}"
            )
        }
    }

    class WebAppInterface(private val mContext: Context) {

        /** Show a toast from the web page  */
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }


}