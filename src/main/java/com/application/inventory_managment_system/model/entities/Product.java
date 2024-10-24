package com.application.inventory_managment_system.model.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID товара")
    private Long id;

    @NotBlank
    @Length(max = 30)
    @Comment("Название товара")
    private String name;

    @NotNull
    @Length(max = 500)
    @Comment("Описание товара")
    private String description;

    @NotNull
    @Positive
    @Comment("Цена товара")
    private double price;

    @NotNull
    @PositiveOrZero
    @Comment("Количество товара в наличии")
    private int quantity; 

    @CreationTimestamp
    @Comment("Дата и время регистрации товара")
    @Column(name = "creation_date_time", nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @UpdateTimestamp
    @Comment("Дата и время последнего изменения данных о товаре")
    @Column(name = "last_up_date_time", nullable = false, updatable = true)
    private LocalDateTime lastUpDateTime;
}
