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
import com.team.cat_hackathon.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeAdapter(
    private val users: ArrayList<User>? = null,
    private val teams: ArrayList<Team>? = null,
    val onTeamClicked: (team: Team) -> Unit
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    lateinit var usersAdapter: MembersAdapter
    lateinit var teamsAdapter: TeamAdapter

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

        if (position == 0) {
            val layoutManager = LinearLayoutManager(holder.binding.root.context)
            teamsRecyclerView = holder.binding.rv
            teamsRecyclerView.layoutManager = layoutManager
                teamsAdapter = TeamAdapter(teams, onTeamClicked)
                teamsRecyclerView.adapter = teamsAdapter

        } else {
            val gridLayoutManager = GridLayoutManager(
                holder.binding.root.context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
            usersRecyclerView = holder.binding.rv
                usersRecyclerView.layoutManager = gridLayoutManager
                usersAdapter = MembersAdapter(users)
                usersRecyclerView.adapter = usersAdapter

        }
    }
    override fun getItemCount(): Int = 2

}
