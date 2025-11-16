package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.MenuPage;

import java.time.Duration;

public class NavigationMenuTests {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://pizzeria.skillbox.cc/");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testNavigationMenu() {
        MenuPage menuPage = new MenuPage(driver);

        menuPage.openPizza();
        Assertions.assertTrue(driver.getCurrentUrl().contains("/product-category/menu/pizza/"),
                "Switching to Pizza section did not occur");

        menuPage.openDesert();
        Assertions.assertTrue(driver.getCurrentUrl().contains("/product-category/menu/deserts/"),
                "Switching to Deserts section did not occur");

        menuPage.openDrinks();
        Assertions.assertTrue(driver.getCurrentUrl().contains("/product-category/menu/drinks/"),
                "Switching to Drinks section did not occur");
    }

}

