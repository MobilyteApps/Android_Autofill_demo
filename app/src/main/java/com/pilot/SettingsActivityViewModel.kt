package com.pilot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pilot.database.FillsDAO
import com.pilot.database.FillsDTO
import kotlinx.coroutines.*

class SettingsActivityViewModel(
    val database: FillsDAO,
    application: Application
) : AndroidViewModel(application) {


    private var _currentID = MutableLiveData<Long>()
    val currentID
    get() = _currentID

    private var _toSend = MutableLiveData<FillsDTO>()
    val toSend
        get() = _toSend

    val allFills = database.getAllFills()

    init {
        _currentID.value = Long.MIN_VALUE
    }

    fun enterINdb(fillsDTO: FillsDTO) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
                database.insert(fillsDTO)
            }
        }
    }

    fun changeCurrentID(id : Long){
        _currentID.value = id
    }

    fun getCurrentFill() {
        CoroutineScope(Dispatchers.Main + Job()).launch {
           _toSend.value = getFromDB()
        }
    }

    private suspend fun getFromDB() : FillsDTO {
        return withContext(Dispatchers.IO){
                database.get(_currentID.value!!)
        }
    }

    fun deleteAllValues(){
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
                database.deleteAll()
            }
        }
    }

}

class TitleFragViewModelFactory(
    private val dataSource : FillsDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsActivityViewModel::class.java)) {
            return SettingsActivityViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}