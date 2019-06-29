package com.example.attendance_mobile.home.homedosen.startclass.startschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.home.homedosen.bottomsheet.BaseBottomSheet
import com.example.attendance_mobile.home.homedosen.startclass.beaconscan.BeaconBtmSheet
import com.example.attendance_mobile.model.remote.RemoteRepository
import kotlinx.android.synthetic.main.start_schedule_bottom_sheet_layout.view.*

class StartScheduleBtmSheet : BaseBottomSheet(), BaseView<StartScheduleBtmSheetPresenter>,
    StartScheduleBtmSheetContract.ViewContract {

    override lateinit var presenter: StartScheduleBtmSheetPresenter
    lateinit var v: View
    lateinit var dsnSchedule: JadwalDsn
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.start_schedule_bottom_sheet_layout, container, false)
        dsnSchedule = arguments?.getSerializable("dsnSchedule") as JadwalDsn
        presenter = StartScheduleBtmSheetPresenter(dsnSchedule, this, RemoteRepository())
        v.start_room_dropdown.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    v.start_class_btn.isEnabled = true
                    presenter.onSelectRoomItem(position)
                }
            }
        }
        v.start_schedule_radio_group.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == v.use_modified.id) {
                v.start_class_btn.isEnabled = false
                presenter.doFetchRoomList()
                v.start_room_dropdown.isEnabled = true
            } else {
                v.start_class_btn.isEnabled = true
                v.start_room_dropdown.isEnabled = false
            }
        }
        v.start_class_btn.setOnClickListener {
            dismiss()
            val bundle = Bundle()
            bundle.putSerializable("dsnSchedule", dsnSchedule)
            val beaconBottomSheet = BeaconBtmSheet()
            beaconBottomSheet.apply {
                arguments = bundle
                isCancelable = false
            }
            beaconBottomSheet
                .show(activity!!.supportFragmentManager, "beacon_btm_sheet")
        }

        return v
    }

    override fun initRoomAdapter(data: List<String>) {
        v.start_room_dropdown.adapter = ArrayAdapter(context, R.layout.dropdown_menu_popup_item, data)
    }

    override fun onRoomDataReady(data: List<String>) {

    }
}