package com.company.project.modules.system.controller;

import com.company.project.cache.UserCacheUtil;
import com.company.project.component.plugin.CustomSpringConfigurator;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.core.ServiceException;
import com.company.project.modules.system.entity.Notice;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.service.UserNoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户通告 WebSocket 服务
 */
@Slf4j
@Controller
@ServerEndpoint(value = "/user/notice/{token}", configurator = CustomSpringConfigurator.class)
public class UserNoticeServer {

    private final ObjectMapper objectMapper;

    private final UserNoticeService userNoticeService;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    public UserNoticeServer(ObjectMapper objectMapper, UserNoticeService userNoticeService) {
        this.objectMapper = objectMapper;
        this.userNoticeService = userNoticeService;
    }

    void sendNotice() {
        for (String token : sessionPools.keySet()) {
            User user = UserCacheUtil.getUser(token);
            List<Notice> list = userNoticeService.getByUser(user);
            Result<List<Notice>> listResult = Results.SUCCESS.setData(list);
            String message;
            try {
                message = objectMapper.writeValueAsString(listResult);
            } catch (JsonProcessingException e) {
                throw new ServiceException(Results.Fail.setErrorMessage("消息格式化失败"));
            }
            this.sendInfo(token, message);
        }
    }

    //给指定用户发送信息
    private void sendInfo(String token, String message) {
        Session session = sessionPools.get(token);
        try {
            synchronized (this) {
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        sessionPools.put(token, session);
        addOnlineCount();
        log.info(token + "加入webSocket！当前人数为" + onlineNum);
        this.sendNotice();
    }

    //关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "token") String token) {
        sessionPools.remove(token);
        subOnlineCount();
        log.info(token + "断开webSocket连接！当前人数为" + onlineNum);
    }

    //收到客户端信息
    @OnMessage
    public void onMessage(String message) throws IOException {
        message = "客户端：" + message + ",已收到";
        log.info(message);
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("发生错误");
        throwable.printStackTrace();
    }

    public static void addOnlineCount() {
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }

}
