package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BonusProgramPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public BonusProgramPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //private By issueCardButton = By.cssSelector("button.issue-bonus-card");
    private By nameField = By.id("bonus_username");
    private By phoneField = By.id("bonus_phone");
    //private By emailField = By.id("bonus-email");
    //private By agreementCheckbox = By.id("bonus-agree");
    private By submitButton = By.cssSelector(".woocommerce-form-register__submit");
    private By successMessage = By.id("bonus_main");

    public void open() {
        driver.get("https://pizzeria.skillbox.cc/bonus");
    }

    public void applyForBonusCard(String name, String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
        driver.findElement(phoneField).sendKeys(phone);
        driver.findElement(submitButton).click();
    }

    public boolean isSuccessMessageDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Ждем появления alert
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Текст алерта: " + alert.getText());
            alert.accept();

            // Ждем, пока итоговое сообщение действительно поменяется на текст о выдаче карты
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    successMessage, "карта"
            ));

            // Выводим реальный текст успешного сообщения
            String messageText = driver.findElement(successMessage).getText();
            System.out.println("Текст успешного сообщения: " + messageText);

            return true;

        } catch (TimeoutException e) {
            System.out.println("Успешное сообщение так и не появилось.");
            return false;
        }
    }

}

