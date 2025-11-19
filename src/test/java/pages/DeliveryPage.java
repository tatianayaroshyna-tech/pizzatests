package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DeliveryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By totalPriceField = By.cssSelector("span.woocommerce-Price-amount bdi"); // сумма заказа
    private By placeOrderButton = By.id("place_order"); // кнопка оформления
    private By errorMessage = By.cssSelector(".woocommerce-error li"); // сообщение об ошибке
    private By successMessage = By.cssSelector(".woocommerce-thankyou-order-received");

    @FindBy(id = "terms")
    private WebElement termsCheckbox;


    public DeliveryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("https://pizzeria.skillbox.cc/checkout"); // страница оформления заказа
    }

    public double getTotalPrice() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(totalPriceField)).getText();

        text = text.replaceAll("[^0-9,\\.]", "").replace(",", ".");
        return Double.parseDouble(text);
    }

    public void clickPlaceOrder() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("place_order")));

        ((JavascriptExecutor) driver)
                .executeScript("document.querySelector('#place_order').click();");
    }

    public void acceptTerms() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By termsCheckbox = By.id("terms");

        wait.until(ExpectedConditions.presenceOfElementLocated(termsCheckbox));
        ((JavascriptExecutor) driver).executeScript(
                "document.querySelector('#terms').click();"
        );
    }

    public String waitForOrderSuccessMessage() {
        WebElement success = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        String text = success.getText();
        System.out.println("Сообщение после оформления: " + text);
        return text;
    }
}


