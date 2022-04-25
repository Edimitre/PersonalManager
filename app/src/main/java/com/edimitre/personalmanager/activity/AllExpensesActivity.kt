package com.edimitre.personalmanager.activity

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.adapter.DescriptionAdapter
import com.edimitre.personalmanager.adapter.ExpenseAdapter
import com.edimitre.personalmanager.adapter.ProductInExpenseAdapter
import com.edimitre.personalmanager.data.dao.DescriptionDao
import com.edimitre.personalmanager.data.dao.ExpenseDao
import com.edimitre.personalmanager.data.dao.MainUserDao
import com.edimitre.personalmanager.data.model.Description
import com.edimitre.personalmanager.data.model.Expense
import com.edimitre.personalmanager.data.model.Product
import com.edimitre.personalmanager.data.repository.DescriptionRepository
import com.edimitre.personalmanager.data.repository.ExpenseRepository
import com.edimitre.personalmanager.data.repository.MainUserRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.utils.TimeUtils
import com.edimitre.personalmanager.data.viewmodel.DescriptionViewModel
import com.edimitre.personalmanager.data.viewmodel.ExpenseViewModel
import com.edimitre.personalmanager.data.viewmodel.MainUserViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.DescriptionVMFactory
import com.edimitre.personalmanager.data.viewmodelfactory.ExpenseVMFactory
import com.edimitre.personalmanager.data.viewmodelfactory.MainUserVMFactory
import com.edimitre.personalmanager.databinding.ActivityExpenseBinding
import com.edimitre.personalmanager.fragment.AddProductToExpenseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


