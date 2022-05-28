package com.example.ravn.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ravn.R
import com.example.ravnstarwars.AllPeopleStarWarsQuery


class AllPeopleAdapter(
    context: Context,
    peopleList: MutableList<AllPeopleStarWarsQuery.Person>) :
    RecyclerView.Adapter<AllPeopleAdapter.PersonViewHolder>()
{
    class PersonViewHolder(itemView: View, adapter: AllPeopleAdapter) :
        RecyclerView.ViewHolder(itemView) {
        private val myAdapter = adapter
        val personName: TextView = itemView.findViewById(R.id.personName)
        val personFooter: TextView = itemView.findViewById(R.id.personFooter)
        init {
            itemView.setOnClickListener {
                myAdapter.onItemClick(layoutPosition)
            }
        }
    }
    private val myPeopleList = peopleList
    private val myInflater = LayoutInflater.from(context)
    private var myOnItemClickListener: ((AllPeopleStarWarsQuery.Person) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val myItemView = myInflater.inflate(R.layout.item_people, parent, false)
        return PersonViewHolder(myItemView, this)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = myPeopleList[position]
        holder.personName.text = person.name
        val personSpecies = person.species?.name ?: "Unknown"
        val personHomeWorld = person.homeworld?.name ?: "Far, far away"
        holder.personFooter.text = "$personSpecies from $personHomeWorld"
    }

    override fun getItemCount(): Int {
        return myPeopleList.size
    }

    fun onItemClickListener(listener: (AllPeopleStarWarsQuery.Person) -> Unit) {
        myOnItemClickListener = listener
    }

    private fun onItemClick(position: Int) {
        myOnItemClickListener?.invoke(myPeopleList[position])
    }
}