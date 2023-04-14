package com.bory.todolistapi.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "todo")
data class TodoEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var todo: String,

    @Column(nullable = false)
    var completed: Boolean = false,

    @CreatedDate
    @Column(name = "created_time")
    var createdTime: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "modified_time")
    var modifiedTime: LocalDateTime? = null
)