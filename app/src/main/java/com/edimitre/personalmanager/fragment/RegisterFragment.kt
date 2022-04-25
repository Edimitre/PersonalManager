package com.edimitre.personalmanager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.edimitre.personalmanager.activity.MainActivity
import com.edimitre.personalmanager.data.dao.MainUserDao
import com.edimitre.personalmanager.data.model.MainUser
import com.edimitre.personalmanager.data.repository.MainUserRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.viewmodel.MainUserViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.MainUserVMFactory
import com.edimitre.personalmanager.databinding.FragmentRegisterBinding


class RegisterFragment : DialogFragment() {


    private lateinit var factory: MainUserVMFactory

    private lateinit var mainUserRepository: MainUserRepository

    private lateinit var mainUserDao: MainUserDao

    private lateinit var mainUserViewModel: MainUserViewModel

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViewModel()

        binding.btnSaveMainUser.setOnClickListener {
            val name = binding.inputName.text.toString()
            val amountOfMoney = binding.inputTotalAmountOfMoney.text.toString()


            if (!name.isEmpty() && !amountOfMoney.isEmpty()) {
                mainUserViewModel.saveMainUser(MainUser(1, name, amountOfMoney.toDouble()))
                dismiss()
                Toast.makeText(requireContext(), "Regjistrimi u krye me sukses", Toast.LENGTH_SHORT)
                    .show()

                val intent =
                    Intent(this@RegisterFragment.requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else if (!name.isEmpty() && amountOfMoney.isEmpty()) {
                mainUserViewModel.saveMainUser(MainUser(1, name, 0.0))
                dismiss()
                Toast.makeText(
                    requireContext(),
                    "Regjistrimi u krye me sukses,..te ardhurat jane 0 ",
                    Toast.LENGTH_SHORT
                ).show()
                val intent =
                    Intent(this@RegisterFragment.requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Emri nuk mund te jete bosh", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun loadViewModel() {
        mainUserDao = MyRoomDatabase.getInstance(requireContext()).mainUserDao
        mainUserRepository = MainUserRepository(mainUserDao)
        factory = MainUserVMFactory(mainUserRepository)
        mainUserViewModel = ViewModelProvider(this, factory)[MainUserViewModel::class.java]
    }

}


