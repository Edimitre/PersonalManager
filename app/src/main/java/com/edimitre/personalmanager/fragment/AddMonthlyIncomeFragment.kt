package com.edimitre.personalmanager.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.activity.MonthlyIncomeActivity
import com.edimitre.personalmanager.data.dao.MonthlyIncomeDao
import com.edimitre.personalmanager.data.model.MonthlyIncome
import com.edimitre.personalmanager.data.model.MonthlyIncomeType
import com.edimitre.personalmanager.data.repository.MonthlyIncomeRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.utils.TimeUtils
import com.edimitre.personalmanager.data.viewmodel.MonthlyIncomeViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.MonthlyIncomeVMFactory
import com.edimitre.personalmanager.databinding.FragmentAddMonthlyIncomeBinding


class AddMonthlyIncomeFragment : AppCompatDialogFragment() {


    private var listener: AddMonthlyIncomeListener? = null

    private lateinit var monthlyIncomeDao: MonthlyIncomeDao

    private lateinit var monthlyIncomeRepository: MonthlyIncomeRepository

    private lateinit var factory: MonthlyIncomeVMFactory

    private lateinit var monthlyIncomeViewModel: MonthlyIncomeViewModel

    private var _binding: FragmentAddMonthlyIncomeBinding? = null

    private val binding get() = _binding!!

    private var monthlyIncomeTypeList: List<MonthlyIncomeType>? = null

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
        _binding = FragmentAddMonthlyIncomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViewModel()

        fillSpinnerWithMonthlyIncomeType()

        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.btnAddMonthlyIncome.setOnClickListener {

            if (inputIsOk()) {

                val monthlyIncomeType =
                    binding.monthlyIncomeTypeSpinner.selectedItem as MonthlyIncomeType

                val description = binding.monthlyIncomeDescriptionInput.text.toString()

                val value = binding.monthlyIncomeValueInput.text.toString().toDouble()

                val monthlyIncome = MonthlyIncome(
                    0, description, value,
                    TimeUtils().getCurrentYear(),
                    TimeUtils().getCurrentMonth(),
                    TimeUtils().getCurrentDay(),
                    TimeUtils().getCurrentHour(),
                    TimeUtils().getCurrentMinute(),
                    monthlyIncomeType
                )

                listener!!.addMonthlyIncome(monthlyIncome)

            }

            dismiss()

        }

        binding.btnCloseAddMonthlyIncomeDialog.setOnClickListener {
            dismiss()
        }
    }

    private fun inputIsOk(): Boolean {

        val value = binding.monthlyIncomeValueInput.text.toString()
        val description = binding.monthlyIncomeDescriptionInput.text.toString()

        return when {

            value.isEmpty() -> {
                Toast.makeText(
                    MonthlyIncomeActivity(),
                    "vlera nuk mund te jete bosh",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            description.isEmpty() -> {
                Toast.makeText(
                    MonthlyIncomeActivity(),
                    "vendosni nje pershkrim",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loadViewModel() {

        monthlyIncomeDao = MyRoomDatabase.getInstance(requireContext()).monthlyIncomeDao
        monthlyIncomeRepository = MonthlyIncomeRepository(monthlyIncomeDao)
        factory = MonthlyIncomeVMFactory(monthlyIncomeRepository)
        monthlyIncomeViewModel =
            ViewModelProvider(this, factory)[MonthlyIncomeViewModel::class.java]


    }

    private fun fillSpinnerWithMonthlyIncomeType() {

        monthlyIncomeTypeList = monthlyIncomeViewModel.monthlyIncomeTypesList

        val spinnerAdapter: ArrayAdapter<MonthlyIncomeType> =
            ArrayAdapter<MonthlyIncomeType>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                monthlyIncomeTypeList!!
            )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.monthlyIncomeTypeSpinner.adapter = spinnerAdapter
    }

    interface AddMonthlyIncomeListener {
        fun addMonthlyIncome(monthlyIncome: MonthlyIncome?)
    }

    override fun onAttach(context: Context) {
        listener = try {
            context as AddMonthlyIncomeListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "shto listener")
        }
        super.onAttach(context)
    }


}
