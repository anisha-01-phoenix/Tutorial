package com.example.retrocoroutines

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.retrocoroutines.api.RetrofitInstance
import com.example.retrocoroutines.databinding.ActivityEditBinding
import com.example.retrocoroutines.models.Post
import com.example.retrocoroutines.viewModels.EditViewModel
import com.example.retrocoroutines.viewModels.ResultStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "EditActivity"

class EditActivity : AppCompatActivity() {
    private lateinit var viewModel: EditViewModel
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val post = intent.getSerializableExtra("EXTRA_POST") as Post
        title = "Editing Post #${post.id}"

        binding.etTitle.setText(post.title)
        binding.etContent.setText(post.body)

        viewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        viewModel.post.observe(this, Observer { updatedPost ->
            if (updatedPost == null) {
                binding.clPostResult.visibility = View.GONE
                return@Observer
            }
            binding.tvUpdatedTitle.text = updatedPost.title
            binding.tvUpdatedContent.text = updatedPost.body
            binding.clPostResult.visibility = View.VISIBLE
        })

        viewModel.currentStatus.observe(this, Observer { currentStatus ->
            when (currentStatus) {
                ResultStatus.IDLE -> {
                    binding.tvStatus.text = "Idle"
                    binding.tvStatus.setTextColor(Color.DKGRAY)
                }
                ResultStatus.WORKING -> {
                    binding.tvStatus.text = "Working"
                    binding.tvStatus.setTextColor(Color.MAGENTA)
                }
                ResultStatus.SUCCESS -> {
                    binding.tvStatus.text = "Success"
                    binding.tvStatus.setTextColor(Color.GREEN)
                }
                ResultStatus.ERROR -> {
                    binding.tvStatus.text = "Error"
                    binding.tvStatus.setTextColor(Color.RED)
                }
                else -> {
                    throw IllegalStateException("Unexpected Result state found")
                }
            }
        })

        viewModel.wasDeletionSuccessful.observe(this, Observer { wasDeletionSuccessful ->
            if (wasDeletionSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Post was not deleted", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnUpdatePut.setOnClickListener {
            viewModel.updatePost(
                post.id,
                Post(
                    post.userId,
                    post.id,
                    binding.etTitle.text.toString(),
                    binding.etContent.text.toString()
                )
            )
        }

        binding.btnUpdatePatch.setOnClickListener {
            viewModel.patchPost(
                post.id,
                binding.etTitle.text.toString(),
                binding.etContent.text.toString()
            )
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deletePost(post.id)
        }
    }

    //create our own coroutine scope
    private fun createPost() {
        CoroutineScope(Dispatchers.IO).launch {
            val localNewPost = Post(2, 32, "Anisha", "Content of Post#32")
            val newPost = RetrofitInstance.api.createPost(localNewPost)
            Log.i(TAG, "New Post :$newPost")
            //form data post creation
            val urlEncodedPost =
                RetrofitInstance.api.createPostUrlEncoded(4, "New Title", "New Content")
            Log.i(TAG, "New Post :$urlEncodedPost")

        }
    }




}