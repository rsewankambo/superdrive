package com.udacity.jwdnd.course1.superdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement inputUsername;
    
    @FindBy(id = "inputPassword")
    private WebElement inputPassword;
    
    @FindBy(css = "button[type=submit]")
    private WebElement submitButton;
    
    @FindBy(className = "alert")
    private WebElement alert;
    
    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
    
    public void loginUser(String inputUsername, String inputPassword) {
        this.inputUsername.sendKeys(inputUsername);
        this.inputPassword.sendKeys(inputPassword);
        this.submitButton.click();
    }
    
    public String getAlertText() {
        return alert.getText();
    }
}
