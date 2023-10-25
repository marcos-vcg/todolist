package br.com.marcoscosta.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tasks")
public class TaskModel {
    
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private UUID idUser;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private LocalDateTime endedAt;
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;


    public void setTitle(String title) throws Exception {
        if(title.length() > 50){
            throw new Exception("O campo title deve ter no máximo 50 caracteres");
        }
        this.title = title;
    }
}
