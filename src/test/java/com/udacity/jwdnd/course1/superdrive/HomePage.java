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
    
    @FindBy(css = "#noteTable tbody tr:last-of-type button")
    private WebElement lastNoteEditButton;
    
    @FindBy(css = "#noteTable tbody tr:last-of-type a")
    private WebElement lastNoteDeleteButton;

    @FindBy(css = "#noteTable tbody tr:last-of-type th.note-title")
    private WebElement lastNoteTitle;

    @FindBy(css = "#noteTable tbody tr:last-of-type td.note-desc")
    private WebElement lastNoteDescription;

    @FindBy(id = "note-title")
    private WebElement noteFormTitle;
    
    @FindBy(id = "note-description")
    private WebElement noteFormDescription;
    
    @FindBy(id = "saveNoteButton")
    private WebElement saveNoteButton;
    
    @FindBy(id = "saveCredentialButton")
    private WebElement saveCredentialButton;
    
    WebDriverWait wait;
    
    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 5);
    }
    
    public void selectFilesTab() {
        this.filesTab.click();
    }
    
    public void selectNotesTab() {
        WebElement notesTabClickable = wait.until(ExpectedConditions.elementToBeClickable(notesTab));
        notesTabClickable.click();
    }
    
    public void selectCredentialsTab() {
        this.credentialsTab.click();
    }
    
    public void newNoteButton() {
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
    
    public void addNote(WebDriver driver, String title, String description) throws InterruptedException {
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
    
    public void logout() {
        WebElement logoutButtonClickable = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButtonClickable.click();
    }
}
