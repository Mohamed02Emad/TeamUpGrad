package com.team.cat_hackathon.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.team.cat_hackathon.R
import com.team.cat_hackathon.data.api.BASE_URL_WITHOUT_API
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.MemberDataModelBinding


class MembersAdapter(
    val members: ArrayList<User>? = null,
    val userClicked: (User) -> Unit,
    val linkedInClicked: (String) -> Unit,
    val faceBookClicked: (String) -> Unit,
    val gitHubClicked: (String) -> Unit
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
        
        if ((member?.imageUrl?.length ?: 6) >= 7) {
            Glide.with(img)
                .load(BASE_URL_WITHOUT_API + member?.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .error(R.drawable.ic_profile)
                .into(img)
        }

        holder.binding.itemName.text = member?.name
        holder.binding.itemPosition.text = member?.track

        if (member?.facebookUrl == null || member.facebookUrl?.trim().isNullOrEmpty()) {
            holder.binding.facebook.isVisible = false
        } else {
            holder.binding.facebook.apply {
                isVisible = true
                setOnClickListener {
                    faceBookClicked(member.facebookUrl!!)
                }
            }
        }

        if (member?.githubUrl == null || member.githubUrl?.trim().isNullOrEmpty()) {
            holder.binding.gitHub.isVisible = false
        } else {
            holder.binding.gitHub.apply {
                isVisible = true
                setOnClickListener {
                    gitHubClicked(member.githubUrl!!)
                }
            }
        }

        if (member?.linkedinUrl == null || member.linkedinUrl?.trim().isNullOrEmpty()) {
            holder.binding.linkedIn.isVisible = false
        } else {
            holder.binding.linkedIn.apply {
                isVisible = true
                setOnClickListener {
                    linkedInClicked(member.linkedinUrl!!)
                }
            }
        }


        holder.binding.card.setOnClickListener {
            val user = User(member?.id ?: 0)
            user.facebookUrl = member?.facebookUrl
            user.imageUrl = member?.imageUrl
            user.linkedinUrl = member?.linkedinUrl
            user.githubUrl = member?.githubUrl
            user.name = member?.name.toString()
            user.track = member?.track
            user.email = member?.email.toString()
            userClicked(user)
        }

    }

}