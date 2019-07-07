package com.example.attendance_mobile.home.homedosen.startclass.beaconscan

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.attendance_mobile.BaseView
import com.example.attendance_mobile.R
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.home.homedosen.HomeDsnActivity
import com.example.attendance_mobile.home.homedosen.bottomsheet.BaseBottomSheet
import com.example.attendance_mobile.model.remote.RemoteRepository
import com.example.attendance_mobile.model.service.BeaconService
import com.example.attendance_mobile.utils.Constants
import kotlinx.android.synthetic.main.beacon_btm_sheet_layout.view.*

class BeaconBtmSheet : BaseBottomSheet(),BaseView<BeaconBtmSheetPresenter>,BeaconBtmSheetContract.ViewContract{
    override lateinit var presenter: BeaconBtmSheetPresenter
    lateinit var v: View
    lateinit var mActivity : HomeDsnActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.beacon_btm_sheet_layout, container, false)
        val obj = arguments?.getSerializable("dsnSchedule") as JadwalDsn
        presenter = BeaconBtmSheetPresenter(this, RemoteRepository(), obj)
        presenter.startRanging(obj.ruangan.macAddress)
        v.cancel_beacon_btn.setOnClickListener {
            presenter.cancelBeaconRanging()
        }
        v.btm_sheet_ruangan.text = obj.ruangan.kodeRuangan
        return v
    }

    override fun stopService() {
        context?.stopService(Intent(context, BeaconService::class.java))
    }

    override fun startService(macAddress : String) {
        val intent = Intent(context, BeaconService::class.java)
        intent.action = "START_RANGING"
        intent.putExtra("macAddress",macAddress)
        context?.startService(intent)
    }

    override fun closeBtmSheet() {
        dismiss()
    }

    override fun reloadList() {
        mActivity.reloadList()
    }

    override fun showSnackBarOnParent(message : String){
        mActivity.showSnackBar(message)
    }

    override fun registerReceiver(receiver: BeaconService.BeaconReceiver<BeaconBtmSheetContract.BeaconContract>) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.ON_TIMEOUT)
        intentFilter.addAction(Constants.ON_BEACON_FOUND)
        context?.registerReceiver(receiver, intentFilter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as HomeDsnActivity
    }
}