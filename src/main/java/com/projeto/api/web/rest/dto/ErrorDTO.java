package com.projeto.api.web.rest.dto;

import lombok.Builder;

@Builder
public class ErrorDTO {
    private String message;
    private int code;
}
