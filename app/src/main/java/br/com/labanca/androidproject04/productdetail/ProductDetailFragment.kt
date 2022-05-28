package br.com.labanca.androidproject04.productdetail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.labanca.androidproject04.R
import br.com.labanca.androidproject04.databinding.FragmentProductDetailBinding
import com.google.firebase.analytics.FirebaseAnalytics

private const val TAG = "ProductDetailFragment"

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private var productCode: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        Log.i(TAG, "Creating ProductDetailFragment")
        binding = FragmentProductDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        productCode = ProductDetailFragmentArgs.fromBundle(requireArguments()).productCode
        val productDetailViewModelFactory = ProductDetailViewModelFactory(productCode)
        binding.productDetailViewModel = ViewModelProvider(this, productDetailViewModelFactory).get(ProductDetailViewModel::class.java)

        //Fetch the product code and create the ViewModel here
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_product -> {
                binding.productDetailViewModel?.deleteProduct()

                val firebaseAnalytics = FirebaseAnalytics.getInstance(this.requireContext())
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, productCode)
                firebaseAnalytics.logEvent("delete_item", bundle)

                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}