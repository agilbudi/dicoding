package com.agil.storyapp.utils

import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.remote.response.AuthLogin
import com.agil.storyapp.data.remote.response.ResponseMessage
import com.agil.storyapp.model.User

object DataDummy {
    fun generatedDataDummyStory(): List<StoryEntity>{
        val storyData: MutableList<StoryEntity> = arrayListOf()
        val photoUrl = "https://firebasestorage.googleapis.com/v0/b/project-dummy-by-hide.appspot.com/o/other%2Fwamika%2Fkufi_wamika.png?alt=media&token=7f7900d0-0e22-4aca-9d51-0ce302154feb"
        for (i in 0..10){
            val position = i/10
            val story = StoryEntity("id-00$i","name-$i", "description $i",
            photoUrl,"2022-11-11T11:11:11Z", -7.797068F*position, 110.370529F*position)
            storyData.add(story)
        }
        return storyData
    }
    fun generatedDataDummyAuth(): AuthLogin {
        val error = false
        val message = "success"
        val loginResult = User(
            "user-UFeVawscYBLd9qSH","agil",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.",
            null,null
        )

        return AuthLogin(error, message, loginResult)
    }
    fun generatedDataDummyResponse(): ResponseMessage {
        val error = false
        val message = "error"

        return ResponseMessage(error, message)
    }
}