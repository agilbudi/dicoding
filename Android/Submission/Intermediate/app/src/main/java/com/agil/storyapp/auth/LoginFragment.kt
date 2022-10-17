package com.agil.storyapp.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.agil.storyapp.MainActivity
import com.agil.storyapp.R
import com.agil.storyapp.UserPreference
import com.agil.storyapp.databinding.FragmentLoginBinding
import com.agil.storyapp.model.User
import com.agil.storyapp.viewmodel.AuthViewModel


class LoginFragment : Fragment(), TextWatcher, View.OnFocusChangeListener, View.OnClickListener {
    private var password: String = ""
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels()

    companion object {
        const val EXTRA_PASSWORD = "statePassword"
        val TAG: String = LoginFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

        val email = viewModel.getEmail()
        binding.edLoginEmail.setText(email)
        getResponseFromLogin(view.context)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            binding.btnToRegister.id -> {
                replaceFragment(RegisterFragment())
            }
            binding.btnLogin.id -> {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()
                viewModel.loginData(email, password)
            }
        }
    }

    private fun getResponseFromLogin(context: Context) {
        val userPreference = UserPreference(context)
        viewModel.getUserData().observe(viewLifecycleOwner){ listItem ->
            if (listItem != null) {
                val token = listItem.token
                val name = listItem.name
                val userId = listItem.userId
                userPreference.setUser(User(userId, name, token))
                startActivity(Intent(context, MainActivity::class.java))
            }
        }
        viewModel.getLoginResponse().observe(viewLifecycleOwner){
            if (it != null) {
                Toast.makeText(context, it.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) = setButtonEnable(s)

    private fun setButtonEnable(text: CharSequence?) {
        val txtEmail = binding.edLoginEmail.text
        val txtPassword = binding.edLoginPassword.text
        if (!text.isNullOrEmpty()){
            if(!txtEmail.isNullOrEmpty() && !txtPassword.isNullOrEmpty() && txtPassword.length >= 6){
                if(isValidEmail(txtEmail)){
                    binding.btnLogin.isEnabled = true
                }
            }else{
                binding.btnLogin.isEnabled = false
            }
        }else{
            binding.btnLogin.isEnabled = false
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        viewModel.resetResponseStatus()
        val mFragmentManager = fragmentManager
        mFragmentManager?.beginTransaction()?.apply {
            replace(R.id.fg_auth, fragment, fragment.tag)
            addToBackStack(null)
            commit()
        }
    }

    private fun isErrorPassword(password: String?){
        if (!password.isNullOrEmpty()){
            if (password.length < 6){
                binding.edLoginPassword.error = getString(R.string.required_password)
            }
        }
    }
    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus){
            when(v?.id){
                binding.edLoginEmail.id -> {
                    val email = binding.edLoginEmail.text.toString()
                    if (email.isNotEmpty() && !isValidEmail(email)){
                        binding.edLoginEmail.error = getString(R.string.email_not_valid)
                    }
                    viewModel.setEmail(email)
                }
                binding.edLoginPassword.id ->{
                    val pass = binding.edLoginPassword.text.toString()
                    isErrorPassword(pass)
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
}