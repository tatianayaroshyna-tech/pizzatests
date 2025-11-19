package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "td.product-name a")
    private List<WebElement> productNames;

    @FindBy(css = "input.qty")
    private List<WebElement> quantityInputs;

    @FindBy(css = "button[name='update_cart']")
    private WebElement updateCartButton;

    @FindBy(css = "span.woocommerce-Price-amount bdi")
    private List<WebElement> priceElements;

    @FindBy(css = ".cart-subtotal .woocommerce-Price-amount bdi")
    private WebElement subtotalAmount;

    @FindBy(css = ".checkout-button")
    private WebElement checkoutButton;

    @FindBy(css = "input#coupon_code")
    private WebElement promoCodeInput;

    @FindBy(css = "button[name='apply_coupon']")
    private WebElement applyCouponButton;

    @FindBy(css = ".woocommerce-message")
    private WebElement promoMessage;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void increaseQuantity(int index) {
        List<WebElement> qtys = getQuantityInputsList();
        if (qtys.size() <= index) {
            throw new IllegalStateException("No quantity input to increase at index " + index);
        }
        WebElement qtyInput = qtys.get(index);
        int currentValue = Integer.parseInt(Objects.requireNonNull(qtyInput.getAttribute("value")));
        qtyInput.clear();
        qtyInput.sendKeys(String.valueOf(currentValue + 1));
        updateCart();
    }

    public void increaseQuantityAndWaitForTotal(int index, double oldTotal) {
        increaseQuantity(index);
        wait.until(driver -> getTotalAmount() > oldTotal);
    }

    public void decreaseQuantityAndWaitForTotal(int index, double oldTotal) {
        decreaseQuantity(index);
        wait.until(driver -> getTotalAmount() < oldTotal);
    }

    public void decreaseQuantity(int index) {
        List<WebElement> qtys = getQuantityInputsList();
        if (qtys.size() <= index) {
            throw new IllegalStateException("No quantity input to decrease at index " + index);
        }
        WebElement qtyInput = qtys.get(index);
        int currentValue = Integer.parseInt(Objects.requireNonNull(qtyInput.getAttribute("value")));
        if (currentValue > 1) {
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(currentValue - 1));
            updateCart();
        }
    }

    public void updateCart() {
        updateCartButton.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".cart-subtotal .woocommerce-Price-amount bdi"), "₽"));
    }

    public List<WebElement> getQuantityInputsList() {
        // ждём появления хотя бы одного input.qty
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.qty")));
        return driver.findElements(By.cssSelector("input.qty"));
    }

    public int getQuantity(int index) {
        List<WebElement> qtys = getQuantityInputsList();
        if (qtys.size() <= index) {
            throw new IllegalStateException("No quantity input at index " + index + ", total inputs: " + qtys.size());
        }
        return Integer.parseInt(Objects.requireNonNull(qtys.get(index).getAttribute("value")));
    }

    public double getTotalAmount() {
        WebElement totalElement = driver.findElement(By.cssSelector(".order-total .woocommerce-Price-amount"));
        String totalText = totalElement.getText().replaceAll("[^\\d,\\.]", "").replace(",", ".");
        return Double.parseDouble(totalText);
    }
    
    public void applyPromoCode(String code) {
        promoCodeInput.clear();
        promoCodeInput.sendKeys(code);
        applyCouponButton.click();
        wait.until(ExpectedConditions.visibilityOf(promoMessage));
    }

    public boolean isPromoApplied() {
        return promoMessage.isDisplayed();
    }

    public void removePromoCode() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOf(updateCartButton));
    }

    public boolean isCheckoutButtonEnabled() {
        return checkoutButton.isEnabled();
    }

    public void clickCheckoutButton() {
        checkoutButton.click();
    }
}
