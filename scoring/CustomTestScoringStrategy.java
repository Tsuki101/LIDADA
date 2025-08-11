package com.lizhi.lidada.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lizhi.lidada.model.dto.question.QuestionContentDTO;
import com.lizhi.lidada.model.entity.App;
import com.lizhi.lidada.model.entity.Question;
import com.lizhi.lidada.model.entity.ScoringResult;
import com.lizhi.lidada.model.entity.UserAnswer;
import com.lizhi.lidada.model.vo.QuestionVO;
import com.lizhi.lidada.service.AppService;
import com.lizhi.lidada.service.QuestionService;
import com.lizhi.lidada.service.ScoringResultService;

import javax.annotation.Resource;

import java.util.*;

/**
 * 自定义测评类应用评分策略
 */
@ScoringStrategyConfig(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;
    @Resource
    private AppService appService;
    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choice, App app) throws Exception {
        //1 根据id查询到题目和题目结果 mybatis-plus getone-lambda查询
        Long appId = app.getId();
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId));
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, appId));
//2 根据用户的选择统计属性个数
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 初始化一个对象，用于存储每个选项的计数
        Map<String, Integer> optionCount = new HashMap<>();

        // 遍历题目列表
        for (int i = 0; i < questionContent.size(); i++) {
            QuestionContentDTO questionContentDTO = questionContent.get(i);
            //List<QuestionContentDTO.Option> options = questionContentDTO.getOptions(); // 获取与当前题目对应的答案
            //List<QuestionContentDTO.Option> options = Optional.ofNullable(questionContentDTO.getOptions()).orElse(Collections.emptyList());

            // 遍历题目中的选项
            for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                // 如果答案和选项的key匹配
                if (option.getKey().equals(choice.get(i))) {
                    // 获取选项的result属性
                    String result = option.getResult();
                    if(!optionCount.containsKey(result)){optionCount.put(result,0);}
                    // 在optionCount中增加计数
                    optionCount.put(result, optionCount.getOrDefault(result, 0) + 1);
                }
            }
        }
//3 遍历评分结果 计算哪个结果的分数更高
        // 初始化最高分数和最高分数对应的评分结果
        int maxScore = -1; // 使用 -1 以确保任何得分都会大于它
        ScoringResult maxScoringResult = scoringResultList.get(0); // 初始为 null


        // 遍历评分结果列表
        for (ScoringResult result : scoringResultList) {
            List<String> resultProp = JSONUtil.toList(result.getResultProp(), String.class);
            // 计算当前评分结果的分数 各类型人格的分数，选择最高的作为结果
            int score = resultProp
                    .stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                    .sum();

            // 如果分数高于当前最高分数，或者在相同分数下根据自定义条件选择（例如第一个结果）
            if (score > maxScore) {
                maxScore = score;
                maxScoringResult = result;
            } else if (score == maxScore && maxScoringResult == null) {
                // 处理平局情况，选择第一个
                maxScoringResult = result;
            }
        }
        //4构造返回值，填充答案对象的属性
        UserAnswer userAnswer = new UserAnswer();

        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choice));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());

        return userAnswer;


        //return null;
    }
}
