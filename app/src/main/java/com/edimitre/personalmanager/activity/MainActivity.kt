package com.edimitre.personalmanager.activity

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.roomdb.MyRoomDatabase
import com.edimitre.personalmanager.databinding.ActivityMainBinding
import com.edimitre.personalmanager.system.SystemService


class MainActivity : AppCompatActivity() {


    private lateinit var profileItem: MenuItem

    private lateinit var reloadItem: MenuItem

    private lateinit var backUpDbItem: MenuItem

    private lateinit var searchItem: MenuItem

    private lateinit var clearSearchItem: MenuItem

    lateinit var binding: ActivityMainBinding

    private val mySystemService = SystemService(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val systemService = SystemService(this)

        systemService.createNotificationChannel()

        setToolbar()

        setBtnListeners()

        if (!systemService.permissionGranted()) {
            askForWritePermissions()
        }

        val db = MyRoomDatabase.getInstance(this)
        Log.e("PersonalManager", "db " + db.isOpen)


    }

    override fun onPause() {
        super.onPause()
        val systemService = SystemService(this)
        systemService.startDbBackupWorker()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        profileItem = menu!!.findItem(R.id.btn_toolbar_profile)
        backUpDbItem = menu.findItem(R.id.btn_back_up_db)
        reloadItem = menu.findItem(R.id.btn_reload_db)
        searchItem = menu.findItem(R.id.btn_toolbar_search)
        clearSearchItem = menu.findItem(R.id.btn_clear_search)


        reloadItem.isVisible = false
        backUpDbItem.isVisible = false
        searchItem.isVisible = false
        clearSearchItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.btn_toolbar_profile -> {
                intent = Intent(this, ReportsActivity::class.java)
                startActivity(intent)

            }


        }


        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        binding.toolbar.title = "PersonalManager"
        binding.toolbar.setTitleTextColor(Color.BLACK)
        setSupportActionBar(binding.toolbar)

    }

    private fun askForWritePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission.WRITE_EXTERNAL_STORAGE),
            0
        )
    }

    private fun permissionGranted(): Boolean {
        val result1 =
            ContextCompat.checkSelfPermission(this@MainActivity, permission.WRITE_EXTERNAL_STORAGE)
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("ResourceType")
    private fun setBtnListeners() {

        binding.descriptionCard.setOnClickListener {

            intent = Intent(this, DescriptionActivity::class.java)
            startActivity(intent)

        }

        binding.expensesCard.setOnClickListener {
            intent = Intent(this, AllExpensesActivity::class.java)
            startActivity(intent)

        }

        binding.reminderCard.setOnClickListener {

            intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)

        }

        binding.productsCard.setOnClickListener {

            intent = Intent(this, ProductsActivity::class.java)
            startActivity(intent)

        }


    }


}