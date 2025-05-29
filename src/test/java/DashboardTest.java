import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DashboardTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        sidebarPage = new SidebarPage(driver);
        dashboardPage = new DashboardPage(driver);
        driver.get("http://localhost:5173/login");
    }

    @Test
    public void testAccessDashboard() throws InterruptedException {
        // Login terlebih dahulu
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("admin123");
        loginPage.clickLoginButton();

        // Tunggu hingga sidebar muncul
        assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login");

        // Navigasi ke Dashboard
        sidebarPage.clickDashboardMenu();

        // Verifikasi halaman Dashboard
        assertTrue(dashboardPage.isDashboardPageVisible(), "Halaman dashboard tidak terlihat");
        assertTrue(dashboardPage.isStatCardVisible(), "Statistik card tidak terlihat");
        assertTrue(dashboardPage.isChartVisible(), "Grafik tidak terlihat");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }
}