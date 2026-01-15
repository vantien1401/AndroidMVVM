package com.example.androidmvvm.feature.main.list

import android.os.Bundle
import com.example.androidmvvm.core.extension.setImageUrl
import com.example.androidmvvm.core.platform.BaseBindingViewHolder
import com.example.androidmvvm.databinding.ItemRepoBinding
import com.example.androidmvvm.feature.main.model.RepoItem

class RepoViewHolder(
    viewBinding: ItemRepoBinding,
    onItemClickAction: (repo: RepoItem) -> Unit,
) : BaseBindingViewHolder<RepoItem, ItemRepoBinding>(viewBinding, onItemClickAction) {

    override fun onBind(item: RepoItem) {
        with(viewBinding) {
            imgAvatar.setImageUrl(item.imageUrl)
            txtRepoName.text = item.repoName
            txtOwnerName.text = item.ownerName
        }
    }

    override fun onBind(item: RepoItem, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            super.onBind(item, payloads)
            return
        }

        payloads.forEach { payload ->
            if (payload is Bundle) {
                with(viewBinding) {
                    payload.getString(RepoDiffCallback.PAYLOAD_REPO_NAME)?.let {
                        txtRepoName.text = it
                    }
                    payload.getString(RepoDiffCallback.PAYLOAD_OWNER_NAME)?.let {
                        txtOwnerName.text = it
                    }
                    payload.getString(RepoDiffCallback.PAYLOAD_IMAGE_URL)?.let {
                        imgAvatar.setImageUrl(it)
                    }
                }
            }
        }
    }
}