package com.lizhi.lidada.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建题目请求
 *
 * @author <a href="https://github.com/Tsuki101">程序员梨汁</a>
 * @from <a href="https://github.com/Tsuki101">github个人主页</a>
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 题目内容（json格式）->转换成封装类
     */
    private List<QuestionContentDTO> questionContent;

    /**
     * 应用 id
     */
    private Long appId;


    private static final long serialVersionUID = 1L;
}