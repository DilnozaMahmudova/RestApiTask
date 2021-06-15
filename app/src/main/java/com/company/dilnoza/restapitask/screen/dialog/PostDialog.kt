package com.company.dilnoza.restapitask.screen.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.company.dilnoza.restapitask.databinding.DialogContBinding
import com.company.dilnoza.restapitask.source.room.entity.PostData
import com.company.dilnoza.restapitask.utils.SingleBlock


class PostDialog(context: Context, actionName:String) : AlertDialog(context) {
    private val binding= DialogContBinding.inflate(LayoutInflater.from(context),null,false)
    private var listener: SingleBlock<PostData>? = null
    private var postData:PostData? = null

    init {
        setView(binding.root)
        setButton(BUTTON_POSITIVE, actionName) { _, _ ->
            val data = postData ?: PostData(0,"","",1)
            data.title = binding.inputTitle.text.toString()
            data.body=binding.inputBody.text.toString()
            listener?.invoke(data)
        }
        setButton(BUTTON_NEGATIVE, "Cancel") { _, _ -> }
    }

    fun setPostData(post: PostData) = with(binding.root) {
        postData = post
        binding.inputTitle.setText(post.title)
       binding.inputBody.setText(post.body)
    }


    fun setOnClickListener(block: SingleBlock<PostData>) {
        listener = block
    }
}