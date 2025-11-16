package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {

    @FindBy(css = ".slick-next")
    private WebElement nextSlideButton;

    @FindBy(css = ".slick-prev")
    private WebElement prevSlideButton;

    @FindBy(css = "li.slick-slide.slick-active:not(.slick-cloned) .item-img a")
    private WebElement activePizzaLink;

    @FindBy(css = "#ak-top")
    private WebElement scrollUpButton;

    @FindBy(css = ".text-5-value a")
    private List<WebElement> socialLinks;

    @FindBy(css = ".ap-cat-list .wp-post-image")
    private WebElement drinkImage;

    @FindBy(css = ".ap-cat-list .ajax_add_to_cart")
    private WebElement addToCartButtonForDrink;

    @FindBy(css = ".prod2-slider .wp-post-image")
    private WebElement dessertImage;

    public WebElement getNextSlideButton() {
        return nextSlideButton;
    }

    public WebElement getPrevSlideButton() {
        return prevSlideButton;
    }

    public void hoverOnDrinkAndWaitForAddToCart() {
        Actions actions = new Actions(driver);
        actions.moveToElement(drinkImage).perform();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(addToCartButtonForDrink));
    }

    public void clickDessertImageAndWaitForPageLoad(String urlPart) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dessertImage);
        dessertImage.click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains(urlPart));
    }

    public boolean isScrollUpButtonVisibleWithWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(scrollUpButton))
                .isDisplayed();
    }

    public List<WebElement> getSocialLinksWithWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> !socialLinks.isEmpty() ? socialLinks : null);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String getActivePizzaTitle() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            return wait.until(d -> {
                try {
                    String title = activePizzaLink.getAttribute("title");
                    if (title != null && !title.trim().isEmpty()) {
                        return title.trim();
                    }
                    return null;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            });
        } catch (Exception e) {
            return "";
        }
    }

    public WebElement getAddToCartButtonForDrink() {
        return addToCartButtonForDrink;
    }
}

