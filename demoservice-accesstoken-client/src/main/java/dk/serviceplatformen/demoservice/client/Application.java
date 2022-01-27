package dk.serviceplatformen.demoservice.client;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.SimpleFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration

public class Application implements CommandLineRunner {

	@Autowired
	private ApplicationRunner applicationRunner;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		// System.setProperty("https.protocols", "TLSv1"); 
		//System.setProperty("https.protocols", "TLSv1.2");
		final Scanner reader = new Scanner(System.in);
		while (true) {
			System.out.println("Enter test message or 'q' for exit");
			final String message = reader.nextLine();
			if ("q".equals(message)) {
				return;
			} else {
				System.out.println("Enter error message");
				final String errorMessage = reader.nextLine();
				applicationRunner.callWithToken(message,errorMessage);
			}
		}

	}


}
