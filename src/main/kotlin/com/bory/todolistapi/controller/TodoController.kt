package com.bory.todolistapi.controller

import com.bory.todolistapi.dto.TodoDto
import com.bory.todolistapi.service.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/todos")
class TodoController(
    private val todoService: TodoService
) {

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable("id") id: Long): ResponseEntity<TodoDto> {
        val dto = todoService.getTodoById(id)
        return if (dto != null) ResponseEntity.ok(dto) else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAllTodos(): List<TodoDto> {
        return todoService.getAllTodos()
    }

    @PostMapping
    fun addTodo(@RequestBody dto: TodoDto): ResponseEntity<TodoDto> {
        val createdDto = todoService.addTodo(dto)
        return ResponseEntity.created(createdDto.toUri()).body(createdDto)
    }

    @PutMapping("/{id}")
    fun modifyTodo(
        @PathVariable("id") id: Long,
        @RequestBody dto: TodoDto
    ): ResponseEntity<TodoDto> {
        val modifiedDto = todoService.modifyTodo(id, dto)
        return ResponseEntity.ok(modifiedDto)
    }

    @DeleteMapping("/{id}")
    fun deleteTodoById(@PathVariable("id") id: Long): ResponseEntity<TodoDto> {
        val deletedDto = todoService.deleteTodo(id)
        return ResponseEntity.ok(deletedDto)
    }

    @PostMapping("/{id}/toggle")
    fun toggleTodoById(@PathVariable("id") id: Long): ResponseEntity<TodoDto> {
        val toggledDto = todoService.toggleTodo(id)
        return ResponseEntity.ok(toggledDto)
    }

    private fun TodoDto.toUri(): URI {
        return URI("/todo/api/v1/$id")
    }
}
