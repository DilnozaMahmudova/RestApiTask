package com.company.dilnoza.restapitask.screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.company.dilnoza.restapitask.R
import com.company.dilnoza.restapitask.databinding.ItemBinding
import com.company.dilnoza.restapitask.source.room.entity.PostData
import com.company.dilnoza.restapitask.utils.SingleBlock
import com.company.dilnoza.restapitask.utils.extentions.bindItem

class PostAdapter:androidx.recyclerview.widget.ListAdapter<PostData, PostAdapter.ViewHolder>(PostData.ITEM_CALLBACK){
    private var listenerEdit: SingleBlock<PostData>? = null
    private var listenerDelete: SingleBlock<PostData>? = null
    private lateinit var binding: ItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)=holder.bind()
    fun setOnItemEditListener(block: SingleBlock<PostData>) {
        listenerEdit = block
    }

    fun setOnItemDeleteListener(block: SingleBlock<PostData>) {
        listenerDelete = block
    }
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        init{
            itemView.apply {
                binding.menu.setOnClickListener {
                    val menu = PopupMenu(context,it)
                    menu.inflate(R.menu.menu_more)
                    menu.setOnMenuItemClickListener {item->
                        when(item.itemId){
                            R.id.menuDelete -> listenerDelete?.invoke(getItem(adapterPosition))
                            R.id.menuEdit -> listenerEdit?.invoke(getItem(adapterPosition))
                        }
                        true
                    }
                    menu.show()
                }
            }
        }
        fun bind() = bindItem {
            val d = getItem(adapterPosition)
            binding.titlePost.text =d.title
            binding.bodyPost.text = d.body
            binding.idPost.text=d.id.toString()

        }
    }
}
