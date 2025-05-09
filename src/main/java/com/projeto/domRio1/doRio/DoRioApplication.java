package com.projeto.domRio1.doRio;

import com.projeto.domRio1.doRio.controller.LoginController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javafx.application.Application;
import javafx.stage.Stage;

@Configuration
@SpringBootApplication
public class DoRioApplication extends Application {

	private static ConfigurableApplicationContext applicationContext;

	@Override
	public void init() throws Exception {
		this.applicationContext = SpringApplication.run(DoRioApplication.class);
	}

	@Override
	public void stop() throws Exception {
		applicationContext.close();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		LoginController.loadView(stage);
	}

	public static ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
