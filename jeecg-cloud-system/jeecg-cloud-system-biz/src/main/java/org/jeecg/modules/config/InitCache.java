package org.jeecg.modules.config;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InitCache implements ApplicationRunner {

    @Autowired
    private ISysGatewayRouteService sysGatewayRouteService;

    @Override
    @Async
    public void run(ApplicationArguments arg) throws Exception {
        log.info("加载路由信息到redis>>");
        sysGatewayRouteService.addRoute2Redis(CacheConstant.GATEWAY_ROUTES);
    }
}
