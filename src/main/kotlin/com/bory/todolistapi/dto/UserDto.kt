package com.bory.todolistapi.dto

import com.bory.todolistapi.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDto(
    val id: Long?,
    val userid: String,
    val pwd: String,
    val authorities: Set<String>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities.map { SimpleGrantedAuthority(it) }
    }

    override fun getUsername(): String {
        return userid
    }

    override fun getPassword(): String {
        return pwd
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    companion object {
        fun fromEntity(entity: UserEntity): UserDto {
            return UserDto(
                id = entity.id,
                userid = entity.username!!,
                pwd = entity.password!!,
                authorities = entity.roles.toSet()
            )
        }

        fun toEntity(dto: UserDto): UserEntity {
            return UserEntity(
                id = dto.id,
                username = dto.userid,
                password = dto.pwd,
                roles = dto.authorities.toList()
            )
        }
    }
}