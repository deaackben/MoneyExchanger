package com.example.demo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
public class DemoApplicationTests {

	@BeforeClass
	public static void setUpTests() {
		System.setProperty("log4j.xml","src/test/log4j.xml");
	}

	@Before
	public void testThirdPartyAPI() throws IOException {
		String url = PropertyReader.getProperty(Controller.getAPI());
		System.out.println("Third party API: " + url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		Assert.assertNotNull(con);
	}

	@Test
	public void contextLoads() throws IOException {
		try {
			String url = PropertyReader.getProperty(Controller.getAPI());
			System.out.println(url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			Assert.assertNotNull(con);
			Log4jTest.logger.info("Test passed");
		} catch (IOException e) {
			Log4jTest.logger.error("Test failed");
			e.printStackTrace();
		}
	}

	@Test
	public void get200() throws IOException {
		String url = "http://localhost:8080/";
		System.out.println(url);
		SpringApplication.run(DemoApplication.class);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		int responseCode = con.getResponseCode();
		Assert.assertEquals(responseCode, 200);
		Assert.assertNotNull(con);
	}
}
