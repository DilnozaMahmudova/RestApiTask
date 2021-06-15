package com.company.dilnoza.restapitask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dilnoza.restapitask.app.App
import com.company.dilnoza.restapitask.databinding.ActivityMainBinding
import com.company.dilnoza.restapitask.screen.adapter.PostAdapter
import com.company.dilnoza.restapitask.screen.dialog.PostDialog
import com.company.dilnoza.restapitask.source.AppDatabase
import com.company.dilnoza.restapitask.source.retrofit.ApiClient
import com.company.dilnoza.restapitask.source.retrofit.PostsApi
import com.company.dilnoza.restapitask.source.room.entity.PostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val adapter = PostAdapter()
    private val api = ApiClient.retrofit.create(PostsApi::class.java)
    private val database = AppDatabase.getDatabase(App.instance)
    private val postDao = database.postDao()
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
        loadFromDatabase()
        deletePost()
        updatePost()
    }

    private fun deletePost() {
        adapter.setOnItemDeleteListener {
            val post = PostData(it.id, it.body, it.title, it.userId)

            api.remove(post.id).enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Please try again", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<Any>,
                    response: Response<Any>
                ) {
                    if (response.isSuccessful) {
                        val ls = adapter.currentList.toMutableList()
                        ls.remove(post)
                        runOnUiThread { adapter.submitList(ls) }
                        executor.execute { postDao.delete(it) }
                    } else {
                        Toast.makeText(this@MainActivity, "Post not deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            })
        }
    }

    private fun updatePost() {
        adapter.setOnItemEditListener { it ->
            val dialog = PostDialog(this, "Edit")
            dialog.setPostData(it)
            dialog.setOnClickListener {
                api.update(it.id, it).enqueue(object : Callback<PostData> {
                    override fun onFailure(
                        call: Call<PostData>,
                        t: Throwable
                    ) {
                        Toast.makeText(this@MainActivity, "onFailure", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<PostData>,
                        response: Response<PostData>
                    ) {
                        val data = response.body() ?: return
                        if (response.isSuccessful) {
                            val ls = adapter.currentList.toMutableList()
                            val index = ls.indexOfFirst { it.id == data.id }
                            ls[index] = data
                            adapter.submitList(ls)
                            adapter.notifyItemChanged(index)
                            executor.execute { postDao.update(data) }
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Post not updated",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                })
            }
            dialog.show()
        }
    }

    private fun loadPost() {
        api.getPosts().enqueue(object : Callback<List<PostData>> {
            override fun onFailure(
                call: Call<List<PostData>>,
                t: Throwable
            ) {
                Toast.makeText(this@MainActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<PostData>>,
                response: Response<List<PostData>>
            ) {
                val data = response.body() ?: return
                if (response.isSuccessful) {
                    executor.execute {
                        postDao.deleteAll(postDao.getAll())
                        runOnUiThread { adapter.submitList(data) }
                        postDao.insertAll(data)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Posts not loaded",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        })
    }
    private fun loadFromDatabase(){
        executor.execute {
            val ls=postDao.getAll()
            runOnUiThread { adapter.submitList(ls) }
        }
    }

    private fun addPost() {
        val dialog = PostDialog(this, "Add")
        dialog.setOnClickListener {
            api.add(it).enqueue(object : Callback<PostData> {
                override fun onFailure(
                    call: Call<PostData>,
                    t: Throwable
                ) {
                    Toast.makeText(this@MainActivity, "$t", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<PostData>,
                    response: Response<PostData>
                ) {
                    val data = response.body() ?: return
                    if (response.isSuccessful) {
                        val ls = adapter.currentList.toMutableList()
                        ls.add(data)
                        runOnUiThread {
                            adapter.submitList(ls)
                            binding.recycler.smoothScrollToPosition(ls.size - 1)
                        }
                        executor.execute { postDao.insert(data) }
                    } else {
                        Toast.makeText(this@MainActivity, "Post not added", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            })
        }
        dialog.show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAdd -> {
                addPost()
            }
            R.id.menuBack -> finish()
            R.id.menuRefresh -> {
                loadPost()
            }
        }
        return true
    }


}