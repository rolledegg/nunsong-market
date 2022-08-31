package smu.app.nunsong_market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import smu.app.nunsong_market.databinding.ActivityPromiseListBinding

class PromiseListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPromiseListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseListBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}