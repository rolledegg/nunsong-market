package smu.app.nunsong_market.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import smu.app.nunsong_market.R
import smu.app.nunsong_market.dto.Message
import smu.app.nunsong_market.util.TimeUtil

class MessageAdapter(val context: Context, val msgList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    val ITEM_DATE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            // inflate receive
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_received_msg, parent, false)
            return ReceiveVH(view)
        } else if (viewType == 2) {
            // inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_sent_msg, parent, false)
            return SentVH(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.item_date_msg, parent, false)
            return DateVH(view)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = msgList[position]
        val time = currentMsg.time?.slice(11..15)


        if (holder.javaClass == SentVH::class.java) {
            // do the stuff for sent view holder

            val viewHolder = holder as SentVH
            holder.sentMsg.text = currentMsg.message
            holder.sentTime.text = TimeUtil.formateTime(time!!)

        } else if (holder.javaClass == ReceiveVH::class.java) {
            // do the stuff for receive view holder

            val viewHolder = holder as ReceiveVH
            if (position > 1){
                if( currentMsg.senderId == msgList[position-1].senderId){
                    holder.receiveProfile.isVisible = false
                }
            }
            holder.receiveMsg.text = currentMsg.message
            holder.receiveTime.text = TimeUtil.formateTime(time!!)
        } else {
            // do the stuff for receive view holder

            val viewHolder = holder as DateVH
            val date = currentMsg.time!!.slice(0..9).split("-")
            holder.date.text =date[0]+"년 "+ date[1]+"월 "+ date[2]+"일"


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
        } else if ("DATE".equals(currentMsg.senderId)) {
            return ITEM_DATE
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
        val receiveProfile = itemView.findViewById<ImageView>(R.id.profile_iv)
        val receiveTime = itemView.findViewById<TextView>(R.id.receive_time)
    }

    class DateVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date = itemView.findViewById<TextView>(R.id.date_tv)
    }
}