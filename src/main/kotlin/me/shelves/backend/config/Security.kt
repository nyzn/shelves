package me.shelves.backend.config

import me.shelves.backend.utile.ShelvesBasicAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
@Order(-1)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class Security: WebSecurityConfigurerAdapter() {

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
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("admin"))
                .authorities("ROLE_ADMIN")
                .roles("ADMIN")
    }

    override fun configure(http: HttpSecurity) {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/swagger-ui.html").authenticated()
                .antMatchers("/swagger-resources/**").authenticated()
                .antMatchers("/webjars/**").authenticated()
                .antMatchers("/v2/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic().realmName("Shelves Application").authenticationEntryPoint(ShelvesBasicAuthenticationEntryPoint())
                //.and()
                //.formLogin().permitAll()
                .and()
                .csrf().disable()
    }
}