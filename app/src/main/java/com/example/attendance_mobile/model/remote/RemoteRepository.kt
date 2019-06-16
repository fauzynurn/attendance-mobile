package com.example.attendance_mobile.model.remote

import android.os.Handler
import com.example.attendance_mobile.data.*
import com.example.attendance_mobile.data.response.AttendanceResponse
import com.example.attendance_mobile.data.response.BaseResponse
import com.example.attendance_mobile.data.response.ScheduleResponse
import com.example.attendance_mobile.detailsummary.DetailSummaryContract
import com.example.attendance_mobile.home.homedosen.HomeDsnContract
import com.example.attendance_mobile.home.homedosen.mhslist.MhsListContract
import com.example.attendance_mobile.home.homedosen.startclass.startschedule.StartScheduleBtmSheetContract
import com.example.attendance_mobile.home.homemhs.HomeMhsContract
import com.example.attendance_mobile.login.LoginContract
import com.example.fingerprintauth.RetrofitClient
import com.example.fingerprintauth.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback

class RemoteRepository {
    private var retrofitService: RetrofitInterface? = null

    init {
        retrofitService = RetrofitClient.getInstance()?.create(RetrofitInterface::class.java)
    }

    fun doValidateDosen(kddsn: String, pass: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("kddsn", kddsn)
            put("password", pass)
            put("imei", imei)
        }
        val call: Call<BaseResponse> = retrofitService!!.checkDsn(body)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {
                listener.onValidateDsnResult(response.body())
            }

        })

    }

    fun doValidateMhs(nim: String, pass: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("nim", nim)
            put("password", pass)
            put("imei", imei)
        }
        val call: Call<BaseResponse> = retrofitService!!.checkMhs(body)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {
                listener.onValidateMhsResult(response.body())
            }

        })
    }

    fun doRegisterMhs(publicKey: String, nim: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("publicKey", publicKey)
            put("nim", nim)
            put("imei", imei)
        }
        val call: Call<BaseResponse> = retrofitService!!.registerMhs(body)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {
                listener.onRegisterMhsResult(response.body())
            }

        })
    }

    fun doRegisterDsn(publicKey: String, kddsn: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("publicKey", publicKey)
            put("kddsn", kddsn)
            put("imei", imei)
        }
        val call: Call<BaseResponse> = retrofitService!!.registerDsn(body)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {
                listener.onRegisterDsnResult()
            }

        })
    }

    fun doFetchSummary(nim: String, listener: HomeMhsContract.InteractorContract) {
        val body: HashMap<String, Int> = HashMap()
        Handler().postDelayed({
            body.run {
                put("sakit", 2)
                put("izin", 4)
                put("alpa", 0)
            }
            listener.onSummaryResult(body)
        }, 3000)
//        val call : Call<HashMap<String, Int>> = retrofitService!!.fetchPresenceSummary(body)
//        call.enqueue(object: Callback<HashMap<String,Int>> {
//            override fun onFailure(call: Call<HashMap<String,Int>>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<HashMap<String,Int>>, response: retrofit2.BaseResponse<HashMap<String,Int>>) {
//
//            }
//
//        })
    }

    fun doFetchMhsScheduleList(kelas: String, tgl: String, listener: HomeMhsContract.InteractorContract) {
        val sd1 = JadwalMhs(
            "Pengolahan Citra Digital",
            true,
            "16TKO6022",
            listOf("Nurjannah Syakrani"),
            "07:00:00",
            "07:13:00",
            Ruangan(
                "D219",
                "C2:00:E2:00:00:6A"
            ),
            "07:01:00",
            arrayListOf(
                KehadiranPerSesi(1, "07:00:00", "07:05:00", true),
                KehadiranPerSesi(2, "07:05:00", "07:10:00", false),
                KehadiranPerSesi(3, "07:12:00", "07:17:00", false)
            )
        )
        val sd2 = JadwalMhs(
            "Pengantar Akuntansi",
            true,
            "16JTK6012",
            listOf("Arry Irawan", "Irawan Thamrin", "Ade Hodijah"),
            "08:40:00",
            "11:30:00",
            Ruangan(
                "D219",
                "C2:00:E2:00:00:6A"
            ),
            "08:45:00",
            arrayListOf(
                KehadiranPerSesi(1, "08:40:00", "09:30:00", true),
                KehadiranPerSesi(2, "09:30:00", "10:20:00", false),
                KehadiranPerSesi(3, "10:40:00", "11:30:00", false)
            )
        )
        val scheduleRes = ScheduleResponse(
            arrayListOf(sd1, sd2), arrayListOf(
                JadwalMhs(
                    "Pengembangan Perangkat Lunak",
                    true,
                    "16JTK6014",
                    listOf("Irawan Thamrin"),
                    "13:40:00",
                    "14:10:00",
                    Ruangan(
                        "D213",
                        "C2:00:E2:00:00:6A"
                    ),
                    "",
                    arrayListOf(KehadiranPerSesi(1, "13:40:00", "14:10:00", true))
                )
            )
        )
        Handler().postDelayed({
            listener.onScheduleListResult(scheduleRes)
        }, 2000)
//        val body : HashMap<String,String> = HashMap()
//        body["kdKelas"] = kelas
//        body["tgl"] = tgl
//        val call : Call<ArrayList<JadwalMhs>> = retrofitService!!.fetchScheduleList(body)
//        call.enqueue(object: Callback<ScheduleResponse> {
//            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<ScheduleResponse>, response: retrofit2.BaseResponse<ScheduleResponse>) {
//                listener.onScheduleListResult(response.body()!!)
//            }
//
//        })
    }

    fun doFetchDsnScheduleList(kelas: String, tgl: String, listener: HomeDsnContract.InteractorContract) {
        val sd1 = DsnSchedule(
            "Pengolahan Citra Digital",
            true,
            "16TKO6022",
            "07:00:00",
            "3B",
            "",
            "07:13:00",
            Ruangan("D219", "C2:00:E2:00:00:6A")
        )
        val scheduleRes = ScheduleResponse(
            arrayListOf(sd1), arrayListOf(
                DsnSchedule(
                    "Pengembangan Perangkat Lunak",
                    true,
                    "16JTK6014",
                    "13:40:00",
                    "3B",
                    "",
                    "14:10:00",
                    Ruangan("D213", "C2:00:E2:00:00:6A")
                )
            )
        )
        Handler().postDelayed({
            listener.onScheduleListResult(scheduleRes)
        }, 2000)
//        val body : HashMap<String,String> = HashMap()
//        body["kdKelas"] = kelas
//        body["tgl"] = tgl
//        val call : Call<ArrayList<JadwalMhs>> = retrofitService!!.fetchScheduleList(body)
//        call.enqueue(object: Callback<ScheduleResponse> {
//            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<ScheduleResponse>, response: retrofit2.BaseResponse<ScheduleResponse>) {
//                listener.onScheduleListResult(response.body()!!)
//            }
//
//        })
    }

    fun doFetchMatkulList(kddsn: String, listener: HomeDsnContract.InteractorContract) {
        val list = listOf(
            Matkul("ABCDE", "Dasar dasar pemrograman", listOf("1A", "1B")),
            Matkul("DEFGH", "Pengembangan Perangkat Lunak", listOf("2A", "2B"))
        )
        Handler().postDelayed({
            listener.onMatkulListResult(list)
        }, 6000)
    }

    fun doFetchSessionList(tgl: String, kelas: String, listener: HomeDsnContract.InteractorContract) {
        val list = HashMap<Int, String>()
        list.apply {
            put(2, "07:50:00".substring(0, "07:50:00".length - 3))
            put(3, "11:40:00".substring(0, "11:40:00".length - 3))
            put(6, "14:40:00".substring(0, "14:40:00".length - 3))
        }
        Handler().postDelayed({
            listener.onSessionListAvailableResult(list)
        }, 2000)
    }

    fun doFetchJwlPenggantiRoomList(time: String, session: Int, listener: HomeDsnContract.InteractorContract) {
        val list = listOf("D216", "D103-DB")
        Handler().postDelayed({
            listener.onRoomListAvailableResult(list)
        }, 2000)
    }

    fun doRequestJwlPengganti(jwlPengganti: JwlPengganti) {
        Handler().postDelayed({

        }, 2000)
        //        val call : Call<Void> = retrofitService!!.requestJwlPengganti(jwlPengganti)
//        call.enqueue(object: Callback<ScheduleResponse> {
//            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<ScheduleResponse>, response: retrofit2.BaseResponse<ScheduleResponse>) {
//                listener.onScheduleListResult(response.body()!!)
//            }
//
//        })
    }

    fun doStoreCurrentAttendance(list: List<AttendanceResponse>,onSuccess : (BaseResponse) -> Unit) {
        val call: Call<BaseResponse> = retrofitService!!.storeCurrentAttendance(list)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {}

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {
                onSuccess(response.body()!!)
            }

        })
    }

    fun doFetchTimeListFromRoom(
        tgl: String,
        kodeRuangan: String,
        listener: StartScheduleBtmSheetContract.InteractorContract
    ) {
        val list = mutableListOf<String>()
        Handler().postDelayed({
            list.add("13:00")
            list.add("13:50")
            list.add("14:40")
            list.add("15:30")
            list.add("16:00")
            listener.onTimeListFromRoomResult(list)
        }, 2000)
    }

    fun doFetchRoomListFromTime(
        tgl: String,
        jamMulai: String,
        listener: StartScheduleBtmSheetContract.InteractorContract
    ) {
        val list = mutableListOf<Ruangan>()
        Handler().postDelayed({
            list.add(Ruangan("D217", "XXXZ"))
            list.add(Ruangan("D210", "XXXA"))
            listener.onRoomListFromTimeResult(list)
        }, 2000)
    }

    fun doFetchAllRoom(listener: StartScheduleBtmSheetContract.InteractorContract) {
        val list = mutableListOf<Ruangan>()
        Handler().postDelayed({
            list.apply {
                add(Ruangan("D216", "XXXX"))
                add(Ruangan("D217", "XXXX"))
                add(Ruangan("D218", "XXXX"))
                add(Ruangan("D219", "XXXX"))
                add(Ruangan("D220", "XXXX"))
            }
            listener.onRoomListResult(list)
        }, 2000)
    }

    fun doFetchDetailSummary(nim: String, listener: DetailSummaryContract.InteractorContract) {
        val list = ArrayList<DetailSummary>()
        Handler().postDelayed({
            list.apply {
                add(
                    DetailSummary(
                        "Pengolahan Citra Digital",
                        true,
                        12,
                        0
                    )
                )
                add(
                    DetailSummary(
                        "Proyek 5",
                        true,
                        30,
                        5
                    )
                )
                add(
                    DetailSummary(
                        "Pengantar Akuntansi",
                        true,
                        3,
                        0
                    )
                )
                add(
                    DetailSummary(
                        "Pengembangan Perangkat Lunak",
                        true,
                        9,
                        1
                    )
                )
            }
            listener.onSummaryListResult(list)
        }, 4000)
//        val body : HashMap<String,String> = HashMap()
//        body["nim"] = nim
//        val call : Call<ArrayList<DetailSummary>> = retrofitService!!.fetchSummaryList(body)
//        call.enqueue(object: Callback<ArrayList<DetailSummary>> {
//            override fun onFailure(call: Call<ArrayList<DetailSummary>>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(call: Call<ArrayList<DetailSummary>?>, response: retrofit2.BaseResponse<ArrayList<DetailSummary>>) {
//                listener.onSummaryListResult(response.body()!!)
//            }
//
//        })
    }

