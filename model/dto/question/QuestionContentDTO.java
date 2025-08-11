package com.lizhi.lidada.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionContentDTO {

    /**
     * 题目标题
     */
    private String title;
    /**
     * 题目选项
     */
    private List<Option> options;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor

    public static class Option {
        private String result;
        private int score;
        private String value;
        private String key;
    }
}
