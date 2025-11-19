package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PizzaPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PizzaPageTests {
    private WebDriver driver;
    private PizzaPage pizzaPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://pizzeria.skillbox.cc/product-category/menu/pizza/");
        pizzaPage = new PizzaPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testSortByPriceAscending() {
        pizzaPage.selectSortOption("По возрастанию цены");
        List<Double> prices = pizzaPage.getPizzaPrices();
        System.out.println("Found prices: " + prices);

        Assertions.assertFalse(prices.isEmpty(), "Failed to find prices on the page");

        for (int i = 1; i < prices.size(); i++) {
            Assertions.assertTrue(prices.get(i) >= prices.get(i - 1),
                    "Error: pizzas are not sorted by ascending price");
        }
    }

    @Test
    public void testFilterByPrice() {
        int min = 450;
        int max = 500;

        pizzaPage.applyPriceFilter(min, max);
        List<Double> prices = pizzaPage.getPizzaPrices();

        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        System.out.println("List of pizza prices by increase after filtering: " + sortedPrices);

        Assertions.assertFalse(prices.isEmpty(), "Prices were not found after filtering");

        for (Double price : prices) {
            Assertions.assertTrue(price >= min && price <= max,
                    "Found a pizza with a price outside the expected range: " + price);
        }
    }

    @Test
    public void addFirstPizzaToCart() {
        List<WebElement> buttons = pizzaPage.getAddToCartButtons();
        Assertions.assertFalse(buttons.isEmpty(), "'Add to Cart' button is not found");

        WebElement firstButton = buttons.get(0);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstButton);
        firstButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement moreButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("a.added_to_cart.wc-forward")));

        Assertions.assertEquals("ПОДРОБНЕЕ", moreButton.getText(),
                "The 'Add to Cart' button did not update after adding the item");
    }
}

