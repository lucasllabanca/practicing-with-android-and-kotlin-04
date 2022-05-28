package br.com.labanca.androidproject04.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import br.com.labanca.androidproject04.databinding.FragmentProductsListBinding

private const val TAG = "ProductsListFragment"
class ProductsListFragment: Fragment() {

    private val productListViewModel: ProductListViewModel by lazy {
        ViewModelProvider(this).get(ProductListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentProductsListBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //At this point the lazy above is executed
        binding.productListViewModel = productListViewModel

        //decorator between items
        val itemDecor = DividerItemDecoration(context, VERTICAL)
        binding.rcvProducts.addItemDecoration(itemDecor);

        //adding click listener to the product item inside the rcvProducts list of the xml id
        binding.rcvProducts.adapter = ProductAdapter(ProductAdapter.ProductClickListener{

            Log.i(TAG,"Product selected: ${it.name}")

            this.findNavController()
                .navigate(ProductsListFragmentDirections.actionShowProductDetail(it.code!!))
        })

        binding.fab.setOnClickListener { view ->
            this.findNavController()
                .navigate(ProductsListFragmentDirections.actionShowProductDetail(null))
        }

        return binding.root
    }
}