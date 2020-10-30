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

import static org.junit.jupiter.api.Assertions.*;

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
		signupPage.signupUser(driver, "first1", "last1", "user32", "password1");
		assertEquals("You successfully signed up!", loginPage.getAlertText());

		loginPage.loginUser("user32", "password1");
		assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		homePage.logout();
		assertEquals("You have been logged out", loginPage.getAlertText());

		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());
	}
	
	@Test
	public void testCreateNewNote() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		HomePage homePage = new HomePage(driver);
		homePage.selectNotesTab();
		homePage.newNoteButtonClick();
		homePage.addNote("test new title", "test new description");
		
		ResultPage resultPage = new ResultPage(driver);
		assertTrue(resultPage.getResultMessage().contains("Your note was successfully saved"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String lastNoteTitle = homePage.getLastNoteTitle();
		assertEquals("test new title", lastNoteTitle);
		String lastNoteDescription = homePage.getLastNoteDescription();
		assertEquals("test new description", lastNoteDescription);
	}
	
	@Test
	public void testEditExistingNote() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		HomePage homePage = new HomePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		
		populateNotes(homePage, resultPage, "test title 1", "test description 1");
		populateNotes(homePage, resultPage, "test title 2", "test description 2");
		
		homePage.logout();
		loginHelper();
		
		// note title edit test
		String titleBeforeUpdate = homePage.getLastNoteTitle();
		String titleAfterUpdate = "test title 2 update";
		homePage.lastNoteEditButtonClick();
		homePage.addNote(titleAfterUpdate, null);
		
		assertTrue(resultPage.getResultMessage().contains("Your note was updated"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String currentTitle = homePage.getLastNoteTitle();
		assertNotEquals(titleBeforeUpdate, currentTitle);
		assertEquals(titleAfterUpdate, currentTitle);
		
		// note description edit test
		String descBeforeUpdate = homePage.getLastNoteDescription();
		String descAfterUpdate = "test description 2 update";
		homePage.lastNoteEditButtonClick();
		homePage.addNote( null, descAfterUpdate);
		
		assertTrue(resultPage.getResultMessage().contains("Your note was updated"));
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
		
		HomePage homePage = new HomePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		
		populateNotes(homePage, resultPage, "test title 3", "test description 3");
		populateNotes(homePage, resultPage, "test title 4", "test description 4");
		
		homePage.logout();
		loginHelper();
		
		String lastNoteTitleBefore = homePage.getLastNoteTitle();
		String lastNoteDescBefore = homePage.getLastNoteDescription();
		homePage.lastNoteDeleteButtonClick();
		assertTrue(resultPage.getResultMessage().contains("Your note was successfully deleted"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String lastNoteTitleAfter = homePage.getLastNoteTitle();
		String lastNoteDescAfter = homePage.getLastNoteDescription();
		assertNotEquals(lastNoteTitleBefore, lastNoteTitleAfter);
		assertNotEquals(lastNoteDescBefore, lastNoteDescAfter);
	}
	
	@Test
	public void testCreateNewCredential() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		HomePage homePage = new HomePage(driver);
		homePage.selectCredentialsTab();
		homePage.newCredentialButtonClick();
		homePage.addCredential("test new url", "test new username", "test new password");
		
		ResultPage resultPage = new ResultPage(driver);
		assertTrue(resultPage.getResultMessage().contains("Your credential was successfully saved"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String lastCredentialUrl = homePage.getLastCredentialUrl();
		assertEquals("test new url", lastCredentialUrl);
		String lastCredentialUsername = homePage.getLastCredentialUsername();
		assertEquals("test new username", lastCredentialUsername);
		String lastCredentialPassword = homePage.getLastCredentialPassword();
		assertNotEquals("test new password", lastCredentialPassword);
	}
	
	@Test
	public void testEditExistingCredential() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		HomePage homePage = new HomePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		
		populateCredentials(homePage, resultPage, "www.testUrl1.com", "testUsername1", "testPassword1");
		populateCredentials(homePage, resultPage, "www.testUrl2.com", "testUsername2", "testPassword2");
		
		homePage.logout();
		loginHelper();
		
		// credential URL edit test
		String urlBeforeUpdate = homePage.getLastCredentialUrl();
		String urlAfterUpdate = "www.testUrl2Update.com";
		homePage.lastCredentialEditButtonClick();
		homePage.addCredential(urlAfterUpdate, null, null);
		
		assertTrue(resultPage.getResultMessage().contains("Your credential was updated"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String currentUrl = homePage.getLastCredentialUrl();
		assertNotEquals(urlBeforeUpdate, currentUrl);
		assertEquals(urlAfterUpdate, currentUrl);
		
		// credential username edit test
		String usernameBeforeUpdate = homePage.getLastCredentialUsername();
		String usernameAfterUpdate = "testUsername2Update";
		homePage.lastCredentialEditButtonClick();
		homePage.addCredential(null, usernameAfterUpdate, null);
		
		assertTrue(resultPage.getResultMessage().contains("Your credential was updated"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String currentUsername = homePage.getLastCredentialUsername();
		assertNotEquals(usernameBeforeUpdate, currentUsername);
		assertEquals(usernameAfterUpdate, currentUsername);
		
		// credential password edit test
		String passwordBeforeUpdate = homePage.getLastCredentialPassword();
		String passwordAfterUpdate = "testPassword2Update";
		homePage.lastCredentialEditButtonClick();
		homePage.addCredential(null, null, passwordAfterUpdate);
		
		assertTrue(resultPage.getResultMessage().contains("Your credential was updated"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		passwordAfterUpdate = homePage.getLastCredentialPassword();
		assertNotEquals(passwordBeforeUpdate, passwordAfterUpdate);
	}
	
	@Test
	public void testDeleteExistingCredential() throws InterruptedException {
		signupHelper();
		loginHelper();
		
		HomePage homePage = new HomePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		
		populateCredentials(homePage, resultPage, "test url 3", "test username 3", "test password 3");
		populateCredentials(homePage, resultPage, "test url 4", "test username 4", "test password 3");
		
		homePage.logout();
		loginHelper();
		
		String lastCredUrlBefore = homePage.getLastCredentialUrl();
		String lastCredUsernameBefore = homePage.getLastCredentialUsername();
		String lastCredPasswordBefore = homePage.getLastCredentialUsername();
		homePage.lastCredentialDeleteButtonClick();
		assertTrue(resultPage.getResultMessage().contains("Your credential was successfully deleted"));
		resultPage.moveOn();
		
		Thread.sleep(1000);
		String lastCredUrlAfter = homePage.getLastCredentialUrl();
		String lastCredUsernameAfter = homePage.getLastCredentialUsername();
		String lastCredPasswordAfter = homePage.getLastCredentialUsername();
		assertNotEquals(lastCredUrlBefore, lastCredUrlAfter);
		assertNotEquals(lastCredUsernameBefore, lastCredUsernameAfter);
		assertNotEquals(lastCredPasswordBefore, lastCredPasswordAfter);
	}
	
	private void signupHelper() {
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signupUser(driver,"first2", "last2", "user2", "password2");
	}
	
	private void loginHelper() {
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser("user2", "password2");
	}
	
	private void populateNotes(HomePage homePage, ResultPage resultPage, String title, String description) throws InterruptedException {
		homePage.selectNotesTab();
		homePage.newNoteButtonClick();
		homePage.addNote(title, description);
		Thread.sleep(1000);
		resultPage.moveOn();
	}
	
	private void populateCredentials(HomePage homePage, ResultPage resultPage, String url, String username, String password) throws InterruptedException {
		homePage.selectCredentialsTab();
		homePage.newCredentialButtonClick();
		homePage.addCredential(url, username, password);
		Thread.sleep(1000);
		resultPage.moveOn();
	}
}
