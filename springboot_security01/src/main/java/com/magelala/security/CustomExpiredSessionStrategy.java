package com.magelala.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:CustomExpiredSessionStrategy
 * @Author:Timelin
 **/
@Component
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy{

    private ObjectMapper objectMapper= new ObjectMapper();

    // 处理旧用户登陆失败的逻辑
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {

        Map<String,Object> map = new HashMap<>(16);
        map.put("code",0);
        map.put("msg","已经另一台机器登录，您被迫下线。"+event.getSessionInformation().getLastRequest());

        // Map-->Json
        String json = objectMapper.writeValueAsString(map);

        event.getResponse().setContentType("application/json;charset=UTF-8");
        event.getResponse().getWriter().write(json);

        // 如果是跳转HTML页面，url代表跳转的地址
       // redirectStrategy.sendRedirect(event.getRequest(),event.getResponse(),"url");
    }
}
