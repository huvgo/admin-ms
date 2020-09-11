package com.company.project.modules.engine.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.company.project.component.plugin.CustomSpringConfigurator;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.core.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户通告 WebSocket 服务
 */
@Slf4j
@Controller
@ServerEndpoint(value = "/socket/engine/oshi/{token}", configurator = CustomSpringConfigurator.class)
public class OshiServer {

    private final ObjectMapper objectMapper;

    private Session session;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();


    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    public OshiServer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void sendNotice() {
        for (String token : sessionPools.keySet()) {
            List<Object> list = new ArrayList<>();
            list.add(DateUtil.format(new Date(), "HH:mm:ss"));
            Runtime runtime = Runtime.getRuntime();
            long jvmTotalMemoryByte = runtime.totalMemory();
            //jvm最大可申请
            //空闲空间
            long freeMemoryByte = runtime.freeMemory();

            String format = new DecimalFormat("##").format((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte * 100.0);
            log.info(format);
            int v = Convert.toInt(format);
            list.add(v);

            SystemInfo systemInfo = new SystemInfo();
            GlobalMemory memory = systemInfo.getHardware().getMemory();
            //总内存
            long totalByte = memory.getTotal();
            //剩余
            long acaliableByte = memory.getAvailable();
            String format1 = new DecimalFormat("##").format((totalByte - acaliableByte) * 1.0 / totalByte * 100.0);
            int memoryU = Convert.toInt(format1);
            list.add(memoryU);

            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            long[] prevTicks = processor.getSystemCpuLoadTicks();
            // 睡眠1s
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

            // cpu当前使用率
            float cpuUseTotal = Convert.toFloat(new DecimalFormat("##.##").format((1.0 - (idle * 1.0 / totalCpu)) * 100.0));

            list.add(cpuUseTotal);

            Result<List<Object>> listResult = Results.SUCCESS.setData(list);
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
