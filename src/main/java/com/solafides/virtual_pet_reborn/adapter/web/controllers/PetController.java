package com.solafides.virtual_pet_reborn.adapter.web.controllers;

import com.solafides.virtual_pet_reborn.adapter.web.dtos.PetDto;
import com.solafides.virtual_pet_reborn.adapter.web.requests.FeedPetRequest;
import com.solafides.virtual_pet_reborn.adapter.web.requests.GiftPetRequest;
import com.solafides.virtual_pet_reborn.application.exceptions.UserManagmentException;
import com.solafides.virtual_pet_reborn.application.usecases.*;
import com.solafides.virtual_pet_reborn.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static reactor.core.publisher.Mono.*;

@RequiredArgsConstructor
@Configuration
class PetController {

    // Use cases injected here

    private final PetCrudService petCrudService;
    private final UserCrudService userCrudService;
    private final FeedPetService feedPetService;
    private final GiveGiftToPetService giveGiftToPetService;


    // The exposed API as a bean

    @Bean
    public RouterFunction<ServerResponse> petEndpoints() {
        return route(GET("/api/pets/{username}"), this::getPetByUsername)
                .andRoute(POST("/api/pets/{username}"), this::createPet)
                .andRoute(GET("/api/pets/{username}/{id}"), this::getPetByUsernameAndId)
                .andRoute(POST("/api/pets/{username}/{id}/feed"), this::feedPet)
                .andRoute(POST("/api/pets/{username}/{id}/gift"), this::giveGiftToPet)
                .andRoute(DELETE("/api/pets/{username}/{id}"), this::deletePet);
    }

    private Mono<ServerResponse> deletePet(ServerRequest request) {

        return petCrudService.deleteById(Long.valueOf(request.pathVariable("id")))

                .then(ok().build());

    }

    // Controller logic

    private Mono<ServerResponse> giveGiftToPet(ServerRequest request) {

        // 0 - Try to get the request model
        return request.bodyToMono(GiftPetRequest.class)

                // 1 - Get the user using username
                .flatMap(gift -> userCrudService.findByUsername(request.pathVariable("username"))

                        // 2 - Load user's pets
                        .flatMapMany(user -> petCrudService.findAllByMasterId(user.id()))

                        // 3 - Filter by Id to find the right one
                        .filter(pet -> Objects.equals(pet.getId(), Long.valueOf(request.pathVariable("id"))))

                        // 3b - Ensure there is a single pet with the id
                        .single()

                        // 4 - Call the service
                        .flatMap(pet -> giveGiftToPetService.giveGiftToPet(pet, Gift.valueOf(gift.gift())))

                )

                // The return type is the current energy level, the response model may be adapted
                .flatMap(mood -> ok().contentType(APPLICATION_JSON).bodyValue(mood));

        // TODO Some error handling may be useful here

    }

    private Mono<ServerResponse> feedPet(ServerRequest request) {

        // 0 - Try to get the request model
        return request.bodyToMono(FeedPetRequest.class)

                // 1 - Get the user using username
                .flatMap(food -> userCrudService.findByUsername(request.pathVariable("username"))

                        // 2 - Load user's pets
                        .flatMapMany(user -> petCrudService.findAllByMasterId(user.id()))

                        // 3 - Filter by Id to find the right one
                        .filter(pet -> Objects.equals(pet.getId(), Long.valueOf(request.pathVariable("id"))))

                        // 3b - Ensure there is a single pet with the id
                        .single()

                        // 4 - Call the service
                        .flatMap(pet -> feedPetService.feedPet(pet, Food.valueOf(food.food())))

                )

                // The return type is the current energy level, the response model may be adapted
                .flatMap(energyLevel -> ok().contentType(APPLICATION_JSON).bodyValue(energyLevel));

        // TODO Some error handling may be useful here

    }

    private Mono<ServerResponse> getPetByUsernameAndId(ServerRequest request) {

        // TODO this one needs some error handling
        return userCrudService.findByUsername(request.pathVariable("username"))

                .flatMapMany(user -> petCrudService.findAllByMasterId(user.id()))

                .filter(pet -> Objects.equals(pet.getId(), Long.valueOf(request.pathVariable("id"))))

                // This should ensure that the flux contains only one element, if not it will throw an error
                .single()

                .map(pet -> new PetDto(
                        pet.getId(),
                        pet.getMasterId(),
                        pet.getName(),
                        pet.getPetType(),
                        pet.getEnergy(),
                        pet.getMood(),
                        pet.getPetLevel(),
                        pet.getLastFeedTime()
                ))

                .flatMap(petDto -> ok().contentType(APPLICATION_JSON).bodyValue(petDto));

    }

    private Mono<ServerResponse> createPet(ServerRequest request) {

        return request.bodyToMono(PetDto.class)

                .flatMap(petDto -> userCrudService.findByUsername(request.pathVariable("username"))
                        .map(user -> new PetDto(
                                petDto.getId(),
                                user.id(),
                                petDto.getName(),
                                petDto.getPetType(),
                                petDto.getEnergy(),
                                petDto.getMood(),
                                petDto.getPetLevel(),
                                petDto.getLastFeedTime())))

                .map(PetMapper::dtoToEntity)

                .flatMap(petCrudService::save)

                // Create a 200 OK response. This should maybe return a 201 plus the DTO of the newly created resource
                .flatMap(pet -> ok().build())

                // Generic error handler
                .onErrorResume(e -> status(INTERNAL_SERVER_ERROR).body(e.getMessage(), String.class));

    }

    private Mono<ServerResponse> getPetByUsername(ServerRequest request) {

        return userCrudService.findByUsername(request.pathVariable("username"))

                .switchIfEmpty(error(new UserManagmentException("User with name " + request.pathVariable("username") + " not found.")))

                .flatMapMany(user -> Objects.equals(user.role(), "ADMIN") ? petCrudService.findAll() : petCrudService.findAllByMasterId(user.id()))

                .map(pet -> new PetDto(
                        pet.getId(),
                        pet.getMasterId(),
                        pet.getName(),
                        pet.getPetType(),
                        pet.getEnergy(),
                        pet.getMood(),
                        pet.getPetLevel(),
                        pet.getLastFeedTime()
                ))

                .collectList()

                .flatMap(pets -> ok().contentType(APPLICATION_JSON).bodyValue(pets));

    }

}
