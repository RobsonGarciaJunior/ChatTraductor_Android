package com.example.chattraductor.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.databinding.FragmentChatBinding

class ChatAdapter (
    private val onClickListener: (Chat) -> Unit
) : ListAdapter<Chat, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding =
            FragmentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)

        holder.itemView.setOnClickListener {
            onClickListener(chat)
        }

    }

    inner class ChatViewHolder(private val binding: FragmentChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {

        }
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return (oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.chatter1 == newItem.chatter1
                    && oldItem.chatter2 == newItem.chatter2)
        }

    }

}