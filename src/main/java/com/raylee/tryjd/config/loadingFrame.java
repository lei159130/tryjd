package com.raylee.tryjd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.raylee.tryjd.frame.CookiesJFrame;

@Configuration
@PropertySource("classpath:/config.properties")
public class loadingFrame implements CommandLineRunner {

	@Autowired
	CookiesJFrame cookiesJFrame;

	@Override
	public void run(String... args) throws Exception {
		cookiesJFrame.init();
	}

}
