package com.lizhi.lidada.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReviewRequest implements Serializable {
    private Long id;
    /**
     * 0-待审核 1-审核通过 2-拒绝
     */
    private Integer reviewStatus;
    private String reviewMessage;
    private static final long serialVersionUID = 1L;

}
