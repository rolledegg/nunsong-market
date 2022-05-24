package smu.app.nunsong_market.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import smu.app.nunsong_market.model.Product
import smu.app.nunsong_market.databinding.ItemArticleBinding


class ProductAdapter(val clickListener:(Product) -> Unit): ListAdapter<Product,ProductAdapter.ArtitleItemViewHolder>(diffUtil) {
    inner class ArtitleItemViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root){
        // 데이터를 가져와서 바인드 해주는 역할의 함수
        @SuppressLint("SetTextI18n")
        fun bind(productModel:Product){
            binding.productTitleTv.text =productModel.title
            binding.productDateTv.text=productModel.date
            binding.productPriceTv.text =productModel.price+"원"
            binding.productDateTv.text ="5월 2일 오후4:00"
            binding.productStatusTv.text ="거래완료"

            Glide
                .with(binding.productIv.context)
                .load(productModel.coverSmallUrl)
                .into(binding.productIv)

            binding.root.setOnClickListener {
                clickListener(productModel)
            }

            binding.root.setOnClickListener{
                clickListener(productModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtitleItemViewHolder {
        // 미리 만들어진 뷰홀더가 없을 경우에 새로 생성하는 함수
        return ArtitleItemViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ArtitleItemViewHolder, position: Int) {
        // 실제로 뷰홀더에 데이ㅓ를 바인드해주는 함수
        holder.bind(currentList[position])
    }

    //뷰의 포지션이 변경되었을때 같은 아이템이 올라오면 다시 할당할 필요가 없기 때문에 같은 아이템인지 판단하는 것
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}