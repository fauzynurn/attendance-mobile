package com.example.attendance_mobile.model.remote

import android.os.Handler
import com.example.attendance_mobile.data.DetailAkumulasiKehadiran
import com.example.attendance_mobile.data.JadwalDsn
import com.example.attendance_mobile.data.JadwalMhs
import com.example.attendance_mobile.data.Ruangan
import com.example.attendance_mobile.data.request.JwlPenggantiRequest
import com.example.attendance_mobile.data.request.ListAttendanceRequest
import com.example.attendance_mobile.data.response.*
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
import retrofit2.Response

class RemoteRepository {
    private var retrofitService: RetrofitInterface? = null

    init {
        retrofitService = RetrofitClient.getInstance()?.create(RetrofitInterface::class.java)
    }

    fun doValidateDosen(kddsn: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("kddsn", kddsn)
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

    fun doValidateMhs(nim: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("nim", nim)
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

    fun doRegisterMhs(nim: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
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

    fun doRegisterDsn(kddsn: String, imei: String, listener: LoginContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("kddsn", kddsn)
            put("imei", imei)
        }
        val call: Call<BaseResponse> = retrofitService!!.registerDsn(body)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {
                listener.onRegisterDsnResult(response.body())
            }

        })
    }

    fun doFetchSummary(nim: String, listener: HomeMhsContract.InteractorContract) {
//        val body: HashMap<String, String> = HashMap()
//        Handler().postDelayed({
//            body.run {
//                put("sakit", "2")
//                put("izin", "4")
//                put("alpa", "0")
//            }
//            listener.onSummaryResult(body)
//        }, 3000)
        val body: HashMap<String, String> = HashMap()
        body.run {
            put("nim", nim)
        }
        val call: Call<HashMap<String, String>> = retrofitService!!.fetchSummary(body)
        call.enqueue(object : Callback<HashMap<String, String>> {
            override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(
                call: Call<HashMap<String, String>>,
                response: retrofit2.Response<HashMap<String, String>>
            ) {
                listener.onSummaryResult(response.body()!!)
            }

        })
    }

