package com.lizhi.lidada.manager;

import com.lizhi.lidada.common.ErrorCode;
import com.lizhi.lidada.exception.BusinessException;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用AI能力*/
@Component
public class AiManager {
    private static final float STABLE_TEMPERATURE=0.05f;
    private static final float UNSTABLE_TEMPERATURE=0.99f;
@Resource
private ClientV4 clientV4;
/**
 * 同步调用（答案稳定）
 * @param systemMessage
 * @param userMessage
 * @return
 */
public String doSyncStableRequest(String systemMessage,String userMessage){
    return doSyncRequest(systemMessage,userMessage,STABLE_TEMPERATURE);
}
/**
 * 同步调用（答案较随机）
 * @param systemMessage
 * @param userMessage
 * @return
 */
public String doSyncUnstableRequest(String systemMessage,String userMessage){
    return doSyncRequest(systemMessage,userMessage,UNSTABLE_TEMPERATURE);
}
/**
 * 同步请求
 * @param systemMessage
 * @param userMessage
 * @param temperature
 * @return
 */
public String doSyncRequest(String systemMessage,String userMessage,Float temperature){
    return doRequest(systemMessage,userMessage,Boolean.FALSE,temperature);
}

    /**
     * 通用构造请求
     * @param systemMessage
     * @param userMessage
     * @param stream
     * @param temperature
     * @return
     */
public String doRequest(String systemMessage,String userMessage,Boolean stream,Float temperature){
    List<ChatMessage> messages = new ArrayList<>();
    ChatMessage systemChatMessage=new ChatMessage(ChatMessageRole.SYSTEM.value(),systemMessage);
    ChatMessage userChatMessage=new ChatMessage(ChatMessageRole.USER.value(),userMessage);
    messages.add(systemChatMessage);
    messages.add(userChatMessage);
    return doRequest(messages,stream,temperature);
}
/**
 * 构造请求
 * @param messages
 * @param stream
 * @param temperature
 * @return
 */
public String doRequest(List<ChatMessage> messages,Boolean stream,Float temperature){
    ChatCompletionRequest chatCompletionRequest =ChatCompletionRequest.builder()
            .model(Constants.ModelChatGLM4)
            .stream(stream)
            .invokeMethod(Constants.invokeMethod)
            .temperature(temperature)
            .messages(messages)
            .build();
    //ModelApiResponse invokeModelApiResp=clientV4.invokeModelApi(chatCompletionRequest);

    try {ModelApiResponse invokeModelApiResp=clientV4.invokeModelApi(chatCompletionRequest);

        return invokeModelApiResp.getData().getChoices().get(0).toString();}
    catch(Exception e){
        e.printStackTrace();
        throw new BusinessException(ErrorCode.SYSTEM_ERROR,e.getMessage());}
}
/**
 * 通用流式请求*/
public Flowable<ModelData> doStreamRequest(String systemMessage, String userMessage, Float temperature){
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemChatMessage=new ChatMessage(ChatMessageRole.SYSTEM.value(),systemMessage);
        ChatMessage userChatMessage=new ChatMessage(ChatMessageRole.USER.value(),userMessage);
        messages.add(systemChatMessage);
        messages.add(userChatMessage);
        return doStreamRequest(messages,temperature);
    }
/**流式请求
 * */
public Flowable<ModelData> doStreamRequest(List<ChatMessage> messages, Float temperature){
        ChatCompletionRequest chatCompletionRequest =ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .invokeMethod(Constants.invokeMethod)
                .temperature(temperature)
                .messages(messages)
                .build();


        try {ModelApiResponse invokeModelApiResp=clientV4.invokeModelApi(chatCompletionRequest);

            return invokeModelApiResp.getFlowable();}
        catch(Exception e){
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,e.getMessage());}
    }
}
