package org.magcube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    var context = SpringApplication.run(Application.class, args);
    var gameClient = context.getBean(GameWebClient.class);
    // We need to block for the content here or the JVM might exit before the message is logged
    System.out.println(">> message = " + gameClient.getMessage().block());
  }
}