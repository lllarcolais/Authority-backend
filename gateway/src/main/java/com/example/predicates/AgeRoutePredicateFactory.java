package com.example.predicates;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class AgeRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeRoutePredicateFactory.Config> {

    public AgeRoutePredicateFactory() {
        super(AgeRoutePredicateFactory.Config.class);

    }

    // 将配置文件中的值按返回集合的顺序，赋值给配置类
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(new String[] {"minAge", "maxAge"});
    }

    // 断言
    @Override
    public Predicate<ServerWebExchange> apply(AgeRoutePredicateFactory.Config config) {

        return (ServerWebExchange serverWebExchange)->{
            // TODO 获取请求参数age，判断是否满足[18, 60)
            String age = serverWebExchange.getRequest().getQueryParams().getFirst("age");
            if (StringUtils.isEmpty(age)) {
                return false;
            }

            if (!age.matches("[0-9]+")) {
                return false;
            }

            int iAge = Integer.parseInt(age);

            if (iAge >= config.minAge && iAge < config.maxAge) {
                return true;
            } else {
                return false;
            }
        };
    }

    // 配置类，属性用于接收配置文件中的值
    @Data
    public static class Config {
        private int minAge;
        private int maxAge;
    }
}
