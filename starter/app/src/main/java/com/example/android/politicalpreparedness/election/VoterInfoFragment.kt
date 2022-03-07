package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
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
import retrofit2.http.Url

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

        //Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //Handle loading of URLs
        viewModel.locationUrl.observe(viewLifecycleOwner, Observer {
            it?.let{ url ->
                binding.stateLocations.visibility = View.VISIBLE
                binding.stateLocations.setOnClickListener { navigateToURL(url) }
            }
        })

        viewModel.ballotUrl.observe(viewLifecycleOwner, Observer {
            it?.let{ url ->
                binding.stateBallot.visibility = View.VISIBLE
                binding.stateBallot.setOnClickListener { navigateToURL(url) }
            }
        })

        //Handle save button UI state
        //cont'd Handle save button clicks
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

    //Create method to load URL intents
    private fun navigateToURL(url: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}