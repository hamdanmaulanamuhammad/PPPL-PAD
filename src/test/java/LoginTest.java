import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        sidebarPage = new SidebarPage(driver);
        driver.get("http://localhost:5173/login");
    }

    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        // Verifikasi halaman login terlihat
        assertTrue(loginPage.isLoginPageVisible(), "Halaman login tidak terlihat");

        // Masukkan kredensial
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("admin#123");
        loginPage.clickLoginButton();

        // Tunggu hingga sidebar muncul, menandakan redirect ke dashboard selesai
        assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login");
    }

    @Test
    public void testFailedLogin() throws InterruptedException {
        // Verifikasi halaman login terlihat
        assertTrue(loginPage.isLoginPageVisible(), "Halaman login tidak terlihat");

        // Masukkan kredensial salah
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        // Verifikasi pesan error
        assertTrue(loginPage.isErrorMessageVisible(), "Pesan error tidak terlihat");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }
}