package com.ewa.mikulska.listoffriends.peopledetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bumptech.glide.Glide
import com.ewa.mikulska.listoffriends.databinding.FragmentPeopleDetailsBinding

class PeopleDetailsFragment : Fragment(), PersonAddDescriptionDialog.Callback {
    private lateinit var binding: FragmentPeopleDetailsBinding
    private val viewModel: PeopleDetailsViewModel by viewModel()
    private val args: PeopleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.getDetails(args.personId)

        binding.floatingActionButtonAddDescription.setOnClickListener {
            PersonAddDescriptionDialog().apply {
                callback = this@PeopleDetailsFragment
            }.show(childFragmentManager, "Add description")
        }

        binding.deletePerson.setOnClickListener {
            viewModel.removeFriend()
        }

        viewModel.person.observe(viewLifecycleOwner) { it ->
            Glide
                .with(this)
                .load(it?.imageURL)
                .into(binding.imagePersonDetails)

            viewModel.event.observe(viewLifecycleOwner) {
                if (it is PeopleDetailsViewModel.MyEvent.Error) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.navigationEvent.observe(viewLifecycleOwner) {
                if (it is NavDirections) {
                    findNavController().navigate(it)
                }
            }
        }
    }

    override fun sendDescription(descrption: String) {
        viewModel.addDescription(args.personId, descrption)
    }
}