package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import kotlinx.android.synthetic.main.fragment_launch.*

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Add ViewModel values and create ViewModel
        val elecId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val division = VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
        val database = ElectionDatabase.getInstance(requireContext()).electionDao
        viewModel = ViewModelProvider(
            this,
            VoterInfoViewModelFactory(elecId,division,database)
        )
            .get(VoterInfoViewModel::class.java)

        //Add binding values
        val binding: FragmentVoterInfoBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_voter_info,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
        viewModel.savedElection.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(!it) {
                    binding.saveButton.text = getString(R.string.save_button_text)
                    binding.saveButton.setOnClickListener { viewModel.saveElection() }
                } else {
                    binding.saveButton.text = getString(R.string.unfollow_button_text)
                    binding.saveButton.setOnClickListener { viewModel.removeElection() }
                }
            }
        })

        return binding.root

    }

    //TODO: Create method to load URL intents

}