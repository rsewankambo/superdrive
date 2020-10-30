package com.udacity.jwdnd.course1.superdrive;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultPage {
    
    @FindBy(className = "msg")
    private WebElement msg;
    
    @FindBy(className = "continue")
    private WebElement continueLink;
    
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 5);
        js = (JavascriptExecutor) driver;
    }
    
    public String getResultMessage() {
        return msg.getText();
    }
    
    public void moveOn() {
        js.executeScript("arguments[0].click();", this.continueLink);
    }
}
