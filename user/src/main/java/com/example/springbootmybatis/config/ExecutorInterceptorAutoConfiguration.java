//package com.example.springbootmybatis.config;
//
//import com.example.springbootmybatis.interceptor.ExecutorInterceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
//@Configuration
//public class ExecutorInterceptorAutoConfiguration {
//    @Autowired
//    private List<SqlSessionFactory> sqlSessionFactoryList;
//
//    @PostConstruct
//    public void addMysqlInterceptor() {
//        ExecutorInterceptor executorInterceptor = new ExecutorInterceptor();
//
//        for (SqlSessionFactory sqlSessionFactory:sqlSessionFactoryList){
//            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
//            configuration.addInterceptor(executorInterceptor);
//        }
//        System.out.println(sqlSessionFactoryList);
//    }
//}
