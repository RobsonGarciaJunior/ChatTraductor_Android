package com.example.chattraductor.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.chattraductor.MainActivity
import com.example.chattraductor.R
import com.example.chattraductor.data.repository.remote.RemoteUserDataSource
import com.example.chattraductor.databinding.FragmentHomeBinding
import com.example.chattraductor.ui.chat.ChatViewModel
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import java.util.Locale
import java.util.regex.Pattern

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val userRepository = RemoteUserDataSource()
    private lateinit var loginButton: Button
    private val handler = Handler(Looper.getMainLooper())
    val loggedUser = MyApp.userPreferences.getUser()

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(userRepository, requireContext())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*
        val homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(userRepository, requireContext())).get(
                HomeViewModel::class.java
            )
        //ViewModelProvider(this).get(HomeViewModel::class.java)
         */
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loginButton = binding.login
        loginButton.isEnabled = false
        handler.postDelayed({
            loginButton.isEnabled = true
        }, 2000)

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.login.setOnClickListener {

            var email = binding.email.text.toString()
            email = lowerCaseEmail(email)
            val password = binding.password.text.toString()
            if (checkData()) {
                homeViewModel.onLogIn(email, password)
            }
//            mockData()
        }
        homeViewModel.loginUser.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val userResource = homeViewModel.loginUser.value
                    if (userResource != null && userResource.status == Resource.Status.SUCCESS) {
                        val user = userResource.data
                        if (user != null) {
                            user.accessToken?.let { it1 -> MyApp.userPreferences.saveAuthToken(it1) }
                            MyApp.userPreferences.saveUser(user)
                            Log.d("ChatFragment", "Datos cargados correctamente: ${it.data}")
                        }
                        redirectAfterLogin()
                    }
                }

                Resource.Status.ERROR -> {
                    /*Toast.makeText(
                        this,
                        "Los datos introducidos no pertenecen a un usuario del centro",
                        Toast.LENGTH_LONG
                    ).show()*/
                    loginButton.isEnabled = true
                }

                Resource.Status.LOADING -> {
                }
            }
        })
        if (loggedUser != null) {
            findNavController().navigate(R.id.nav_chat)
        }
        return root
    }

    private fun redirectAfterLogin() {
        findNavController().navigate(R.id.nav_chat)
    }

    private fun checkData(): Boolean {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, R.string.toast_empty_1, Toast.LENGTH_LONG).show()
            binding.email.setHintTextColor(Color.RED)
            binding.password.setHintTextColor(Color.RED)
            return false
        }
        if (!validateEmail(email)) {
//            Toast.makeText(this, R.string.toast_format_email, Toast.LENGTH_LONG).show()
            binding.email.setTextColor(Color.RED)
            binding.password.setTextColor(Color.BLACK)

            binding.email.setHintTextColor(Color.BLACK)
            binding.password.setHintTextColor(Color.BLACK)
            return false
        }

        if (password.length < 8) {
//            Toast.makeText(this, R.string.toast_password_lenght, Toast.LENGTH_LONG).show()
            binding.email.setTextColor(Color.BLACK)
            binding.password.setTextColor(Color.RED)

            binding.email.setHintTextColor(Color.BLACK)
            binding.password.setHintTextColor(Color.BLACK)
            return false
        }
        return true
    }

    private fun lowerCaseEmail(input: String): String {
        return input.lowercase(Locale.ROOT)
    }

    private fun validateEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}