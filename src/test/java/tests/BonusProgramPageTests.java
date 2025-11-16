package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.BonusProgramPage;

import java.time.Duration;

public class BonusProgramPageTests {

    private WebDriver driver;
    private BonusProgramPage bonusProgramPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        bonusProgramPage = new BonusProgramPage(driver);
    }

    @Test
    public void testSuccessfulBonusCardIssuing() {

        String name = "Tatyana";
        String phone = "80332869854";

        bonusProgramPage.open();
        bonusProgramPage.applyForBonusCard(name, phone);

        Assertions.assertTrue(
                phone.matches("^[/\\+78]+[0-9]{10,10}$"), "Phone number format is invalid. It must meet the following requirements: ^[/\\+78]+[0-9]{10,10}$");

        Assertions.assertTrue(
                bonusProgramPage.isSuccessMessageDisplayed(),
                "Failed to display the successful bonus card issuance message"
        );
    }

}
