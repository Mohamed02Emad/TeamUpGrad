package com.team.cat_hackathon.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.MemberDataModelBinding
import com.team.cat_hackathon.databinding.TeamDataModelBinding


class MembersAdapter(
    private val members: List<User>? = null
):RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MemberDataModelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MemberDataModelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return members?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = members?.get(position)

        val img = holder.binding.itemImage
        if(member?.imageUrl!= null){
            Glide.with(img)
                .load(member?.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .into(img)
        }
        else
            img.setImageResource(R.drawable.ellipse)

        holder.binding.itemName.text=member?.name
        holder.binding.itemPosition.text=member?.track

        holder.binding.gitHub.setOnClickListener {

        }
        holder.binding.facebook.setOnClickListener {

        }
        holder.binding.linkedIn.setOnClickListener {

        }

    }


}