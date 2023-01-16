package com.example.exercisetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exercisetracker.data.model.PR

class MyAdapter(private val prList: ArrayList<PR>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pr_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = prList[position]

        holder.weight.text = currentitem.weight
        holder.date.text = currentitem.date

    }

    override fun getItemCount(): Int {

        return prList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val weight : TextView = itemView.findViewById(R.id.weight)
        val date : TextView = itemView.findViewById(R.id.date)

    }
}