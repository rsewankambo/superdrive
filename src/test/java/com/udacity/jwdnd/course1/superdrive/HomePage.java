package com.udacity.jwdnd.course1.superdrive;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    @FindBy(css = "#logoutDiv button[type=submit]")
    private WebElement logoutButton;
    
    @FindBy(id = "nav-files-tab")
    private WebElement filesTab;
    
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;
    
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;
    
    @FindBy(css = "#nav-notes > button")
    private WebElement newNoteButton;
    
    @FindBy(css = "#nav-credentials > button")
    private WebElement newCredButton;
    
    @FindBy(id = "note-title")
    private WebElement noteFormTitle;
    
    @FindBy(id = "note-description")
    private WebElement noteFormDescription;
    
    @FindBy(id = "saveNoteButton")
    private WebElement saveNoteButton;
    
    @FindBy(css = "#noteTable tbody tr:last-of-type button")
    private WebElement lastNoteEditButton;
    
    @FindBy(css = "#noteTable tbody tr:last-of-type a")
    private WebElement lastNoteDeleteButton;

    @FindBy(css = "#noteTable tbody tr:last-of-type th.note-title")
    private WebElement lastNoteTitle;

    @FindBy(css = "#noteTable tbody tr:last-of-type td.note-desc")
    private WebElement lastNoteDescription;
    
    @FindBy(id = "credential-url")
    private WebElement credentialFormUrl;
    
    @FindBy(id = "credential-username")
    private WebElement credentialFormUsername;
    
    @FindBy(id = "credential-password")
    private WebElement credentialFormPassword;
    
    @FindBy(css = "#credentialTable tbody tr:last-of-type button")
    private WebElement lastCredEditButton;
    
    @FindBy(css = "#credentialTable tbody tr:last-of-type a")
    private WebElement lastCredDeleteButton;
    
    @FindBy(css = "#credentialTable tbody tr:last-of-type th.cred-url")
    private WebElement lastCredentialUrl;
    
    @FindBy(css = "#credentialTable tbody tr:last-of-type td.cred-un")
    private WebElement lastCredentialUsername;
    
    @FindBy(css = "#credentialTable tbody tr:last-of-type td.cred-pwd")
    private WebElement lastCredentialPassword;
    
    @FindBy(id = "saveCredentialButton")
    private WebElement saveCredentialButton;
    
    WebDriverWait wait;
    
    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 5);
    }
    
    public void selectNotesTab() {
        WebElement notesTabClickable = wait.until(ExpectedConditions.elementToBeClickable(notesTab));
        notesTabClickable.click();
    }
    
    public void selectCredentialsTab() {
        this.credentialsTab.click();
    }
    
    public void newNoteButtonClick() {
        WebElement newNoteButtonVisible = wait.until(ExpectedConditions.visibilityOf(newNoteButton));
        newNoteButtonVisible.click();
    }
    
    public void lastNoteEditButtonClick() {
        WebElement lastNoteEditButtonVisible = wait.until(ExpectedConditions.visibilityOf(lastNoteEditButton));
        lastNoteEditButtonVisible.click();
    }
    
    public void lastNoteDeleteButtonClick() {
        WebElement lastNoteDeleteButtonVisible = wait.until(ExpectedConditions.visibilityOf(lastNoteDeleteButton));
        lastNoteDeleteButtonVisible.click();
    }
    
    public void addNote(String title, String description) {
        WebElement noteFormTitleVisible = wait.until(ExpectedConditions.visibilityOf(noteFormTitle));
        if (title != null) {
            noteFormTitleVisible.clear();
            noteFormTitleVisible.sendKeys(title);
        }
        if (description != null) {
            this.noteFormDescription.clear();
            this.noteFormDescription.sendKeys(description);
        }
        this.saveNoteButton.click();
    }
    
    public String getLastNoteTitle() {
        return this.lastNoteTitle.getText();
    }
    
    public String getLastNoteDescription() {
        return this.lastNoteDescription.getText();
    }
    
    public void newCredentialButtonClick() {
        WebElement newCredButtonVisible = wait.until(ExpectedConditions.visibilityOf(newCredButton));
        newCredButtonVisible.click();
    }
    
    public void lastCredentialEditButtonClick() {
        WebElement lastCredEditButtonVisible = wait.until(ExpectedConditions.visibilityOf(lastCredEditButton));
        lastCredEditButtonVisible.click();
    }
    
    public void lastCredentialDeleteButtonClick() {
        WebElement lastCredDeleteButtonVisible = wait.until(ExpectedConditions.visibilityOf(lastCredDeleteButton));
        lastCredDeleteButtonVisible.click();
    }
    
    public void addCredential(String url, String username, String password) {
        WebElement credentialFormUrlVisible = wait.until(ExpectedConditions.visibilityOf(credentialFormUrl));
        if (url != null) {
            credentialFormUrlVisible.clear();
            credentialFormUrlVisible.sendKeys(url);
        }
        if (username != null) {
            this.credentialFormUsername.clear();
            this.credentialFormUsername.sendKeys(username);
        }
        if (password != null) {
            this.credentialFormPassword.clear();
            this.credentialFormPassword.sendKeys(password);
        }
        this.saveCredentialButton.click();
    }
    
    public String getLastCredentialUrl() {
        return this.lastCredentialUrl.getText();
    }
    
    public String getLastCredentialUsername() {
        return this.lastCredentialUsername.getText();
    }
    
    public String getLastCredentialPassword() {
        return this.lastCredentialPassword.getText();
    }
    
    public void logout() {
        WebElement logoutButtonClickable = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButtonClickable.click();
    }
}
