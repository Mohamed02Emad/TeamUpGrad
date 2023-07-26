package com.team.cat_hackathon.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.Member
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.NotificationCardBinding


class NotificationsAdapter(
    private val users: MutableList<Member> ,
    private val acceptUser : (Int ) -> Unit,
    private val rejectUser : (Int ) -> Unit
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
        holder.binding.tvUserName.text=  "no name"
        holder.binding.tvUserTrack.text=  "no track"
        val img = holder.binding.ivUserImage

//        user.imageUrl?.let{url->
//            Glide.with(img)
//                .load(user.imageUrl)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .centerInside()
//                .error(R.drawable.ellipse)
//                .into(img)
//        }

        holder.binding.btnAccept.setOnClickListener{
            acceptUser(user.user_id)
        }
        holder.binding.btnReject.setOnClickListener{
            rejectUser(user.user_id)
        }
    }

    fun removeUser(userId: Int) {
       for (user in users){
           if (user.user_id == userId){
               users.remove(user)
               Log.d("mohamed", "removeUser: ")
               notifyDataSetChanged()
               break
           }
       }
    }


}