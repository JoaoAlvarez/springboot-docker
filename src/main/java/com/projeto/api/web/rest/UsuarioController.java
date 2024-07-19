package com.projeto.api.web.rest;

import com.projeto.api.domain.Usuario;
import com.projeto.api.service.UsuarioService;
import com.projeto.api.web.rest.dto.ErrorDTO;
import com.projeto.api.web.rest.dto.NovoUsuarioDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody NovoUsuarioDTO usuario){
        try{
            usuarioService.create(new Usuario(null, usuario.getUsername(), usuario.getPassword(), usuario.getRoles()));
            return ResponseEntity.noContent().build();
        } catch (Error e) {
            return ResponseEntity.badRequest().body(ErrorDTO.builder().message(e.getMessage()).code(400));
        }

    }
}