package com.readmate.buddle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BuddleApplication {

  public static void main(String[] args) {
    SpringApplication.run(BuddleApplication.class, args);
  }
}
