package com.example.attendance_mobile.home.homedosen.jadwalpengganti

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.attendance_mobile.R
import com.example.attendance_mobile.home.homedosen.bottomsheet.BaseBottomSheet
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.jwl_pengganti_bottom_sheet_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class JwlPenggantiBottomSheet(val presenter: JwlPenggantiPresenter) : BaseBottomSheet(),
    JwlPenggantiContract.JwlPenggantiBtmSheetViewContract {
    lateinit var v: View
    lateinit var matkulAdapter: ArrayAdapter<String>
    lateinit var kelasAdapter: ArrayAdapter<String>
    lateinit var ruanganAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.jwl_pengganti_bottom_sheet_layout, container, false)
        presenter.initAndAttachJwlPenggantiBottomSheet(this)
        presenter.doFetchMatkulList()
        return v
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
                context, null,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.datePicker.minDate = now.timeInMillis
            dpd.setOnDateSetListener { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
                val tgl = simpleDateFormat.format(calendar.time)
                v.tgl_datepicker.setText(tgl)
                presenter.doFetchSessionList(tgl)
                v.real_group_chips.visibility = View.GONE
                v.no_session_available_warning.visibility = View.GONE
                v.loading_view.visibility = View.VISIBLE
            }
            dpd.show()
        }

        v.tgl_diganti_field.setEndIconOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(
                context, null,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.setOnDateSetListener { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
                val tgl = simpleDateFormat.format(calendar.time)
                v.tgl_diganti_datepicker.setText(tgl)
                presenter.onSelectOriginTime(tgl)
            }
            dpd.show()
        }

        v.kelas_dropdown.apply {
            keyListener = null
            setOnItemClickListener { _, _, position, _ -> presenter.onSelectKelasItem(position) }
        }
    }

    fun initMatkulArrayAdapter(data: List<String>) {
        matkulAdapter = ArrayAdapter(
            context,
            R.layout.dropdown_menu_popup_item,
            data
        )
        v.matkul_dropdown.setAdapter(matkulAdapter)
    }

    override fun setUpConfirmButton() {
        v.jwl_confirm_btn.apply {
            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#0766ff"))
            isEnabled = true
            setTextColor(resources.getColor(R.color.white))
            setOnClickListener {
                dismiss()
                val activity = activity as JwlPenggantiActivity
                activity.reloadList()
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

    override fun onRoomDataReady(data: List<String>) {
        initRuanganArrayAdapter(data)
        v.ruangan_dropdown.apply {
            keyListener = null
            setOnItemClickListener { _, _, position, _ ->
                setUpConfirmButton()
                presenter.onSelectRuanganItem(position)
            }
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
            presenter.onSelectTimeItem(selectedChip?.text.toString())
        }
    }
}