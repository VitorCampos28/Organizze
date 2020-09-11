package com.example.organize.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.organize.R
import com.example.organize.model.Movement

public class AdapterMovement(var movement:List<Movement> , var context: Context) : RecyclerView.Adapter<AdapterMovement.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemList:View = LayoutInflater.from(parent.context).inflate(R.layout.activity_adapter_movement,parent ,false)
        return MyViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var movePosition:Movement = movement[position]
        holder.title.text = movePosition.description
        holder.textValue.text = movePosition.value.toString()
        holder.category.text = movePosition.category

        if (movePosition.type == "d" ){
            holder.textValue.setTextColor(context.resources.getColor(R.color.colorAccentBill))
            holder.textValue.text = "-" + movePosition.value
        }
    }

    override fun getItemCount(): Int {
        return movement.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var title:TextView  = itemView.findViewById(R.id.textAdapterTitle)
        var textValue:TextView = itemView.findViewById(R.id.textAdapterValue)
        var category:TextView  = itemView.findViewById(R.id.textAdapterCategory)
    }
}