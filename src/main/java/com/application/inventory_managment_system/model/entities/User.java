package com.application.inventory_managment_system.model.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.application.inventory_managment_system.model.view.UserView;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID пользователя")
    private Long id;

    @NotBlank(groups = { UserView.CreateUser.class, UserView.UpdateUser.class })
    @Comment("Логин пользователя")
    private String username;

    @NotBlank(groups = { UserView.CreateUser.class, UserView.UpdateUser.class })
    @Comment("Email пользователя")
    private String email;

    @NotBlank(groups = UserView.UpdateUserPassword.class)
    @Comment("Пароль пользователя")
    private String password;

    @CreationTimestamp
    @Comment("Дата и время регистрации пользователя")
    @Column(name = "creation_date_time", nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    @Comment("Дата и время последнего изменения данных пользователя")
    @Column(name = "last_up_date_time", nullable = false, updatable = true)
    private LocalDateTime lastUpDateTime;

}
