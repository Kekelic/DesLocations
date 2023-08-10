package com.example.deslocations.repository

import com.example.deslocations.model.Response
import com.example.deslocations.model.User
import com.example.deslocations.model.response.UserResponse
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signIn(user: User): Response<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(user.email, user.password).await()
            Response.Success(true)
        } catch (e: FirebaseAuthException) {
            Response.Failure(e, e.errorCode)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun signUp(
        user: User
    ): Response<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val newUser = hashMapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "isModerator" to user.isModerator,
            )
            db.collection("Users").document(currentUser!!.uid).set(newUser).await()
            Response.Success(true)
        } catch (e: FirebaseAuthException) {
            Response.Failure(e, e.errorCode)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)


    override suspend fun isModerator(): Boolean {
        val isModerator: Boolean
        return try {
            val documentSnapshot = db.collection("Users").document(currentUser!!.uid).get().await()
            isModerator = documentSnapshot.get("isModerator").toString() == "true"
            isModerator
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUserDetails(): Response<UserResponse> {
        return try {
            val documentSnapshot = db.collection("Users").document(currentUser!!.uid).get().await()
            val userResponse = documentSnapshot.toObject(UserResponse::class.java)
            userResponse!!.id = currentUser!!.uid
            userResponse.email = currentUser?.email!!
            Response.Success(userResponse)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Response<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Response.Success(true)
        } catch (e: FirebaseAuthException) {
            Response.Failure(e, e.errorCode)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun changeAccountDetails(user: User): Response<Boolean> {
        return try {
            val credential = EmailAuthProvider.getCredential(currentUser?.email!!, user.password)
            currentUser!!.reauthenticate(credential).await()

            currentUser!!.updateEmail(user.email).await()

            val userHashMap: HashMap<String, Any> = hashMapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
            )

            db.collection("Users").document(currentUser!!.uid).update(userHashMap).await()

            Response.Success(true)
        } catch (e: FirebaseAuthException) {
            Response.Failure(e, e.errorCode)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


}