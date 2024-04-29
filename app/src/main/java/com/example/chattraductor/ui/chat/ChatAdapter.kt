package com.example.chattraductor.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chattraductor.data.model.User
import com.example.chattraductor.databinding.FragmentChatBinding

class UserAdapter (
    private val onClickListener: (User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            FragmentChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)

        holder.itemView.setOnClickListener {
            onClickListener(chat)
        }

    }

    inner class UserViewHolder(private val binding: FragmentChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: User) {

        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return (oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.surname == newItem.surname
                    && oldItem.phoneNumber1 == newItem.phoneNumber1)
        }

    }

}