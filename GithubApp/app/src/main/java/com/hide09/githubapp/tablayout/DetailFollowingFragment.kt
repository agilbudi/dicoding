package com.hide09.githubapp.tablayout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hide09.githubapp.DetailUserActivity
import com.hide09.githubapp.adapter.UserListAdapter
import com.hide09.githubapp.databinding.FragmentDetailFollowingBinding
import com.hide09.githubapp.model.User
import com.hide09.githubapp.viewmodel.UserDetailTabViewModel

class DetailFollowingFragment : Fragment() {
    private var _binding: FragmentDetailFollowingBinding? = null
    private val binding get() = _binding!!
    lateinit var detailTabVM: UserDetailTabViewModel
    private var detailAdapter = UserListAdapter()

    companion object{
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): DetailFollowingFragment {
            val fragment = DetailFollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)
        binding.rvFollowing.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = detailAdapter
        }
        detailTabVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailTabViewModel::class.java)
        detailTabVM.setUserFollowing(username!!)
        detailTabVM.getUserFollowing().observe(activity!!, { items ->
            if (items != null){
                detailAdapter.updateUsers(items)
            }
        })

        detailAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                itemSelected(data)
            }
        })
    }

    private fun itemSelected(data: User) {
        val intent = Intent(activity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, data.username)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}