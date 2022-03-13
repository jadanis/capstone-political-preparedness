package com.example.android.politicalpreparedness.representative

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import java.util.Locale
import android.Manifest

class DetailFragment : Fragment() {

    companion object {
        //Add Constant for Location request
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    //Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this)
            .get(RepresentativeViewModel::class.java)
    }

    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Establish bindings
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_representative,
            container,
            false
        )
        //Per Submission feedback
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //Define and assign Representative adapter
        val repAdapter = RepresentativeListAdapter()
        val spinAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,resources.getStringArray(R.array.states))
        binding.representativeList.adapter = repAdapter
        binding.state.adapter = spinAdapter

        //Populate Representative adapter
        viewModel.representatives.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("RepFragment","Let Statement")
                repAdapter.submitList(it)
            }
        })

        viewModel.address.observe(viewLifecycleOwner, Observer {
            it?.let{
                viewModel.getRepresentatives(it.toFormattedString())
            }
        })

        //Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            Log.i("RepFragment",binding.addressLine1.text.toString())
            Log.i("RepFragment",binding.addressLine2.text.toString())
            Log.i("RepFragment",binding.city.text.toString())
            //Log.i("RepFragment",it.state)
            Log.i("RepFragment",binding.zip.text.toString())
            Log.i("RepFragment","Item position: ${binding.state.selectedItemPosition}")
            Log.i("RepFragment","Item: ${binding.state.selectedItem}")
            //Can't seem to get BindingAdapter and the Spinner to play nice
            // But this seems to preserve the state nevertheless
            viewModel.getAddressFromLines(binding.state.selectedItem as String)
        }

        binding.buttonLocation.setOnClickListener{
            hideKeyboard()
            getLocation()
        }

        //Fix Submission 2 feedback?
        savedInstanceState?.getParcelable<Address>("address")?.let{
            viewModel.getAddressFromLocation(it)
        }

        //Per Submission feedback
        //Mentor article: https://knowledge.udacity.com/questions/809749
        savedInstanceState?.getInt("motionLayout")?.let{
            binding.representativeContainer.transitionToState(it)
        }

        return binding.root
    }

    //Mentor article: https://knowledge.udacity.com/questions/809749
    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("RepresentativeFragment","onSavedInstanceState called")
        super.onSaveInstanceState(outState)
        outState.putInt("motionLayout",binding.representativeContainer.currentState)
        //Fix Submission 2 feedback?
        outState.putParcelable("address",binding.viewModel?.address?.value)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.i("RepFragment","onRequestPermissionsResult called")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //Handle location permission result to get location on permission granted
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                getLocation()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        Log.i("RepFragment","checkLocationPermissions called")
        return if (isPermissionGranted()) {
            true
        } else {
            Log.i("RepFragment","Else branch")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        Log.i("RepFragment","isPermissionGranted called")
        //Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.i("RepFragment","getLocation called")
        if(checkLocationPermissions()){
            //Get location from LocationServices
            Log.i("RepFragment","Check locations success")
            LocationServices.getFusedLocationProviderClient(requireContext())
                .lastLocation
                .addOnSuccessListener {
                    Log.i("RepFragment","onSuccessListener")
                    it?.let{
                        Log.i("RepFragment","Location: $it")
                        //The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
                        viewModel.getAddressFromLocation(geoCodeLocation(it))
                    }
                }
        } else {
            Log.i("RepFragment","Check locations fail")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION)
        }

    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Log.i("RepFragment","Address: $address")
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}