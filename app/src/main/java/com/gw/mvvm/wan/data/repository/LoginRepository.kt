package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.framework.network.AppException
import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 登录,注册,登出数据仓库
 * @author play0451
 */
class LoginRepository {

    /**
     * 登录
     * @param userName String   用户名
     * @param password String   密码
     */
    suspend fun login(userName: String, password: String): ApiResponse<UserInfo> {
        return withContext(Dispatchers.IO) {
            apiService.login(userName, password)
        }
    }

    /**
     * 注册
     * @param userName String   用户名
     * @param password String   密码
     * @param confirmPassword String    确认密码
     * @return ApiResponse<UserInfo>
     */
    suspend fun register(
        userName: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<UserInfo> {
        return withContext(Dispatchers.IO) {
            val result: ApiResponse<Any?> = apiService.register(userName, password, confirmPassword)
            if (result.isSucces()) {
                apiService.login(userName, password)
            } else {
                throw AppException(result.code(), result.msg())
            }
        }
    }

    /**
     * 登出
     * @return ApiResponse<Any?>
     */
    suspend fun logout(): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.logout()
        }
    }
}