package smu.app.nunsong_market.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import smu.app.nunsong_market.ChattingActivity
import smu.app.nunsong_market.R
import smu.app.nunsong_market.model.Contact
import smu.app.nunsong_market.util.TimeUtil

class ContactAdapter(val context: Context, val contactList:ArrayList<Contact>)
    :RecyclerView.Adapter<ContactAdapter.UserVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent,false)
        return UserVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val currentContact = contactList[position]

        holder.textName.text = currentContact.name
        holder.lastMsg.text = currentContact.lastMessage
        // yyyy-MM-dd aa hh:mm:ss
        val today = TimeUtil.getTime().slice(0..9)
        val DateOfMsg = currentContact.lastTime!!.slice(0..9)
        val TimeOfMsg = currentContact.lastTime!!.slice(11..18)
        if(DateOfMsg == today){
            holder.lastTime.text = TimeOfMsg
        } else{
            holder.lastTime.text= DateOfMsg.slice(5..6) + "월 " + DateOfMsg.slice(8..9) +"일"
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChattingActivity::class.java).apply{
                putExtra("id",currentContact.itemId)
                putExtra("sellerName",currentContact.name)
                putExtra("sellerUid",currentContact.uid)
            }
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class UserVH(itemView: View): RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.name_tv)
        val lastMsg = itemView.findViewById<TextView>(R.id.last_msg_tv)
        val lastTime = itemView.findViewById<TextView>(R.id.last_time_tv)
    }

}