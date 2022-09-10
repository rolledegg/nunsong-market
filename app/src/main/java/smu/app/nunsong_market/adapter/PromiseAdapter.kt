package smu.app.nunsong_market.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.PromiseListActivity
import smu.app.nunsong_market.api.PromiseApi
import smu.app.nunsong_market.databinding.ItemPromiseBinding
import smu.app.nunsong_market.dto.Promise
import smu.app.nunsong_market.util.ServiceGenerator
import smu.app.nunsong_market.viewmodel.PromiseListViewModel


class PromiseAdapter(context: Context) :
    ListAdapter<Promise, PromiseAdapter.PromiseItemViewHolder>(diffUtil) {
    private lateinit var firebaseAuth: FirebaseAuth
    private val promiseApi by lazy { ServiceGenerator.createService(PromiseApi::class.java) }
    val context: Context = context

    inner class PromiseItemViewHolder(private val binding: ItemPromiseBinding) :
        RecyclerView.ViewHolder(binding.root) {


        // 데이터를 가져와서 바인드 해주는 역할의 함수
        @SuppressLint("SetTextI18n")
        fun bind(promise: Promise) {
            firebaseAuth = Firebase.auth

            binding.productTitleTv.text = promise.title
            binding.productDateTv.text = promise.date
            binding.productPriceTv.text = promise.location
            binding.productMemoTv.text = promise.memo

            // 예정된 약속
            if (promise.status == 2) configAcceptedPro(promise)
            // 요청온 악속
            else if (promise.status == 0 && promise.receiverUid == firebaseAuth.uid!!) configRequestedPro(promise)
            // 요청 보낸 약속
            else if (promise.status == 0 && promise.senderUid == firebaseAuth.uid!!) configRequestPro(promise)
            // 거절된 약속
            else if (promise.status == 1) configRejectedPro(promise)

        }


        private fun configAcceptedPro(promise: Promise) {
            binding.msgTv.visibility = View.GONE
            binding.headBtn.visibility = View.GONE
            binding.middleBtn.apply {
                text = "삭제"
                setOnClickListener {
                    deleteProm(promise.promiseId!!)
                }
            }
            binding.tailBtn.visibility = View.GONE

        }

        private fun configRequestedPro(promise: Promise) {
            binding.msgTv.visibility = View.GONE
            binding.headBtn.apply {
                text = "수락"
                setOnClickListener {
                    changeStatus(promise.promiseId!!,2)
                }
            }
            binding.middleBtn.visibility = View.GONE
            binding.tailBtn.apply {
                text = "거절"
                setOnClickListener {
                    changeStatus(promise.promiseId!!,1)
                }
            }
        }

        private fun configRequestPro(promise: Promise) {
            binding.msgTv.text = "요청 수락을 대기중인 약속입니다."
            binding.headBtn.visibility = View.GONE
            binding.middleBtn.visibility = View.GONE
            binding.tailBtn.visibility = View.GONE
        }

        private fun configRejectedPro(promise: Promise) {
            binding.msgTv.text = "요청 수락을 거절당했습니다."
            binding.headBtn.visibility = View.GONE
            binding.middleBtn.apply {
                text = "확인"
                setOnClickListener {
                    deleteProm(promise.promiseId!!)
                }
            }
                binding.tailBtn.visibility = View.GONE

        }

    }

    fun deleteProm(promiseId: Long) {
        promiseApi.deletePromise(promiseId)
            .enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    Log.d(PromiseListViewModel.TAG, "onResponse: ..")
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(PromiseListViewModel.TAG, "onResponse: Not success")
                        return
                    }

                    response.body()?.let {
                        Log.d(PromiseListViewModel.TAG, "onResponse: ${it}")
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Log.e(PromiseListViewModel.TAG, t.toString())
                }


            })
        PromiseListActivity.isPromiseChange.postValue(true)


    }

    fun changeStatus(promiseId: Long, status: Int) {
        promiseApi.changePromiseStatus(
            promiseId,
            Promise(
                null,
                0,
                "",
                "",
                "",
                "",
                1,
                "",
                ""
            ),
            status
        )
            .enqueue(object : Callback<Promise> {
                override fun onResponse(call: Call<Promise>, response: Response<Promise>) {
                    Log.d(PromiseListViewModel.TAG, "onResponse: ..")
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(PromiseListViewModel.TAG, "onResponse: Not success")
                        return
                    }

                    response.body()?.let {
                        Log.d(PromiseListViewModel.TAG, "onResponse: ${it}")
                    }
                }

                override fun onFailure(call: Call<Promise>, t: Throwable) {
                    Log.e(PromiseListViewModel.TAG, t.toString())
                }

            })
        PromiseListActivity.isPromiseChange.postValue(true)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseItemViewHolder {
        // 미리 만들어진 뷰홀더가 없을 경우에 새로 생성하는 함수
        return PromiseItemViewHolder(
            ItemPromiseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PromiseItemViewHolder, position: Int) {
        // 실제로 뷰홀더에 데이터를 바인드해주는 함수
        holder.bind(currentList[position])
    }

    //뷰의 포지션이 변경되었을때 같은 아이템이 올라오면 다시 할당할 필요가 없기 때문에 같은 아이템인지 판단하는 것
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Promise>() {
            override fun areItemsTheSame(oldItem: Promise, newItem: Promise): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Promise, newItem: Promise): Boolean {
                return (oldItem.itemId == newItem.itemId) && (oldItem.senderUid == newItem.senderUid) && (oldItem.receiverUid == newItem.receiverUid)
            }

        }
    }
}