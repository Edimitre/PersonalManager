package com.edimitre.personalmanager.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.adapter.ProductAdapter
import com.edimitre.personalmanager.data.dao.ProductDao
import com.edimitre.personalmanager.data.model.Product
import com.edimitre.personalmanager.data.repository.ProductRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.viewmodel.ProductViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.ProductVMFactory
import com.edimitre.personalmanager.databinding.ActivityProductsBinding
import com.edimitre.personalmanager.fragment.AddProductFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ProductsActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel

    private lateinit var factory: ProductVMFactory

    private lateinit var productRepository: ProductRepository

    private lateinit var productDao: ProductDao

    private lateinit var adapter: ProductAdapter

    private lateinit var binding: ActivityProductsBinding

    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductsBinding.inflate(layoutInflater)

        setContentView(binding.root)


        start()
    }

    private fun start() {

        loadBackEnd()

        loadFrontend()

        setToolbar()

        setBottomAppBar()

        setBtnListeners()

        showProducts()

    }

    private fun setToolbar() {

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Produktet "
        binding.toolbar.setTitleTextColor(Color.BLACK)


    }

    private fun setBottomAppBar() {

        setSupportActionBar(binding.bottomAppBar)

    }

    private fun setBtnListeners() {
        binding.btnAddProduct.setOnClickListener {
            val productFragment = AddProductFragment()
            productFragment.show(supportFragmentManager, "add product")
        }
    }

    private fun loadBackEnd() {
        productDao = MyRoomDatabase.getInstance(applicationContext).productDao
        productRepository = ProductRepository(productDao)
        factory = ProductVMFactory(productRepository)
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

    }

    private fun loadFrontend() {

        adapter = ProductAdapter()

        binding.productsRecyclerView.setHasFixedSize(true)
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.productsRecyclerView.adapter = adapter


    }

    private fun showProducts() {
        productViewModel.allProducts.observe(this, Observer(adapter::putProducts))
        enableTouchFunctions()
    }

    private fun enableTouchFunctions() {
        itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val product: Product? =
                        adapter.getProductByPos(viewHolder.adapterPosition)

                    if (product != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@ProductsActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Produkti :" + product.name)

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("po") { _, _ ->

                            productViewModel.deleteProduct(product)
                            Toast.makeText(
                                applicationContext,
                                "produkti u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showProducts()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(binding.productsRecyclerView)
    }

}