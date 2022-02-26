package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
    }

    //Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this)
            .get(RepresentativeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Establish bindings
        val binding: FragmentRepresentativeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_representative,
            container,
            false
        )
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

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            Log.i("RepFragment",binding.addressLine1.text.toString())
            Log.i("RepFragment",binding.addressLine2.text.toString())
            Log.i("RepFragment",binding.city.text.toString())
            //Log.i("RepFragment",it.state)
            Log.i("RepFragment",binding.zip.text.toString())
            Log.i("RepFragment","Item position: ${binding.state.selectedItemPosition}")
            Log.i("RepFragment","Item: ${binding.state.selectedItem}")
            viewModel.getAddressFromLines(
                binding.addressLine1.text.toString(),
                binding.addressLine2.text.toString(),
                binding.city.text.toString(),
                binding.state.selectedItem as String,
                binding.zip.text.toString()
            )
        }

        //TODO: Establish button listeners for field and location search

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}