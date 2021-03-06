package com.pilot

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pilot.database.FillsDTO
import com.pilot.database.FillsDatabase
import kotlinx.android.synthetic.main.activity_settings.*

const val REQUEST_CODE_AUTOFILL =  102

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsViewModel : SettingsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val dataSource = FillsDatabase.getInstance(applicationContext).fillsDatabaseDao

        if (dataSource.getAllFills().value.isNullOrEmpty()){
            tempButton.visibility = View.VISIBLE
        }

        val viewModelFactory = TitleFragViewModelFactory(dataSource, application)
        settingsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SettingsActivityViewModel::class.java)

        val adapter = FillsAdapter(
            clickListener = MyFillsListener {
                settingsViewModel.changeCurrentID(it)
            }
        )

        recyclerView.adapter = adapter

        //filling the Text fields of the app
        settingsViewModel.currentID.observe(this, Observer {
            if(it == Long.MIN_VALUE){
                primaryEditText.setText("")
                passwordEditText.setText("")
                urlEditText.setText("")
            }else{
                settingsViewModel.getCurrentFill()
            }
        })


        settingsViewModel.toSend.observe(this, Observer {
            primaryEditText.setText(it.username)
            passwordEditText.setText(it.password)
            urlEditText.setText(it.webDomain)

            save_button.visibility = View.GONE
        })

        settingsViewModel.allFills.observe(this, Observer {
            adapter.submitList(it)
        })

    }

    fun saveEmailAddresses(view: View) {

        if(primaryEditText.text.toString().isBlank()){
            Toast.makeText(this, "Fill all the values", Toast.LENGTH_SHORT).show()
            return
        }

        val primaryEmail = primaryEditText.text.toString()
        val primaryPassword = passwordEditText.text.toString()
        val url = urlEditText.text.toString()

        if(primaryEmail.isNotBlank() && primaryPassword.isNotBlank() && url.isNotBlank()){
            FillsDTO(
                webDomain = url,
                username = primaryEmail,
                password = primaryPassword
            ).apply {
                settingsViewModel.enterINdb(this)
            }
        }else{
            Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()
        }
    }


    fun getPermission(view: View) {

        Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE).apply {
            data = Uri.parse("package:$packageName")
            startActivityForResult(this, REQUEST_CODE_AUTOFILL)

        }
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE_AUTOFILL && resultCode == Activity.RESULT_OK) {
                tempButton.visibility = View.GONE
                tempText.visibility = View.GONE
            }
        }

    //Add new fill data
        fun addNewFill(view: View) {
            settingsViewModel.changeCurrentID(Long.MIN_VALUE)
            if (save_button.visibility != View.VISIBLE)
                save_button.visibility = View.VISIBLE
        }

}