package com.icjardinapps.dm2.bakio.Ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icjardinapps.dm2.bakio.R
/**
 * Adaptador para el RecyclerView que muestra la lista de clasificación (ranking).
 * Este adaptador maneja una lista de objetos [String] que representan los elementos de clasificación.
 *
 * @param rankingList Lista de elementos de clasificación a mostrar en el RecyclerView.
 */
class RankingAdapter(private val rankingList: List<String>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {
/**
     * Infla el layout de un ítem individual del RecyclerView y crea una nueva instancia de [RankingViewHolder].
     *
     * @param parent El contenedor que contiene este item.
     * @param viewType El tipo de vista. No se usa en este caso.
     * @return Un nuevo [RankingViewHolder] con el layout inflado.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }
/**
     * Vincula los datos de la lista de clasificación a la vista en cada celda del RecyclerView.
     *
     * @param holder El [RankingViewHolder] que contiene las vistas.
     * @param position La posición en la lista de clasificación de los datos.
     */
    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(rankingList[position])
    }
    /**
     * Devuelve el número total de elementos en la lista de clasificación.
     *
     * @return El número de elementos en [rankingList].
     */
    override fun getItemCount(): Int {
        return rankingList.size
    }
     /**
     * ViewHolder para representar un ítem del ranking.
     * Este [ViewHolder] mantiene las vistas individuales de cada ítem del RecyclerView.
     *
     * @param itemView La vista inflada que contiene las vistas de cada ítem.
     */
    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankingTextView: TextView = itemView.findViewById(R.id.txtRanking)
         /**
         * Asocia los datos de un elemento de clasificación a las vistas correspondientes.
         *
         * @param ranking El texto de clasificación que se mostrará en el item.
         */
        fun bind(ranking: String) {
            rankingTextView.text = ranking
        }
    }


}
