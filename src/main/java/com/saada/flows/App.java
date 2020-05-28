package com.saada.flows;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;




@SpringBootApplication
@EnableAsync
public class App extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication. run(App.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // executor.setCorePoolSize(8); // create 10 Threads at the time of initialization
        // executor.setQueueCapacity(8); // queue capacity
        // executor.setMaxPoolSize(4); // if queue is full, then it will create new thread and go till 25
        executor.setThreadNamePrefix("MSGBIRD-");
        // executor.initialize();
        return executor;
    }



}
