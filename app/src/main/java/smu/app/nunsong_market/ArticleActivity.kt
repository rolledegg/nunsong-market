package smu.app.nunsong_market

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import smu.app.nunsong_market.databinding.ActivityArticleBinding
import smu.app.nunsong_market.util.BuildConfig
import kotlin.math.log


class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var productWebView: WebView
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth
    private lateinit var sellerUid: String
    private lateinit var sellerName: String


    private companion object {
        private const val TAG = "ARTICLE_ACTIVITY"
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

        //TODO: 본인 게시글은 어떻게 처리 할 것인지
        //본인의 게시글에서는 채팅 버튼이 다르게
        if (sellerUid == mAuth.currentUser?.uid) {
            binding.chattingBtn.text = "본인 게시글임"
        }

        if (itemId == -1) {
            Toast.makeText(this, "Can't get item id", Toast.LENGTH_SHORT)
        } else {
            val loadUrl = BuildConfig.PRODUCT_URL + itemId.toString()
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

        configChattingBtnClickListener(itemId)
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
            productWebView.evaluateJavascript("javascript:getAndroidUser('" + sellerUid + "')"){
                Log.d(TAG, "onPageFinished: call function")
                Log.d(TAG, "onPageFinished: $it")
            }
        }


    }

    inner class MyWebChromeClient: WebChromeClient(){
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            return super.onConsoleMessage(consoleMessage)
            Log.d(TAG, "${consoleMessage?.message()},${consoleMessage?.lineNumber()}, ${consoleMessage?.sourceId()}")
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