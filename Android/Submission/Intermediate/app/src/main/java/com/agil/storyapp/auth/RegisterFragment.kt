package com.agil.storyapp.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.agil.storyapp.R
import com.agil.storyapp.databinding.FragmentRegisterBinding
import com.agil.storyapp.viewmodel.AuthViewModel

class RegisterFragment : Fragment(), TextWatcher, View.OnFocusChangeListener, View.OnClickListener {
    private var password: String? = null
    private var name: String? = null
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by activityViewModels()

    companion object {
        const val EXTRA_PASSWORD = "statePassword"
        const val EXTRA_NAME = "stateName"
        val TAG:String = RegisterFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonEnable(null)
        binding.edRegisterName.apply {
            hint = "Name"
            addTextChangedListener(this@RegisterFragment)
            onFocusChangeListener = this@RegisterFragment
        }
        binding.edRegisterEmail.apply {
            hint = "Email"
            addTextChangedListener(this@RegisterFragment)
            onFocusChangeListener = this@RegisterFragment
        }
        binding.edRegisterPassword.apply {
            addTextChangedListener(this@RegisterFragment)
            onFocusChangeListener = this@RegisterFragment
        }
        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        val email = viewModel.getEmail()
        binding.edRegisterEmail.setText(email)

        getResponseFromRegister()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            binding.btnToLogin.id -> {
                replaceFragment(LoginFragment())
            }
            binding.btnRegister.id -> {
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()
                viewModel.registerData(name, email, password)
            }
        }
    }

    private fun getResponseFromRegister() {
        viewModel.getRegisterResponse().observe(viewLifecycleOwner){
            val error = it?.error
            val message = it?.message
            if (error == false) replaceFragment(LoginFragment())
            if (!message.isNullOrEmpty()) {
                Toast.makeText(context, getString(R.string.register)+": $message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) = setButtonEnable(s)

    private fun setButtonEnable(text: CharSequence?) {
        val txtName = binding.edRegisterName.text
        val txtEmail = binding.edRegisterEmail.text
        val txtPassword = binding.edRegisterPassword.text
        if (!text.isNullOrEmpty()){
            if(!txtName.isNullOrEmpty() && !txtEmail.isNullOrEmpty() && !txtPassword.isNullOrEmpty() && txtPassword.length >= 6){
                if(isValidEmail(txtEmail)){
                    binding.btnRegister.isEnabled = true
                }
            }else{
                isErrorPassword(txtPassword.toString())
                binding.btnRegister.isEnabled = false
            }
        }else{
            binding.btnRegister.isEnabled = false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        viewModel.resetResponseStatus()
        val mFragmentManager = parentFragmentManager
        mFragmentManager.popBackStack()
        mFragmentManager.beginTransaction().apply {
            replace(R.id.fg_auth, fragment, fragment.tag)
            commit()
        }
    }

    private fun isErrorPassword(password: String?){
        if (!password.isNullOrEmpty()){
            if (password.length < 6){
                binding.edRegisterPassword.error = getString(R.string.required_password)
            }
        }
    }
    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus){
            when(v?.id) {
                binding.edRegisterName.id ->{
                    val textName = binding.edRegisterName.text.toString()
                    name = textName
                }
                binding.edRegisterEmail.id -> {
                    val email = binding.edRegisterEmail.text.toString()
                    if (email.isNotEmpty() && !isValidEmail(email)){
                        binding.edRegisterEmail.error = getString(R.string.email_not_valid)
                    }
                    viewModel.setEmail(email)
                }
                binding.edRegisterPassword.id ->{
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
}