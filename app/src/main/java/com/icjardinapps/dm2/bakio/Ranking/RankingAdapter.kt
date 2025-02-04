package com.icjardinapps.dm2.bakio.Ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icjardinapps.dm2.bakio.R

class RankingAdapter(private val rankingList: List<String>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(rankingList[position])
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }

    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankingTextView: TextView = itemView.findViewById(R.id.txtRanking)

        fun bind(ranking: String) {
            rankingTextView.text = ranking
        }
    }
}
