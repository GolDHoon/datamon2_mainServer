package com.datamon.datamon2;

import com.datamon.datamon2.common.CommonCodeCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Datamon2BackendApplication {

    public static void main(String[] args) {
        System.out.println("테스트" + CommonCodeCache.getSystemIdIdx());
        SpringApplication.run(Datamon2BackendApplication.class, args);
    }

}
