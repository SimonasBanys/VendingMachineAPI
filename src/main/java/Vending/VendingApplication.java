package Vending;

import Vending.entity.Machine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class VendingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendingApplication.class, args);
		System.setProperty("java.awt.headless", "false");
		new Machine();
	}
}
