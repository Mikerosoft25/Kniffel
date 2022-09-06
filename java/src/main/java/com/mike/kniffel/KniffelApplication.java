package com.mike.kniffel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class KniffelApplication {

  public static void main(final String[] args) {
    SpringApplication.run(KniffelApplication.class, args);
  }
}
