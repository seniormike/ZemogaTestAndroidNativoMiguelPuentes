package com.example.mobiletestzemogamiguelpuentes.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletestzemogamiguelpuentes.R
import com.example.mobiletestzemogamiguelpuentes.model.db.DataPost
import com.google.android.material.card.MaterialCardView

class AdapterPosts(
    private val context: Context,
    private var list: List<DataPost>,
    private var listener: OnClickDetail
) :
    RecyclerView.Adapter<AdapterPosts.ViewHolder>() {

    fun setData(list: List<DataPost>) {
        this.list = list
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        var title: TextView = mView.findViewById(R.id.post_title)
        var description: TextView = mView.findViewById(R.id.post_description)
        var favorite: ImageView = mView.findViewById(R.id.favorite_icon_post_item)
        var clickPost: MaterialCardView = mView.findViewById(R.id.click_post)
        var clickDelete: FrameLayout = mView.findViewById(R.id.deleteItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.description.text = item.body

        if (item.favorite) {
            holder.favorite.setImageDrawable(getDrawable(context, R.drawable.ic_favorite))
        } else {
            holder.favorite.setImageDrawable(getDrawable(context, R.drawable.ic_empty_star))
        }

        // Listeners
        holder.clickPost.setOnClickListener {
            listener.onClickDetail(item)
        }
        holder.clickDelete.setOnClickListener {
            listener.onClickDelete(item)
        }
    }

    interface OnClickDetail {
        fun onClickDetail(item: DataPost)
        fun onClickDelete(item: DataPost)
    }
}