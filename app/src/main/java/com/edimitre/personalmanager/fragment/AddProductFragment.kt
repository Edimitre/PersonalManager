package com.edimitre.personalmanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.data.dao.ProductDao
import com.edimitre.personalmanager.data.model.Product
import com.edimitre.personalmanager.data.repository.ProductRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.viewmodel.ProductViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.ProductVMFactory
import com.edimitre.personalmanager.databinding.FragmentAddProductBinding


class AddProductFragment : DialogFragment() {


    private lateinit var factory: ProductVMFactory

    private lateinit var productRepository: ProductRepository

    private lateinit var productDao: ProductDao

    private lateinit var productViewModel: ProductViewModel

    private var _binding: FragmentAddProductBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        );
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViewModel()


        binding.btnAddProduct.setOnClickListener {
            val name = binding.inputProductName.text.toString()
            val price = binding.inputProductPrice.text.toString()

            when {
                name.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Emri nuk mund te jete bosh",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                price.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Cmimi nuk mund te jete bosh",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    productViewModel.saveProduct(Product(0, name, price.toDouble(), 0))

                    binding.inputProductName.setText("")
                    binding.inputProductPrice.setText("")

                    dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Produkti u ruajt me sukses",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        binding.btnCloseProductDialog.setOnClickListener {
            dismiss()
        }

    }


    private fun loadViewModel() {
        productDao = MyRoomDatabase.getInstance(requireContext()).productDao
        productRepository = ProductRepository(productDao)
        factory = ProductVMFactory(productRepository)
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]
    }


}