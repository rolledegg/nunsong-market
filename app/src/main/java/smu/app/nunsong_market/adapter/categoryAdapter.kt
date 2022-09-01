package smu.app.nunsong_market.adapter

import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import smu.app.nunsong_market.R
import smu.app.nunsong_market.databinding.ItemArticleBinding
import smu.app.nunsong_market.databinding.ItemChatListBinding
import smu.app.nunsong_market.dto.Contact


class categoryAdapter(private val list: ArrayList<Contact>): PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val binding = ItemChatListBinding.inflate(inflater)
        binding.nameTv.text = list[position].name
        binding.lastMsgTv.text = list[position].lastMessage
        binding.lastTimeTv.text = list[position].lastTime

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return list.size
    }
}