package com.lizhi.lidada.scoring;

import com.lizhi.lidada.model.entity.App;
import com.lizhi.lidada.model.entity.UserAnswer;

import javax.annotation.Resource;
import java.util.List;


/**
 * 评分策略接口
 * @author lizhi
 * @version 1.0
 */
public interface ScoringStrategy {

    /**
     * @param choice
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choice, App app) throws Exception;

}
