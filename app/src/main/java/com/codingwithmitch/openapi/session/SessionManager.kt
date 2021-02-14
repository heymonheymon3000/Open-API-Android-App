package com.codingwithmitch.openapi.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.openapi.models.AuthToken
import com.codingwithmitch.openapi.persistence.AuthTokenDao
import kotlinx.coroutines.*

import javax.inject.Inject
import javax.inject.Singleton


import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.N)
@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {
    private val _cachedToken = MutableLiveData<AuthToken>()

    companion object {
        private var _isConnectedToInternet: Boolean = false
    }

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    init {
        registerNetworkCallback()
    }

    fun login(newValue: AuthToken) {
        setValue(newValue)
    }

    fun logout() {
        Timber.d("logout: ")
        CoroutineScope(Dispatchers.IO).launch {
            var errorMessage: String? = null
            try {
                _cachedToken.value!!.account_pk?.let {
                    authTokenDao.nullifyToken(it)
                } ?: throw CancellationException("Token Error. Logging out user.")
            } catch (e: CancellationException) {
                Timber.d("logout: ${e.message}")
                errorMessage = e.message
            } catch (e: Exception) {
                Timber.e("logout: ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            } finally {
                errorMessage?.let {
                    Timber.e("logout: $errorMessage")
                }
                Timber.d("logout: finally")
                setValue(null)
            }
        }
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Dispatchers.Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }

    fun isConnectedToTheInternet(): Boolean{
        return _isConnectedToInternet
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun registerNetworkCallback() {
        try {
            val connectivityManager =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            object : ConnectivityManager.NetworkCallback() {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onAvailable(network: Network) {
                    _isConnectedToInternet = true
                }
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onLost(network: Network) {
                    _isConnectedToInternet = false
                }
            })
            _isConnectedToInternet = false
        } catch (e: Exception) {
            _isConnectedToInternet = false
        }
    }
}
