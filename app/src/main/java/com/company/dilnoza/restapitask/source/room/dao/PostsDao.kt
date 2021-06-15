package com.company.dilnoza.restapitask.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.company.dilnoza.restapitask.source.room.entity.PostData

@Dao
interface PostsDao : BaseDao<PostData> {
    @Query("SELECT * FROM PostData")
    fun getAll(): List<PostData>

    @Query("Delete from PostData")
    fun clearPosts()
}