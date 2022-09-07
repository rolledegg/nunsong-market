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
    private lateinit var acceptedAdapter: PromiseAdapter
    private lateinit var requestedAdapter: PromiseAdapter
    private lateinit var requestAdapter: PromiseAdapter

    //데이터 배열
    var emptyList = ArrayList<Promise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(PromiseListViewModel::class.java)
        viewModel.loadPromise()

        binding.backBtn.setOnClickListener { this.finish() }

        initAcceptedRecyclerView()
        initRequestedRecyclerView()
        initRequestRecyclerView()

        configPromsClickListener()

        uiUpdate()
    }


    private fun uiUpdate() {
        observeAndSubmitPromList()
        updatePromVisibility()
        isPromiseChange.observe(this){
            if(isPromiseChange.value == true) {
                viewModel.loadPromise()
                isPromiseChange.postValue(false)
            }
        }
    }

    private fun observeAndSubmitPromList() {
        viewModel.acceptedPromList.observe(this){
            acceptedAdapter.submitList(it)
            //update count
            if(it.isNotEmpty()){
                binding.acceptedCount.visibility = View.VISIBLE
                binding.acceptedCount.text = it.size.toString()
            }

        }

         viewModel.requestedPromList.observe(this){
            requestedAdapter.submitList(it)
             //update count
             if(it.isNotEmpty()) {
                 binding.requestedCount.visibility = View.VISIBLE
                 binding.requestedCount.text = it.size.toString()
             }
        }

         viewModel.requestPromList.observe(this){
            requestAdapter.submitList(it)
             //update count
             if(it.isNotEmpty()) {
                 binding.requestCount.visibility = View.VISIBLE
                 binding.requestCount.text = it.size.toString()
             }
        }
    }

    private fun updatePromVisibility() {
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

        viewModel.isRequestedPromOpen.observe(this){
            if(viewModel.isRequestedPromOpen.value == true){
                binding.requestedRcv.visibility = View.VISIBLE
                binding.requestedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_up_24))
            }
            else{
                binding.requestedRcv.visibility = View.GONE
                binding.requestedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_down_24))
            }
        }

        viewModel.isRequestPromOpen.observe(this){
            if(viewModel.isRequestPromOpen.value == true){
                binding.requestRcv.visibility = View.VISIBLE
                binding.requestNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_up_24))
            }
            else{
                binding.requestRcv.visibility = View.GONE
                binding.requestNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_down_24))
            }
        }
    }

    private fun configPromsClickListener() {
        binding.acceptedPromiseTv.setOnClickListener {
            viewModel.clickAcceptedProm()
        }

        binding.requestedPromiseTv.setOnClickListener {
            viewModel.clickRequestedProm()
        }

        binding.requestPromiseTv.setOnClickListener {
            viewModel.clickRequestProm()
        }
    }

    private fun initAcceptedRecyclerView() {
        acceptedAdapter = PromiseAdapter(this)

        acceptedAdapter.submitList(emptyList)
        binding.acceptedRcv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.acceptedRcv.adapter = acceptedAdapter
    }

    private fun initRequestedRecyclerView() {
        requestedAdapter = PromiseAdapter(this)

        requestedAdapter.submitList(emptyList)
        binding.acceptedRcv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.acceptedRcv.adapter = requestedAdapter
    }

    private fun initRequestRecyclerView() {
        requestAdapter = PromiseAdapter(this)

        requestAdapter.submitList(emptyList)
        binding.acceptedRcv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.acceptedRcv.adapter = requestAdapter
    }
}