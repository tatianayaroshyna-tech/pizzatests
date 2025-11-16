package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import java.time.Duration;
import java.util.Objects;

public class HomePageTests extends BaseTest {

    private HomePage homePage;

    @BeforeEach
    public void initPage() {
        homePage = new HomePage(driver);
    }

    @Test
    public void testSwitchPizzaSlider() throws InterruptedException {

        String firstPizza = homePage.getActivePizzaTitle();

        WebElement sliderContainer = driver.findElement(By.cssSelector(".slick-slider"));
        Actions actions = new Actions(driver);
        actions.moveToElement(sliderContainer).perform();

        Thread.sleep(500);
        WebElement nextButton = homePage.getNextSlideButton();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextButton);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(d -> !homePage.getActivePizzaTitle().equals(firstPizza));

        String secondPizza = homePage.getActivePizzaTitle();
        Assertions.assertNotEquals(firstPizza, secondPizza,
                "Pizza does not switch when the right arrow is pressed");

        actions.moveToElement(sliderContainer).perform();
        Thread.sleep(500);

        WebElement prevButton = homePage.getPrevSlideButton();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", prevButton);

        wait.until(d -> homePage.getActivePizzaTitle().equals(firstPizza));

        String thirdPizza = homePage.getActivePizzaTitle();
        Assertions.assertEquals(firstPizza, thirdPizza,
                "Pizza did not return when pressing the left arrow");
    }

    @Test
    public void testHoverOnDrinkShowsAddToCart() {
        homePage.hoverOnDrinkAndWaitForAddToCart();
        Assertions.assertTrue(homePage.getAddToCartButtonForDrink().isDisplayed());
    }

    @Test
    public void testDessertImageOpensDessertPage() {
        homePage.clickDessertImageAndWaitForPageLoad("/product/");
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("/product/"));
    }

    @Test
    public void testScrollUpButtonAppears() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Assertions.assertTrue(homePage.isScrollUpButtonVisibleWithWait());
    }

    @Test
    public void testSocialLinksOpenCorrectly() {
        for (WebElement link : homePage.getSocialLinksWithWait()) {
            homePage.scrollToElement(link);
            String href = link.getAttribute("href");
            Assertions.assertNotNull(href);

            driver.get(href);
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !Objects.equals(d.getCurrentUrl(), "https://pizzeria.skillbox.cc/"));

            String currentUrl = driver.getCurrentUrl();
            Assertions.assertTrue(
                    currentUrl.contains("facebook.com") ||
                    currentUrl.contains("vk.com") ||
                    currentUrl.contains("instagram.com"));

            driver.navigate().back();
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> Objects.equals(d.getCurrentUrl(), "https://pizzeria.skillbox.cc/"));
        }
    }
}

