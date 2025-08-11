package com.lizhi.lidada.model.dto.question;

import com.lizhi.lidada.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询题目请求
 *
 * @author <a href="https://github.com/Tsuki101">程序员梨汁</a>
 * @from <a href="https://github.com/Tsuki101">github个人主页</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {


    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 题目id
     */
    private Long id;
    private Long appId;
    private String questionContent;
    /**
     * id
     */
    private Long notId;


    private static final long serialVersionUID = 1L;
}