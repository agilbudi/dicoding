package com.agil.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.agil.storyapp.adapter.MapPagingAdapter
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.repository.MapStoryRepository
import com.agil.storyapp.data.repository.Result
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
class MapViewModelTest{
    @get:Rule
    val instantExecutorsRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    @Mock
    private lateinit var mapRepository: MapStoryRepository
    private lateinit var mapViewModel: MapViewModel
    private val dummyStory = DataDummy.generatedDataDummyStory()
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVVGZVZhd3NjWUJMZDlxU0giLCJpYXQiOjE2NjUxNzQ1ODV9.lXmvbYwaJ6he7g0WDR6ZMMFFZxervNSMJdar5gXq4zo"

    @Before
    fun setUp(){
        mapViewModel = MapViewModel(mapRepository)
    }

    @Test
    fun `when Get list Story Return Success Should Not Null and correct size`()= runTest {
        val data: PagingData<StoryEntity> = StoryPagingSourceTest.snapshot(dummyStory)
        val expectedStory = MutableLiveData<Result<PagingData<StoryEntity>>>()
        expectedStory.value = Result.Success(data)

        Mockito.`when`(mapRepository.getListOnMapStory(token)).thenReturn(expectedStory)

        val actualStory = mapViewModel.listStory(token).getOrAwaitValue()
        Mockito.verify(mapRepository).getListOnMapStory(token)
        val differ = Result.Success(AsyncPagingDataDiffer(
            diffCallback = MapPagingAdapter.DIFF_CALLBACK,
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
    fun `when Get list Story Return Error Should Not Null`() = runTest{
        val expectedStory = MutableLiveData<Result<PagingData<StoryEntity>>>()
        expectedStory.value = Result.Error("error")

        Mockito.`when`(mapRepository.getListOnMapStory(token)).thenReturn(expectedStory)

        val actualStory = mapViewModel.listStory(token).getOrAwaitValue()
        Mockito.verify(mapRepository).getListOnMapStory(token)

        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Error)
    }


    @Test
    fun `when Get marker Story Return Success Should Not Null and correct size`()= runTest {
        val expectedStory = MutableLiveData<Result<List<StoryEntity>>>()
        expectedStory.value = Result.Success(dummyStory)

        Mockito.`when`(mapRepository.getMapStory()).thenReturn(expectedStory)

        val actualStory = mapViewModel.markerStory().getOrAwaitValue()
        Mockito.verify(mapRepository).getMapStory()

        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
        Assert.assertEquals(dummyStory, (actualStory as Result.Success).data)
        Assert.assertEquals(dummyStory.size, actualStory.data.size)
    }
    @Test
    fun `when Get map Story Return Error Should Not Null`() = runTest{
        val expectedStory = MutableLiveData<Result<List<StoryEntity>>>()
        expectedStory.value = Result.Error("error")

        Mockito.`when`(mapRepository.getMapStory()).thenReturn(expectedStory)

        val actualStory = mapViewModel.markerStory().getOrAwaitValue()
        Mockito.verify(mapRepository).getMapStory()

        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Error)
    }
}
