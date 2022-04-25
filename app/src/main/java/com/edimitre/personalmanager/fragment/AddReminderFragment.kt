package com.edimitre.personalmanager.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.dao.ReminderDao
import com.edimitre.personalmanager.data.model.Reminder
import com.edimitre.personalmanager.data.repository.ReminderRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.utils.TimeUtils
import com.edimitre.personalmanager.data.viewmodel.ReminderViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.ReminderVMFactory
import com.edimitre.personalmanager.databinding.FragmentAddReminderBinding
import java.text.DateFormat
import java.text.SimpleDateFormat


class AddReminderFragment : AppCompatDialogFragment() {


    private var listener: AddReminderListener? = null

    private lateinit var reminderDao: ReminderDao

    private lateinit var reminderRepository: ReminderRepository

    private lateinit var factory: ReminderVMFactory

    private lateinit var reminderViewModel: ReminderViewModel

    private var _binding: FragmentAddReminderBinding? = null

    private val binding get() = _binding!!

    private var year: Int? = null

    private var month: Int? = null

    private var date: Int? = null

    private var hour: Int? = null

    private var minutes: Int? = null

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
        _binding = FragmentAddReminderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViewModel()

        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadViewModel() {

        reminderDao = MyRoomDatabase.getInstance(requireContext()).reminderDao
        reminderRepository = ReminderRepository(reminderDao)
        factory = ReminderVMFactory(reminderRepository)
        reminderViewModel = ViewModelProvider(this, factory)[ReminderViewModel::class.java]


    }

    private fun setListeners() {
        binding.btnOpenAlarmDatePicker.setOnClickListener {

            openDatePicker()

        }

        binding.btnAddReminder.setOnClickListener {

            if (inputIsOk()) {

                val timeInMillis =
                    TimeUtils().getTimeInMilliSeconds(year!!, month!!, date!!, hour!!, minutes!!)
                val description = binding.reminderDescriptionInput.text.toString()
                val reminder = Reminder(0, timeInMillis, description, true)
                listener!!.addReminder(reminder)

                dismiss()
            }


        }

        binding.btnCloseReminderDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun openDatePicker() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val dateDialog = DatePickerDialog(requireContext(), R.style.DialogTheme)
            dateDialog.setOnDateSetListener { _, y, m, d ->
                year = y
                month = m
                date = d
                showSelectedDate(
                    year!!,
                    month!!,
                    date!!,
                    TimeUtils().getCurrentHour(),
                    TimeUtils().getCurrentMinute()
                )
                openTimePicker()
            }
            dateDialog.show()
        }

    }

    private fun openTimePicker() {

        val timePickerDialog = TimePickerDialog(
            context, R.style.DialogTheme, { _, h, m ->
                hour = h
                minutes = m
                showSelectedDate(year!!, month!!, date!!, hour!!, minutes!!)
            },
            TimeUtils().getCurrentHour(), TimeUtils().getCurrentMinute(), true
        )

        timePickerDialog.show()

    }

    private fun inputIsOk(): Boolean {


        val description = binding.reminderDescriptionInput.text.toString()

        return when {

            year == null && month == null && date == null && hour == null -> {
                Toast.makeText(
                    requireContext(),
                    "dita edhe ora nuk mund te jene bosh",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "vendosni nje pershkrim", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            else -> {
                true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSelectedDate(year: Int, month: Int, day: Int, hour: Int, minute: Int) {

        @SuppressLint("SimpleDateFormat")
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")

        @SuppressLint("SimpleDateFormat")
        val timeFormat: DateFormat = SimpleDateFormat("HH:mm:ss")


        val rDate =
            dateFormat.format(TimeUtils().getTimeInMilliSeconds(year, month, day, hour, minute))

        val rTime =
            timeFormat.format(TimeUtils().getTimeInMilliSeconds(year, month, day, hour, minute))

        binding.selectedDateText.text = rDate

        binding.selectedTimeText.text = rTime
    }

    interface AddReminderListener {
        fun addReminder(reminder: Reminder?)
    }

    override fun onAttach(context: Context) {
        listener = try {
            context as AddReminderListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "shto listener")
        }
        super.onAttach(context)
    }


}