    fun doFetchMhsScheduleList(nim: String, kelas: String, tgl: String, listener: HomeMhsContract.InteractorContract) {
//        val sd1 = JadwalMhs(
//            "Pengolahan Citra Digital",
//            true,
//            "16TKO6022",
//            listOf("Nurjannah Syakrani"),
//            "07:00:00",
//            "08:40:00",
//            Ruangan(
//                "D219",
//                "C2:00:E2:00:00:6A"
//            ),
//            "07:01:00",
//            listOf(
//                KehadiranPerSesi(id = "",sesi=1,jamMulai="07:00:00", jamSelesai="07:50:00", status=true,tglKuliah = ""),
//                KehadiranPerSesi(id="",sesi=2, jamMulai="07:50:00", jamSelesai="08:40:00", status=false,tglKuliah = "")
//            )
//        )
//        val sd2 = JadwalMhs(
//            "Pengantar Akuntansi",
//            true,
//            "16JTK6012",
//            listOf("Arry Irawan", "Irawan Thamrin", "Ade Hodijah"),
//            "08:40:00",
//            "10:20:00",
//            Ruangan(
//                "D219",
//                "C2:00:E2:00:00:6A"
//            ),
//            "08:45:00",
//            listOf(
//                KehadiranPerSesi(id = "",sesi=1,jamMulai="08:40:00", jamSelesai="09:30:00", status=false,tglKuliah = ""),
//                KehadiranPerSesi(id="",sesi=2, jamMulai="09:30:00", jamSelesai="10:20:00", status=false,tglKuliah = ""),
//                KehadiranPerSesi(id="",sesi=3, jamMulai="10:40:00", jamSelesai="11:30:00", status=false,tglKuliah="")
//            )
//        )
//        val scheduleRes = ScheduleResponse(
//            arrayListOf(sd1, sd2), arrayListOf(
//                JadwalMhs(
//                    "Pengembangan Perangkat Lunak",
//                    true,
//                    "16JTK6014",
//                    listOf("Irawan Thamrin"),
//                    "13:40:00",
//                    "14:10:00",
//                    Ruangan(
//                        "D213",
//                        "C2:00:E2:00:00:6A"
//                    ),
//                    "",
//                    listOf(KehadiranPerSesi(id = "",sesi=1,jamMulai="08:40:00", jamSelesai="09:30:00", status=false,tglKuliah = ""),
//                        KehadiranPerSesi(id="",sesi=2, jamMulai="09:30:00", jamSelesai="10:20:00", status=false,tglKuliah = ""),
//                        KehadiranPerSesi(id="",sesi=3, jamMulai="10:40:00", jamSelesai="11:30:00", status=false,tglKuliah=""))
//                )
//            )
//        )
//        Handler().postDelayed({
//            listener.onScheduleListResult(scheduleRes)
//        }, 1000)

        val body: HashMap<String, String> = HashMap()
        body["kdKelas"] = kelas
        body["tgl"] = tgl
        body["nim"] = nim
        val call: Call<ScheduleResponse<JadwalMhs>> = retrofitService!!.fetchScheduleMhsList(body)
        call.enqueue(object : Callback<ScheduleResponse<JadwalMhs>> {
            override fun onFailure(call: Call<ScheduleResponse<JadwalMhs>>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(
                call: Call<ScheduleResponse<JadwalMhs>>,
                response: retrofit2.Response<ScheduleResponse<JadwalMhs>>
            ) {
                listener.onScheduleListResult(response.body()!!)
            }

        })
    }

    fun doFetchDsnScheduleList(kddosen: String, tgl: String, listener: HomeDsnContract.InteractorContract) {
        val sd1 = JadwalDsn(
            "Pengolahan Citra Digital",
            true,
            "16TKO6022",
            "07:00:00",
            "3B",
            "",
            "08:40:00",
            Ruangan("D219", "C2:00:E2:00:00:6A")
        )
        val scheduleRes = ScheduleResponse(
            arrayListOf(sd1), arrayListOf(
                JadwalDsn(
                    "Pengolahan Citra Digital",
                    true,
                    "16TKO6022",
                    "13:40:00",
                    "3A",
                    "",
                    "14:30:00",
                    Ruangan("D213", "C2:00:E2:00:00:6A")
                )
            )
        )
        Handler().postDelayed({
            listener.onScheduleListResult(scheduleRes)
        }, 3000)
//        val body: HashMap<String, String> = HashMap()
//        body["kddsn"] = kddosen
//        body["tgl"] = tgl
//        val call: Call<ScheduleResponse<JadwalDsn>> = retrofitService!!.fetchScheduleDsnList(body)
//        call.enqueue(object : Callback<ScheduleResponse<JadwalDsn>> {
//            override fun onFailure(call: Call<ScheduleResponse<JadwalDsn>>, t: Throwable) {
//                listener.onFail(t.message)
//            }
//
//            override fun onResponse(
//                call: Call<ScheduleResponse<JadwalDsn>>,
//                response: Response<ScheduleResponse<JadwalDsn>>
//            ) {
//                listener.onScheduleListResult(response.body()!!)
//            }
//
//        })
    }

    fun doFetchMatkulList(kddsn: String, listener: HomeDsnContract.InteractorContract) {
        val body: HashMap<String, String> = HashMap()
        body["kdDosen"] = kddsn
        val call: Call<MatkulListResponse> = retrofitService!!.fetchMatkulList(body)
        call.enqueue(object : Callback<MatkulListResponse> {
            override fun onFailure(call: Call<MatkulListResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<MatkulListResponse>, response: retrofit2.Response<MatkulListResponse>) {
                listener.onMatkulListResult(response.body()!!)
            }

        })
    }

    fun doFetchSessionList(
        tgl: String,
        kelas: String,
        namaMatkul: String,
        jenisMatkul: String,
        listener: HomeDsnContract.InteractorContract
    ) {
//        val list = HashMap<Int, String>()
////        list.apply {
////            put(2, "07:50:00".substring(0, "07:50:00".length - 3))
////            put(3, "11:40:00".substring(0, "11:40:00".length - 3))
////            put(6, "14:40:00".substring(0, "14:40:00".length - 3))
////        }
////        Handler().postDelayed({
////            listener.onSessionListAvailableResult(list)
////        }, 2000)
        val body: HashMap<String, String> = HashMap()
        body["tgl"] = tgl
        body["kdKelas"] = kelas
        body["namaMatkul"] = namaMatkul
        body["jenisMatkul"] = jenisMatkul
        val call: Call<TimeAvailableResponse> = retrofitService!!.fetchAvailableTime(body)
        call.enqueue(object : Callback<TimeAvailableResponse> {
            override fun onFailure(call: Call<TimeAvailableResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(
                call: Call<TimeAvailableResponse>,
                response: retrofit2.Response<TimeAvailableResponse>
            ) {
                listener.onSessionListAvailableResult(response.body()!!)
            }

        })
    }

    fun doFetchJwlPenggantiRoomList(
        tgl: String,
        jamMulai: String,
        kodeKelas: String,
        namaMatkul: String,
        jenisMatkul: String,
        listener: HomeDsnContract.InteractorContract
    ) {
        val body: HashMap<String, String> = HashMap()
        body["tgl"] = tgl
        body["jamMulai"] = jamMulai
        body["kdKelas"] = kodeKelas
        body["namaMatkul"] = namaMatkul
        body["jenisMatkul"] = jenisMatkul
        val call: Call<RoomAvailableResponse> = retrofitService!!.fetchAvailableRoom(body)
        call.enqueue(object : Callback<RoomAvailableResponse> {
            override fun onFailure(call: Call<RoomAvailableResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(
                call: Call<RoomAvailableResponse>,
                response: retrofit2.Response<RoomAvailableResponse>
            ) {
                listener.onRoomListAvailableResult(response.body()!!)
            }

        })
    }

    fun doFetchStartClassRoomList(
        tgl: String,
        jamMulai: String,
        kodeKelas: String,
        namaMatkul: String,
        jenisMatkul: String,
        listener: StartScheduleBtmSheetContract.InteractorContract
    ) {
        val body: HashMap<String, String> = HashMap()
        body["tgl"] = tgl
        body["jamMulai"] = jamMulai
        body["kdKelas"] = kodeKelas
        body["namaMatkul"] = namaMatkul
        body["jenisMatkul"] = jenisMatkul
        val call: Call<RoomAvailableResponse> = retrofitService!!.fetchAvailableRoom(body)
        call.enqueue(object : Callback<RoomAvailableResponse> {
            override fun onFailure(call: Call<RoomAvailableResponse>, t: Throwable) {
                listener.onFail(t.message!!)
            }

            override fun onResponse(
                call: Call<RoomAvailableResponse>,
                response: Response<RoomAvailableResponse>
            ) {
                listener.onRoomListResult(response.body()!!)
            }

        })
    }

    fun doRequestJwlPengganti(jwlPengganti: JwlPenggantiRequest, listener: HomeDsnContract.InteractorContract) {
        val call: Call<BaseResponse> = retrofitService!!.requestJwlPengganti(jwlPengganti)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                listener.onJwlPenggantiCreated(response.body()!!)
            }

        })
    }

