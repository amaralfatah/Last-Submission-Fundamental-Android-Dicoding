package com.example.githubperson.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubperson.data.remote.ItemsItem
import com.example.githubperson.adapter.MainAdapter
import com.example.githubperson.databinding.FragmentFollowingFollowersBinding
import com.example.githubperson.viewmodel.factory.FollowingFollowersFactory
import com.example.githubperson.viewmodel.FollowingFollowersViewModel
import androidx.fragment.app.viewModels




class FollowingFollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowingFollowersBinding

    companion object {
        const val _POSITION = "position"
        const val _USERNAME = "username"
    }

    private var position = 0
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFollowingFollowersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainViewModel : FollowingFollowersViewModel by viewModels {
            FollowingFollowersFactory(username)
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFragment.layoutManager = layoutManager
        mainViewModel.follow.observe(viewLifecycleOwner) {follow -> setPersonData(follow)}
        mainViewModel.isLoading.observe(requireActivity()) {showLoading(it)}
        arguments?.let {
            position = it.getInt(_POSITION)
            username = it.getString(_USERNAME).toString()
        }
        if (position == 1) {
            mainViewModel.findUserFollowers(username)
        } else {
            mainViewModel.findUserFollowing(username)
        }
    }


    private fun setPersonData(personSearch: List<ItemsItem>){

        val getPersonData = personSearch.map {
            ItemsItem(it.login, it.id)
        }

        val adapter = MainAdapter(getPersonData)
        binding.rvFragment.adapter =adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}