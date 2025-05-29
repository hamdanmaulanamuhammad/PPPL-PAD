import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SellerFlowTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private DashboardPage dashboardPage;
    private WithdrawalPage withdrawalPage;
    private EtalasePage etalasePage;
    private ReportPage reportPage;

    @BeforeEach
    public void setUp() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        sidebarPage = new SidebarPage(driver);
        dashboardPage = new DashboardPage(driver);
        withdrawalPage = new WithdrawalPage(driver);
        etalasePage = new EtalasePage(driver);
        reportPage = new ReportPage(driver);
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
        withdrawalPage.clickWithdrawButton();
        Thread.sleep(2000);

        // Tambah rekening baru
        System.out.println("Menambahkan rekening baru...");
        withdrawalPage.addNewAccount("odit", "1234567890", "John Doe");
        Thread.sleep(2000);

        // Ajukan penarikan
        System.out.println("Mengajukan penarikan dana...");
        withdrawalPage.submitWithdrawal("odit - 1234567890", "1000000");
        Thread.sleep(2000);

        // Verifikasi (opsional, tergantung UI setelah pengajuan)
        System.out.println("Penarikan dana diajukan.");

        // Langkah 7: Akses halaman Etalase
        System.out.println("Mengakses halaman Etalase...");
        sidebarPage.clickEtalaseMenu();
        Thread.sleep(2000);
        assertTrue(etalasePage.isEtalasePageVisible(), "Halaman Etalase tidak terlihat");
        assertTrue(etalasePage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");

        // Langkah 8: Klik tombol "Lihat Lainnya" untuk kategori
        System.out.println("Mengklik tombol 'Lihat Lainnya' untuk kategori...");
        String categoryName = "Sample Category"; // Ganti dengan nama kategori yang valid
        etalasePage.clickSeeMoreButton(categoryName);
        Thread.sleep(2000);

        // Langkah 9: Verifikasi produk kategori ditampilkan
        System.out.println("Memeriksa produk untuk kategori " + categoryName + "...");
        assertTrue(etalasePage.isCategoryProductsVisible(categoryName),
                "Produk untuk kategori " + categoryName + " tidak terlihat");

        // Langkah 10: Akses halaman Laporan (Gherkin: Seller sudah masuk ke halaman "Laporan")
        System.out.println("Mengakses halaman Laporan...");
        sidebarPage.clickReportMenu();
        Thread.sleep(2000);
        assertTrue(reportPage.isReportPageVisible(), "Halaman Laporan tidak terlihat");
        assertTrue(reportPage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");
        assertTrue(reportPage.isReportCardsVisible(), "Kartu laporan tidak terlihat");
        assertTrue(reportPage.isSalesChartVisible(), "Grafik penjualan tidak terlihat");

        // Langkah 11: Skenario 1 - Seller melakukan ekspor data penjualan (Excel)
        System.out.println("Mengklik tombol Export ke Excel...");
        reportPage.clickExportExcelButton();
        Thread.sleep(2000);
        assertTrue(reportPage.isExportSuccessful(), "Ekspor Excel tidak berhasil");

        // Langkah 12: Skenario 1 - Seller melakukan ekspor data penjualan (PDF)
        System.out.println("Mengklik tombol Export ke PDF...");
        reportPage.clickExportPdfButton();
        Thread.sleep(2000);
        assertTrue(reportPage.isExportSuccessful(), "Ekspor PDF tidak berhasil");

        // Langkah 13: Skenario 2 - Seller tidak bisa mengexport data penjualan (Excel)
        System.out.println("Mencoba ekspor Excel gagal...");
        reportPage.clickExportExcelButton();
        Thread.sleep(2000);
        assertTrue(reportPage.isExportErrorMessageVisible(), "Pesan error ekspor Excel tidak terlihat");
        assertFalse(reportPage.isExportSuccessful(), "Ekspor Excel seharusnya gagal tetapi berhasil");

        // Langkah 14: Skenario 2 - Seller tidak bisa mengexport data penjualan (PDF)
        System.out.println("Mencoba ekspor PDF gagal...");
        reportPage.clickExportPdfButton();
        Thread.sleep(2000);
        assertTrue(reportPage.isExportErrorMessageVisible(), "Pesan error ekspor PDF tidak terlihat");
        assertFalse(reportPage.isExportSuccessful(), "Ekspor PDF seharusnya gagal tetapi berhasil");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }
}