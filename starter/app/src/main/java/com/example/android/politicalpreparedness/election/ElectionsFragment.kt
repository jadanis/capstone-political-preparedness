package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    //Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Add ViewModel values and create ViewModel
        viewModel = ViewModelProvider(
            this,
            ElectionsViewModelFactory(requireContext())
        )
            .get(ElectionsViewModel::class.java)


        //Add binding values
        val binding: FragmentElectionBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_election,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Link elections to voter info
        //Initiate recycler adapters
        val listener = ElectionListener { elec ->
            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(elec.id,elec.division))
        }
        val listener2 = ElectionListener { elec ->
            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(elec.id,elec.division))
        }
        val upcomingElectionsAdapter = ElectionListAdapter(listener)
        val savedElectionsAdapter = ElectionListAdapter(listener2)

        binding.savedElecsList.adapter = savedElectionsAdapter
        binding.upcomingElecsList.adapter = upcomingElectionsAdapter

        //Populate recycler adapters
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("ElectionsFragment","Upcoming List Submitted")
                upcomingElectionsAdapter.submitList(it)
            }
        })
        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            it?.let{
                Log.i("ElectionsFragment","Saved List Submitted")
                savedElectionsAdapter.submitList(it)
            }
        })


        return binding.root
    }


    //Refresh adapters when fragment loads
//    override fun onResume() {
//        super.onResume()
//        viewModel.getElections()
//    }
}