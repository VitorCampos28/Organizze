package com.example.organize.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.organize.R
import com.example.organize.model.Movement

public class AdapterMovement(var movement:MutableList<Movement> , var context: Context) : RecyclerView.Adapter<AdapterMovement.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_adapter_movement, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var movePosition:Movement = movement[position]
        holder.title.text = movePosition.description
        holder.textValue.text = movePosition.value.toString()
        holder.category.text = movePosition.category

        if (movePosition.type == "d" || movePosition.type == "D" ){
            holder.textValue.setTextColor(context.resources.getColor(R.color.colorAccentBill))
            holder.textValue.text = "-" + movePosition.value
        }else{
            holder.textValue.setTextColor(context.resources.getColor(R.color.colorAccentIncome))
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