//    fun getAttendanceSessionList(nim : String) : List<LocalAttendance>{
//        val list = mutableListOf<LocalAttendance>()
//        list.add(LocalAttendance("ID3",1,"KDMK2","TGL3","","",1,"X"))
//        list.add(LocalAttendance("ID5",3,"KDMK2","TGL5","","",1,"X"))
//        list.add(LocalAttendance("ID4",2,"KDMK2","TGL4","","",1,"X"))
//        return list
//    }

    fun doFetchMhsList(listener: MhsListContract.InteractorContract) {
        val list = ArrayList<KehadiranMhs>()
        Handler().postDelayed({
            list.apply {
                add(
                    KehadiranMhs(
                        "Achmad Fadhitya M.",
                        true
                    )
                )
                add(
                    KehadiranMhs(
                        "Aditia Nugraha",
                        true
                    )
                )
                add(
                    KehadiranMhs(
                        "Cecep Sutisna",
                        false
                    )
                )
                add(
                    KehadiranMhs(
                        "Dhika Bagus Danindra",
                        true
                    )
                )
                add(
                    KehadiranMhs(
                        "Fauzan Akmal Khalqi",
                        true
                    )
                )
                add(
                    KehadiranMhs(
                        "Farrel Priambodo",
                        true
                    )
                )
                add(
                    KehadiranMhs(
                        "Fauzi Nur Noviansyah",
                        false
                    )
                )
                add(
                    KehadiranMhs(
                        "M. Ridwan Herlambang D.P",
                        true
                    )
                )
                add(
                    KehadiranMhs(
                        "Nino Maryono",
                        true
                    )
                )
            }
            listener.onMhsListResult(list)
        }, 4000)
    }

}