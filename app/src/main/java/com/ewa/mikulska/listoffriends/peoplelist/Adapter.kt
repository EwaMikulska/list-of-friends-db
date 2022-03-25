package com.ewa.mikulska.listoffriends.peoplelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ewa.mikulska.listoffriends.data.Person
import com.ewa.mikulska.listoffriends.databinding.ViewHolderBinding

class Adapter(
    var glide: RequestManager? = null,
    var callback: Callback? = null
): ListAdapter<Person, Adapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)

        holder.binding.txtNameViewHolder.text = item.name
        holder.binding.txtSurnameViewHolder.text = item.surname

        glide?.load(item.imageURL)?.into(holder.binding.imageViewPersonViewHolder)

        if (item.isFriend) {
            holder.binding.floatingActionButtonFriendAdded.isVisible = true
            holder.binding.floatingActionButtonAddFriend.isVisible = false
        } else {
            holder.binding.floatingActionButtonFriendAdded.isVisible = false
            holder.binding.floatingActionButtonAddFriend.isVisible = true
        }

        holder.binding.floatingActionButtonAddFriend.setOnClickListener {
            callback?.checkIsFriend(item)
        }

        holder.binding.floatingActionButtonFriendAdded.setOnClickListener {
            callback?.undoFriend(item)
        }

        holder.binding.root.setOnClickListener {
            callback?.onPersonClick(item)
        }
    }

    class Holder(val binding: ViewHolderBinding) : RecyclerView.ViewHolder(binding.root)

    interface Callback {
        fun undoFriend(person: Person)
        fun checkIsFriend(person: Person)
        fun onPersonClick(person: Person)
    }
}

class DiffCallBack : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Person,
        newItem: Person
    ): Boolean {
        return oldItem == newItem
    }
}