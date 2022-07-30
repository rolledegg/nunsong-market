package smu.app.nunsong_market

import android.app.Dialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import smu.app.nunsong_market.databinding.ActivityArticleBinding
import smu.app.nunsong_market.databinding.ActivityLoginBinding

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private lateinit var productWebView: WebView
    private lateinit var progressBar: ProgressBar


    private companion object {
        private const val TAG = "ARTICLE_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productWebView = binding.productDetailWv
        progressBar = binding.webviewProgressBar

        productWebView.apply {
            webViewClient = WebViewClient()
            webChromeClient = webChromeClient()
            loadUrl("https://ns-market-nsgw4gncj-nami-koko.vercel.app/products/73")

        }


    }
    inner class webChromeClient: android.webkit.WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            // 0~100의 정수로 로딩 정도를 알려준다.
            progressBar.progress = newProgress
        }
    }

    inner class WebViewClient: android.webkit.WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.isVisible = true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            progressBar.isVisible = false
        }
    }


}