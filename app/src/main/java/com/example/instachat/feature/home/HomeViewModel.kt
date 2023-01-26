package com.example.instachat.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.instachat.services.models.PostModel
import com.example.instachat.services.models.PostModelItem
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

    val adapter =  HomeDataAdapter()

    fun getAllPosts(){
        val db = Firebase.firestore
        db.collection("posts").addSnapshotListener { value, error ->
            val data = value?.documents?.map { it.data }?.map {it->
                Gson().fromJson(Gson().toJson(it), PostModelItem::class.java)
            }

            for(dc in value!!.documentChanges){
                when(dc.type){
                    DocumentChange.Type.ADDED ->{
                        adapter.submitList(data)
                    }
                    DocumentChange.Type.MODIFIED ->{

                    }
                    DocumentChange.Type.REMOVED ->{

                    }

                }
            }

        }

    }
}