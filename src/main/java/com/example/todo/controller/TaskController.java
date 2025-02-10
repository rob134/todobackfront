package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:8100")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository; // 🔹 Adicionado para buscar o usuário no banco

    /**
     * Obtém todas as tarefas do usuário autenticado.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 🔹 Garantindo que as tarefas estão carregadas antes de fechar a sessão
        List<Task> tasks = taskService.getTasksByUser(user);
        tasks.size(); // 🔹 Força a inicialização das tarefas

    return ResponseEntity.ok(tasks);
    }


    /**
     * Cria uma nova tarefa para o usuário autenticado.
     */
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você precisa estar logado para criar tarefas.");
        }

        // 🔹 Obtém o nome do usuário autenticado e busca no banco
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return ResponseEntity.ok(taskService.createTask(task, user));
    }

    /**
     * Atualiza uma tarefa existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task existingTask = taskService.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setCompleted(task.isCompleted());

        return ResponseEntity.ok(taskService.updateTask(id, existingTask));
    }

    /**
     * Marca uma tarefa como concluída.
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        task.setCompleted(true);
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    /**
     * Exclui uma tarefa pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
