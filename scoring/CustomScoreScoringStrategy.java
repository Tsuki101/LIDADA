package com.lizhi.lidada.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lizhi.lidada.model.dto.question.QuestionContentDTO;
import com.lizhi.lidada.model.entity.App;
import com.lizhi.lidada.model.entity.Question;
import com.lizhi.lidada.model.entity.ScoringResult;
import com.lizhi.lidada.model.entity.UserAnswer;
import com.lizhi.lidada.model.vo.QuestionVO;
import com.lizhi.lidada.service.QuestionService;
import com.lizhi.lidada.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@ScoringStrategyConfig(appType = 0,scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;
    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choice, App app) throws Exception {
        // 1 根据ID查询题目以及结果信息，按分数降序排序
        Long appId = app.getId();
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId));
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, appId).orderByDesc(ScoringResult::getResultScoreRange));
        // 2 根据结果信息，计算总得分
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
        int totalScore = 0;//初始化总得分
        for (int i = 0; i < questionContent.size(); i++) {
            QuestionContentDTO questionContentDTO = questionContent.get(i);
            //List<QuestionContentDTO.Option> options = questionContentDTO.getOptions(); // 获取与当前题目对应的答案
            List<QuestionContentDTO.Option> options = Optional.ofNullable(questionContentDTO.getOptions()).orElse(Collections.emptyList());

            // 遍历题目中的选项
            for (QuestionContentDTO.Option option : options) {
                // 如果答案和选项的key匹配
                if (option.getKey().equals(choice.get(i))) {
                    // 获取选项的result属性
                    String result = option.getResult();
                    // 在optionCount中增加计数
                    int score=Optional.of(option.getScore()).orElse(0);
                    totalScore = totalScore + score;
                }
            }
        }

        // 3 根据得分得出匹配的ScoringResult
        ScoringResult maxScoringResult = scoringResultList.get(0); // 初始为 null
        for (ScoringResult scoringResult : scoringResultList) {
            if (totalScore>= scoringResult.getResultScoreRange()) {
                maxScoringResult = scoringResult;
                break;
            }
        }
        // 4 返回结果UserAnswer
        UserAnswer userAnswer = new UserAnswer();

        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choice));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        userAnswer.setResultScore(totalScore);
        return userAnswer;


    }
}
