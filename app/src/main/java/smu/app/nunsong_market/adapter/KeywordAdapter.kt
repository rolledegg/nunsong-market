package smu.app.nunsong_market.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import smu.app.nunsong_market.ArticleListActivity
import smu.app.nunsong_market.R
import smu.app.nunsong_market.SearchActivity
import smu.app.nunsong_market.databinding.ItemSearchWordBinding
import smu.app.nunsong_market.dto.Keyword


class KeywordAdapter(val context: Context, val keywordList: ArrayList<Keyword>) :
    RecyclerView.Adapter<KeywordAdapter.KeywordVH>() {

    inner class KeywordVH(private val binding: ItemSearchWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keywordModel: Keyword) {
            binding.keywordTv.text = keywordModel.keyword

            configItemClickLister(keywordModel)
            configCloseBtnClickLister(keywordModel)
        }

        private fun configCloseBtnClickLister(keywordModel: Keyword) {
            binding.closeBtn.setOnClickListener {

                keywordList.remove(Keyword(keywordModel.keyword))
                SearchActivity.keywordLiveData.postValue(keywordList)

            }

        }

        private fun configItemClickLister(keywordModel: Keyword) {
            binding.root.setOnClickListener {
                val intent = Intent(context, ArticleListActivity::class.java).apply {
                    putExtra("type", 2)
                    putExtra("title", keywordModel.keyword)
                    putExtra("value", keywordModel.keyword)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordVH {
        return KeywordVH(
            ItemSearchWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: KeywordVH, position: Int) {
        // 실제로 뷰홀더에 데이ㅓ를 바인드해주는 함수
        holder.bind(keywordList.get(position))
    }

    override fun getItemCount(): Int {
        return keywordList.size
    }

}
