package smu.app.nunsong_market.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import smu.app.nunsong_market.ArticleActivity
import smu.app.nunsong_market.R
import smu.app.nunsong_market.dto.Product
import smu.app.nunsong_market.databinding.ItemArticleBinding


class ProductAdapter(context: Context) :
    ListAdapter<Product, ProductAdapter.ArtitleItemViewHolder>(diffUtil) {

    val context: Context = context

    inner class ArtitleItemViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // 데이터를 가져와서 바인드 해주는 역할의 함수
        @SuppressLint("SetTextI18n")
        fun bind(productModel: Product) {
            // 날짜 형식 커스텀
            if (productModel.date != null) {
                var customDate: String = productModel.date.slice(7..12)
                var customHour: String = productModel.date.slice(14..15)
                var customMin: String = ":" + productModel.date.slice(18..19)
                if (customHour.toInt() > 12) {
                    customHour = " 오후 " + (customHour.toInt() % 12).toString()
                } else {
                    customHour = " 오전 " + customHour
                }
                binding.productDateTv.text = customDate + customHour + customMin
            }


            // 가격 텍스트 커스텀
            var customPrice = productModel.price.toString()
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

            binding.productTitleTv.text = productModel.title
            binding.productPriceTv.text = customPrice + "원"
            binding.productStatusTv.text = productModel.status

            if (productModel.coverSmallUrl == null) {
                binding.productIv.setImageDrawable(context.getDrawable(R.drawable.img_no_image))
            } else {
                Glide
                    .with(binding.productIv.context)
                    .load(productModel.coverSmallUrl)
                    .into(binding.productIv)
            }

            configItemClickLister(productModel)

            //status에 따라 ui 변경
            when (productModel.status) {
                "판매중" -> {
//                    binding.productStatusTv.background = context.getDrawable(R.drawable.square_sold)
//                    binding.productStatusTv.setTextColor(context.getColor(R.color.gray))
                    binding.productStatusTv.background = null
                }

                "거래 완료" -> binding.productStatusTv.background =
                    context.getDrawable(R.drawable.square_sold_out)
                else -> binding.productStatusTv.background = null
            }
        }

        private fun configItemClickLister(productModel: Product) {
            binding.root.setOnClickListener {
                val intent = Intent(context, ArticleActivity::class.java).apply {
                    putExtra("id", productModel.id)
                    putExtra("sellerName", productModel.sellerName)
                    putExtra("sellerUid", productModel.sellerUid)
                    Log.d(
                        "ARTICLE_ACTIVITY",
                        "adapter: ${productModel.id} / ${productModel.sellerName} / ${productModel.sellerUid}"
                    )
                }
                startActivity(context, intent, null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtitleItemViewHolder {
        // 미리 만들어진 뷰홀더가 없을 경우에 새로 생성하는 함수
        return ArtitleItemViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArtitleItemViewHolder, position: Int) {
        // 실제로 뷰홀더에 데이터를 바인드해주는 함수
        holder.bind(currentList[position])
    }

    //뷰의 포지션이 변경되었을때 같은 아이템이 올라오면 다시 할당할 필요가 없기 때문에 같은 아이템인지 판단하는 것
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}