package com.team.cat_hackathon.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

        if (member != null) {
            val img = Uri.parse(member.imageUrl)
            if(img!= null)
            holder.binding.itemImage.setImageURI(img)
            else{

            }
            holder.binding.itemName.text=member.name
            holder.binding.itemPosition.text=member.track
        }else{

        }

    }


}