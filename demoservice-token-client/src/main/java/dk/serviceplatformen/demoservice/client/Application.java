package dk.serviceplatformen.demoservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private ApplicationRunner applicationRunner;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.exit(0);
    }

    @Override
    public void run(String... args) {
        final Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.println("Enter test message or 'q' for exit");
            final String message = reader.nextLine();
            if ("q".equals(message)) {
                return;
            } else {
                applicationRunner.callWithToken(message);
            }
        }
    }
}