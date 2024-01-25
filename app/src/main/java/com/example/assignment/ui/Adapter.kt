package com.example.assignment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.assignment.R
import com.example.assignment.databinding.ItemBinding

class UserAdapter: RecyclerView.Adapter<Item>() {

    private val items = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item =
        Item(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: Item, position: Int) =
        holder.show(items[position])

    override fun getItemCount(): Int =
        items.size

    fun show(users: List<User>) {
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }
}

class Item(view: View): RecyclerView.ViewHolder(view) {
    private val ui = ItemBinding.bind(view)

    fun show(user: User) {
        ui.image.load(user.avatar) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
        }
        ui.lastName.text = user.lastName
        ui.email.text = user.email
    }
}