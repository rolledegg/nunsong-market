package smu.app.nunsong_market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import smu.app.nunsong_market.adapter.MessageAdapter
import smu.app.nunsong_market.model.Message
import smu.app.nunsong_market.databinding.ActivityChattingBinding
import smu.app.nunsong_market.model.Contact
import java.text.SimpleDateFormat
import java.util.*
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
        Log.d(
            "ARTICLE_ACTIVITY",
            "chatting: id: $itemId receiver name: $recieverName r_uid: $receiverUid s_uid: $senderUid"
        )
        //TODO: UI 마무리시 삭제되어야 할 부분
        //name test
        binding.sellerNameTv.text = recieverName

        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid + itemId.toString()
        receiverRoom = senderUid + receiverUid + itemId.toString()

        configRecyclerView()
        configSendBtnClickListener()
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
            val time = getTime()
            val msgObject = Message(msg, senderUid, time)

            var senderRoomMsgsQuery = mDbRef.child("chats").child(senderRoom!!).child("messages")
            var senderContactsQuery = mDbRef.child("users").child(senderUid).child("contacts")
            var receiverRoomMsgsQuery = mDbRef.child("chats").child(receiverRoom!!).child("messages")
            var receiverContactsQuery = mDbRef.child("users").child(receiverUid).child("contacts")

            // sender contacts에  contact등록
            senderContactsQuery.child(senderRoom!!)
                .setValue(Contact(itemId, recieverName, receiverUid, msg, time))
                .addOnSuccessListener {
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
        }
    }


    private fun getTime(): String {
        var mNow = System.currentTimeMillis()
        var mDate = Date(mNow)
        val mFormat = SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss")

        return mFormat.format(mDate)
    }
}