package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PizzaPage {
    private WebDriver driver;
    private PizzaPage pizzaPage;

    public PizzaPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "select.orderby")
    private WebElement sortDropdown;

    @FindBy(css = "span.price bdi")
    private List<WebElement> pizzaPrices;

    @FindBy(css = ".price_slider_amount button")
    private WebElement filterButton;

    @FindBy(css = "a.add_to_cart_button")
    private List<WebElement> addToCartButtons;


    public void selectSortOption(String optionText) {
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(optionText);

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfAllElements(pizzaPrices)); // ждём обновления списка

    }

    public List<Double> getPizzaPrices() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.price bdi")));

        List<WebElement> priceElements = driver.findElements(By.cssSelector("span.price bdi"));
        List<Double> prices = new ArrayList<>();

        for (WebElement priceEl : priceElements) {
            String text = priceEl.getText().replace("₽", "").trim().replace(",", ".");
            prices.add(Double.parseDouble(text));
        }

        return prices;
    }

    public void applyPriceFilter(int min, int max) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement minInput = driver.findElement(By.cssSelector(".price_slider_amount input#min_price"));
        WebElement maxInput = driver.findElement(By.cssSelector(".price_slider_amount input#max_price"));
        WebElement filterButton = driver.findElement(By.cssSelector("button.button"));

        js.executeScript("arguments[0].value = arguments[1];", minInput, String.valueOf(min));
        js.executeScript("arguments[0].value = arguments[1];", maxInput, String.valueOf(max));

        filterButton.click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("span.price bdi"), 0));
        pizzaPrices = driver.findElements(By.cssSelector("span.price bdi"));
    }

    public List<WebElement> getAddToCartButtons() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("a.add_to_cart_button")));
        return driver.findElements(By.cssSelector("a.add_to_cart_button"));
    }

    public void addFirstPizzaToCart() {
        List<WebElement> buttons = getAddToCartButtons();
        if (buttons.isEmpty()) {
            throw new IllegalStateException("Кнопки 'В корзину' не найдены на странице");
        }

        WebElement firstButton = buttons.get(0);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstButton);
        firstButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("a.added_to_cart.wc-forward")));
    }
}

