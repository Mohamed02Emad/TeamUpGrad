package com.team.cat_hackathon.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.databinding.TeamDataModelBinding


class TeamAdapter(
    private val teams: List<Team>? = null,
    val onTeamClicked : (team: Team) -> Unit
):RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TeamDataModelBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TeamDataModelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return teams?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teams?.get(position)
        holder.binding.teamNameTextView.text= team?.name ?: ""
        holder.binding.teamNumTextView.text= "${team?.numOfMembers}/10"
        holder.binding.teamDescriptionTextView.text= team?.bio ?: ""
        holder.binding.btnView.setOnClickListener{
            onTeamClicked(team!!)
        }

    }


}