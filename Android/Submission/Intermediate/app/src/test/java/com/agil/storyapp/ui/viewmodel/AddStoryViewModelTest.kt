package com.agil.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.agil.storyapp.data.remote.response.ResponseMessage
import com.agil.storyapp.data.repository.AddStoryRepository
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.utils.DataDummy
import com.agil.storyapp.utils.MainDispatcherRule
import com.agil.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    val instantExecutorsRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    @Mock
    private lateinit var addStoryRepository: AddStoryRepository
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyResponse = DataDummy.generatedDataDummyResponse()
    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVVGZVZhd3NjWUJMZDlxU0giLCJpYXQiOjE2NjUxNzQ1ODV9.lXmvbYwaJ6he7g0WDR6ZMMFFZxervNSMJdar5gXq4zo"
    private val url = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png"
    private val description = "Description"

    @Before
    fun setUp(){
        addStoryViewModel = AddStoryViewModel(addStoryRepository)
    }

    @Test
    fun `when uploadStory should Return Success, not null and match data`()= runTest {
        val file = File(url)
        val imagePart = MultipartBody.Part.createFormData("name",file.name)
        val desc = description.toRequestBody("text/plain".toMediaType())
        val expectedData = MutableLiveData<Result<ResponseMessage>>()
        expectedData.value = Result.Success(dummyResponse)

        Mockito.`when`(addStoryRepository.uploadData(token,imagePart,desc, 0F,0F)).thenReturn(expectedData)

        val actualData = addStoryViewModel.uploadStory(token,imagePart,desc, 0F,0F).getOrAwaitValue()
        Mockito.verify(addStoryRepository).uploadData(token,imagePart,desc, 0F,0F)

        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Success)
        Assert.assertEquals(dummyResponse, (actualData as Result.Success).data)
    }

    @Test
    fun `when uploadStory should Return Error, not null and match data`()= runTest {
        val file = File(url)
        val imagePart = MultipartBody.Part.createFormData("name",file.name)
        val desc = description.toRequestBody("text/plain".toMediaType())
        val expectedData = MutableLiveData<Result<ResponseMessage>>()
        expectedData.value = Result.Error(dummyResponse.message)

        Mockito.`when`(addStoryRepository.uploadData(token,imagePart,desc, 0F,0F)).thenReturn(expectedData)

        val actualData = addStoryViewModel.uploadStory(token,imagePart,desc, 0F,0F).getOrAwaitValue()
        Mockito.verify(addStoryRepository).uploadData(token,imagePart,desc, 0F,0F)

        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Error)
        Assert.assertEquals(dummyResponse.message, (actualData as Result.Error).error)
    }
}