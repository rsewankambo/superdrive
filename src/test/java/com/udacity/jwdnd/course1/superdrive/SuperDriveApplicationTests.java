package com.udacity.jwdnd.course1.superdrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SuperDriveApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}
	
	@Test
	public void testUnAuthorizedAccess() {
		driver.get(baseURL + "/signup");
		assertEquals("Sign Up", driver.getTitle());

		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());

		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUserSignup() throws InterruptedException {
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		signupPage.signupUser("first1", "last1", "user10", "password1");
		assertEquals("You successfully signed up!", loginPage.getAlertText());

		Thread.sleep(1000);
		loginPage.loginUser("user10", "password1");
		Thread.sleep(1000);
		assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		homePage.logout();
		Thread.sleep(1000);
		assertEquals("You have been logged out", loginPage.getAlertText());

		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());
	}
}
