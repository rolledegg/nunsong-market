package smu.app.nunsong_market

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import smu.app.nunsong_market.databinding.ActivityArticleBinding
import smu.app.nunsong_market.util.BuildConfig

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        productWebView = binding.productDetailWv
        progressBar = binding.webviewProgressBar

        var itemId= intent.getIntExtra("id",-1)
        sellerName= intent.getStringExtra("sellerName").toString()
        sellerUid= intent.getStringExtra("sellerUid").toString()
        Log.d(TAG, "onCreate: id: $itemId name: $sellerName uid: $sellerUid ")

        //본인의 게시글에서는 채팅 버튼이 다르게
        if(sellerUid == mAuth.currentUser?.uid){
            binding.chattingBtn.text = "본인 게시글임"
       }

        if(itemId == -1){
            Toast.makeText(this,"Can't get item id",Toast.LENGTH_SHORT)
        }
        else{
            val loadUrl= BuildConfig.PRODUCT_URL + itemId.toString()
            Log.d(TAG, "onCreate: $loadUrl")

            productWebView.apply {
                webViewClient = WebViewClient()
                webChromeClient = webChromeClient()
                loadUrl(loadUrl)

            }

            binding.chattingBtn.setOnClickListener {
                val intent = Intent(this, ChattingActivity::class.java).apply{
                    putExtra("id",itemId)
                    putExtra("sellerName",sellerName)
                    putExtra("sellerUid",sellerUid)
                }
                startActivity(intent)
            }
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
        }
    }


}