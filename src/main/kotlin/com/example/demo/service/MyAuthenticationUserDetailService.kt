package com.example.demo.service

import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

//MyPreAuthenticatedFilterで取り出された認証情報から、アプリケーションで利用するユーザー情報を作成する。
class MyAuthenticationUserDetailService: AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    override fun loadUserDetails(token: PreAuthenticatedAuthenticationToken?): UserDetails {

        val credential = token?.credentials

        //credentialがなければエラー
        if (credential.toString() == "") {
            throw UsernameNotFoundException("User not found")
        }

        //credentialを確認しユーザーを作成
        return when (credential) {
            "key1" -> User("user", "", AuthorityUtils.createAuthorityList("ROLE_USER"))
            "key2" -> User("admin", "", AuthorityUtils.createAuthorityList("ROLE_ADMIN"))
            else -> User("user", "", AuthorityUtils.createAuthorityList("ROLE_INVALID") )
        }
    }
}
