package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;
import java.time.Duration;

public class CartPageTests {
    private WebDriver driver;
    private CartPage cartPage;
    private LoginPage loginPage;
    private CheckoutPage checkoutPage;
    private PizzaPage pizzaPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        driver.get("https://pizzeria.skillbox.cc/product-category/menu/pizza/");
        pizzaPage = new PizzaPage(driver);
        pizzaPage.addFirstPizzaToCart();

        driver.get("https://pizzeria.skillbox.cc/cart/");
        cartPage = new CartPage(driver);
        loginPage = new LoginPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }


    @Test
    public void testChangeProductQuantity() {
        Assertions.assertFalse(cartPage.getQuantityInputsList().isEmpty(),
                "The cart is empty — the item was not added");

        int initialQty = cartPage.getQuantity(0);
        double initialTotal = cartPage.getTotalAmount();

        cartPage.increaseQuantityAndWaitForTotal(0, initialTotal);

        int newQty = cartPage.getQuantity(0);
        double newTotal = cartPage.getTotalAmount();

        Assertions.assertEquals(initialQty + 1, newQty, "The quantity didn’t increase");
        Assertions.assertTrue(newTotal > initialTotal, "The total sum didn't increase");
    }

    @Test
    public void testCartTotalUpdates() {
        double totalBefore = cartPage.getTotalAmount();
        cartPage.increaseQuantityAndWaitForTotal(0, totalBefore);

        double totalAfter = cartPage.getTotalAmount();

        Assertions.assertTrue(totalAfter > totalBefore, "Failed to update the amount after the cart change");
    }

    @Test
    public void testCheckoutForAuthorizedUser() {
        loginPage.login("Tanya", "345345Ta");

        driver.get("https://pizzeria.skillbox.cc/product-category/menu/pizza/");
        pizzaPage.addFirstPizzaToCart();

        driver.get("https://pizzeria.skillbox.cc/cart/");

        Assertions.assertTrue(cartPage.isCheckoutButtonEnabled(),
                "The 'Proceed to Payment' button is unavailable for the authorized user");

        cartPage.clickCheckoutButton();
        Assertions.assertTrue(checkoutPage.isPaymentFormVisible(),
                "The payment form does not appear after navigation");
    }

    @Test
    public void testApplyPromoCode() {
        double totalBefore = cartPage.getTotalAmount();
        cartPage.applyPromoCode("GIVEMEHALYAVA");
        System.out.println("Total before applying promo code: " + totalBefore);

        cartPage.decreaseQuantityAndWaitForTotal(0, totalBefore);

        Assertions.assertTrue(cartPage.isPromoApplied(), "The promo code was not applied");

        double totalAfter = cartPage.getTotalAmount();
        System.out.println("Total after applying promo code: " + totalAfter);

        Assertions.assertTrue(totalAfter < totalBefore, "The total amount did not decrease after applying the promo code");

        cartPage.removePromoCode();
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
