package smu.app.nunsong_market

import android.icu.util.ULocale
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import smu.app.nunsong_market.databinding.ActivityPublishBinding

class PublishActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityPublishBinding
    private val  categorySpinner: Spinner by lazy {
        binding.categorySpinner
    }
    private val  exitBtn: ImageButton by lazy {
        binding.exitBtn
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exitBtn.setOnClickListener{
            this.finish()
        }
        val categorItems = arrayOf("의류","전자기기","생활용품","도서","기타")

        //TODO: Add spinner hint
        val categoryAdapter = ArrayAdapter(this,R.layout.item_category,categorItems)
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

        categorySpinner.setSelection(4)
    }
}