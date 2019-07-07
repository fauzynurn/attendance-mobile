package com.example.attendance_mobile.home.homedosen.jadwalpengganti

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_mobile.R

class JwlPenggantiAdapter(private val presenter: JwlPenggantiPresenter) : RecyclerView.Adapter<JwlPenggantiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JwlPenggantiViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.jwl_pengganti_item_layout, parent, false)
        return JwlPenggantiViewHolder(v)
    }

    override fun getItemCount(): Int {
        return presenter.jwlListSize()
    }

    override fun onBindViewHolder(holder: JwlPenggantiViewHolder, position: Int) {
        presenter.onBindJwlListItem(position,holder)
    }
}