package com.example.demo

import com.example.demo.service.MyAuthenticationUserDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider

@Configuration
@EnableWebSecurity
class SecurityConfig  {
    @Bean
    fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager) : SecurityFilterChain {
        http.authorizeRequests()
            //helloにアクセスするにはUSERかADMINのロールが必要
            ?.antMatchers("/hello")?.hasAnyRole("USER", "ADMIN")
            //全てのURLリクエストは認証されているユーザーのみアクセスできる。
            ?.anyRequest()?.authenticated()
            //独自フィルタを認証に追加
            ?.and()?.addFilter(preAuthenticatedProcessingFilter(authenticationManager))
            // リクエスト毎に認証を実施
            ?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        return http.build()
    }

    //AuthenticationManagerをBean登録
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    //PreAuthentifiatedFilterで取得された認証情報から MyAuthenticationUserDetailService を利用して、ユーザー情報を作成
    @Bean
    fun preAuthenticatedAuthenticationProvider(): PreAuthenticatedAuthenticationProvider {
        return PreAuthenticatedAuthenticationProvider().apply {
            setPreAuthenticatedUserDetailsService(MyAuthenticationUserDetailService())
        }
    }

    // 独自フィルタをBean登録
    @Bean
    fun preAuthenticatedProcessingFilter(authenticationManager: AuthenticationManager): AbstractPreAuthenticatedProcessingFilter {
        return MyPreAuthenticatedFilter().apply {
            setAuthenticationManager(authenticationManager)
        }
    }
}