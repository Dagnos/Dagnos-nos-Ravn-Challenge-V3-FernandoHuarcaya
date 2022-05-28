package com.example.ravn.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ravn.R
import com.example.ravnstarwars.DetailedPersonsStarWarsQuery


class VehiclesAdapter (context: Context, vehicleList: MutableList<DetailedPersonsStarWarsQuery.Vehicle>) :
    RecyclerView.Adapter<VehiclesAdapter.VehicleViewHolder>() {
    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataL: TextView = itemView.findViewById(R.id.dataL)
        init {
            // the vehicles do not need a right space
            itemView.findViewById<TextView>(R.id.dataR).visibility = View.GONE
        }
    }

    private val mVehicleList = vehicleList
    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val mItemView = mInflater.inflate(R.layout.item_info, parent, false)
        return VehicleViewHolder(mItemView)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.dataL.text = mVehicleList[position].name ?: "Unknown"
    }

    override fun getItemCount(): Int {
        return mVehicleList.size
    }
}