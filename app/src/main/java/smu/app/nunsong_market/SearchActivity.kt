package smu.app.nunsong_market

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.json.JSONArray
import org.json.JSONException
import smu.app.nunsong_market.adapter.KeywordAdapter
import smu.app.nunsong_market.databinding.ActivitySearchBinding
import smu.app.nunsong_market.dto.Keyword
import smu.app.nunsong_market.dto.Product


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var viewModel: ViewModel

    var activity= this
    var keywordList = ArrayList<Keyword>()

    companion object {

        // keywordAdapter에서 closeBtn 눌렀을 때 즉각 ui update를 위해서 만듬
        val keywordLiveData: MutableLiveData<List<Keyword>> = MutableLiveData()

        private const val TAG = "SEARCH_ACTIVITY"
        private const val KEY = "RECENT_KEYWORDS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        configEditText()
        configRecyclerView()
        configSearchBtn()
        configBackBtn()
        configDeleteAllTv()

        keywordLiveData.observe(this) {
            binding.recentKeywordRcv.adapter = KeywordAdapter(this, keywordList)
            setStringArrayPref(keywordLiveData.value as ArrayList<Keyword>)
        }

    }

    private fun configDeleteAllTv() {
        binding.deleteAllTv.setOnClickListener {
            keywordList.clear()
            keywordLiveData.postValue(keywordList)
        }
    }

    private fun configBackBtn() {
        binding.backBtn.setOnClickListener { this.finish() }
    }

    private fun configEditText() {
        binding.searchEtv.apply {
            requestFocus()
            setOnKeyListener { view, keyCode, event ->
                if (keyCode == KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    setKeywordAndStartActivity()
                }
                if(keyCode ==KeyEvent.KEYCODE_BACK){activity.finish()}
                true
            }
        }
    }


    private fun configRecyclerView() {
        getStringArrayPref()
        binding.recentKeywordRcv.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = KeywordAdapter(context, keywordList)
        }
    }

    private fun configSearchBtn() {
        binding.searchIbtn.setOnClickListener {
            setKeywordAndStartActivity()
        }
    }

    private fun setKeywordAndStartActivity() {
        val searchWord = binding.searchEtv.text.toString()
        if (searchWord.isNotEmpty()) {
            keywordList.add(0,Keyword(searchWord))
            setStringArrayPref(keywordList)

            val intent = Intent(this, ArticleListActivity::class.java).apply {
                putExtra("type", 2)
                putExtra("title", searchWord)
                putExtra("value", searchWord)
            }
            startActivity(intent)
            this.finish()
        }
    }

    private fun setStringArrayPref(list: ArrayList<Keyword>) {
        // arrayList -> json
        val json = JSONArray()
        for (i in 0 until list.size) {
            json.put(list.get(i).keyword)
        }
        if (!list.isEmpty()) {
            editor.putString(KEY, json.toString())
        } else {
            editor.putString(KEY, null)
        }
        editor.apply()
    }


    private fun getStringArrayPref() {
        // json -> arrayList
        val json = sharedPref.getString(KEY, null)
        keywordList.clear()

        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val keyword = a.optString(i)
                    keywordList.add(Keyword(keyword))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}