    fun doStoreCurrentAttendance(attendanceList: ListAttendanceRequest, onSuccess: () -> Unit) {
        val call: Call<BaseResponse> = retrofitService!!.storeCurrentAttendance(attendanceList)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {}

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {
                onSuccess()
            }

        })
    }

    fun doFetchDetailSummary(nim: String, listener: DetailSummaryContract.InteractorContract) {
//        val list = ArrayList<DetailAkumulasiKehadiran>()
//        Handler().postDelayed({
//            list.apply {
//                add(
//                    DetailAkumulasiKehadiran(
//                        "Pengolahan Citra Digital",
//                        true,
//                        "12",
//                        "2"
//                    )
//                )
//                add(
//                    DetailAkumulasiKehadiran(
//                        "Proyek 5",
//                        true,
//                        "30",
//                        "0"
//                    )
//                )
//                add(
//                    DetailAkumulasiKehadiran(
//                        "Pengantar Akuntansi",
//                        true,
//                        "3",
//                        "0"
//                    )
//                )
//                add(
//                    DetailAkumulasiKehadiran(
//                        "Pengembangan Perangkat Lunak",
//                        true,
//                        "12",
//                        "4"
//                    )
//                )
//            }
//            listener.onSummaryListResult(list)
//        }, 2000)
        val body: HashMap<String, String> = HashMap()
        body["nim"] = nim
        val call: Call<ArrayList<DetailAkumulasiKehadiran>> = retrofitService!!.fetchSummaryList(body)
        call.enqueue(object : Callback<ArrayList<DetailAkumulasiKehadiran>> {
            override fun onFailure(call: Call<ArrayList<DetailAkumulasiKehadiran>>, t: Throwable) {
                listener.onFail(t.message)
            }

            override fun onResponse(
                call: Call<ArrayList<DetailAkumulasiKehadiran>?>,
                response: retrofit2.Response<ArrayList<DetailAkumulasiKehadiran>>
            ) {
                listener.onSummaryListResult(response.body()!!)
            }

        })
    }

