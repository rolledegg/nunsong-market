package smu.app.nunsong_market.adapter

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

class ContactAdapter(val context: Context, val contactList:ArrayList<Contact>)
    :RecyclerView.Adapter<ContactAdapter.UserVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent,false)
        return UserVH(view)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        val currentContact = contactList[position]

        holder.textName.text = currentContact.name
        holder.itemId.text = currentContact.itemId.toString()

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
        val itemId = itemView.findViewById<TextView>(R.id.item_tv)
    }

}