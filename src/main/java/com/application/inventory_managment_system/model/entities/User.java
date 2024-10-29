package com.application.inventory_managment_system.model.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.application.inventory_managment_system.model.view.UserView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Comment("ID пользователя")
    private UUID id;

    @NotBlank(groups = { UserView.CreateUser.class, UserView.UpdateUser.class })
    @Comment("Логин пользователя")
    private String username;

    @NotBlank(groups = { UserView.CreateUser.class, UserView.UpdateUser.class })
    @Comment("Email пользователя")
    private String email;

    @NotNull
    @Comment("Список товаров, которые покупал пользователь")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Product> products;

    @CreationTimestamp
    @Comment("Дата и время регистрации пользователя")
    @Column(name = "creation_date_time", nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    @Comment("Дата и время последнего изменения данных пользователя")
    @Column(name = "last_up_date_time", nullable = false, updatable = true)
    private LocalDateTime lastUpDateTime;

}
