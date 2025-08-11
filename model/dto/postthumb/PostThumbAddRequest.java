package com.lizhi.lidada.model.dto.postthumb;

import java.io.Serializable;

import lombok.Data;

/**
 * 帖子点赞请求
 *
 * @author <a href="https://github.com/Tsuki101">程序员梨汁</a>
 * @from <a href="https://github.com/Tsuki101">github个人主页</a>
 */
@Data
public class PostThumbAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long postId;

    private static final long serialVersionUID = 1L;
}