package com.bory.todolistapi.dto

import com.bory.todolistapi.entity.TodoEntity
import java.time.LocalDateTime

data class TodoDto(
    val id: Long? = null,
    val todo: String,
    var completed: Boolean = false,
    val createdTime: LocalDateTime? = null,
    val modifiedTime: LocalDateTime? = null
) {
    companion object {
        fun fromEntity(entity: TodoEntity): TodoDto {
            return TodoDto(
                id = entity.id,
                todo = entity.todo,
                completed = entity.completed,
                createdTime = entity.createdTime,
                modifiedTime = entity.modifiedTime
            )
        }
    }

    fun toEntity(): TodoEntity {
        return TodoEntity(
            id = id,
            todo = todo,
            completed = completed,
            createdTime = createdTime,
            modifiedTime = modifiedTime
        )
    }
}