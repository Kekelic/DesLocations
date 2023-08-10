package com.example.deslocations.di

import com.example.deslocations.repository.AuthRepository
import com.example.deslocations.repository.AuthRepositoryImpl
import com.example.deslocations.repository.ChatRepository
import com.example.deslocations.repository.ChatRepositoryImpl
import com.example.deslocations.repository.PlacesRepository
import com.example.deslocations.repository.PlacesRepositoryImpl
import com.example.deslocations.repository.PostImageRepository
import com.example.deslocations.repository.PostImageRepositoryImpl
import com.example.deslocations.repository.PostRepository
import com.example.deslocations.repository.PostRepositoryImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth,
        db = Firebase.firestore
    )

    @Provides
    fun providePlacesRepository(): PlacesRepository = PlacesRepositoryImpl(
        db = Firebase.firestore,
        firebaseMessaging = Firebase.messaging
    )

    @Provides
    fun providePostRepository(): PostRepository = PostRepositoryImpl(
        storage = Firebase.storage,
        db = Firebase.firestore
    )

    @Provides
    fun providePostImageRepository(): PostImageRepository = PostImageRepositoryImpl(
        storage = Firebase.storage,
        db = Firebase.firestore
    )

    @Provides
    fun provideChatRepository(): ChatRepository = ChatRepositoryImpl(
        db = Firebase.firestore
    )


}