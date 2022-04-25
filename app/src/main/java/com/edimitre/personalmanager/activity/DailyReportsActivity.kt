package com.edimitre.personalmanager.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.adapter.DailyReportsAdapter
import com.edimitre.personalmanager.data.dao.DailyReportDao
import com.edimitre.personalmanager.data.model.DailyReport
import com.edimitre.personalmanager.data.model.Expense
import com.edimitre.personalmanager.data.repository.DailyReportRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.viewmodel.DailyReportsViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.DailyReportsVMFactory
import com.edimitre.personalmanager.databinding.ActivityDailyReportsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


@Suppress("UNCHECKED_CAST")
class DailyReportsActivity : AppCompatActivity() {

    // expense
    private lateinit var dailyReportRepository: DailyReportRepository

    private lateinit var dailyReportDao: DailyReportDao

    private lateinit var factory: DailyReportsVMFactory

    private lateinit var dailyReportsViewModel: DailyReportsViewModel

    private lateinit var adapter: DailyReportsAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var binding: ActivityDailyReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDailyReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        start()
    }


    private fun start() {

        loadViewModel()

        showDailyReports()

        setToolbar()

        setBottomAppBar()
    }

    private fun showDailyReports() {

        adapter = DailyReportsAdapter()
        binding.dailyReportRecyclerView.setHasFixedSize(true)
        binding.dailyReportRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.dailyReportRecyclerView.adapter = adapter
        dailyReportsViewModel.allDailyReports!!.observe(this) { adapter.putDailyReports(it as List<DailyReport>) }

        enableTouchFunctions()

    }

    private fun loadViewModel() {

        dailyReportDao = MyRoomDatabase.getInstance(applicationContext).dailyReportDao
        dailyReportRepository = DailyReportRepository(dailyReportDao)
        factory = DailyReportsVMFactory(dailyReportRepository)
        dailyReportsViewModel = ViewModelProvider(this, factory)[DailyReportsViewModel::class.java]

    }

    private fun setToolbar() {

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Raportet Ditore "
        binding.toolbar.setTitleTextColor(Color.BLACK)


    }

    private fun setBottomAppBar() {

        setSupportActionBar(binding.bottomAppBar)

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
                    val dailyReport: DailyReport? =
                        adapter.getDailyReportByPos(viewHolder.adapterPosition)

                    if (dailyReport != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@DailyReportsActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Raporti i dates :" + dailyReport.date)

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("po") { _, _ ->

                            dailyReportsViewModel.deleteDailyReport(dailyReport)
                            Toast.makeText(
                                applicationContext,
                                "raporti u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showDailyReports()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(binding.dailyReportRecyclerView)
    }


}