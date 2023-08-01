package com.team.cat_hackathon.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.ApiVars.BASE_URL_WITHOUT_API
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.NotificationCardBinding


class NotificationsAdapter(
    private val users: MutableList<User> ,
    private val acceptUser : (Int ) -> Unit,
    private val rejectUser : (Int ) -> Unit,
    private val userClicked :(User) -> Unit
):RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: NotificationCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NotificationCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users.get(position)
        holder.binding.tvUserName.text=  user.name
        holder.binding.tvUserTrack.text=  user.track ?: ""
        val img = holder.binding.ivUserImage

            Glide.with(img)
                .load(BASE_URL_WITHOUT_API +user.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(img)

        holder.binding.btnAccept.setOnClickListener{
            acceptUser(user.id)
        }
        holder.binding.btnReject.setOnClickListener{
            rejectUser(user.id)
        }
        holder.binding.root.setOnClickListener{
            userClicked(user)
        }

    }

    fun removeUser(userId: Int) {
       for (user in users){
           if (user.id == userId){
               users.remove(user)
               notifyDataSetChanged()
               break
           }
       }
    }


}