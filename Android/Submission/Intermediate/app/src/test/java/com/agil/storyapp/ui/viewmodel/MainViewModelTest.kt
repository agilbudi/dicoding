package com.agil.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.agil.storyapp.adapter.StoryPagingAdapter
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.data.repository.StoryRepository
import com.agil.storyapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorsRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyStory = DataDummy.generatedDataDummyStory()
    private val dummyResponse = DataDummy.generatedDataDummyResponse()
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVVGZVZhd3NjWUJMZDlxU0giLCJpYXQiOjE2Njg0MDgyMTl9.pmRqSxWkMF0ppyIyLYRCiYs_3je0KZA_35-iOneh9WU"


    @Before
    fun setUp(){
        mainViewModel = MainViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null, Return Success and same size`() = runTest {
        val data: PagingData<StoryEntity> = StoryPagingSourceTest.snapshot(dummyStory)
        val expectedStory = MutableLiveData<Result<PagingData<StoryEntity>>>()
        expectedStory.value = Result.Success(data)

        Mockito.`when`(storyRepository.getPageStory(token)).thenReturn(expectedStory)

        val actualStory = mainViewModel.story(token).getOrAwaitValue()
        Mockito.verify(storyRepository).getPageStory(token)
        val differ = Result.Success(AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCategory,
            workerDispatcher = Dispatchers.Main
        ))

        Assert.assertNotNull(differ.data.snapshot())
        Assert.assertTrue(actualStory is Result.Success)
        differ.data.submitData((actualStory as Result.Success).data)
        Assert.assertEquals(dummyStory, differ.data.snapshot())
        Assert.assertEquals(dummyStory.size, differ.data.snapshot().size)
    }
    @Test
    fun `when Get Story Should Not Null and Return Error`() = runTest {
        val expectedStory = MutableLiveData<Result<PagingData<StoryEntity>>>()
        expectedStory.value = Result.Error("error")

        Mockito.`when`(storyRepository.getPageStory(token)).thenReturn(expectedStory)

        val actualStory = mainViewModel.story(token).getOrAwaitValue()
        Mockito.verify(storyRepository).getPageStory(token)

        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Error)
    }
}