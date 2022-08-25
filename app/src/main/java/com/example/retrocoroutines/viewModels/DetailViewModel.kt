package com.example.retrocoroutines.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrocoroutines.api.RetrofitInstance
import com.example.retrocoroutines.models.Post
import com.example.retrocoroutines.models.User
import kotlinx.coroutines.launch

private const val TAG = "DetailViewModel"

class DetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _post: MutableLiveData<Post> = MutableLiveData()
    val post: LiveData<Post>
        get() = _post

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User>
        get() = _user

    fun getPostDetail(postId: Int) {
        val api = RetrofitInstance.api
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val fetchedPost = api.getPost(postId)
                val fetchedUser = api.getUser(fetchedPost.userId)
                Log.i(TAG, "Fetched User: ${fetchedUser}")
                _post.value = fetchedPost
                _user.value = fetchedUser
            } catch (e: Exception) {
                Log.e(TAG, "Exception ${e}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}