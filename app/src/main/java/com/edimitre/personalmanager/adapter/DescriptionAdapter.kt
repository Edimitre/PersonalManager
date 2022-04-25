package com.edimitre.personalmanager.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edimitre.personalmanager.R
import com.edimitre.personalmanager.data.model.Description

class DescriptionAdapter : RecyclerView.Adapter<DescriptionAdapter.DescriptionHolder>() {

    private var allDescriptions: List<Description>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.description_item_view, parent, false)

        return DescriptionHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionHolder, position: Int) {


        val description: Description? = allDescriptions?.getOrNull(position)

        if (description != null) {
            holder.descriptionName.text = description.name

            holder.addExpenseToSelectedDescription.setOnClickListener(View.OnClickListener {
                Log.e("Adapter Message : ", "clicked product :" + description.name)
            })

        }

    }

    override fun getItemCount(): Int {
        return allDescriptions?.size ?: 0
    }

    class DescriptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val descriptionName: TextView = itemView.findViewById(R.id.description_name_text)

        val addExpenseToSelectedDescription: Button =
            itemView.findViewById(R.id.btn_add_expense_to_this_description)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun putDescriptions(descriptionList: List<Description>) {
        allDescriptions = descriptionList
        notifyDataSetChanged()
    }

    fun getDescriptionByPos(pos: Int): Description? {
        return this.allDescriptions?.get(pos)
    }

}