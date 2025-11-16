package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.AccountPage;
import pages.LoginPage;

import java.time.Duration;

public class AccountPageTests {

    private WebDriver driver;
    private AccountPage accountPage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        loginPage = new LoginPage(driver);
        loginPage.login("Tanya", "345345Ta");

        driver.get("https://pizzeria.skillbox.cc/my-account/");
        accountPage = new AccountPage(driver);
    }

    @Test
    public void testUploadProfilePicture() {
        accountPage.openAccountDetails();

        String filePath = "D:\\Modsen\\Testdoc.txt";
        accountPage.uploadProfilePicture(filePath);
        accountPage.saveChanges();

        boolean isSuccess = accountPage.isUploadSuccessful();
        System.out.println("Result of the notification check: " + isSuccess);

        Assertions.assertTrue(isSuccess, "Failed to upload the file");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

