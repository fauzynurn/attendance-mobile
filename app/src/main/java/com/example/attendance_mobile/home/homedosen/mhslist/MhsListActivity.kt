package com.example.attendance_mobile.home.homedosen.mhslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.mhs_list_layout.*

class MhsListActivity : AppCompatActivity(), BaseView<MhsListPresenter>, MhsListContract.ViewContract {

    override lateinit var presenter: MhsListPresenter
    private lateinit var adapter: MhsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mhs_list_layout)
        presenter = MhsListPresenter(this, RemoteRepository())
        adapter = MhsListAdapter(presenter)
        mhs_list.adapter = adapter
        mhs_list.layoutManager = LinearLayoutManager(this)
        presenter.doFetchMhsList()
    }

    override fun setJmlhHadir(jml: Int) {
        total_present.text = jml.toString()
    }

    override fun setJmlTdkHadir(jml: Int) {
        total_absent.text = jml.toString()
    }

    override fun onMhsListLoaded() {
        loading_view.visibility = View.GONE
    }

    override fun handleEmptyMhsList() {
        no_data_found_text.visibility = View.VISIBLE
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(mhs_list_parent, message, Snackbar.LENGTH_LONG).show()
    }

    override fun refreshList() {
        adapter.notifyDataSetChanged()
    }
}