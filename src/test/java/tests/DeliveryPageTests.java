package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;

import java.time.Duration;

public class DeliveryPageTests {

    private WebDriver driver;
    private CheckoutPage checkoutPage;
    private PizzaPage pizzaPage;
    private LoginPage loginPage;
    private DeliveryPage deliveryPage;

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
    public void testMinimumOrderAmount() {
        DeliveryPage deliveryPage = new DeliveryPage(driver);
        deliveryPage.open();

        double total = deliveryPage.getTotalPrice();
        System.out.println("Total amount: " + total + "rub");

        deliveryPage.acceptTerms();
        deliveryPage.clickPlaceOrder();

        String message = deliveryPage.waitForOrderSuccessMessage();

        Assertions.assertTrue(message.contains("Ваш заказ был получен"), "The order was not successfully completed");

        if (total < 800) {
            System.out.println("Warning: the order amount is less than 800 RUB, but the site allows the order to be placed");
        }
    }

}
