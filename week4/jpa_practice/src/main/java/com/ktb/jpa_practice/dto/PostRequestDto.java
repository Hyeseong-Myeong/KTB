package com.ktb.jpa_practice.dto;

import lombok.Data;

@Data
public class PostRequestDto {

    private String title;
    private String content;
    private String email;
}
