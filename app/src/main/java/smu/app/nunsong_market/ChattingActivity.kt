package smu.app.nunsong_market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smu.app.nunsong_market.adapter.MessageAdapter
import smu.app.nunsong_market.api.ProductApi
import smu.app.nunsong_market.dto.Message
import smu.app.nunsong_market.databinding.ActivityChattingBinding
import smu.app.nunsong_market.dto.Contact
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.util.ServiceGenerator
import smu.app.nunsong_market.util.TimeUtil
import kotlin.collections.ArrayList

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding
    private lateinit var msgAdapter: MessageAdapter
    private lateinit var msgList: ArrayList<Message>

    private lateinit var mDbRef: DatabaseReference

    private var itemId: Int = -1
    private lateinit var receiverUid: String
    private lateinit var senderUid: String
    private lateinit var recieverName: String
    private lateinit var senderName: String

    private val productApi by lazy { ServiceGenerator.createService(ProductApi::class.java) }

    var receiverRoom: String? = null
    var senderRoom: String? = null

    private companion object {
        private const val TAG = "CHATTING_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemId = intent.getIntExtra("id", -1)
        recieverName = intent.getStringExtra("sellerName").toString()
        receiverUid = intent.getStringExtra("sellerUid").toString()
        senderUid = Firebase.auth.currentUser?.uid.toString()
        senderName = Firebase.auth.currentUser?.email.toString().split("@")[0]

        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid + itemId.toString()
        receiverRoom = senderUid + receiverUid + itemId.toString()

        configProductlayout()
        configRecyclerView()
        configSendBtnClickListener()

        binding.backBtn.setOnClickListener {
            this.finish()
        }
    }

    private fun configProductlayout() {
        productApi.getProductById(itemId)
            .enqueue(object :Callback<Product>{
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful.not()) {
                        //예외처리
                        Log.d(TAG, "NOT SUCCESS")
                        return
                    }
                    bindProduct(response.body()!!)
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })

    }

    private fun bindProduct(product: Product) {
        if (product.coverSmallUrl == null){
            binding.productIv.setImageDrawable(getDrawable(R.drawable.no_image))
        }else {
            Glide
                .with(binding.productIv.context)
                .load(product.coverSmallUrl)
                .into(binding.productIv)
        }

        // 가격 텍스트 커스텀
        var customPrice = product.price.toString()
        val length = customPrice.length
        // 3~6자리
        if (length in 4..7) {
            var front = customPrice.slice(0..(length - 4))
            var tail = customPrice.slice((length - 3)..(length - 1))
            customPrice = front + "," + tail
        }
        // 7자리 이상
        else if (length >= 7) {
            var front = customPrice.slice(0..(length - 7))
            var middle = customPrice.slice((length - 6)..(length - 4))
            var tail = customPrice.slice((length - 3)..(length - 1))
            customPrice = front + "," + middle + "," + tail
        }

        binding.titleBar.text = product.sellerName
        // 내가 올린 글이라면 내 제품을 사려는 사람의 아이디를 타이틀로
        if(product.sellerName == senderName) {
            binding.titleBar.text = recieverName
        }

        binding.productTitleTv.text = product.title
        binding.productPriceTv.text = customPrice + "원"
        binding.productStatusTv.text = product.status

        //status에 따라 ui 변경
        when (product.status) {
            "판매중" -> binding.productStatusTv.background =
                getDrawable(R.drawable.square_sold)
            "거래 완료" -> binding.productStatusTv.background =
                getDrawable(R.drawable.square_sold_out)
            else -> binding.productStatusTv.background = null
        }

        binding.productLayout.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java).apply {
                putExtra("id", product.id)
                putExtra("sellerName", product.sellerName)
                putExtra("sellerUid", product.sellerUid)
            }
            startActivity(intent)
        }
    }

    private fun configRecyclerView() {
        //init recyclerview
        msgList = ArrayList<Message>()
        msgAdapter = MessageAdapter(this, msgList)

        binding.chatRcv.layoutManager = LinearLayoutManager(this)
        binding.chatRcv.adapter = msgAdapter

        //logic for adding data to recyclerView
        var senderRoomMsgsQuery = mDbRef.child("chats").child(senderRoom!!).child("messages")
        senderRoomMsgsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                msgList.clear()

                for (postSnapShot in snapshot.children) {

                    val msg = postSnapShot.getValue(Message::class.java)
                    msgList.add(msg!!)
                }
                msgAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun configSendBtnClickListener() {
        binding.sendBtn.setOnClickListener {
            val msg = binding.msgEdt.text.toString()
            val time = TimeUtil.getTime()
            val msgObject = Message(msg, senderUid, time)

            var senderRoomMsgsQuery = mDbRef.child("chats").child(senderRoom!!).child("messages")
            var senderContactsQuery = mDbRef.child("users").child(senderUid).child("contacts")
            var receiverRoomMsgsQuery = mDbRef.child("chats").child(receiverRoom!!).child("messages")
            var receiverContactsQuery = mDbRef.child("users").child(receiverUid).child("contacts")

            if (msg !=""){
                // sender contacts에  contact등록
                senderContactsQuery.child(senderRoom!!)
                    .setValue(Contact(itemId, recieverName, receiverUid, msg, time))
                    .addOnSuccessListener {
                        // receiver contacts에  contact등록
                        receiverContactsQuery.child(receiverRoom!!).setValue(Contact(itemId, senderName, senderUid, msg, time))
                    }
                    .addOnFailureListener {
                        Log.d("ARTICLE_ACTIVITY", "fail to post contact to firebase")
                    }

                // msg 등록
                senderRoomMsgsQuery.push().setValue(msgObject)
                    .addOnSuccessListener {
                        receiverRoomMsgsQuery.push().setValue(msgObject)
                    }


                binding.msgEdt.setText("")
                //TODO: 메세지를 보낸 후에 가장 하단의 메세지에 포커스가 맞춰지도록 recycerview ui update
            }else{
                Toast.makeText(this,"메세지를 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }
    }

}