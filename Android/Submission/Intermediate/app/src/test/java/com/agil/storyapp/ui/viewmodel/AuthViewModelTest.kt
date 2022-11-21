package com.agil.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.agil.storyapp.data.remote.response.AuthLogin
import com.agil.storyapp.data.remote.response.ResponseMessage
import com.agil.storyapp.data.repository.AuthRepository
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.utils.DataDummy
import com.agil.storyapp.utils.MainDispatcherRule
import com.agil.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.function.ThrowingRunnable
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    val instantExecutorsRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var authViewModel: AuthViewModel
    private val dummyAuth = DataDummy.generatedDataDummyAuth()
    private val dummyResponse = DataDummy.generatedDataDummyResponse()
    private val name = "agil"
    private val email = "email@gmail.com"
    private val password = "123456"

    @Before
    fun setUp(){
        authViewModel = AuthViewModel(authRepository)
    }


    @Test
    fun `when login Return Success Should Not Null`()= runTest {
        val expectedData = MutableLiveData<Result<AuthLogin>>()
        expectedData.value = Result.Success(dummyAuth)

        Mockito.`when`(authRepository.login(email, password)).thenReturn(expectedData)

        val actualData = authViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(authRepository).login(email, password)

        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Success)
        Assert.assertEquals(dummyAuth, (actualData as Result.Success).data)
        Assert.assertEquals(dummyAuth.loginResult.token, actualData.data.loginResult.token)
    }
    @Test
    fun `when login Return Error Should Not Null`()= runTest{
        val expectedData = MutableLiveData<Result<AuthLogin>>()
        expectedData.value = Result.Error(dummyResponse.message)

        Mockito.`when`(authRepository.login(email, password)).thenReturn(expectedData)

        val actualData = authViewModel.login(email, password).getOrAwaitValue()
        Mockito.verify(authRepository).login(email, password)

        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Error)
        Assert.assertEquals(dummyResponse.message, (actualData as Result.Error).error)
    }


    @Test
    fun `when register Return Success Should Not Null`()= runTest {
        val expectedData = MutableLiveData<Result<ResponseMessage>>()
        expectedData.value = Result.Success(dummyResponse)

        Mockito.`when`(authRepository.register(name, email, password)).thenReturn(expectedData)

        val actualData = authViewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(authRepository).register(name, email, password)

        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Success)
        Assert.assertEquals(dummyResponse.message, (actualData as Result.Success).data.message)
    }
    @Test
    fun `when register Return Error Should Not Null`()= runTest{
        val expectedStory = MutableLiveData<Result<ResponseMessage>>()
        expectedStory.value = Result.Error(dummyResponse.message)

        Mockito.`when`(authRepository.register(name, email, password)).thenReturn(expectedStory)

        val actualData = authViewModel.register(name, email, password).getOrAwaitValue()
        Mockito.verify(authRepository).register(name, email, password)

        Assert.assertNotNull(actualData)
        Assert.assertTrue(actualData is Result.Error)
        Assert.assertEquals(dummyResponse.message, (actualData as Result.Error).error)
    }

    @Test
    fun `when get Email should Not Null then get data`()= runTest {
        val expectedEmail = "email@gmail.com"

        Mockito.`when`(authRepository.getEmail()).thenReturn(expectedEmail)

        val actualData = authViewModel.getEmail()
        Mockito.verify(authRepository).getEmail()

        Assert.assertNotNull(actualData)
        Assert.assertEquals(email, actualData)
    }

    @Test
    fun `Throw Error when set Email is null`()= runTest {
        Mockito.`when`(authRepository.setEmail(null)).thenThrow(IllegalArgumentException::class.java)

        val actualData = ThrowingRunnable { authViewModel.setEmail(null) }
        Assert.assertThrows(IllegalArgumentException::class.java, actualData)
    }
}