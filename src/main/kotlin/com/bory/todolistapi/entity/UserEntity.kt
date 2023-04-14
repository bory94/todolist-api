package com.bory.todolistapi.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var username: String? = null,

    @Column(nullable = false)
    var password: String? = null,

    @Column(nullable = false)
    var enabled: Boolean = true,

    @ElementCollection(fetch = FetchType.EAGER)
    var roles: List<String> = listOf(),

    @CreatedDate
    @Column(name = "created_time")
    var createdTime: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "modified_time")
    var modifiedTime: LocalDateTime? = null
    // constructors omitted for brevity
)
