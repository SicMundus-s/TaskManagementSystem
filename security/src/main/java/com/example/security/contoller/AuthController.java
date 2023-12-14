package com.example.security.contoller;

import com.example.security.service.AuthService;
import com.example.security.service.impl.RabbitMQSenderImpl;
import io.micrometer.common.util.StringUtils;
import com.example.security.dto.UserDTO;
import com.example.security.entity.jwt.JwtRequest;
import com.example.security.entity.jwt.JwtResponse;
import com.example.security.entity.jwt.RefreshJwtRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth/")
@Profile("security")
@Tag(name = "Authentication Controller", description = "Контроллер для аутентификации пользователей")
public class AuthController {
    private final AuthService authService;
    private final RabbitMQSenderImpl rabbitMQSender;

    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя и возвращает JWT токен")
    @ApiResponse(responseCode = "200", description = "Успешная регистрация")
    @ApiResponse(responseCode = "400", description = "Неверные данные для регистрации")
    @PostMapping("registration")
    public ResponseEntity<EntityModel<JwtResponse>> registration(@RequestBody UserDTO userDTO) {
        JwtResponse token = authService.registration(userDTO);
        if (token.getAccessToken() != null) {
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class)
                    .registration(userDTO)).withSelfRel();
            EntityModel<JwtResponse> response = EntityModel.of(token, selfLink);
            rabbitMQSender.send(selfLink.getHref());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Вход пользователя", description = "Аутентификация пользователя и получение JWT токена")
    @ApiResponse(responseCode = "200", description = "Успешный вход")
    @ApiResponse(responseCode = "400", description = "Неверные данные для входа")
    @PostMapping("login")
    public ResponseEntity<EntityModel<JwtResponse>> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        if (!StringUtils.isEmpty(token.getAccessToken())) {
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class)
                    .login(authRequest)).withSelfRel();
            EntityModel<JwtResponse> response = EntityModel.of(token, selfLink);
            rabbitMQSender.send(selfLink.getHref());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получение нового Access токена", description = "Получение нового Access токена с использованием Refresh токена")
    @ApiResponse(responseCode = "200", description = "Новый Access токен предоставлен")
    @ApiResponse(responseCode = "400", description = "Неверный Refresh токен")
    @PostMapping("token")
    public ResponseEntity<EntityModel<JwtResponse>> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class)
                .getNewAccessToken(request)).withSelfRel();
        EntityModel<JwtResponse> response = EntityModel.of(token, selfLink);
        rabbitMQSender.send(selfLink.getHref());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получение нового Refresh токена", description = "Получение нового Refresh токена")
    @ApiResponse(responseCode = "200", description = "Новый Refresh токен предоставлен")
    @ApiResponse(responseCode = "400", description = "Неверный Refresh токен")
    @PostMapping("refresh")
    public ResponseEntity<EntityModel<JwtResponse>> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthController.class)
                .getNewRefreshToken(request)).withSelfRel();
        EntityModel<JwtResponse> response = EntityModel.of(token, selfLink);
        rabbitMQSender.send(selfLink.getHref());
        return ResponseEntity.ok(response);
    }
}
