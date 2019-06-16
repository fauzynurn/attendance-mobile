package com.example.attendance_mobile.home.homedosen.startclass.startschedule

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.DsnSchedule
import com.example.attendance_mobile.home.homedosen.bottomsheet.BaseBottomSheet
import com.example.attendance_mobile.home.homedosen.startclass.beaconscan.BeaconBtmSheet
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.start_schedule_bottom_sheet_layout.view.*
import java.util.*

class StartScheduleBtmSheet : BaseBottomSheet(), BaseView<StartScheduleBtmSheetPresenter>,
    StartScheduleBtmSheetContract.ViewContract,TimePickerDialog.OnTimeSetListener {

    override lateinit var presenter: StartScheduleBtmSheetPresenter
    lateinit var v: View
    lateinit var dsnSchedule: DsnSchedule
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.start_schedule_bottom_sheet_layout, container, false)
        dsnSchedule = arguments?.getSerializable("dsnSchedule") as DsnSchedule
        presenter = StartScheduleBtmSheetPresenter(dsnSchedule,this, RemoteRepository())
        v.start_room_dropdown.apply {
            isEnabled = false
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    v.start_schedule_real_group_chips.visibility = View.GONE
                    v.no_data_found_text.visibility = View.GONE
                    v.loading_view.visibility = View.VISIBLE
                    presenter.doFetchTimeFromRoom(position)
                }
            }
        }
        v.start_time_timepicker.setEndIconOnClickListener {
            val now = Calendar.getInstance()
            val dpd = TimePickerDialog(
                context, this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),true
            )
            dpd.show()
        }
        v.start_schedule_real_group_chips.apply {
            visibility = View.GONE
            v.no_data_found_text.visibility = View.VISIBLE
            setOnCheckedChangeListener { group, checkedId ->
                v.start_class_btn.isEnabled = group.checkedChipId != -1
                val selectedChip: Chip? = v.findViewById(checkedId)
                presenter.onRoomChipSelected(selectedChip?.text.toString())
            }
        }
        v.modified_filter_radio_group.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == v.based_on_room.id) {
                v.start_room_dropdown.apply {
                    visibility = View.VISIBLE
                    isEnabled = true
                }
                v.start_time_timepicker.visibility = View.GONE
                v.time_label.apply {
                    setTextColor(resources.getColor(R.color.black))
                    visibility = View.VISIBLE
                }
                v.room_label.visibility = View.GONE
                v.setOnClickListener(null)
                v.start_schedule_real_group_chips.apply {
                    visibility = View.GONE
                    v.no_data_found_text.visibility = View.VISIBLE
                    setOnCheckedChangeListener { group, checkedId ->
                        Log.i("CHECKEDID","CHECKEDID: $checkedId")
                        v.start_class_btn.isEnabled = group.checkedChipId != -1
                        val selectedChip: Chip? = v.findViewById(checkedId)
                        presenter.onTimeChipSelected(selectedChip?.text.toString())
                    }
                }
            } else {
                v.start_time_timepicker.visibility = View.VISIBLE
                v.start_room_dropdown.visibility = View.GONE
                v.room_label.apply {
                    setTextColor(resources.getColor(R.color.black))
                    visibility = View.VISIBLE
                }
                v.time_label.visibility = View.GONE
                v.setOnClickListener(null)
                v.start_schedule_real_group_chips.apply {
                    visibility = View.GONE
                    v.no_data_found_text.visibility = View.VISIBLE
                    setOnCheckedChangeListener { group, checkedId ->
                        v.start_class_btn.isEnabled = group.checkedChipId != -1
                        val selectedChip: Chip? = v.findViewById(checkedId)
                        presenter.onRoomChipSelected(selectedChip?.text.toString())
                    }
                }
            }
        }
        v.start_schedule_radio_group.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == v.use_modified.id) {
                v.start_class_btn.isEnabled = false
                presenter.doFetchRoomList()
                v.based_on_room.isEnabled = true
                v.based_on_time.isEnabled = true
                v.start_room_dropdown.isEnabled = true
                v.time_label.setTextColor(resources.getColor(R.color.black))
                v.no_data_found_text.setTextColor(resources.getColor(R.color.black))
            } else {
                v.start_schedule_real_group_chips.visibility = View.GONE
                v.no_data_found_text.apply{
                    visibility = View.VISIBLE
                    setTextColor(resources.getColor(R.color.grey))
                }
                v.start_class_btn.isEnabled = true
                v.based_on_room.isEnabled = false
                v.based_on_time.isEnabled = false
                v.start_room_dropdown.isEnabled = false
                v.time_label.setTextColor(resources.getColor(R.color.grey))
                v.no_data_found_text.setTextColor(resources.getColor(R.color.grey))
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

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        v.start_schedule_real_group_chips.visibility = View.GONE
        val time = "$hourOfDay:$minute"
        v.start_time_timepicker_field.setText(time)
        presenter.doFetchRoomFromTime(time)
        v.no_data_found_text.visibility = View.GONE
        v.loading_view.visibility = View.VISIBLE
    }

    override fun onRoomDataReady(data : List<String>) {
        v.loading_view.visibility = View.GONE
        v.start_schedule_real_group_chips.visibility = View.VISIBLE
        v.start_schedule_real_group_chips.removeAllViews()
        for (item in data) {
            val chip = Chip(v.start_schedule_real_group_chips.context)
            chip.text = item
            chip.isCheckable = true
            v.start_schedule_real_group_chips.addView(chip)
        }
    }

    override fun onTimeDataReady(data : List<String>) {
        v.loading_view.visibility = View.GONE
        v.start_schedule_real_group_chips.visibility = View.VISIBLE
        v.start_schedule_real_group_chips.removeAllViews()
        for (item in data) {
            val chip = Chip(v.start_schedule_real_group_chips.context)
            chip.text = item
            chip.isCheckable = true
            v.start_schedule_real_group_chips.addView(chip)
        }
    }
}