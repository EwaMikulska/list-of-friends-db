package com.ewa.mikulska.listoffriends.peopledetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bumptech.glide.Glide
import com.ewa.mikulska.listoffriends.R
import com.ewa.mikulska.listoffriends.databinding.FragmentPeopleDetailsBinding
import com.ewa.mikulska.listoffriends.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import kotlin.random.Random

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

        binding.imagePersonDetails.setImageResource(R.drawable.blank_profile_photo)

        val listUrl = ArrayList<String>()

        //TODO: assign a background to a person

        lifecycleScope.launch {
            try {
                RetrofitClient.instance
                    .getBackgroundAsync()
                    .await()
                    .body()?.forEach {
                        listUrl.add(it.download_url)
                    }
            } catch (
                e: Exception
            ){
                Toast.makeText(context, R.string.toast_error, Toast.LENGTH_SHORT).show()
            }
        }

        binding.changeBackroundButton.setOnClickListener {
            Glide
                .with(this)
                .load(listUrl[Random.nextInt(listUrl.size - 1)])
                .into(binding.backgroundImage)
        }

        binding.floatingActionButtonAddDescription.setOnClickListener {
            PersonAddDescriptionDialog().apply {
                callback = this@PeopleDetailsFragment
            }.show(childFragmentManager, "Add description")
        }

        binding.deletePerson.setOnClickListener {
            viewModel.removeFriend()
        }

        viewModel.person.observe(viewLifecycleOwner) { it ->
            if (it?.image != null) {
                Glide
                    .with(this)
                    .load(it.image)
                    .into(binding.imagePersonDetails)
            }

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