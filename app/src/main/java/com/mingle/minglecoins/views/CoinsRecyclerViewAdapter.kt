package com.mingle.minglecoins.views

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.mingle.minglecoins.databinding.RvCoinItemBinding
import com.mingle.minglecoins.models.Coin

class CoinsRecyclerViewAdapter(private var lstCoins: List<Coin>, private val glide : RequestManager) : RecyclerView.Adapter<CoinsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvCoinItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, glide)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(lstCoins[position])

    override fun getItemCount(): Int = lstCoins.size

    class ViewHolder(private var binding: RvCoinItemBinding, private var glide: RequestManager) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) {
            binding.coinModel = coin
            binding.executePendingBindings()
            glide.load("https://www.cryptocompare.com/${coin.imageUrl}").into(binding.imgCoin)

        }
    }

    fun replaceData(newData: List<Coin>) {
        lstCoins = newData
    }
}
