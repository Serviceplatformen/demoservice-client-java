package dk.serviceplatformen.demoservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private ApplicationRunner applicationRunner;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        System.exit(0);
    }

    @Override
    public void run(String... args) {
        Scanner reader = new Scanner(System.in);

        while (true) {
            System.out.println("Press '1' to run InvocationContext test");
            System.out.println("Press '2' to run AuthorityContext test");
            System.out.println("Press 'q' to exit");
            String input = reader.nextLine();
            if("1".equals(input)) {
                String message = getTestMessage(reader);
                applicationRunner.callWithInvocationContext(message);
            } else if ("2".equals(input)) {
                String message = getTestMessage(reader);
                applicationRunner.callWithAuthorityContext(message);
            } else if ("q".equals(input)) {
                return;
            }
        }
    }

    private String getTestMessage(Scanner reader) {
        System.out.println("Enter test message");
        return reader.nextLine();
    }
}
