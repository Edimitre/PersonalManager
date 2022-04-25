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
import com.edimitre.personalmanager.adapter.ReminderAdapter
import com.edimitre.personalmanager.data.dao.ReminderDao
import com.edimitre.personalmanager.data.model.Reminder
import com.edimitre.personalmanager.data.repository.ReminderRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.viewmodel.ReminderViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.ReminderVMFactory
import com.edimitre.personalmanager.databinding.ActivityReminderBinding
import com.edimitre.personalmanager.fragment.AddReminderFragment
import com.edimitre.personalmanager.system.SystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@Suppress("NAME_SHADOWING")
class ReminderActivity : AppCompatActivity(), AddReminderFragment.AddReminderListener {


    private lateinit var reminderDao: ReminderDao

    private lateinit var reminderRepository: ReminderRepository

    private lateinit var factory: ReminderVMFactory

    private lateinit var reminderViewModel: ReminderViewModel

    private lateinit var adapter: ReminderAdapter

    private lateinit var binding: ActivityReminderBinding

    private lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReminderBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadViewModel()

        loadAdapter()

        setTopToolbar()

        setBottomAppBar()

        setListeners()

        showAllReminders()
    }

    private fun setTopToolbar() {

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Kujtesat"
        binding.toolbar.setTitleTextColor(Color.BLACK)


    }

    private fun setBottomAppBar() {

        setSupportActionBar(binding.bottomAppBar)

    }

    private fun loadViewModel() {

        reminderDao = MyRoomDatabase.getInstance(this).reminderDao
        reminderRepository = ReminderRepository(reminderDao)
        factory = ReminderVMFactory(reminderRepository)
        reminderViewModel = ViewModelProvider(this, factory)[ReminderViewModel::class.java]


    }

    private fun loadAdapter() {
        adapter = ReminderAdapter()

        binding.reminderRecyclerView.setHasFixedSize(true)
        binding.reminderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reminderRecyclerView.adapter = adapter
    }

    private fun setListeners() {

        binding.btnOpenReminderDialog.setOnClickListener {
            val reminderFragment = AddReminderFragment()
            reminderFragment.show(supportFragmentManager, "add reminder")
        }
    }

    private fun showAllReminders() {

        reminderViewModel.allReminders!!.observe(this) { adapter.putReminders(it as List<Reminder>) }

        enableTouchFunctions()
    }

    private fun isReminderValid(reminder: Reminder): Boolean {
        return reminder.alarmTimeInMillis >= System.currentTimeMillis()
    }

    override fun addReminder(reminder: Reminder?) {

        if (reminder != null && isReminderValid(reminder)) {

            reminderViewModel.saveReminder(reminder)
            val systemService = SystemService(this)
            systemService.setAlarm(reminder.alarmTimeInMillis)

        }

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
                    val reminder: Reminder? =
                        adapter.getReminderByPos(viewHolder.adapterPosition)

                    if (reminder != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@ReminderActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Kujtesa :" + reminder.description)

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("po") { _, _ ->

                            reminderViewModel.deleteReminder(reminder)

                            val systemService = SystemService(this@ReminderActivity)
                            systemService.cancelAllAlarms()

                            val reminder: Reminder? = reminderViewModel.getFirstReminder()

                            if (reminder != null) {
                                systemService.setAlarm(reminder.alarmTimeInMillis)
                            }

                            Toast.makeText(applicationContext, "kujtesa u fshi", Toast.LENGTH_SHORT)
                                .show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showAllReminders()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(binding.reminderRecyclerView)
    }
}