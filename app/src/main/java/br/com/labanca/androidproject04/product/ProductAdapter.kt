package br.com.labanca.androidproject04.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.labanca.androidproject04.persistence.Product
import br.com.labanca.androidproject04.databinding.ItemProductBinding

//ProductViewHolder holds the ProductViewModel
//ProductDiff it's similar to the Equals, it's a way we define how to differentiate objects
class ProductAdapter(val onProductClickListener: ProductClickListener):

    ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ProductViewHolder {
        return ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    //gets the item in the position and binds it to the view
    //adds click listener to the product binded
    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {

        val product = getItem(position)
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onProductClickListener.onClick(product)
        }
    }

    //ItemProductBinding holds the binding that represents the fragment or activity
    //This will be called for every element of the list and draws the product fragment on screen
    class ProductViewHolder(private var binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }

    }

    //Singleton
    companion object ProductDiff: DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return ((oldItem.id == newItem.id)
                 && (oldItem.name.equals(newItem.name))
                 && (oldItem.code.equals(newItem.code))
                 && (oldItem.price == newItem.price))
        }
    }

    class ProductClickListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }
}