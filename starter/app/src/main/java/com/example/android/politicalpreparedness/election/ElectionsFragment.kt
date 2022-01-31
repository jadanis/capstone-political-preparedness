package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
            ElectionsViewModelFactory()
        )
            .get(ElectionsViewModel::class.java)
        //TODO: add viewModel to binding
        //binding.viewModel = viewModel

        //TODO: Add binding values
        val binding: FragmentElectionBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_election,
            container,
            false
        )
        binding.lifecycleOwner = this


        //TODO: Validate ok to use same adapter for both RecyclerViews
        val listener = ElectionListener { elec ->
            findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(elec.id,elec.division))
        }
        val adapter = ElectionListAdapter(listener)
        binding.savedElecsList.adapter = adapter
        binding.upcomingElecsList.adapter = adapter

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return null
    }

    //TODO: Refresh adapters when fragment loads

}