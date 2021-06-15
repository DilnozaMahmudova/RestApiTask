package com.company.dilnoza.restapitask.source.room.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostData(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var body: String,
    var title: String,
    var userId: Int
){
    companion object{
        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<PostData>() {
            override fun areItemsTheSame(oldItem: PostData, newItem: PostData) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: PostData, newItem: PostData) = oldItem.title == newItem.title && oldItem.body == newItem.body
        }
    }
}