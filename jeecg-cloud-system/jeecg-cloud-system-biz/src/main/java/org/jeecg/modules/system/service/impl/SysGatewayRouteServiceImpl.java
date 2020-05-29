package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.modules.system.entity.SysGatewayRoute;
import org.jeecg.modules.system.mapper.SysGatewayRouteMapper;
import org.jeecg.modules.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: gateway路由管理
 * @Author: jeecg-boot
 * @Date:   2020-05-26
 * @Version: V1.0
 */
@Service
public class SysGatewayRouteServiceImpl extends ServiceImpl<SysGatewayRouteMapper, SysGatewayRoute> implements ISysGatewayRouteService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addRoute2Redis(String key) {
        List<SysGatewayRoute> ls = this.list(new LambdaQueryWrapper<SysGatewayRoute>().eq(SysGatewayRoute::getStatus,1));
        redisTemplate.opsForValue().set(key, JSON.toJSONString(ls));
    }

    @Override
    public void updateAll(JSONArray array) {
        this.remove(new LambdaQueryWrapper<SysGatewayRoute>().eq(SysGatewayRoute::getStatus,1));
        List<SysGatewayRoute> ls = new ArrayList<>();
        for(int i =0;i<array.size();i++){
            JSONObject json = array.getJSONObject(i);
            SysGatewayRoute route = new SysGatewayRoute();
            route.setId(json.getString("id"));
            route.setName(json.getString("name"));
            route.setPredicates(json.getString("predicates"));
            route.setFilters(json.getString("filters"));
            route.setUri(json.getString("uri"));
            if(json.get("status")==null){
                route.setStatus(1);
            }else{
                route.setStatus(json.getInteger("status"));
            }
            ls.add(route);
        }
        this.saveBatch(ls);
        redisTemplate.opsForValue().set(CacheConstant.GATEWAY_ROUTES,  JSON.toJSONString(ls));
    }

    @Override
    public void clearRedis() {
        redisTemplate.opsForValue().set(CacheConstant.GATEWAY_ROUTES,  null);
    }
}
