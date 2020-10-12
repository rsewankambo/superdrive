package com.udacity.jwdnd.course1.superdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResultPage {
    
    @FindBy(className = "msg")
    private WebElement msg;
    
    @FindBy(className = "continue")
    private WebElement continueLink;
    
    
    WebDriverWait wait;
    
    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 5);
    }
    
    public String getResultMessage() {
        return msg.getText();
    }
    
    public void moveOn() {
        WebElement continueLinkClickable = wait.until(ExpectedConditions.elementToBeClickable(continueLink));
        continueLinkClickable.click();
    }
}
