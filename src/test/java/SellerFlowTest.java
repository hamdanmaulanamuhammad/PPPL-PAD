import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SellerFlowTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private DashboardPage dashboardPage;
    private WithdrawalPage withdrawalPage;

    @BeforeEach
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        sidebarPage = new SidebarPage(driver);
        dashboardPage = new DashboardPage(driver);
        withdrawalPage = new WithdrawalPage(driver);
        driver.get("http://localhost:5173/login");
    }

    @Test
    public void testSellerFlow() throws InterruptedException {
        // Langkah 1: Verifikasi halaman login terlihat
        System.out.println("Memeriksa halaman login...");
        assertTrue(loginPage.isLoginPageVisible(), "Halaman login tidak terlihat");
        Thread.sleep(2000);

        // Langkah 2: Test login gagal
        System.out.println("Mencoba login gagal...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        Thread.sleep(1000);
        loginPage.enterPassword("");
        Thread.sleep(1000);
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        assertTrue(loginPage.isErrorMessageVisible(), "Pesan error tidak terlihat setelah login gagal");
        loginPage.clickTryAgainButton();
        Thread.sleep(2000);

        // Langkah 3: Test login berhasil
        System.out.println("Mencoba login berhasil...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        Thread.sleep(1000);
        loginPage.enterPassword("admin#123");
        Thread.sleep(1000);
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login berhasil");

        // Langkah 4: Akses halaman Dashboard
        System.out.println("Mengakses halaman dashboard...");
        sidebarPage.clickDashboardMenu();
        Thread.sleep(2000);
        assertTrue(dashboardPage.isDashboardPageVisible(), "Halaman dashboard tidak terlihat");
        assertTrue(dashboardPage.isStatCardVisible(), "Statistik card tidak terlihat");
        assertTrue(dashboardPage.isChartVisible(), "Grafik tidak terlihat");
        dashboardPage.printDashboardStats();
        Thread.sleep(2000);

        // Langkah 5: Akses halaman Penarikan Dana
        System.out.println("Mengakses halaman penarikan...");
        sidebarPage.clickWithdrawalMenu();
        Thread.sleep(2000);
        assertTrue(withdrawalPage.isWithdrawalPageVisible(), "Halaman penarikan tidak terlihat");
        assertTrue(withdrawalPage.isTableVisible() || withdrawalPage.isErrorMessageVisible(),
                "Tabel penarikan tidak muncul dan tidak ada pesan error");

        // Langkah 6: Simulasi penarikan dana
        System.out.println("Memulai simulasi penarikan dana...");
        withdrawalPage.clickWithdrawButton(); // Penundaan sudah ditambahkan di metode

        // Tambah rekening baru
        System.out.println("Menambahkan rekening baru...");
        withdrawalPage.addNewAccount("odit", "1234567890", "John Doe"); // Penundaan sudah ditambahkan di metode

        // Ajukan penarikan
        System.out.println("Mengajukan penarikan dana...");
        withdrawalPage.submitWithdrawal("odit - 1234567890", "1000000"); // Penundaan sudah ditambahkan di metode

        // Verifikasi (opsional, tergantung UI setelah pengajuan)
        System.out.println("Penarikan dana diajukan.");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }
}