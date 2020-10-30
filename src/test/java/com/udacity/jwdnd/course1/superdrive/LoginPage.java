package com.udacity.jwdnd.course1.superdrive;

import org.openqa.selenium.JavascriptExecutor;
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
    
    private JavascriptExecutor js;
    
    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        js = (JavascriptExecutor) driver;
    }
    
    public void loginUser(String username, String password) {
        js.executeScript("arguments[0].value='" + username + "';", this.inputUsername);
        js.executeScript("arguments[0].value='" + password + "';", this.inputPassword);
        js.executeScript("arguments[0].click();", this.submitButton);
    }
    
    public String getAlertText() {
        return alert.getText();
    }
}
