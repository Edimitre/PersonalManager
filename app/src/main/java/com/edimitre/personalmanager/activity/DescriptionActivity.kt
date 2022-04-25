package com.edimitre.personalmanager.activity

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.adapter.DescriptionAdapter
import com.edimitre.personalmanager.data.dao.DescriptionDao
import com.edimitre.personalmanager.data.model.Description
import com.edimitre.personalmanager.data.repository.DescriptionRepository
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.data.viewmodel.DescriptionViewModel
import com.edimitre.personalmanager.data.viewmodelfactory.DescriptionVMFactory
import com.edimitre.personalmanager.databinding.ActivityDescriptionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class DescriptionActivity : AppCompatActivity() {

    private lateinit var descriptionViewModel: DescriptionViewModel

    private lateinit var factory: DescriptionVMFactory

    private lateinit var descriptionRepository: DescriptionRepository

    private lateinit var descriptionDao: DescriptionDao

    private lateinit var adapter: DescriptionAdapter

    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var binding: ActivityDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)





        start()


    }

    private fun start() {

        loadDescriptionViewModel()

        setToolBar()

        setBottomAppBar()

        loadRecyclerView()

        showDescriptions()

    }

    private fun setToolBar() {

        setSupportActionBar(binding.descriptionToolbar)

        binding.descriptionToolbar.title = "Pershkrimet"

        binding.descriptionToolbar.setTitleTextColor(Color.BLACK)
    }

    private fun setBottomAppBar() {

        setSupportActionBar(binding.bottomAppBar)

    }

    private fun loadDescriptionViewModel() {
        descriptionDao = MyRoomDatabase.getInstance(applicationContext).descriptionDao
        descriptionRepository = DescriptionRepository(descriptionDao)
        factory = DescriptionVMFactory(descriptionRepository)
        descriptionViewModel = ViewModelProvider(this, factory)[DescriptionViewModel::class.java]

    }

    private fun loadRecyclerView() {

        adapter = DescriptionAdapter()

        binding.descriptionsRecyclerView.setHasFixedSize(true)
        binding.descriptionsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.descriptionsRecyclerView.adapter = adapter

        setBtnListeners()


    }

    private fun setBtnListeners() {

        binding.btnAddDescription.setOnClickListener {
            openAddDescriptionDialog()
        }

    }

    private fun openAddDescriptionDialog() {
        val inputName = EditText(this)
        inputName.hint = "Emri Pershkrimit"
        inputName.inputType = InputType.TYPE_CLASS_TEXT

        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
        )
        dialog.setView(inputName)
        dialog.setTitle("Shto Pershkrim")
        dialog.setMessage(
            "Vendosni nje pershkrim\n" +
                    "si psh :PAZAR_SHPORTE, KARBURANT,KESTI_KREDISE, etj"
        )
        dialog.setPositiveButton("Shto") { _, _ ->

            val name = inputName.text.toString().trim()
            if (name.isNotEmpty()) {
                saveDescription(name.uppercase(Locale.getDefault()))
                Toast.makeText(this, "pershkrimi : $name u shtua me sukses", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Emri nuk mund te jete bosh", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.setNegativeButton("Mbyll") { _, _ ->

        }
        dialog.show()
    }

    private fun saveDescription(name: String) {
        descriptionViewModel.saveDescription(Description(0, name))
    }

    private fun showDescriptions() {

        descriptionViewModel.allDescriptions.observe(this) { adapter.putDescriptions(it) }

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
                    val description: Description? =
                        adapter.getDescriptionByPos(viewHolder.adapterPosition)

                    if (description != null) {

                        val alertDialog = MaterialAlertDialogBuilder(
                            this@DescriptionActivity,
                            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background
                        )

                        alertDialog.setTitle("Pershkrimi :" + description.name)

                        alertDialog.setMessage(
                            "Deshironi Ta Fshini ?\n" +
                                    "KUJDES! ..ky veperim nuk mund te rikthehet!"
                        )
                        alertDialog.setPositiveButton("po") { _, _ ->

                            descriptionViewModel.deleteDescription(description)
                            Toast.makeText(
                                applicationContext,
                                "pershkrimi u fshi",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        alertDialog.setNegativeButton("mbyll") { _, _ ->


                        }

                        alertDialog.setOnDismissListener {
                            showDescriptions()
                        }

                        alertDialog.show()

                    }


                }
            })

        itemTouchHelper.attachToRecyclerView(binding.descriptionsRecyclerView)
    }


}