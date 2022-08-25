package com.example.retrocoroutines.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrocoroutines.api.RetrofitInstance
import com.example.retrocoroutines.models.Post
import kotlinx.coroutines.launch

private const val TAG = "EditViewModel"

class EditViewModel : ViewModel() {
    private val _post: MutableLiveData<Post?> = MutableLiveData()
    val post: LiveData<Post?>
        get() = _post

    private val _currentStatus = MutableLiveData(ResultStatus.IDLE)
    val currentStatus: LiveData<ResultStatus>
        get() = _currentStatus

    private val _wasDeletionSuccessful = MutableLiveData(false)
    val wasDeletionSuccessful: LiveData<Boolean>
        get() = _wasDeletionSuccessful

    fun updatePost(postId: Int, newPostData: Post) {
        viewModelScope.launch {
            try {
                _post.value = null
                _currentStatus.value = ResultStatus.WORKING
                val updatedPost = RetrofitInstance.api.updatePost(postId, newPostData)
                _post.value = updatedPost
                _currentStatus.value = ResultStatus.SUCCESS
            } catch (e: Exception) {
                _currentStatus.value = ResultStatus.ERROR
            }
        }
    }

    fun patchPost(postId: Int, title: String, body: String) {
        viewModelScope.launch {
            try {
                _currentStatus.value = ResultStatus.WORKING
                _post.value = null
                val patchedPost =
                    RetrofitInstance.api.patchPost(postId, mapOf("title" to title, "body" to body))
                _post.value = patchedPost
                _currentStatus.value = ResultStatus.SUCCESS
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                _currentStatus.value = ResultStatus.ERROR

            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                _currentStatus.value = ResultStatus.WORKING
                RetrofitInstance.api.deletePost("anishauth1112", postId)
                _post.value = null
                _currentStatus.value = ResultStatus.SUCCESS
                _wasDeletionSuccessful.value = true
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                _currentStatus.value = ResultStatus.ERROR
                _wasDeletionSuccessful.value = false
            }
        }
    }

}