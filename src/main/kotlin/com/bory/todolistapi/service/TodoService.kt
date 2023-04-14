package com.bory.todolistapi.service

import com.bory.todolistapi.dto.TodoDto
import com.bory.todolistapi.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class TodoService(
    private val todoRepository: TodoRepository
) {

    @Transactional(readOnly = true)
    fun getTodoById(id: Long): TodoDto? {
        return todoRepository.findById(id).map { TodoDto.fromEntity(it) }.orElse(null)
    }

    @Transactional(readOnly = true)
    fun getAllTodos(): List<TodoDto> {
        return todoRepository.findAll().map { TodoDto.fromEntity(it) }
    }

    fun addTodo(dto: TodoDto): TodoDto {
        val entity = dto.toEntity()
        return TodoDto.fromEntity(todoRepository.save(entity))
    }

    fun modifyTodo(id: Long, dto: TodoDto): TodoDto {
        val entity = todoRepository.findById(id).orElseThrow { IllegalArgumentException("Todo not found with id $id") }
        entity.todo = dto.todo
        entity.completed = dto.completed
        entity.modifiedTime = LocalDateTime.now()
        return TodoDto.fromEntity(todoRepository.save(entity))
    }

    fun deleteTodo(id: Long): TodoDto {
        val entity = todoRepository.findById(id).orElseThrow { IllegalArgumentException("Todo not found with id $id") }
        todoRepository.delete(entity)
        return TodoDto.fromEntity(entity)
    }

    fun toggleTodo(id: Long): TodoDto {
        val entity = todoRepository.findById(id).orElseThrow { IllegalArgumentException("Todo not found with id $id") }
        entity.completed = !entity.completed
        entity.modifiedTime = LocalDateTime.now()
        return TodoDto.fromEntity(todoRepository.save(entity))
    }
}
