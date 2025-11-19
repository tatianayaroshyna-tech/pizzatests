package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.PizzaPage;

import java.time.Duration;

public class CheckoutPageTests {

    private WebDriver driver;
    private CheckoutPage checkoutPage;
    private PizzaPage pizzaPage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().window().maximize();

        // Авторизуемся на сайте
        loginPage = new LoginPage(driver);
        loginPage.login("Tanya", "345345Ta");

        // Переходим на страницу пиццы
        driver.get("https://pizzeria.skillbox.cc/product-category/menu/pizza/");
        pizzaPage = new PizzaPage(driver);

        // Добавляем первую пиццу в корзину
        pizzaPage.addFirstPizzaToCart();

        driver.get("https://pizzeria.skillbox.cc/checkout/");
        checkoutPage = new CheckoutPage(driver);
    }

    @Test
    public void testSetOrderDate() {
        String date = "2025-12-31";
        driver.get("https://pizzeria.skillbox.cc/checkout/");
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.setPickupDate(date);

        Assertions.assertEquals(date, checkoutPage.getPickupDate(),
                "Failed to set the correct date in the field");
    }

    @Test
    public void testPlaceOrderWithCash() {
        String date = "2025-12-31";
        driver.get("https://pizzeria.skillbox.cc/checkout/");
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        checkoutPage.fillForm(
                "Tatyana", "Yaroshyna", "ul. Lenina, 1",
                "Minsk", "Minskaya", "1526556", "89998887766", "test@example.com"
        );

        checkoutPage.setPickupDate(date);
        checkoutPage.selectCashPayment();
        checkoutPage.acceptTerms();
        checkoutPage.placeOrder();

        Assertions.assertTrue(checkoutPage.isOrderSuccess(),
                "The success message for the order is not displayed");
        Assertions.assertTrue(driver.getCurrentUrl().contains("order-received"));
    }
}
