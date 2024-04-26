package com.example.chattraductor.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chattraductor.R
import com.example.chattraductor.databinding.FragmentHomeBinding
import java.util.Locale
import java.util.regex.Pattern

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        return root
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