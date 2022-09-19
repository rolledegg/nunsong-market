package smu.app.nunsong_market

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        val isPromiseChange = MutableLiveData(false)
    }

    private lateinit var binding: ActivityPromiseListBinding
    private lateinit var viewModel: PromiseListViewModel
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

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPromise()
            binding.swipeRefresh.isRefreshing=false
        }

        initAcceptedRecyclerView()
        initRequestedRecyclerView()
        initRequestRecyclerView()

        configPromsClickListener()

        uiUpdate()
    }


    private fun uiUpdate() {
        observeAndSubmitPromListAndUpdateCount()
        updatePromVisibility()
        isPromiseChange.observe(this) {
            if (isPromiseChange.value == true) {
                viewModel.loadPromise()
                isPromiseChange.postValue(false)

            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun observeAndSubmitPromListAndUpdateCount() {
        viewModel.acceptedPromList.observe(this) {
            acceptedAdapter.submitList(it)

            //update count
            binding.acceptedCount.visibility= View.VISIBLE
            binding.acceptedCount.background = getDrawable(R.drawable.background_promise_count)
            binding.acceptedCount.text = it.size.toString()
            if (it.isEmpty()) {
                binding.acceptedCount.background = getDrawable(R.drawable.background_promise_count_zero)
                viewModel.isAcceptedPromOpen.postValue(false)
            }
            else{
                viewModel.isAcceptedPromOpen.postValue(true)
            }

        }

        viewModel.requestedPromList.observe(this) {
            requestedAdapter.submitList(it)

            //update count
            binding.requestedCount.visibility= View.VISIBLE
            binding.requestedCount.background = getDrawable(R.drawable.background_promise_count)
            binding.requestedCount.text = it.size.toString()
            if (it.isEmpty()) {
                binding.requestedCount.background = getDrawable(R.drawable.background_promise_count_zero)
                viewModel.isRequestedPromOpen.postValue(false)
            }
        }

        viewModel.requestPromList.observe(this) {
            requestAdapter.submitList(it)

            //update count
            binding.requestCount.visibility= View.VISIBLE
            binding.requestCount.text = it.size.toString()
            binding.requestCount.background = getDrawable(R.drawable.background_promise_count)
            if (it.isEmpty()) {
                binding.requestCount.background = getDrawable(R.drawable.background_promise_count_zero)
                viewModel.isRequestPromOpen.postValue(false)
            }

        }
    }

    private fun updatePromVisibility() {
        viewModel.isAcceptedPromOpen.observe(this) {
            if (viewModel.isAcceptedPromOpen.value == true) {
                binding.acceptedRcv.visibility = View.VISIBLE
                binding.acceptedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_up_24))
            } else {
                binding.acceptedRcv.visibility = View.GONE
                binding.acceptedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_down_24))
            }
        }

        viewModel.isRequestedPromOpen.observe(this) {
            if (viewModel.isRequestedPromOpen.value == true) {
                binding.requestedRcv.visibility = View.VISIBLE
                binding.requestedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_up_24))
            } else {
                binding.requestedRcv.visibility = View.GONE
                binding.requestedNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_down_24))
            }
        }

        viewModel.isRequestPromOpen.observe(this) {
            if (viewModel.isRequestPromOpen.value == true) {
                binding.requestRcv.visibility = View.VISIBLE
                binding.requestNarrow.setImageDrawable(getDrawable(R.drawable.ic_drop_up_24))
            } else {
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
        binding.acceptedRcv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.acceptedRcv.adapter = acceptedAdapter
    }

    private fun initRequestedRecyclerView() {
        requestedAdapter = PromiseAdapter(this)

        requestedAdapter.submitList(emptyList)
        binding.requestedRcv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.requestedRcv.adapter = requestedAdapter
    }

    private fun initRequestRecyclerView() {
        requestAdapter = PromiseAdapter(this)

        requestAdapter.submitList(emptyList)
        binding.requestRcv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.requestRcv.adapter = requestAdapter
    }
}