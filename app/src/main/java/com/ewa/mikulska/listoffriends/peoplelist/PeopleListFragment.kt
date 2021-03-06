package com.ewa.mikulska.listoffriends.peoplelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.ewa.mikulska.listoffriends.data.Person
import com.ewa.mikulska.listoffriends.databinding.FragmentPeopleListBinding
import kotlinx.coroutines.launch

class PeopleListFragment : Fragment(), Adapter.Callback {
    private var adapter = Adapter()
    private lateinit var binding: FragmentPeopleListBinding
    private val viewModel: PeopleListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPeopleListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.glide = Glide.with(this)
        adapter.callback = this


        binding.searchViewFriend.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchDatabase(newText)
                }
                return true
            }
        })

        binding.peopleRecyclerView.apply {
            adapter = this@PeopleListFragment.adapter
        }

        viewModel.peopleList.observe(viewLifecycleOwner) { people ->
            lifecycleScope.launch {
                adapter.submitList(people)
            }
        }

        binding.addFriendFloatingButton.setOnClickListener {
            viewModel.goToAddFriendDialog()
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(it)
        }

        viewModel.event.observe(viewLifecycleOwner) { it ->
            if (it is PeopleListViewModel.MyEvent.Error) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            } else if (it is PeopleListViewModel.MyEvent.ErrorFriend) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                val person = it.personPosition
                val result = adapter.currentList.indexOfFirst {
                    it.id == person.id
                }
                if (result != -1) {
                    adapter.notifyItemChanged(result)
                }
            }
        }
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                adapter.submitList(it)
            }
        }
    }

    override fun undoFriend(person: Person) {
        viewModel.removeFromFriends(person)
    }

    override fun checkIsFriend(person: Person) {
        viewModel.addToFriends(person)
    }

    override fun onPersonClick(person: Person) {
        viewModel.goToPersonDetails(person)
    }

    override fun onDestroyView() {
        adapter.callback = null
        super.onDestroyView()
    }
}

