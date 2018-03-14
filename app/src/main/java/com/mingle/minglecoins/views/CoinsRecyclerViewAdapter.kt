package com.mingle.minglecoins.views

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mingle.minglecoins.databinding.RvCoinItemBinding
import com.mingle.minglecoins.models.Coin

class CoinsRecyclerViewAdapter(private var lstCoins: List<Coin>) : RecyclerView.Adapter<CoinsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = RvCoinItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(lstCoins[position])

    override fun getItemCount(): Int = lstCoins.size

    class ViewHolder(private var binding: RvCoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) {
            binding.coinModel = coin
            binding.executePendingBindings()
        }
    }

    fun replaceData(newData: List<Coin>) {
        lstCoins = newData
    }
}
