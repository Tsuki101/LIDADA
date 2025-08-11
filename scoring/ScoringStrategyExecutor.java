package com.lizhi.lidada.scoring;

import com.lizhi.lidada.common.ErrorCode;
import com.lizhi.lidada.exception.BusinessException;
import com.lizhi.lidada.model.entity.App;
import com.lizhi.lidada.model.entity.UserAnswer;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
 * 评分策略执行器
 * */

/**
 * @author lizhi
 * @version 1.0
 */
@Service
public class ScoringStrategyExecutor {
    /**
     *
     * @param choiceList
     * @param app
     * @return
     * @throws Exception
     */
    @Resource
    private List<ScoringStrategy> scoringStrategyList;
    public UserAnswer doScore(List<String> choiceList, App app) throws Exception{
    Integer appType = app.getAppType();
    Integer appScoringStrategy =app.getScoringStrategy();
    if(appType==null||appScoringStrategy==null){
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,"应用配置有误，未找到匹配的策略");
    }
    for(ScoringStrategy strategy: scoringStrategyList){
        if(strategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)){
            ScoringStrategyConfig scoringStrategyConfig = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
            if(scoringStrategyConfig.appType()==appType&&scoringStrategyConfig.scoringStrategy()==appScoringStrategy)
                return strategy.doScore(choiceList,app);
        }
    }
    throw new BusinessException(ErrorCode.SYSTEM_ERROR,"未找到匹配的策略");
    }
}
