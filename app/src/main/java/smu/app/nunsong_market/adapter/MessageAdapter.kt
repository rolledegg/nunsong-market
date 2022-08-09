package smu.app.nunsong_market.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import smu.app.nunsong_market.R
import smu.app.nunsong_market.model.Message

class MessageAdapter(val context: Context, val msgList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            // inflate receive
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.received_msg, parent, false)
            return ReceiveVH(view)
        } else {
            // inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_msg, parent, false)
            return SentVH(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = msgList[position]


        if (holder.javaClass == SentVH::class.java) {
            // do the stuff for sent view holder

            val viewHolder = holder as SentVH
            holder.sentMsg.text = currentMsg.message
            holder.sentTime.text = currentMsg.time?.slice(11..18)

        } else {
            // do the stuff for receive view holder

            val viewHolder = holder as ReceiveVH
            holder.receiveMsg.text = currentMsg.message
            holder.receiveTime.text = currentMsg.time?.slice(11..18)
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMsg = msgList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMsg.senderId)) {
            //sending msg
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

    // 각각의 layout의 view들을 bind
    class SentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMsg = itemView.findViewById<TextView>(R.id.sent_msg_tv)
        val sentTime = itemView.findViewById<TextView>(R.id.sent_time)
    }

    class ReceiveVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMsg = itemView.findViewById<TextView>(R.id.received_msg_tv)
        val receiveTime = itemView.findViewById<TextView>(R.id.receive_time)
    }
}