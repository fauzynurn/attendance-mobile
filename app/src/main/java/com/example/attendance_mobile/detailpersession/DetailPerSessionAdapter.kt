package com.example.attendance_mobile.detailpersession

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.attendance_mobile.R
import com.example.attendance_mobile.utils.Constants
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.detail_per_session_item.view.*

class DetailPerSessionAdapter (private val presenter: DetailPerSessionPresenter) : RecyclerView.Adapter<DetailPerSessionAdapter.SessionViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_per_session_item, parent, false),viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position,itemCount)
    }
    override fun getItemCount(): Int {
        return presenter.size()
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        presenter.onBindItem(holder,position)
    }

    class SessionViewHolder(private val v: View, viewType: Int) : RecyclerView.ViewHolder(v), DetailPerSessionContract.ItemViewContract{
        init {
            v.timeline.initLine(viewType)
        }
        override fun setSessionNum(session : Int) {
            v.session_text.text = "KehadiranPerSesi ke-$session"
        }

        override fun setMarker(status: Int) {
            when(status){
                Constants.HADIR -> {
                    v.timeline.marker = VectorDrawableCompat.create(v.resources,R.drawable.ic_checked, v.context.theme)
                }
                Constants.TIDAK_HADIR -> {
                    v.timeline.marker = VectorDrawableCompat.create(v.resources,R.drawable.ic_cancel, v.context.theme)
                }
                Constants.TANPA_KETERANGAN -> {
                    v.timeline.marker = VectorDrawableCompat.create(v.resources,R.drawable.no_status_circle, v.context.theme)
                }
            }
        }

        override fun setSessionTime(startTime: String, endTime: String) {
            v.session_time.text = "$startTime - $endTime"
        }

    }
}