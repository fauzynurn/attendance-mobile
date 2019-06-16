package com.example.attendance_mobile.home.homedosen.mhslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homedosen.viewholder.MhsListViewHolder

class MhsListAdapter(private val presenter: MhsListPresenter) : RecyclerView.Adapter<MhsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MhsListViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mhs_list_item, parent, false)
        return MhsListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return presenter.size()
    }

    override fun onBindViewHolder(holder: MhsListViewHolder, position: Int) {
        presenter.onBindMhsItem(holder,position)
    }
}