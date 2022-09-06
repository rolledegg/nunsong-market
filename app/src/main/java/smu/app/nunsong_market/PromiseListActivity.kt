package smu.app.nunsong_market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import smu.app.nunsong_market.adapter.ProductAdapter
import smu.app.nunsong_market.adapter.PromiseAdapter
import smu.app.nunsong_market.databinding.ActivityPromiseListBinding
import smu.app.nunsong_market.dto.Keyword
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.dto.Promise
import smu.app.nunsong_market.viewmodel.HomeViewModel
import smu.app.nunsong_market.viewmodel.PromiseListViewModel

class PromiseListActivity : AppCompatActivity() {
    companion object {
        // promiseAdapter에서 Btn 눌렀을 때 즉각 ui update를 위해서 만듬
        val isPromiseChange= MutableLiveData(false)
    }

    private lateinit var binding:ActivityPromiseListBinding
    private lateinit var viewModel:PromiseListViewModel
    private lateinit var adapter: PromiseAdapter

    //데이터 배열
    var promiseList = ArrayList<Promise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(PromiseListViewModel::class.java)
        viewModel.loadPromise()

        binding.backBtn.setOnClickListener { this.finish() }
        initRecyclerView()

        configAcceptedPro()

        viewModel.articleList.observe(this){
            adapter.submitList(it)
        }
        viewModel.isAcceptedPromOpen.observe(this){
            if(viewModel.isAcceptedPromOpen.value == true){
                binding.acceptedRcv.visibility = View.VISIBLE
                binding.acceptedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_up_24))
            }
            else{
                binding.acceptedRcv.visibility = View.GONE
                binding.acceptedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_down_24))
            }
        }
        isPromiseChange.observe(this){
            if(isPromiseChange.value == true) {
                viewModel.loadPromise()
                isPromiseChange.postValue(false)
            }
        }


    }

    private fun configAcceptedPro() {
        binding.acceptedPromiseTv.setOnClickListener {
            viewModel.clickAcceptedProm()
        }
    }

    private fun initRecyclerView() {
        adapter = PromiseAdapter(this)

        adapter.submitList(promiseList)
        binding.acceptedRcv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.acceptedRcv.adapter = adapter
    }
}