@Suppress("UNCHECKED_CAST")
class AllExpensesActivity() : AppCompatActivity(),
    AddProductToExpenseFragment.AddProductToExpenseListener,
    ExpenseAdapter.OnExpenseClickListener {


    // expense
    private lateinit var expenseRepository: ExpenseRepository

    private lateinit var expenseDao: ExpenseDao

    private lateinit var expFactory: ExpenseVMFactory

    private lateinit var expenseViewModel: ExpenseViewModel

    private lateinit var expAdapter: ExpenseAdapter


    // description
    private lateinit var descriptionViewModel: DescriptionViewModel

    private lateinit var descFactory: DescriptionVMFactory

    private lateinit var descriptionRepository: DescriptionRepository

    private lateinit var descriptionDao: DescriptionDao

    private lateinit var descAdapter: DescriptionAdapter


    // mainuser
    private lateinit var mainUserDao: MainUserDao

    private lateinit var mainUserRepository: MainUserRepository

    private lateinit var mnFactory: MainUserVMFactory

    private lateinit var mainUserViewModel: MainUserViewModel


    private lateinit var binding: ActivityExpenseBinding

    private var saveExpenseItem: MenuItem? = null

    private var clearExpenseItem: MenuItem? = null

    private var datePickerItem: MenuItem? = null

    private var clearSelectedExpenses: MenuItem? = null

    private var expense: Expense? = null

    private val productList: ArrayList<Product> = ArrayList()

    private lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpenseBinding.inflate(layoutInflater)

        setContentView(binding.root)


        start()
    }

    private fun start() {

        loadExpensesViewModel()

        loadDescriptionViewModel()

        loadMainUserViewModel()

        setTopToolbar()

        setBottomAppBar()

        showTodayExpenses()

        setBtnListeners()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)



        saveExpenseItem = menu!!.findItem(R.id.btn_save_expense)
        clearSelectedExpenses = menu.findItem(R.id.btn_close_selected_expenses)
        clearExpenseItem = menu.findItem(R.id.btn_clear_list)
        datePickerItem = menu.findItem(R.id.btn_open_expense_date_picker)


        clearSelectedExpenses!!.isVisible = false

        // it hides save expense btn beacuse the list is empty
        showSaveExpenseButton(productList)
        return true
    }

    private fun openDatePicker() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val dateDialog = DatePickerDialog(this, R.style.DialogTheme)
            dateDialog.setOnDateSetListener { _, year, month, day ->

                expenseViewModel.getAllExpensesByYearMonthAndDateLiveData(year, month, day)!!
                    .observe(this) { expAdapter.putExpenses(it as List<Expense>) }


                var spentValue = expenseViewModel.getValueOfExpenseListByDate(year, month, day)

                if (spentValue != null) {
                    binding.toolbar.title = "Vlera :" + spentValue
                } else {
                    binding.toolbar.title = "Shpenzuar : 0"
                }

                datePickerItem!!.isVisible = false
                clearSelectedExpenses!!.isVisible = true

                disableTouchFunctions()

            }
            dateDialog.show()
        }

    }

    private fun setTopToolbar() {

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Shpenzuar sot " + getTodaySpentValue()
        binding.toolbar.setTitleTextColor(Color.BLACK)


    }

    private fun setBottomAppBar() {

        setSupportActionBar(binding.bottomAppBar)

    }

    private fun setBtnListeners() {

        binding.btnAddExpense.setOnClickListener {


            openExpenseDialog()
        }

        binding.bottomAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->

            when (item.itemId) {

                R.id.btn_save_expense -> {
                    saveExpense(expense!!)
                }

                R.id.btn_clear_list -> {
                    productList.clear()

                    showSaveExpenseButton(productList)

                    showTodayExpenses()
                }

                R.id.btn_open_expense_date_picker -> {

                    openDatePicker()
                }

                R.id.btn_close_selected_expenses -> {
                    showTodayExpenses()
                    binding.toolbar.title = "Shpenzuar sot " + getTodaySpentValue()
                    clearSelectedExpenses!!.isVisible = false
                    datePickerItem!!.isVisible = true
                }

            }



            true
        })

    }

    private fun openExpenseDialog() {
        val productFragment = AddProductToExpenseFragment()
        productFragment.show(supportFragmentManager, "add product")

    }

    private fun showTodayExpenses() {

        expAdapter = ExpenseAdapter(this)
        binding.expensesRecyclerView.setHasFixedSize(true)
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.expensesRecyclerView.adapter = expAdapter
        expenseViewModel.getAllExpensesByYearMonthAndDateLiveData(
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth(), TimeUtils().getCurrentDay()
        )!!.observe(this) { expAdapter.putExpenses(it as List<Expense>) }

        enableTouchFunctions()
    }

    private fun showSaveExpenseButton(productList: List<Product>) {
        if (productList.isEmpty()) {
            saveExpenseItem!!.isVisible = false
            clearExpenseItem!!.isVisible = false
            datePickerItem!!.isVisible = true
        } else {
            saveExpenseItem!!.isVisible = true
            clearExpenseItem!!.isVisible = true
            datePickerItem!!.isVisible = false
        }
    }

    private fun showAddedProductsToExpense(productList: List<Product>) {

        val adapter = ProductInExpenseAdapter()
        binding.expensesRecyclerView.setHasFixedSize(true)
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.expensesRecyclerView.adapter = adapter

        adapter.putProducts(productList)

    }

    // listener method of addProductToExpense fragment
    override fun addProductAndDescription(product: Product, description: Description) {

        // product and description comes from dialogfragment listener
        productList.add(product)


        expense = Expense(
            0,
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth(),
            TimeUtils().getCurrentDay(),
            TimeUtils().getCurrentHour(),
            TimeUtils().getCurrentMinute(),
            productList,
            0.0,
            description
        )


        // shows only product from current expense
        showAddedProductsToExpense(productList)

        // it sets spent value of all products
        setExpenseValue(expense!!)

        // it shows save button only if the list has objects inside
        showSaveExpenseButton(productList)

    }

    // listener method in adapter
    override fun onExpenseClicked(expense: Expense) {
        Toast.makeText(this, "shpenzimi " + expense.productList, Toast.LENGTH_SHORT).show()
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
                    val expense: Expense? =
                        expAdapter.getExpenseByPos(viewHolder.adapterPosition)

                    if (expense != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@AllExpensesActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Shpenzimi :" + expense.description)

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("po") { _, _ ->

                            expenseViewModel.deleteExpense(expense)
                            mainUserViewModel.addMoney(expense.spentValue)
                            Toast.makeText(
                                applicationContext,
                                "shpenzimi u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showTodayExpenses()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(binding.expensesRecyclerView)
    }

    protected fun disableTouchFunctions() {
        itemTouchHelper.attachToRecyclerView(null)
    }

    protected fun getTodaySpentValue(): Int? {

        var value = expenseViewModel.getValueOfExpenseListByDate(
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth(),
            TimeUtils().getCurrentDay()
        )

        if (value == null)
            value = 0
        return value

    }

    protected fun loadExpensesViewModel() {
        expenseDao = MyRoomDatabase.getInstance(applicationContext).expenseDao
        expenseRepository = ExpenseRepository(expenseDao)
        expFactory = ExpenseVMFactory(expenseRepository)
        expenseViewModel = ViewModelProvider(this, expFactory)[ExpenseViewModel::class.java]

    }

    protected fun loadDescriptionViewModel() {

        descriptionDao = MyRoomDatabase.getInstance(applicationContext).descriptionDao
        descriptionRepository = DescriptionRepository(descriptionDao)
        descFactory = DescriptionVMFactory(descriptionRepository)
        descriptionViewModel =
            ViewModelProvider(this, descFactory)[DescriptionViewModel::class.java]

    }

    private fun loadMainUserViewModel() {
        mainUserDao = MyRoomDatabase.getInstance(applicationContext).mainUserDao
        mainUserRepository = MainUserRepository(mainUserDao)
        mnFactory = MainUserVMFactory(mainUserRepository)
        mainUserViewModel = ViewModelProvider(this, mnFactory)[MainUserViewModel::class.java]

    }

    protected fun saveExpense(expense: Expense) {

        expenseViewModel.saveExpense(expense)

        saveExpenseItem!!.setVisible(false)
        clearExpenseItem!!.setVisible(false)

        showTodayExpenses()

        binding.toolbar.title = "Shpenzuar sot " + getTodaySpentValue()

        mainUserViewModel.spentMoney(expense.spentValue)

        productList.clear()
    }

    protected fun setExpenseValue(expense: Expense) {
        val totalCost = expense.productList.sumOf { it.quantity * it.price }
        expense.spentValue = totalCost

        binding.toolbar.title = "Totali : " + expense.spentValue

    }

}
