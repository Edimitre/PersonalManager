package com.edimitre.personalmanager.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edimitre.personalmanager.adapter.MonthlyIncomeAdapter
import com.edimitre.personalmanager.data.dao.MainUserDao
import com.edimitre.personalmanager.data.dao.MonthlyIncomeDao
import com.edimitre.personalmanager.data.model.MonthlyIncome
import com.edimitre.personalmanager.data.repository.MainUserRepository
import com.edimitre.personalmanager.data.repository.MonthlyIncomeRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.utils.TimeUtils
import com.edimitre.personalmanager.data.viewmodel.MainUserViewModel
import com.edimitre.personalmanager.data.viewmodel.MonthlyIncomeViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.MainUserVMFactory
import com.edimitre.personalmanager.data.viewmodelfactory.MonthlyIncomeVMFactory
import com.edimitre.personalmanager.databinding.ActivityMonthlyIncomeBinding
import com.edimitre.personalmanager.fragment.AddMonthlyIncomeFragment
import com.edimitre.personalmanager.system.SystemService

@Suppress("UNCHECKED_CAST")
class MonthlyIncomeActivity : AppCompatActivity(),
    AddMonthlyIncomeFragment.AddMonthlyIncomeListener {

    // monthly income
    private lateinit var monthlyIncomeRepository: MonthlyIncomeRepository

    private lateinit var monthlyIncomeDao: MonthlyIncomeDao

    private lateinit var factory: MonthlyIncomeVMFactory

    private lateinit var monthlyIncomeViewModel: MonthlyIncomeViewModel

    private lateinit var adapter: MonthlyIncomeAdapter

    private lateinit var binding: ActivityMonthlyIncomeBinding


    // mainuser
    private lateinit var mainUserDao: MainUserDao

    private lateinit var mainUserRepository: MainUserRepository

    private lateinit var mnFactory: MainUserVMFactory

    private lateinit var mainUserViewModel: MainUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMonthlyIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        loadExpensesViewModel()

        loadMainUserViewModel()

        setListeners()

        setTopToolbar()

        setBottomAppBar()

        showMonthlyIncomes()
    }

    override fun onPause() {
        super.onPause()

        val monthlyIncome = monthlyIncomeViewModel.getValueOfIncomesByMonth(TimeUtils().getCurrentYear(),TimeUtils().getCurrentMonth())

        if (monthlyIncome!= null && monthlyIncome>1){
            val systemService = SystemService(this)
            systemService.scheduleDailyReportAlarm()
        }

    }

    private fun loadExpensesViewModel() {
        monthlyIncomeDao = MyRoomDatabase.getInstance(applicationContext).monthlyIncomeDao
        monthlyIncomeRepository = MonthlyIncomeRepository(monthlyIncomeDao)
        factory = MonthlyIncomeVMFactory(monthlyIncomeRepository)
        monthlyIncomeViewModel =
            ViewModelProvider(this, factory)[MonthlyIncomeViewModel::class.java]
    }

    private fun setTopToolbar() {

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Te Ardhurat "
        binding.toolbar.setTitleTextColor(Color.BLACK)


    }

    private fun setBottomAppBar() {

        setSupportActionBar(binding.bottomAppBar)

    }

    private fun setListeners() {
        binding.btnAddMonthlyIncome.setOnClickListener {
            val monthlyIncomeFragment = AddMonthlyIncomeFragment()
            monthlyIncomeFragment.show(supportFragmentManager, "add monthly income")
        }

    }

    override fun addMonthlyIncome(monthlyIncome: MonthlyIncome?) {


        monthlyIncomeViewModel.saveMonthlyIncome(monthlyIncome!!)

        mainUserViewModel.addMoney(monthlyIncome.value)


    }

    private fun showMonthlyIncomes() {

        adapter = MonthlyIncomeAdapter()

        binding.monthlyIncomeRecyclerView.setHasFixedSize(true)
        binding.monthlyIncomeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.monthlyIncomeRecyclerView.adapter = adapter
        monthlyIncomeViewModel.monthlyIncomeLiveData!!.observe(this) { adapter.putMonthlyIncomes(it as List<MonthlyIncome>) }

    }

    private fun loadMainUserViewModel() {
        mainUserDao = MyRoomDatabase.getInstance(applicationContext).mainUserDao
        mainUserRepository = MainUserRepository(mainUserDao)
        mnFactory = MainUserVMFactory(mainUserRepository)
        mainUserViewModel = ViewModelProvider(this, mnFactory)[MainUserViewModel::class.java]

    }

}