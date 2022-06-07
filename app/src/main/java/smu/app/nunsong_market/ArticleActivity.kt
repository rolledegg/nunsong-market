package smu.app.nunsong_market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ProgressBar
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
        progressBar=binding.webviewProgressBar

        productWebView.loadUrl("https://naver.com")

//        productWebView.apply {
//            // 클릭시 새창 안뜨게
//            webViewClient = webViewClientClass()
//
//            // 팝업이나 파일 업로드 등 설정해주기 위해 webView.webChromeClient를 설정
//            // 웹뷰에서 크롬이 실행 가능 && 새창띄우기는 안됨
//            // webChromeClient = webChromeClient()
//
//            // 웹뷰에서 팝업창 호출하기 위해
//            webChromeClient
//        }

    }
}