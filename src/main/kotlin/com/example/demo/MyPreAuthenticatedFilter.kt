package com.example.demo

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

//リクエストに埋め込まれた認証情報を取り出すクラス。
class MyPreAuthenticatedFilter: AbstractPreAuthenticatedProcessingFilter() {

    //リクエストから認証情報を抽出。
    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any {
        //ヘッダーからcredentialヘッダーを取得
        return request?.getHeader("Credential") ?: ""
    }

    // //リクエストからユーザーID等のユーザー情報を抽出。
    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any {
        return ""
    }
}