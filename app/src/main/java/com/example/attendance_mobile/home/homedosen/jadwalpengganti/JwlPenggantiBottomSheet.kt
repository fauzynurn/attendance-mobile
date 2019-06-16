package com.example.attendance_mobile.home.homedosen.jadwalpengganti

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homedosen.HomeDsnContract
import com.example.attendance_mobile.home.homedosen.HomeDsnPresenter
import com.example.attendance_mobile.home.homedosen.bottomsheet.BaseBottomSheet
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.jwl_pengganti_bottom_sheet_layout.view.*
import java.util.*


class JwlPenggantiBottomSheet(val presenter: HomeDsnPresenter) : BaseBottomSheet(),
    DatePickerDialog.OnDateSetListener,
    HomeDsnContract.JwlPenggantiBtmSheetViewContract {
    lateinit var v: View
    lateinit var matkulAdapter: ArrayAdapter<String>
    lateinit var kelasAdapter: ArrayAdapter<String>
    lateinit var ruanganAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.jwl_pengganti_bottom_sheet_layout, container, false)
        presenter.initAndAttachJwlPenggantiBottomSheet(this)
        presenter.doFetchMatkulList()
//        v.tgl_field.setEndIconOnClickListener {
//            val now = Calendar.getInstance()
//            val dpd = DatePickerDialog(
//                context, this,
//                now.get(Calendar.YEAR),
//                now.get(Calendar.MONTH),
//                now.get(Calendar.DAY_OF_MONTH)
//            )
//            dpd.datePicker.minDate = now.timeInMillis
//            dpd.show()
//        }

//        v.kelas_dropdown.apply {
//            keyListener = null
//            setOnItemClickListener { _, _, position, _ -> presenter.onSelectKelasItem(position) }
//            setOnTouchListener { v, event ->
//                false
//            }
//        }
        return v
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        v.real_group_chips.visibility = View.GONE
        val tgl = "$dayOfMonth - $month - $year"
        v.tgl_datepicker.setText(tgl)
        presenter.doFetchSessionList(tgl)
        v.no_session_available_warning.visibility = View.GONE
        v.loading_view.visibility = View.VISIBLE
    }

    override fun onMatkulListReady(data: List<String>) {
        initMatkulArrayAdapter(data)
        v.matkul_dropdown.apply {
            keyListener = null
            setOnItemClickListener { _, _, position, _ ->
                presenter.onSelectMatkulItem(position)
            }
        }
        v.tgl_field.setEndIconOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(
                context, this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.datePicker.minDate = now.timeInMillis
            dpd.show()
        }

        v.kelas_dropdown.apply {
            keyListener = null
            setOnItemClickListener { _, _, position, _ -> presenter.onSelectKelasItem(position) }
        }
    }
//        v.matkul_field.apply {
//            isEnabled = true
//            boxStrokeColor = Color.parseColor("#afafaf")
//        }
//
//        v.kelas_field.isEnabled = true
//
//        v.tgl_field.isEnabled = true

    fun initMatkulArrayAdapter(data: List<String>) {
        matkulAdapter = ArrayAdapter(
            context,
            R.layout.dropdown_menu_popup_item,
            data
        )
        v.matkul_dropdown.setAdapter(matkulAdapter)
    }

    override fun setUpConfirmButton(){
        v.jwl_confirm_btn.apply{
            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#0766ff"))
            isEnabled = true
            setTextColor(resources.getColor(R.color.white))
            setOnClickListener{
                presenter.onConfirmJwlPengganti()
            }
        }
    }
    override fun initKelasArrayAdapter(data: List<String>) {
        kelasAdapter = ArrayAdapter(
            context,
            R.layout.dropdown_menu_popup_item,
            data
        )
        v.kelas_dropdown.setAdapter(kelasAdapter)
    }

    fun initRuanganArrayAdapter(data: List<String>) {
        ruanganAdapter = ArrayAdapter(
            context,
            R.layout.dropdown_menu_popup_item,
            data
        )
        v.ruangan_dropdown.setAdapter(ruanganAdapter)
    }

    override fun handleNoSessionFound() {
        v.loading_view.visibility = View.GONE
        v.no_session_available_warning.visibility = View.GONE
    }

    override fun onRoomDataReady(data : List<String>){
        initRuanganArrayAdapter(data)
        v.ruangan_dropdown.apply {
            keyListener = null
            setOnItemClickListener { _, _, position, _ ->
                setUpConfirmButton()
                presenter.onSelectRuanganItem(position) }
        }
    }
    override fun onSessionDataReady(data: List<String>) {
        v.loading_view.visibility = View.GONE
        v.real_group_chips.visibility = View.VISIBLE
        v.real_group_chips.removeAllViews()
        for (item in data) {
            val chip = Chip(v.real_group_chips.context)
            chip.text = item
            chip.isCheckable = true
            v.real_group_chips.addView(chip)
        }
        v.real_group_chips.setOnCheckedChangeListener { group, checkedId ->
            val selectedChip: Chip? = v.findViewById(checkedId)
            presenter.doFetchRoomList(selectedChip?.text.toString())
        }
    }
}