import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.cat_hackathon.data.models.Team
import com.team.cat_hackathon.data.models.User
import com.team.cat_hackathon.databinding.HomeAdapterCardBinding

class HomeAdapter(private val users: List<User>? = null, private val teams: List<Team>? = null) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

//     lateinit var usersAdapter
//     lateinit var teamsAdapter
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
        val tv = holder.binding.tvv
        if (position == 0) {
            tv.text = "0"
            // TODO: create adapter for teams and pass that adapter to this recyclerView
        } else {
            tv.text = "1"
            // TODO: create adapter for users and pass that adapter to this recyclerView
        }
    }

    override fun getItemCount(): Int = 2

}
