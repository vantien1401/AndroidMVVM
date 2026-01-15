package com.example.androidmvvm.feature.main.list

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.androidmvvm.feature.main.model.RepoItem

class RepoDiffCallback : DiffUtil.ItemCallback<RepoItem>() {
    override fun areItemsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RepoItem, newItem: RepoItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: RepoItem, newItem: RepoItem): Any? {
        val bundle = Bundle()
        
        if (oldItem.repoName != newItem.repoName) {
            bundle.putString(PAYLOAD_REPO_NAME, newItem.repoName)
        }
        if (oldItem.ownerName != newItem.ownerName) {
            bundle.putString(PAYLOAD_OWNER_NAME, newItem.ownerName)
        }
        if (oldItem.imageUrl != newItem.imageUrl) {
            bundle.putString(PAYLOAD_IMAGE_URL, newItem.imageUrl)
        }
        
        return if (bundle.isEmpty) null else bundle
    }

    companion object {
        const val PAYLOAD_REPO_NAME = "repo_name"
        const val PAYLOAD_OWNER_NAME = "owner_name"
        const val PAYLOAD_IMAGE_URL = "image_url"
    }
}