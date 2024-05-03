package com.example.chattraductor.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.chattraductor.R
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.local.PopulateLocalDataBase
import com.example.chattraductor.data.repository.local.PopulateLocalDataBaseFactory
import com.example.chattraductor.data.repository.local.chat.RoomChatDataSource
import com.example.chattraductor.data.repository.local.message.RoomMessageDataSource
import com.example.chattraductor.data.repository.local.user.RoomUserDataSource
import com.example.chattraductor.data.repository.remote.RemoteMessageDataSource
import com.example.chattraductor.data.repository.remote.RemoteUserDataSource
import com.example.chattraductor.databinding.FragmentChatBinding
import com.example.chattraductor.ui.message.MessageActivity
import com.example.chattraductor.ui.message.MessageService
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private lateinit var chatAdapter: UserAdapter
    private lateinit var user: User

    private val chatRepository = RoomChatDataSource()

    private val messageRepository = RoomMessageDataSource()
    private val remoteMessageRepository = RemoteMessageDataSource()

    private val userRepository = RoomUserDataSource()
    private val remoteUserRepository = RemoteUserDataSource()

    private val loggedUser = MyApp.userPreferences.getUser()
    private val chatViewModel: ChatViewModel by viewModels {
        ChatViewModel.ChatViewModelFactory(remoteUserRepository, requireContext())
    }
    private val populateLocalDataBase: PopulateLocalDataBase by viewModels {
        PopulateLocalDataBaseFactory(
            chatRepository,
            messageRepository,
            remoteMessageRepository,
            userRepository,
            remoteUserRepository
        )
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        chatAdapter = UserAdapter(
            ::onChatListClickItem
        )

        binding.chatList.adapter = chatAdapter
        if (loggedUser != null) {
            chatViewModel.updateChatList()
            Log.d("User", "LOGGED USER:" + loggedUser.name + loggedUser.accessToken)
        } else {
            Toast.makeText(
                requireContext(), "YOU NEED TO LOG IN TO SEE YOUR CHATS!", Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.nav_home)
        }

        val textView: TextView = binding.textChat
        chatViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

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

    private fun onChatListClickItem(chatter2: User) {
        Log.e("ChatFragment", "HICISTE CLICK")
        this.user = chatter2
        if (chatter2 != null) {
            goToMessages()
        }
    }

    private fun goToMessages() {
        val intent = Intent(requireContext(), MessageActivity::class.java)
        intent.putExtra("usuario Seleccionado", this.user)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}