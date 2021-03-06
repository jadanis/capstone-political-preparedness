package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel() {

    //Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    val line1 = MutableLiveData<String>()
    val line2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    // change init
//    init{
//        address.value = Address("498 Turner Street","","Auburn","Maine","04210")
//        val strAddr = address.value?.toFormattedString() ?: ""
//        getRepresentatives(strAddr)
//    }

    //Create function to fetch representatives from API from a provided address
    fun getRepresentatives(address: String){
        viewModelScope.launch {
            val (offices,officials) = CivicsApi.retrofitService.getRepresentatives(address)
            _representatives.value = offices.flatMap {
                    office -> office.getRepresentatives(officials)
            }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //Create function get address from geo location
    fun getAddressFromLocation(location: Address){
        _address.value = location
    }

    //Create function to get address from individual fields
    fun getAddressFromLines( state: String) {
        _address.value = Address(
            line1.value ?: "",
            line2.value ?: "",
            city.value ?: "",
            state,
            zip.value ?: ""
        )
    }

}
