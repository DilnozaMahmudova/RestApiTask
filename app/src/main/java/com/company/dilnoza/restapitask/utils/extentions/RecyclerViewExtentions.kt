package com.company.dilnoza.restapitask.utils.extentions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.bindItem(block: View.() -> Unit) = block(itemView)