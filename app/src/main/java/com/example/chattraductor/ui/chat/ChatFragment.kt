package com.example.chattraductor.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.local.PopulateLocalDataBase
import com.example.chattraductor.databinding.FragmentChatBinding
import com.example.chattraductor.utils.Resource

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private lateinit var chatAdapter: UserAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel =
            ViewModelProvider(this).get(ChatViewModel::class.java)
        lateinit var chat: Chat
        val populateLocalDataBase = ViewModelProvider(this).get(PopulateLocalDataBase::class.java)
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        chatAdapter = UserAdapter(
            ::onChatListClickItem
        )

        binding.chatList.adapter = chatAdapter


        val textView: TextView = binding.textChat
        chatViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        populateLocalDataBase.finish.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    chatViewModel.updateChatList()
                }

                Resource.Status.ERROR -> {
//                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                }

            }
        })
        chatViewModel.chat.observe(viewLifecycleOwner, Observer {
            Log.e("PruebasDia1", "ha ocurrido un cambio en la lista total")
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        chatAdapter.submitList(it.data)
                        Log.d("ChatFragment", "Datos cargados correctamente: ${it.data}")
                    } else {
                        Log.d("ChatFragment", "La lista de chats está vacía, haga login")
                    }
                }

                Resource.Status.ERROR -> {
                    Log.e("ChatFragment", "Error al cargar datos: ${it.message}")
                }

                Resource.Status.LOADING -> {
                    Log.d("ChatFragment", "Cargando datos...")
                }
            }
        })
        return root
    }

    private fun onChatListClickItem(chat: User) {
//        this.chat = chat
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}