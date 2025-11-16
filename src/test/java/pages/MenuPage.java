package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MenuPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    private By menuRoot = By.cssSelector(".menu-item-389");
    private By pizza = By.cssSelector(".menu-item-390 a");
    private By dessert = By.cssSelector(".menu-item-391 a");
    private By drinks = By.cssSelector(".menu-item-393 a");

    public MenuPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    private void hoverMenu() {
        WebElement root = wait.until(ExpectedConditions.visibilityOfElementLocated(menuRoot));
        actions.moveToElement(root).perform();
    }

    public void openPizza() {
        hoverMenu();
        wait.until(ExpectedConditions.elementToBeClickable(pizza)).click();
    }

    public void openDesert() {
        hoverMenu();
        wait.until(ExpectedConditions.elementToBeClickable(dessert)).click();
    }

    public void openDrinks() {
        hoverMenu();
        wait.until(ExpectedConditions.elementToBeClickable(drinks)).click();
    }
}

