package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "billing_first_name")
    private WebElement billingFirstName;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "billing_first_name")
    private WebElement firstNameInput;

    @FindBy(id = "billing_last_name")
    private WebElement lastNameInput;

    @FindBy(id = "billing_address_1")
    private WebElement addressInput;

    @FindBy(id = "billing_city")
    private WebElement cityInput;

    @FindBy(id = "billing_state")
    private WebElement stateInput;

    @FindBy(id = "billing_postcode")
    private WebElement postcodeInput;

    @FindBy(id = "billing_phone")
    private WebElement phoneInput;

    @FindBy(id = "billing_email")
    private WebElement emailInput;

    @FindBy(id = "order_comments")
    private WebElement commentInput;

    @FindBy(css = "input[name='order_date']")
    private WebElement dateInput;

    @FindBy(id = "payment_method_cod")
    private WebElement payCashOption;

    @FindBy(id = "place_order")
    private WebElement placeOrderButton;

    @FindBy(css = ".woocommerce-thankyou-order-received")
    private WebElement successOrderMessage;

    @FindBy(id = "terms")
    private WebElement termsCheckbox;

    // Установка даты
    public void setPickupDate(String date) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));",
                dateInput, date);
    }

    public String getPickupDate() {
        return dateInput.getAttribute("value");
    }

    // Заполнение формы
    public void fillForm(String firstName, String lastName, String address,
                         String city, String state, String postcode, String phone, String email) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);

        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);

        addressInput.clear();
        addressInput.sendKeys(address);

        cityInput.clear();
        cityInput.sendKeys(city);

        stateInput.clear();
        stateInput.sendKeys(state);

        postcodeInput.clear();
        postcodeInput.sendKeys(postcode);

        phoneInput.clear();
        phoneInput.sendKeys(phone);

        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void selectCashPayment() {
        payCashOption.click();
    }

    public void placeOrder() {
        placeOrderButton.click();
    }

    public boolean isOrderSuccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".woocommerce-thankyou-order-received")
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPaymentFormVisible() {
        return wait.until(ExpectedConditions.visibilityOf(billingFirstName)).isDisplayed();
    }

    public void acceptTerms() {
        if (!termsCheckbox.isSelected()) {
            termsCheckbox.click();
        }
    }
}
