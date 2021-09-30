package com.test.commbank.ui.browseprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.commbank.data.model.BrowseEmployee
import com.test.commbank.databinding.ItemBrowseBinding
import org.jetbrains.anko.sdk27.coroutines.onClick

class BrowseProfileAdapter(
    private val navigator: BrowseProfileNavigator
): PagingDataAdapter<BrowseEmployee.Response.Data, BrowseProfileAdapter.ViewHolder>(DiffUtilCallBack()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, navigator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemBrowseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    class ViewHolder(private var binding: ItemBrowseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: BrowseEmployee.Response.Data,
            navigator: BrowseProfileNavigator
        ) {
            binding.user = data
            binding.btnDelete.onClick {
                navigator.deleteUser(data)
            }

            binding.btnEdit.onClick {
                navigator.updateUser(data)
            }
        }
    }

    class DiffUtilCallBack: DiffUtil.ItemCallback<BrowseEmployee.Response.Data>() {
        override fun areItemsTheSame(oldItem: BrowseEmployee.Response.Data, newItem: BrowseEmployee.Response.Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BrowseEmployee.Response.Data, newItem: BrowseEmployee.Response.Data): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.email == newItem.email
        }
    }
}