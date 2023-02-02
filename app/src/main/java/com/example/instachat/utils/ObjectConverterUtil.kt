package com.example.instachat.utils

import com.example.instachat.services.models.PostModelItem
import com.example.instachat.services.models.dummyjson.User
import com.example.instachat.services.models.dummyjson.UserRest
import com.example.instachat.services.models.rest.PostModelItemRest
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class ObjectConverterUtil<T, J> {

    fun convertToObject(fromObject: T) : J{
        val json = Gson().toJson(fromObject)
        val listType: Type = object : TypeToken<J?>() {}.getType()
        val postModelItems: J = (Gson().fromJson(json, listType))
        return postModelItems
    }

    fun convertListToObject(fromObjectList: List<T>) : List<J>{
        val json = Gson().toJson(fromObjectList)
        val listType: Type = object : TypeToken<ArrayList<J?>?>() {}.getType()
        val usersList: List<J> = (Gson().fromJson(json, listType))
        return usersList
    }
}