package com.example.chattraductor.ui.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chattraductor.R
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.databinding.ItemMessageBinding
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.MyApp.Companion.context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(
    private val onClickListener: (Message) -> Unit
) : ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun submitList(list: List<Message>?) {
        super.submitList(list?.sortedBy { it.id })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding =
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
        holder.itemView.setOnClickListener {
            onClickListener(message)
        }
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {

            binding.image.visibility = View.GONE

            binding.text.text = message.text


            setMessageFormat(message)
        }

        private fun setMessageFormat(message: Message) {
            if (MyApp.userPreferences.getUser()?.id == message.senderId) {
                binding.name.visibility = View.GONE
               /* if (message.saved !== null) {
                    binding.sentHour.text = parseDate(message.sent)
                } else {
                    binding.sentHour.text = parseDate(message.sent)
                    binding.clock.visibility = View.VISIBLE
                }*/

                val drawable = ContextCompat.getDrawable(context, R.drawable.background_sent)
                binding.linearLayout1.background = drawable

            } else {

                val linearLayout = binding.linearLayout1
                val layoutParams = linearLayout.layoutParams as RelativeLayout.LayoutParams
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0)
                linearLayout.layoutParams = layoutParams

                val drawable = ContextCompat.getDrawable(context, R.drawable.background_received)
                binding.linearLayout1.background = drawable

                binding.name.text = message.receiverId.toString()

                //binding.sentHour.text = message.saved?.let { parseDate(it) }

            }

        }

        private fun parseDate(hour: Long): String {
            val hora = Date(hour)
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

            return formatter.format(hora)
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return (oldItem.id == newItem.id && oldItem.text == newItem.text && oldItem.senderId == newItem.senderId && oldItem.receiverId == newItem.receiverId)
        }

    }

}