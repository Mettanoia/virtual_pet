package com.solafides.virtual_pet_reborn.adapter.repository.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "users")
public final class UserEntity {

    @Id
    private final Long id;
    private final String username;
    private final String password;
    private final String role;

}