//    fun getAttendanceSessionList(nim : String) : List<LocalAttendance>{
//        val list = mutableListOf<LocalAttendance>()
//        list.add(LocalAttendance("ID3",1,"KDMK2","TGL3","","",1,"X"))
//        list.add(LocalAttendance("ID5",3,"KDMK2","TGL5","","",1,"X"))
//        list.add(LocalAttendance("ID4",2,"KDMK2","TGL4","","",1,"X"))
//        return list
//    }

    fun doFetchMhsList(
        tgl: String,
        jamSkrng: String,
        jamMulai: String,
        kdKelas: String,
        jenisMatkul: String,
        kodeMatkul: String,
        listener: MhsListContract.InteractorContract
    ) {
        val body: HashMap<String, String> = HashMap()
        body["tgl"] = tgl
        body["jamSkrng"] = jamSkrng
        body["jamMulai"] = jamMulai
        body["kdKelas"] = kdKelas
        body["jenisMatkul"] = jenisMatkul
        body["kodeMatkul"] = kodeMatkul
        val call: Call<MhsListResponse> = retrofitService!!.fetchMhsList(body)
        call.enqueue(object : Callback<MhsListResponse> {
            override fun onFailure(call: Call<MhsListResponse>, t: Throwable) {
                listener.onFail(t.message!!)
            }

            override fun onResponse(call: Call<MhsListResponse>, response: retrofit2.Response<MhsListResponse>) {
                listener.onMhsListResult(response.body()!!)
            }

        })
//        val list = ArrayList<Kehadiran>()
//        Handler().postDelayed({
//            list.apply {
//                add(
//                    Kehadiran(
//                        "Achmad Fadhitya M.",
//                        true
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "Aditia Nugraha",
//                        true
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "Cecep Sutisna",
//                        false
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "Dhika Bagus Danindra",
//                        true
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "Fauzan Akmal Khalqi",
//                        true
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "Farrel Priambodo",
//                        true
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "Fauzi Nur Noviansyah",
//                        false
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "M. Ridwan Herlambang D.P",
//                        true
//                    )
//                )
//                add(
//                    Kehadiran(
//                        "Nino Maryono",
//                        true
//                    )
//                )
//            }
//            listener.onMhsListResult(MhsListResponse("1",list))
//        }, 4000)
    }

    fun startClass(
        tgl: String,
        jamMulaiOlehDosen: String,
        kodeMatkul: String,
        kodeKelas: String,
        jenisMatkul: Boolean,
        kodeRuangan : String
    ) {
        val body: HashMap<String, String> = HashMap()
        body["tgl"] = tgl
        body["jamMulai"] = jamMulaiOlehDosen
        body["kodeMatkul"] = kodeMatkul
        body["kodeKelas"] = kodeKelas
        body["jenisMatkul"] = jenisMatkul.toString()
        body["kodeRuangan"] = kodeRuangan
        val call: Call<BaseResponse> = retrofitService!!.startClass(body)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<BaseResponse>, response: retrofit2.Response<BaseResponse>) {

            }

        })
    }
}