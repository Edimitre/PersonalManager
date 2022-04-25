package com.edimitre.personalmanager.fragment


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.adapter.ProductAdapter
import com.edimitre.personalmanager.data.dao.DescriptionDao
import com.edimitre.personalmanager.data.dao.ProductDao
import com.edimitre.personalmanager.data.model.Description
import com.edimitre.personalmanager.data.model.Product
import com.edimitre.personalmanager.data.repository.DescriptionRepository
import com.edimitre.personalmanager.data.repository.ProductRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.viewmodel.DescriptionViewModel
import com.edimitre.personalmanager.data.viewmodel.ProductViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.DescriptionVMFactory
import com.edimitre.personalmanager.data.viewmodelfactory.ProductVMFactory
import com.edimitre.personalmanager.databinding.FragmentAddProductToExpenseBinding


class AddProductToExpenseFragment : DialogFragment() {


    private lateinit var addProductToExpenseListener: AddProductToExpenseListener

    private lateinit var prdAdapter: ProductAdapter

    private lateinit var factory: ProductVMFactory

    private lateinit var productRepository: ProductRepository

    private lateinit var productDao: ProductDao

    private lateinit var productViewModel: ProductViewModel


    private lateinit var descriptionVMFactory: DescriptionVMFactory

    private lateinit var descriptionRepository: DescriptionRepository

    private lateinit var descriptionDao: DescriptionDao

    private lateinit var descriptionViewModel: DescriptionViewModel


    private var _binding: FragmentAddProductToExpenseBinding? = null

    private val binding get() = _binding!!

    private lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductToExpenseBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadViewModel()

        addDataToSpinner()

        setToolbarSearchMode(false)

        showProducts()

        setBtnListeners()
    }

    private fun setBtnListeners() {
        binding.btnSearch.setOnClickListener {
            setToolbarSearchMode(true)
        }

        binding.btnOk.setOnClickListener {

            val txt = binding.searchedText.text.toString()
            if (txt.isNotEmpty()) {
                productViewModel.getProductByName(txt)
                    .observe(this, Observer(prdAdapter::putProducts))
            } else {

                showProducts()
            }
        }

        binding.btnClose.setOnClickListener {
            showProducts()
            setToolbarSearchMode(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadViewModel() {

        descriptionDao = MyRoomDatabase.getInstance(requireContext()).descriptionDao
        descriptionRepository = DescriptionRepository(descriptionDao)
        descriptionVMFactory = DescriptionVMFactory(descriptionRepository)
        descriptionViewModel =
            ViewModelProvider(this, descriptionVMFactory)[DescriptionViewModel::class.java]



        productDao = MyRoomDatabase.getInstance(requireContext()).productDao
        productRepository = ProductRepository(productDao)
        factory = ProductVMFactory(productRepository)
        productViewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]
    }

    private fun showProducts() {

        prdAdapter = ProductAdapter()
        binding.selectProductRecyclerview.setHasFixedSize(true)
        binding.selectProductRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.selectProductRecyclerview.adapter = prdAdapter

        productViewModel.allProducts.observe(this, Observer(prdAdapter::putProducts))

        enableTouchFunctions()

    }

    private fun addDataToSpinner() {


        val descriptionList: List<Description> = descriptionViewModel.descList!!

        val spinnerAdapter: ArrayAdapter<Description> =
            ArrayAdapter<Description>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                descriptionList
            )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.expenseTypeSpinner.adapter = spinnerAdapter
    }

    override fun onAttach(context: Context) {


        try {
            addProductToExpenseListener = context as AddProductToExpenseListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "shto listener")
        }

        super.onAttach(context)
    }

    interface AddProductToExpenseListener {

        fun addProductAndDescription(product: Product, description: Description)

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
                        prdAdapter.getProductByPos(viewHolder.adapterPosition)

                    if (product != null) {

                        val inputQty = EditText(requireContext())
                        inputQty.inputType = InputType.TYPE_CLASS_NUMBER
                        inputQty.hint = "vendosni sasine e produktit"

                        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                        alertDialog.setView(inputQty)

                        alertDialog.setTitle("Vendos sasine")
                        alertDialog.setPositiveButton("ok") { _, _ ->
                            val qty: Int = inputQty.text.toString().toInt()
                            product.quantity = qty

                            val description: Description =
                                binding.expenseTypeSpinner.selectedItem as Description
                            addProductToExpenseListener.addProductAndDescription(
                                product,
                                description
                            )

                            dismiss()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(binding.selectProductRecyclerview)
    }

    private fun setToolbarSearchMode(active: Boolean) {

        if (active) {
            binding.searchedText.visibility = View.VISIBLE
            binding.btnOk.visibility = View.VISIBLE
            binding.btnClose.visibility = View.VISIBLE
            binding.btnSearch.visibility = View.INVISIBLE
        } else {
            binding.searchedText.visibility = View.INVISIBLE
            binding.btnOk.visibility = View.INVISIBLE
            binding.btnClose.visibility = View.INVISIBLE
            binding.btnSearch.visibility = View.VISIBLE
        }


    }

}