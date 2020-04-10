package me.shelves.backend.config

import me.shelves.backend.user.CustomUserDetailsService
import me.shelves.backend.user.model.Roles
import me.shelves.backend.utile.CustomFilter
import me.shelves.backend.utile.ShelvesBasicAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
@Order(-1)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class Security: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var shelvesBasicAuthenticationEntryPoint: ShelvesBasicAuthenticationEntryPoint

    @Autowired
    lateinit var userDetailsService: CustomUserDetailsService

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val pMap: MutableMap<String, PasswordEncoder> = mutableMapOf()
        pMap["bcrypt"] = BCryptPasswordEncoder()
        return DelegatingPasswordEncoder("bcrypt", pMap)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
/*        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("admin"))
                .authorities("ROLE_ADMIN")
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .roles(Roles.ADMIN)*/
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
        auth.userDetailsService(userDetailsService)
        auth.jdbcAuthentication()
                .dataSource(dataSource)
    }

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/swagger-ui.html").authenticated()
                .antMatchers("/swagger-resources/**").authenticated()
                .antMatchers("/webjars/**").authenticated()
                .antMatchers("/v2/**").authenticated()
                .anyRequest().permitAll()
                /*.and()
                .httpBasic().realmName("Shelves Application").authenticationEntryPoint(shelvesBasicAuthenticationEntryPoint)*/
                .and()
                .formLogin().permitAll()
                .and()
                .logout()
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .contentTypeOptions()
                .and()
                .referrerPolicy(ReferrerPolicy.NO_REFERRER)
                .and()
                .xssProtection().block(true)
                .and()
                .and().csrf().disable()


        http.addFilterAfter(CustomFilter(),
                BasicAuthenticationFilter::class.java)

    }

    @Bean
    protected fun corsFilter(): CorsFilter? {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("OPTIONS")
        config.addAllowedMethod("HEAD")
        config.addAllowedMethod("GET")
        config.addAllowedMethod("PUT")
        config.addAllowedMethod("POST")
        config.addAllowedMethod("DELETE")
        config.addAllowedMethod("PATCH")
        config.addExposedHeader("Location")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

}