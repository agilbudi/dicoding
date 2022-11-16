package com.agil.storyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.agil.storyapp.MainActivity
import com.agil.storyapp.R
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.databinding.FragmentLoginBinding
import com.agil.storyapp.ui.viewmodel.AuthViewModel
import com.agil.storyapp.ui.viewmodel.AuthViewModelFactory
import com.agil.storyapp.utils.UserPreference


class LoginFragment : Fragment(), TextWatcher, View.OnFocusChangeListener, View.OnClickListener {
    private var password: String = ""
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var  factory : AuthViewModelFactory
    private val viewModel: AuthViewModel by activityViewModels { factory }
    private var tempEmail: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        factory = AuthViewModelFactory.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonEnable(null)
        binding.edLoginEmail.apply {
            hint = "Email"
            addTextChangedListener(this@LoginFragment)
            onFocusChangeListener = this@LoginFragment
        }
        binding.edLoginPassword.apply {
            addTextChangedListener(this@LoginFragment)
            onFocusChangeListener = this@LoginFragment
        }
        binding.btnToRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        tempEmail = viewModel.getEmail()
        binding.edLoginEmail.setText(tempEmail)
    }

    override fun onClick(view: View?) {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        when (view?.id) {
            binding.btnToRegister.id -> {
                replaceFragment(RegisterFragment())
                viewModel.setEmail(email)
            }
            binding.btnLogin.id -> {
                getResponseFromLogin(view.context, email, password)
            }
        }
    }

    private fun getResponseFromLogin(context: Context, email: String, password: String) {
        val userPreference = UserPreference(context)
        viewModel.login(email, password).observe(viewLifecycleOwner){ result ->
            if (result != null) {
                when(result){
                    is Result.Success ->{
                        val user = result.data.loginResult
                        userPreference.setUser(user)
                        userPreference.setMainActivity(MainActivity.TAG)
                        startActivity(Intent(context, MainActivity::class.java))
                        showToast(context, result.data.message, false)
                    }
                    is Result.Error -> {
                        showToast(context, result.error, true)
                        showLoading(false)
                    }
                    is Result.Loading -> showLoading(true)
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) = setButtonEnable(s)

    private fun setButtonEnable(text: CharSequence?) {
        val txtEmail = binding.edLoginEmail.text
        val txtPassword = binding.edLoginPassword.text
        if (!text.isNullOrEmpty()) {
            if (!txtEmail.isNullOrEmpty() && !txtPassword.isNullOrEmpty() && txtPassword.length >= 6) {
                if (isValidEmail(txtEmail)) {
                    binding.btnLogin.isEnabled = true
                }
            } else {
                isErrorPassword(txtPassword.toString())
                binding.btnLogin.isEnabled = false
            }
        } else {
            binding.btnLogin.isEnabled = false
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val mFragmentManager = parentFragmentManager
        mFragmentManager.beginTransaction().apply {
            replace(R.id.fg_auth, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun isErrorPassword(password: String?) {
        if (!password.isNullOrEmpty()) {
            if (password.length < 6) {
                binding.edLoginPassword.error = getString(R.string.required_password)
            }
        }
    }
    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showLoading(isLoading: Boolean){
        binding.btnLogin.isEnabled = !isLoading
        binding.btnToRegister.isEnabled = !isLoading
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(context: Context, text: String, isLong: Boolean) {
        if (isLong) Toast.makeText(context, text, Toast.LENGTH_LONG).show() else
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            when (v?.id) {
                binding.edLoginEmail.id -> {
                    val email = binding.edLoginEmail.text.toString()
                    if (email.isNotEmpty() && !isValidEmail(email)) {
                        binding.edLoginEmail.error = getString(R.string.email_not_valid)
                    }
                    tempEmail = email
                }
                binding.edLoginPassword.id -> {
                    val pass = binding.edLoginPassword.text.toString()
                    password = pass
                }
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_PASSWORD, password)
        super.onSaveInstanceState(outState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_PASSWORD = "statePassword"
        val TAG: String = LoginFragment::class.java.simpleName
    }
}