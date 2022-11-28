package com.agil.storyapp.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.agil.storyapp.R
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.databinding.FragmentRegisterBinding
import com.agil.storyapp.ui.viewmodel.AuthViewModel
import com.agil.storyapp.ui.viewmodel.AuthViewModelFactory

class RegisterFragment : Fragment(), View.OnFocusChangeListener, View.OnClickListener {
    private var password: String? = null
    private var name: String? = null
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: AuthViewModelFactory
    private val viewModel: AuthViewModel by activityViewModels { factory }
    private var tempEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = AuthViewModelFactory.getInstance()

        setButtonEnable(null)
        binding.edRegisterName.apply {
            hint = "Name"
            addTextChangedListener(doAfterTextChanged { setButtonEnable(it) })
            onFocusChangeListener = this@RegisterFragment
        }
        binding.edRegisterEmail.apply {
            hint = "Email"
            addTextChangedListener(doAfterTextChanged { setButtonEnable(it) })
            onFocusChangeListener = this@RegisterFragment
        }
        binding.edRegisterPassword.apply {
            addTextChangedListener(doAfterTextChanged { setButtonEnable(it) })
            onFocusChangeListener = this@RegisterFragment
        }
        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        tempEmail = viewModel.getEmail()
        binding.edRegisterEmail.setText(tempEmail)
    }

    override fun onClick(view: View?) {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        when (view?.id) {
            binding.btnToLogin.id -> {
                replaceFragment(LoginFragment())
                viewModel.setEmail(email)
            }
            binding.btnRegister.id -> {
                getResponseFromRegister(name, email, password)
                viewModel.setEmail(email)
            }
        }
    }

    private fun getResponseFromRegister(name: String, email: String, password: String) {
        viewModel.register(name, email, password).observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when(result){
                    is Result.Success ->{
                        showToast(requireContext(), result.data.message, false)
                        replaceFragment(LoginFragment())
                    }
                    is Result.Error -> {
                        showToast(requireContext(), result.error, true)
                        showLoading(false)
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }


    private fun setButtonEnable(text: CharSequence?) {
        val txtName = binding.edRegisterName.text
        val txtEmail = binding.edRegisterEmail.text
        val txtPassword = binding.edRegisterPassword.text
        if (!text.isNullOrEmpty()) {
            if (!txtName.isNullOrEmpty() && !txtEmail.isNullOrEmpty() && !txtPassword.isNullOrEmpty() && txtPassword.length >= 6) {
                if (isValidEmail(txtEmail)) {
                    binding.btnRegister.isEnabled = true
                }
            } else {
                binding.btnRegister.isEnabled = false
            }
        } else {
            binding.btnRegister.isEnabled = false
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun replaceFragment(fragment: Fragment) {
        val mFragmentManager = parentFragmentManager
        mFragmentManager.popBackStack()
        mFragmentManager.beginTransaction().apply {
            replace(R.id.fg_auth, fragment, fragment.tag)
            commit()
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.btnRegister.isEnabled = !isLoading
        binding.btnToLogin.isEnabled = !isLoading
        binding.pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(context: Context, text: String, isLong: Boolean) {
        if (isLong) Toast.makeText(context, text, Toast.LENGTH_LONG).show() else
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            when (v?.id) {
                binding.edRegisterName.id -> {
                    val textName = binding.edRegisterName.text.toString()
                    name = textName
                }
                binding.edRegisterEmail.id -> {
                    val email = binding.edRegisterEmail.text.toString()
                    if (email.isNotEmpty() && !isValidEmail(email)) {
                        binding.edRegisterEmail.error = getString(R.string.email_not_valid)
                    }
                    tempEmail = email
                }
                binding.edRegisterPassword.id -> {
                    val textPassword = binding.edRegisterPassword.text.toString()
                    password = textPassword
                }
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_NAME, name)
        outState.putString(EXTRA_PASSWORD, password)
        super.onSaveInstanceState(outState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_PASSWORD = "statePassword"
        const val EXTRA_NAME = "stateName"
        val TAG: String = RegisterFragment::class.java.simpleName
    }

}