package com.hide09.androidfundamental.navigationevent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hide09.androidfundamental.R
import com.hide09.androidfundamental.databinding.FragmentCategoryNavBinding

class CategoryNavFragment : Fragment() {
    private var _binding: FragmentCategoryNavBinding? = null
    private val binding get() = _binding!!

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_STOCK = "extra_stock"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryNavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCategoryNavLifestyle.setOnClickListener { view ->
            val mBundle = Bundle()
            mBundle.putString(EXTRA_NAME, "Lifestyle")
            mBundle.putLong(EXTRA_STOCK, 7)
            view.findNavController().navigate(R.id.action_categoryNavFragment_to_detailCategoryNavFragment, mBundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}