package com.example.attendance_mobile

import com.example.attendance_mobile.data.response.MhsListResponse
import com.example.attendance_mobile.home.homedosen.mhslist.MhsListContract
import com.example.attendance_mobile.home.homedosen.mhslist.MhsListPresenter
import com.example.attendance_mobile.model.remote.RemoteRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class MhsListTest{

    @Mock
    lateinit var remoteRepository : RemoteRepository

    @Mock
    lateinit var view : MhsListContract.ViewContract

    lateinit var interactor : MhsListContract.InteractorContract

    lateinit var presenter : MhsListPresenter

    private val ITEMS : MhsListResponse = MhsListResponse("2",emptyList())

    @Before
    fun init(){
        MockitoAnnotations.initMocks(this)

        presenter = MhsListPresenter(view,remoteRepository,"3B")
        interactor = presenter
    }

    @Test
    fun fetch_mhs_list_should_return_correct_data_set(){
        presenter.doFetchMhsList()

        verify(remoteRepository).doFetchMhsList("07-07-2019","19:41:00","3B",interactor)
    }
}