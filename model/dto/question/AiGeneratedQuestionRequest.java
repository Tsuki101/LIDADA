package com.lizhi.lidada.model.dto.question;

import lombok.Data;

import java.io.Serializable;
@Data
public class AiGeneratedQuestionRequest implements Serializable {
    private Long appId;
    int questionNumber=10;
    int optionNumber=2;
    private static final long serialVersionUID = 1L;
}

