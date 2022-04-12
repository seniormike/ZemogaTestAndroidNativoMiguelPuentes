package com.example.mobiletestzemogamiguelpuentes.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletestzemogamiguelpuentes.R
import com.example.mobiletestzemogamiguelpuentes.model.Comment

class AdapterComments(
    private val context: Context,
    private var list: List<Comment>,
) :
    RecyclerView.Adapter<AdapterComments.ViewHolder>() {

    fun setData(list: List<Comment>) {
        this.list = list
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        var title: TextView = mView.findViewById(R.id.comment_title)
        var description: TextView = mView.findViewById(R.id.comment_description)
        var email: TextView = mView.findViewById(R.id.comment_email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.title.text = item.name
        holder.description.text = item.body
        holder.email.text = item.email
    }

}