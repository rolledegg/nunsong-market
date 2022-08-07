package smu.app.nunsong_market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
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
    private lateinit var mAuth: FirebaseAuth

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
        senderUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        senderName = FirebaseAuth.getInstance().currentUser?.email.toString().split("@")[0]
        Log.d(
            "ARTICLE_ACTIVITY",
            "chatting: id: $itemId receiver name: $recieverName r_uid: $receiverUid s_uid: $senderUid"
        )

        //name test
        binding.sellerNameTv.text = recieverName

        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid + itemId.toString()
        receiverRoom = senderUid + receiverUid + itemId.toString()

        msgList = ArrayList<Message>()
        msgAdapter = MessageAdapter(this, msgList)

        binding.chatRcv.layoutManager = LinearLayoutManager(this)
        binding.chatRcv.adapter = msgAdapter

        //logic for adding data to recyclerView
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
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

        //adding the msg to database
        binding.sendBtn.setOnClickListener {
            val msg = binding.msgEdt.text.toString()
            val time = getTime()
            val msgObject = Message(msg, senderUid, time)
            // 이 user contacts에  등록
            mDbRef.child("users").child(senderUid).child("contacts").get()
                .addOnSuccessListener {
                    // isOnly를 통해 중복체크
                    var isOnly = true

                    for (contact in it.children) {
                        if (contact.getValue(Contact::class.java) == Contact(itemId,recieverName,receiverUid)) {
                            isOnly = false
                            break
                        }
                    }
                    if(isOnly){
                        mDbRef.child("users").child(senderUid).child("contacts").push()
                            .setValue(Contact(itemId, recieverName, receiverUid))
                            .addOnSuccessListener {
                                postContactToReciver()
                            }
                    }
                }
                .addOnFailureListener {
                    Log.d("ARTICLE_ACTIVITY", "fail to get contact list from firebase")
                }


            // msg 등록
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(msgObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(msgObject)
                }

            binding.msgEdt.setText("")
        }
    }

    private fun postContactToReciver() {
        mDbRef.child("users").child(receiverUid).child("contacts").get()
            .addOnSuccessListener {
                // isOnly를 통해 중복체크
                var isOnly = true

                for (contact in it.children) {
                    if (contact.getValue(Contact::class.java) == Contact(itemId,senderName,senderUid)){
                        isOnly = false
                        break
                    }
                }
                if(isOnly){
                    mDbRef.child("users").child(receiverUid).child("contacts").push()
                        .setValue(Contact(itemId,senderName,senderUid))
                }
            }
            .addOnFailureListener {
                Log.d("ARTICLE_ACTIVITY", "fail to put post on receiver")
            }
    }

    private fun getTime(): String {
        var mNow = System.currentTimeMillis()
        var mDate = Date(mNow)
        val mFormat = SimpleDateFormat("yyyy-MM-dd aa hh:mm:ss")

        return mFormat.format(mDate)
    }
}