package com.hide09.consumergithub.tablayout

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hide09.consumergithub.DetailUserActivity
import com.hide09.consumergithub.adapter.UserListAdapter
import com.hide09.consumergithub.databinding.FragmentDetailFollowingBinding
import com.hide09.consumergithub.model.User
import com.hide09.consumergithub.viewmodel.UserDetailTabViewModel

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
        val username = arguments?.getString(ARG_USERNAME).toString()
        binding.rvFollowing.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = detailAdapter
        }
        showData(username)

        detailAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                itemSelected(data)
            }
        })
    }

    private fun showData(username: String) {
        showLoading(true)
        detailTabVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailTabViewModel::class.java)
        detailTabVM.setUserFollowing(username)
        detailTabVM.getUserFollowing().observe(requireActivity(), { items ->
            if (items != null){
                detailAdapter.updateUsers(items)
            }
            showLoading(false)
        })
    }

    private fun showLoading(status: Boolean) {
        if (status){
            binding.progressBarFollowing.visibility = View.VISIBLE
        }else{
            binding.progressBarFollowing.visibility = View.GONE
        }
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