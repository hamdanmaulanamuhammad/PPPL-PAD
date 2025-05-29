import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WithdrawalTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private WithdrawalPage withdrawalPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        sidebarPage = new SidebarPage(driver);
        withdrawalPage = new WithdrawalPage(driver);
        driver.get("http://localhost:5173/login");
    }

    @Test
    public void testAccessWithdrawalPage() throws InterruptedException {
        // Login terlebih dahulu
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("admin123");
        loginPage.clickLoginButton();

        // Tunggu hingga sidebar muncul
        assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login");

        // Navigasi ke halaman penarikan
        sidebarPage.clickWithdrawalMenu();

        // Verifikasi halaman penarikan
        assertTrue(withdrawalPage.isWithdrawalPageVisible(), "Halaman penarikan tidak terlihat");
        assertTrue(withdrawalPage.isTableVisible() || withdrawalPage.isErrorMessageVisible(),
                "Tabel penarikan tidak muncul dan tidak ada pesan error");
    }

    @Test
    public void testFilterWithdrawalStatus() throws InterruptedException {
        // Login terlebih dahulu
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("admin123");
        loginPage.clickLoginButton();

        // Tunggu hingga sidebar muncul
        assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login");

        // Navigasi ke halaman penarikan
        sidebarPage.clickWithdrawalMenu();

        // Pilih status
        withdrawalPage.selectStatus("Menunggu");
        assertTrue(withdrawalPage.isWithdrawalPageVisible(), "Gagal memfilter daftar penarikan");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }
}