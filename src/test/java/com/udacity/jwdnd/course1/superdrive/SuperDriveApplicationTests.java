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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
	
	@Test
	public void testCreateNewNote() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		homePage.selectNotesTab();
		homePage.newNoteButton();
		homePage.addNote(driver, "test create title", "test create description");
		
		ResultPage resultPage = new ResultPage(driver);
		assertEquals(true, resultPage.getResultMessage().contains("Your note was successfully saved"));
		Thread.sleep(1000);
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String lastNoteTitle = homePage.getLastNoteTitle();
		assertEquals("test create title", lastNoteTitle);
		String lastNoteDescription = homePage.getLastNoteDescription();
		assertEquals("test create description", lastNoteDescription);
	}
	
	@Test
	public void testEditExistingNote() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		
		populateNotes(homePage, resultPage, "test title 1", "test edit description 1");
		Thread.sleep(1000);
		populateNotes(homePage, resultPage, "test title 2", "test edit description 2");
		
		homePage.logout();
		loginHelper();
		Thread.sleep(1000);
		
		// note title edit test
		Thread.sleep(1000);
		String titleBeforeUpdate = homePage.getLastNoteTitle();
		String titleAfterUpdate = "test title 2 update";
		homePage.lastNoteEditButtonClick();
		homePage.addNote(driver, titleAfterUpdate, null);
		
		assertEquals(true, resultPage.getResultMessage().contains("Your note was updated"));
		Thread.sleep(1000);
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String currentTitle = homePage.getLastNoteTitle();
		assertNotEquals(titleBeforeUpdate, currentTitle);
		assertEquals(titleAfterUpdate, currentTitle);
		
		// note description edit test
		String descBeforeUpdate = homePage.getLastNoteDescription();
		String descAfterUpdate = "test edit description 2 update";
		homePage.lastNoteEditButtonClick();
		homePage.addNote(driver, null, descAfterUpdate);
		
		assertEquals(true, resultPage.getResultMessage().contains("Your note was updated"));
		Thread.sleep(1000);
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String currentDesc = homePage.getLastNoteDescription();
		assertNotEquals(descBeforeUpdate, currentDesc);
		assertEquals(descAfterUpdate, currentDesc);
	}
	
	@Test
	public void testDeleteExistingNote() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		
		populateNotes(homePage, resultPage, "test title 3", "test edit description 3");
		Thread.sleep(1000);
		populateNotes(homePage, resultPage, "test title 4", "test edit description 4");
		
		homePage.logout();
		loginHelper();
		Thread.sleep(1000);
		
		String lastNoteTitleBefore = homePage.getLastNoteTitle();
		String lastNoteDescBefore = homePage.getLastNoteDescription();
		homePage.lastNoteDeleteButtonClick();
		assertEquals(true,resultPage.getResultMessage().contains("Your note was successfully deleted"));
		Thread.sleep(1000);
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String lastNoteTitleAfter = homePage.getLastNoteTitle();
		String lastNoteDescAfter = homePage.getLastNoteDescription();
		assertNotEquals(lastNoteTitleBefore, lastNoteTitleAfter);
		assertNotEquals(lastNoteDescBefore, lastNoteDescAfter);
	}
	
	private void signupHelper() {
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signupUser("first2", "last2", "user2", "password2");
	}
	
	private void loginHelper() {
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser("user2", "password2");
	}
	
	private void populateNotes(HomePage homePage, ResultPage resultPage, String noteTitle, String noteDescription) throws InterruptedException {
		homePage.selectNotesTab();
		homePage.newNoteButton();
		homePage.addNote(driver, noteTitle, noteDescription);
		Thread.sleep(1000);
		resultPage.moveOn();
	}
}
