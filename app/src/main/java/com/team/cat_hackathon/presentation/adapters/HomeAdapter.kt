import android.view.LayoutInflater
import android.view.ViewGroup
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
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

     val teamsAdapter: TeamAdapter = TeamAdapter(teams, onTeamClicked)
     val usersAdapter: MembersAdapter = MembersAdapter(users)
    lateinit var usersRecyclerView: RecyclerView
    lateinit var teamsRecyclerView: RecyclerView

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
        when (getItemViewType(position)) {
            0 -> {
                val layoutManager = LinearLayoutManager(holder.itemView.context)
                teamsRecyclerView = holder.binding.rv
                teamsRecyclerView.layoutManager = layoutManager
                teamsRecyclerView.adapter = teamsAdapter
            }
            1 -> {
                val gridLayoutManager = GridLayoutManager(
                    holder.itemView.context,
                    2,
                    GridLayoutManager.VERTICAL,
                    false
                )
                usersRecyclerView = holder.binding.rv
                usersRecyclerView.layoutManager = gridLayoutManager
                usersRecyclerView.adapter = usersAdapter
            }
        }
    }

    override fun getItemCount(): Int = 2


}
