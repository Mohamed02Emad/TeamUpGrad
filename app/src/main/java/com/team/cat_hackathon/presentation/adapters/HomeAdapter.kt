import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.HomeAdapterCardBinding
import com.team.cat_hackathon.presentation.adapters.MembersAdapter
import com.team.cat_hackathon.presentation.adapters.TeamAdapter

class HomeAdapter(
    private val users: ArrayList<User>? = null,
    private val teams: ArrayList<Team>? = null,
    val onTeamClicked: (team: Team) -> Unit
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    lateinit var usersAdapter: MembersAdapter
    lateinit var teamsAdapter: TeamAdapter

    inner class ViewHolder(val binding: HomeAdapterCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeAdapterCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recyclerView = holder.binding.rv
        teamsAdapter = TeamAdapter(teams , onTeamClicked)
        usersAdapter = MembersAdapter(users)

        if (position == 0) {
            val layoutManager = LinearLayoutManager(holder.binding.root.context)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = teamsAdapter
        } else {
            val gridLayoutManager = GridLayoutManager(
                holder.binding.root.context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.adapter = usersAdapter
        }
    }

    override fun getItemCount(): Int = 2

}
