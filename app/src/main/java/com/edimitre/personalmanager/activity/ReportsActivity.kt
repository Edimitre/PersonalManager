package com.edimitre.personalmanager.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.dao.ExpenseDao
import com.edimitre.personalmanager.data.dao.MainUserDao
import com.edimitre.personalmanager.data.repository.ExpenseRepository
import com.edimitre.personalmanager.data.repository.MainUserRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.utils.TimeUtils
import com.edimitre.personalmanager.data.viewmodel.ExpenseViewModel
import com.edimitre.personalmanager.data.viewmodel.MainUserViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.ExpenseVMFactory
import com.edimitre.personalmanager.data.viewmodelfactory.MainUserVMFactory
import com.edimitre.personalmanager.databinding.ActivityReportsBinding
import com.edimitre.personalmanager.fragment.RegisterFragment
import com.edimitre.personalmanager.system.SystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class ReportsActivity : AppCompatActivity() {

    // expense
    private lateinit var expenseDao: ExpenseDao

    private lateinit var expenseRepository: ExpenseRepository

    private lateinit var expFactory: ExpenseVMFactory

    private lateinit var expenseViewModel: ExpenseViewModel

    // user

    private lateinit var mainUserDao: MainUserDao

    private lateinit var mainUserRepository: MainUserRepository

    private lateinit var mnFactory: MainUserVMFactory

    private lateinit var mainUserViewModel: MainUserViewModel


    // toolbar menu

    private lateinit var profileItem: MenuItem

    private lateinit var reloadItem: MenuItem

    private lateinit var backUpDbItem: MenuItem

    private lateinit var searchItem: MenuItem

    private lateinit var clearSearchItem: MenuItem


    // binding

    private lateinit var binding: ActivityReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        start()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        profileItem = menu!!.findItem(R.id.btn_toolbar_profile)
        reloadItem = menu.findItem(R.id.btn_reload_db)
        backUpDbItem = menu.findItem(R.id.btn_back_up_db)
        searchItem = menu.findItem(R.id.btn_toolbar_search)
        clearSearchItem = menu.findItem(R.id.btn_clear_search)


        if (backUpExist()) {
            profileItem.isVisible = false
            reloadItem.isVisible = true
            backUpDbItem.isVisible = true
            searchItem.isVisible = false
            clearSearchItem.isVisible = false
        } else {
            profileItem.isVisible = false
            reloadItem.isVisible = false
            backUpDbItem.isVisible = true
            searchItem.isVisible = false
            clearSearchItem.isVisible = false
        }

        binding.btnNoProfile.isVisible = mainUserViewModel.getMainUser() == null
        binding.btnWatchProfile.isVisible = mainUserViewModel.getMainUser() != null

        binding.noProfileText.isVisible = mainUserViewModel.getMainUser() == null
        binding.btnGoToMonthlyIncomeActivity.isVisible = mainUserViewModel.getMainUser() != null

        binding.noProfileReportsText.isVisible = mainUserViewModel.getMainUser() == null
        binding.btnGoToReportsActivity.isVisible = mainUserViewModel.getMainUser() != null




        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.btn_back_up_db -> {

                backUpDb(this)

            }

            R.id.btn_reload_db -> {
                importDb(this)
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun start() {

        loadExpensesViewModel()
        loadMainUserViewModel()


        setToolbar()

        showReports()

        setListeners()


    }

    private fun setToolbar() {
        binding.toolbar.title = "PersonalManager"
        binding.toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(binding.toolbar)

    }

    private fun backUpExist(): Boolean {
        val db2 = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "PersonalManagerDatabase.db"
        )
        return db2.exists()
    }

    private fun setListeners() {

        binding.btnWatchProfile.setOnClickListener {

            openMainUserReportDialog()

        }

        binding.btnNoProfile.setOnClickListener {
            openRegisterDialog()
        }

        binding.btnGoToMonthlyIncomeActivity.setOnClickListener {

            intent = Intent(this, MonthlyIncomeActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoToReportsActivity.setOnClickListener {
            intent = Intent(this, DailyReportsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showReports() {
        binding.nrOfExpensesThisMonthText.text =
            expenseViewModel.getSizeOfExpenseListByMonth(
                TimeUtils().getCurrentYear(),
                TimeUtils().getCurrentMonth()
            )
                .toString()


        binding.valueOfExpensesThisMonthText.text =
            expenseViewModel.getValueOfExpenseListByMonth(
                TimeUtils().getCurrentYear(),
                TimeUtils().getCurrentMonth()
            )
                .toString()


        binding.nrOfExpensesThisYearText.text =
            expenseViewModel.getSizeOfExpenseListByYear(TimeUtils().getCurrentYear())
                .toString()


        binding.valueOfExpensesThisYearText.text =
            expenseViewModel.getValueOfExpenseListByYear(TimeUtils().getCurrentYear())
                .toString()

        val biggestExpense = expenseViewModel.getBiggestByYearAndMonth(
            TimeUtils().getCurrentYear(),
            TimeUtils().getCurrentMonth()
        )

        if (biggestExpense != null) {
            binding.biggestExpenseText.text = biggestExpense.description.toString()
            binding.valueOfBiggestExpenseText.text = biggestExpense.spentValue.toString()
        }




        binding.btnWatchProfile.setOnClickListener {


        }
    }

    private fun openMainUserReportDialog() {

        val mainUser = mainUserViewModel.getMainUser()

        if (mainUser != null) {

            val dialog = MaterialAlertDialogBuilder(
                this,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
            )

            dialog.setTitle("Pershendetje \n" + mainUser.name.uppercase())

            dialog.setMessage("Vlera Totale e Lekeve eshte :\n" + mainUser.totalAmountOfMoney + " lek")


            dialog.setNegativeButton("Mbyll") { _, _ ->

            }

            dialog.show()

        }

    }

    private fun openRegisterDialog() {
        val productFragment = RegisterFragment()
        productFragment.show(supportFragmentManager, "add product")

    }

    private fun loadExpensesViewModel() {
        expenseDao = MyRoomDatabase.getInstance(applicationContext).expenseDao
        expenseRepository = ExpenseRepository(expenseDao)
        expFactory = ExpenseVMFactory(expenseRepository)
        expenseViewModel = ViewModelProvider(this, expFactory)[ExpenseViewModel::class.java]

    }

    private fun loadMainUserViewModel() {
        mainUserDao = MyRoomDatabase.getInstance(applicationContext).mainUserDao
        mainUserRepository = MainUserRepository(mainUserDao)
        mnFactory = MainUserVMFactory(mainUserRepository)
        mainUserViewModel = ViewModelProvider(this, mnFactory)[MainUserViewModel::class.java]

    }

    private fun backUpDb(context: Context) {
        val dialog = MaterialAlertDialogBuilder(
            context,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )

        dialog.setTitle("Ruaj Databazen")
        dialog.setMessage(
            "Aplikacioni e ruan vete databasen ..\n" +
                    "here pas here \npor ketu e ruani edhe manualisht\nnqs ju duhet per ndonje arsye"
        )
        dialog.setPositiveButton("Ruaj") { _, _ ->


            val systemService = SystemService(context)
            systemService.exportDatabase()
        }
        dialog.setNegativeButton("Mbyll") { _, _ ->

        }

        dialog.show()
    }

    private fun importDb(context: Context) {

        val dialog = MaterialAlertDialogBuilder(
            context,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )

        dialog.setTitle("Ngarko Databazen ?")
        dialog.setMessage(
            "Nqs keni hequr aplikacionin \n" +
                    "per ndonje arsye ..\nketu ngarkon databazen e fundit\n" +
                    "qe eshte ruajtur\ndmth ngarkon te dhenat tuaja \n" +
                    "qe kane qene me pare \n" +
                    "nqs nuk keni hequr dhe instaluar aplikacionin \n" +
                    "nuk keni pse e kryeni kete veprim \n" +
                    "shtypni mbyll"
        )
        dialog.setPositiveButton("Ngarko") { _, _ ->


            val systemService = SystemService(context)
            systemService.importDatabase()

        }
        dialog.setNegativeButton("Mbyll") { _, _ ->

        }

        dialog.show()

    }

}

