package com.projeto.api.web.rest.validation;

import java.util.ArrayList;
import java.util.List;

import com.projeto.api.domain.Usuario;
import com.projeto.api.repository.UsuarioRepository;
import com.projeto.api.web.rest.dto.NovoUsuarioDTO;
import com.projeto.api.web.rest.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class InserirUsuarioValidator implements ConstraintValidator<InserirUsuario, NovoUsuarioDTO> {

    @Autowired
    UsuarioRepository repositorio;

    @Override
    public void initialize(InserirUsuario ann) {
    }

    @Override
    public boolean isValid(NovoUsuarioDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        Usuario existent = repositorio.findByUsername(dto.getUsername());
        if (existent != null) {
            list.add(new FieldMessage("username", "Usuário ja exsite"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}


