/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.lomtech.helloworld.lti.HelloWorldLtsApplication
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 */
package net.lomtech.lti;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LtiApplication {

    public static void main(String[] args) {
        // Load .env
        Dotenv dotenv = Dotenv.load();

        // Pass them as system properties (Spring will resolve ${} from here)
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
        SpringApplication.run(LtiApplication.class, (String[])args);
    }
}

