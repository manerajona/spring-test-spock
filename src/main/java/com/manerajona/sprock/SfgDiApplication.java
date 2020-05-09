package com.manerajona.sprock;

import com.manerajona.sprock.controllers.MyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SfgDiApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SfgDiApplication.class, args);
		MyController controller = (MyController) ctx.getBean("myController");
		String greetings = controller.sayHello();
		System.out.println(greetings);
	}

}
