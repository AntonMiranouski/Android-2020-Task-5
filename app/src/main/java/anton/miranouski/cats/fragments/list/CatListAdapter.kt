package anton.miranouski.cats.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import anton.miranouski.cats.R
import anton.miranouski.cats.databinding.DataRowBinding
import anton.miranouski.cats.model.Cat
import com.bumptech.glide.Glide

class CatListAdapter : PagingDataAdapter<Cat, CatListAdapter.CatViewHolder>(CAT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = DataRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class CatViewHolder(private val binding: DataRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cat: Cat) {
            binding.apply {
                Glide.with(itemView)
                    .load(cat.imageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_baseline_error_outline_200)
                    .into(ivCat)
            }
        }
    }

    companion object {
        private val CAT_COMPARATOR = object : DiffUtil.ItemCallback<Cat>() {
            override fun areItemsTheSame(oldItem: Cat, newItem: Cat) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Cat, newItem: Cat) =
                oldItem == newItem
        }
    }
}