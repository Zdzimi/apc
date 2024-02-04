package com.zdzimi.apc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApcApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApcApplication.class, args);
  }

}
