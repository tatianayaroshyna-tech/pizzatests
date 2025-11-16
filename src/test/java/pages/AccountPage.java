package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class AccountPage {

    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(linkText = "Данные аккаунта")
    private WebElement accountDetailsTab;

    @FindBy(id = "uploadFile") // допустим, input type="file" имеет id
    private WebElement uploadInput;

    @FindBy(css = "button[name='save_account_details']")
    private WebElement saveButton;

    @FindBy(css = ".woocommerce-message")
    private WebElement successMessage;

    public AccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void openAccountDetails() {
        accountDetailsTab.click();
        //wait.until(ExpectedConditions.visibilityOf(uploadInput));
    }

    public void uploadProfilePicture(String filePath) {
        //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", uploadInput);
        uploadInput.sendKeys(filePath);
    }

    public void saveChanges() {
        // Скроллим к кнопке, чтобы она была в видимой области
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);

        // Ждем, пока кнопка станет кликабельной
        wait.until(ExpectedConditions.elementToBeClickable(saveButton));

        saveButton.click();

        // Ждем появления сообщения об успехе
        wait.until(ExpectedConditions.visibilityOf(successMessage));
    }


    public boolean isUploadSuccessful() {
        try {
            String messageText = successMessage.getText();
            System.out.println("Текст уведомления: " + messageText);

            // Проверяем, что сообщение не пустое и содержит ожидаемый текст
            return successMessage.isDisplayed()
                    && (messageText.contains("changed successfully") || messageText.contains("изменения сохранены"));
        } catch (NoSuchElementException e) {
            System.out.println("Элемент уведомления не найден на странице");
            return false;
        }
    }


}
