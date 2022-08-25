package com.example.retrocoroutines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrocoroutines.models.Post

class BlogPostAdapter(
    private val context: Context,
    private val posts: List<Post>,
    private val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<BlogPostAdapter.BlogPostViewHolder>() {

    interface ItemClickListener {
        fun onClick(post: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogPostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_blog_post, parent, false)
        return BlogPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogPostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

    inner class BlogPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId = itemView.findViewById<TextView>(R.id.tvId)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvBody = itemView.findViewById<TextView>(R.id.tvBlogBody)

        fun bind(post: Post) {
            tvId.text = "Post #${post.id}"
            tvTitle.text = post.title
            tvBody.text = post.body
            itemView.setOnClickListener {
                itemClickListener.onClick(post)
            }
        }
    }
}

