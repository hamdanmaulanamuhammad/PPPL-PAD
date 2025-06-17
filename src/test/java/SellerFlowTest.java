package page_objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SellerFlowTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private DashboardPage dashboardPage;
    private WithdrawalPage withdrawalPage;
    private EtalasePage etalasePage;
    private ReportPage reportPage;
    private ProductPage productPage;

    @BeforeEach
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        sidebarPage = new SidebarPage(driver);
        dashboardPage = new DashboardPage(driver);
        withdrawalPage = new WithdrawalPage(driver);
        etalasePage = new EtalasePage(driver);
        reportPage = new ReportPage(driver);
        productPage = new ProductPage(driver);
        driver.get("http://localhost:5173/login");
    }

    @Test
    public void testSellerFlow() throws Exception {
        // Langkah 1: Verifikasi halaman login terlihat
        System.out.println("Memeriksa halaman login...");
        assertTrue(loginPage.isLoginPageVisible(), "Halaman login tidak terlihat");
        Thread.sleep(2000);

        // Langkah 2: Test login gagal
        System.out.println("Mencoba login gagal...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        assertTrue(loginPage.isErrorMessageVisible(), "Pesan error tidak terlihat setelah login gagal");
        loginPage.clickTryAgainButton();
        Thread.sleep(2000);

        // Langkah 3: Test login berhasil
        System.out.println("Mencoba login berhasil...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("admin#123");
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
        System.out.println("=== POSITIVE TEST CASE ===");
        System.out.println("Memulai simulasi penarikan dana...");
        withdrawalPage.clickWithdrawButton();
        Thread.sleep(2000);
        withdrawalPage.addNewAccount("minima", "1234567890", "John Doe");
        Thread.sleep(2000);
        withdrawalPage.submitWithdrawal("minima - 1234567890", "1000000");
        Thread.sleep(2000);
        System.out.println("Penarikan dana diajukan.");

        System.out.println("\n=== NEGATIVE TEST CASE ===");
        System.out.println("Memulai simulasi penarikan dana dengan jumlah negatif...");
        withdrawalPage.clickWithdrawButton();
        Thread.sleep(2000);
        try {
            withdrawalPage.submitWithdrawal("minima - 1234567890", "-200000");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Penarikan gagal seperti yang diharapkan: " + e.getMessage());
        } finally {
            withdrawalPage.closeModal();
        }

        // Langkah 7: Akses halaman Etalase
        System.out.println("Mengakses halaman Etalase...");
        sidebarPage.clickEtalaseMenu();
        Thread.sleep(2000);
        assertTrue(etalasePage.isEtalasePageVisible(), "Halaman Etalase tidak terlihat");
        assertTrue(etalasePage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");

        // Langkah 8: Akses halaman Laporan
        System.out.println("Mengakses halaman Laporan...");
        sidebarPage.clickReportMenu();
        Thread.sleep(2000);
        assertTrue(reportPage.isReportPageVisible(), "Halaman Laporan tidak terlihat");
        assertTrue(reportPage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");
        assertTrue(reportPage.isReportCardsVisible(), "Kartu laporan tidak terlihat");
        assertTrue(reportPage.isSalesChartVisible(), "Grafik penjualan tidak terlihat");

        System.out.println("Mengklik tombol Export ke Excel...");
        reportPage.clickExportExcelButton();
        Thread.sleep(2000);
        assertTrue(reportPage.isExportSuccessful(), "Ekspor Excel tidak berhasil");

        // Langkah 9: Akses halaman Manajemen Produk
        System.out.println("Mengakses halaman Manajemen Produk...");
        sidebarPage.clickProductMenu();
        Thread.sleep(2000);
        assertTrue(productPage.isProductPageVisible(), "Halaman Manajemen Produk tidak terlihat");
        assertTrue(productPage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");
        assertTrue(productPage.isProductTableVisible(), "Tabel produk tidak terlihat");

        // Langkah 10: Tambah produk baru
        System.out.println("=== POSITIVE TEST CASE: Menambah produk ===");
        productPage.clickAddProductButton();
        Thread.sleep(500);
        assertTrue(productPage.isAddModalVisible(), "Modal Tambah Produk tidak terlihat");

        System.out.println("Mengisi data pada modal Tambah Produk Baru...");
        productPage.selectCategory("1");
        productPage.enterProductName("Custom T-Shirt");
        productPage.enterDescription("High-quality custom printed T-shirt");
        productPage.enterUnit("unit");
        productPage.uploadThumbnail("C:\\Users\\VICTUS\\Downloads\\home.png");
        Thread.sleep(1000);

        System.out.println("Menambahkan variasi produk...");
        productPage.clickAddVariationButton();
        productPage.fillVariationDetails("Medium Size", "150000", "50", "200", "1", true);
        Thread.sleep(1000);

        System.out.println("Mengklik tombol Tambahkan Produk...");
        productPage.clickSaveButton();
        Thread.sleep(1000);

        System.out.println("Memeriksa pesan sukses...");
        assertTrue(productPage.isSuccessMessageVisible(), "Pesan sukses tidak terlihat setelah menambahkan produk");
        productPage.clickCancelButton(); // Close any lingering modal
        Thread.sleep(1000);

        System.out.println("Memeriksa produk baru di tabel...");
        WebElement productRow = driver.findElement(By.xpath("//tbody/tr[td[2][text()='Custom T-Shirt']]"));
        assertTrue(productRow.isDisplayed(), "Produk 'Custom T-Shirt' tidak terlihat di tabel");

        // Langkah 11: Test menambah produk baru tanpa harga
        System.out.println("\n=== NEGATIVE TEST CASE: Menambah produk tanpa harga ===");
        productPage.clickAddProductButton();
        Thread.sleep(2000);
        assertTrue(productPage.isAddModalVisible(), "Modal Tambah Produk tidak terlihat");

        System.out.println("Mengisi data pada modal Tambah Produk Baru tanpa harga...");
        productPage.selectCategory("1");
        productPage.enterProductName("T-Shirt No Price");
        productPage.enterDescription("T-shirt without price for testing");
        productPage.enterUnit("unit");
        productPage.uploadThumbnail("C:\\Users\\VICTUS\\Downloads\\home.png");
        Thread.sleep(1000);

        System.out.println("Menambahkan variasi produk tanpa harga...");
        productPage.clickAddVariationButton();
        productPage.fillVariationDetails("Medium Size", "", "50", "200", "1", true);
        Thread.sleep(1000);

        System.out.println("Mengklik tombol Tambahkan Produk...");
        productPage.clickSaveButton();
        Thread.sleep(2000);

        System.out.println("Memeriksa pesan error untuk harga...");
        assertTrue(productPage.isErrorMessageVisible(), "Pesan error untuk harga tidak terlihat");
        assertTrue(productPage.getErrorMessageText().contains("harga"), "Pesan error tidak menyebutkan masalah harga");
        productPage.clickCancelButton();
        Thread.sleep(2000);

        // Langkah 12: Test menambah produk baru dengan harga negatif
        System.out.println("\n=== NEGATIVE TEST CASE: Menambah produk dengan harga negatif ===");
        productPage.clickAddProductButton();
        Thread.sleep(2000);
        assertTrue(productPage.isAddModalVisible(), "Modal Tambah Produk tidak terlihat");

        System.out.println("Mengisi data pada modal Tambah Produk Baru dengan harga negatif...");
        productPage.selectCategory("1");
        productPage.enterProductName("T-Shirt Negative Price");
        productPage.enterDescription("T-shirt with negative price for testing");
        productPage.enterUnit("unit");
        productPage.uploadThumbnail("C:\\Users\\VICTUS\\Downloads\\home.png");
        Thread.sleep(1000);

        System.out.println("Menambahkan variasi produk dengan harga negatif...");
        productPage.clickAddVariationButton();
        productPage.fillVariationDetails("Medium Size", "-150000", "50", "200", "1", true);
        Thread.sleep(1000);

        System.out.println("Mengklik tombol Tambahkan Produk...");
        productPage.clickSaveButton();
        Thread.sleep(2000);

        System.out.println("Memeriksa pesan error untuk harga negatif...");
        assertTrue(productPage.isErrorMessageVisible(), "Pesan error untuk harga negatif tidak terlihat");
        assertTrue(productPage.getErrorMessageText().contains("Harga"), "Pesan error tidak menyebutkan masalah harga");
        productPage.clickCancelButton();
        Thread.sleep(2000);

        // Langkah 13: Test menambah produk baru dengan harga melebihi batas maksimum
        System.out.println("\n=== NEGATIVE TEST CASE: Menambah produk dengan harga melebihi batas maksimum ===");
        productPage.clickAddProductButton();
        Thread.sleep(2000);
        assertTrue(productPage.isAddModalVisible(), "Modal Tambah Produk tidak terlihat");

        System.out.println("Mengisi data pada modal Tambah Produk Baru dengan harga berlebih...");
        productPage.selectCategory("1");
        productPage.enterProductName("T-Shirt Max Price");
        productPage.enterDescription("T-shirt with excessive price for testing");
        productPage.enterUnit("unit");
        productPage.uploadThumbnail("C:\\Users\\VICTUS\\Downloads\\home.png");
        Thread.sleep(1000);

        System.out.println("Menambahkan variasi produk dengan harga melebihi batas maksimum...");
        productPage.clickAddVariationButton();
        productPage.fillVariationDetails("Medium Size", "999999999999", "50", "200", "1", true);
        Thread.sleep(1000);

        System.out.println("Mengklik tombol Tambahkan Produk...");
        productPage.clickSaveButton();
        Thread.sleep(2000);

        System.out.println("Memeriksa pesan error untuk harga melebihi batas maksimum...");
        assertTrue(productPage.isErrorMessageVisible(), "Pesan error untuk harga melebihi batas maksimum tidak terlihat");
        assertTrue(productPage.getErrorMessageText().contains("Harga"), "Pesan error tidak menyebutkan masalah harga");
        productPage.clickCancelButton();
        Thread.sleep(2000);

        // Langkah 14: Edit produk yang baru ditambahkan
        System.out.println("=== POSITIVE TEST CASE: Mengedit produk ===");
        productPage.clickEditProductButton("Custom T-Shirt");
        Thread.sleep(2000);
        assertTrue(productPage.isEditModalVisible(), "Modal Edit Produk tidak terlihat");

        System.out.println("Mengisi data pada modal Edit Produk...");
        productPage.enterProductName("Custom T-Shirt Premium");
        productPage.enterDescription("Premium quality custom printed T-shirt with enhanced durability");
        productPage.enterUnit("piece");
        Thread.sleep(1000);

        System.out.println("Menambahkan variasi baru pada produk...");
        productPage.clickAddVariationButton();
        productPage.fillVariationDetails("Large Size", "180000", "30", "250", "1", false);
        Thread.sleep(1000);

        System.out.println("Mengklik tombol Simpan...");
        productPage.clickSaveButton();
        Thread.sleep(2000);

        System.out.println("Memeriksa pesan sukses...");
        assertTrue(productPage.isSuccessMessageVisible(), "Pesan sukses tidak terlihat setelah mengedit produk");
        productPage.clickCancelButton();
        Thread.sleep(2000);

        System.out.println("Memeriksa produk yang diedit di tabel...");
        productRow = driver.findElement(By.xpath("//tbody/tr[td[2][text()='Custom T-Shirt Premium']]"));
        assertTrue(productRow.isDisplayed(), "Produk 'Custom T-Shirt Premium' tidak terlihat di tabel setelah diedit");
        Map<String, String> productDetails = productPage.getProductDetails("Custom T-Shirt Premium");
        assertEquals("Premium quality custom printed T-shirt with enhanced durability", productDetails.get("description"),
                "Deskripsi produk tidak sesuai setelah diedit");
        assertEquals("2 variasi", productDetails.get("variant"),
                "Jumlah variasi produk tidak sesuai setelah diedit");
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            try {
                Thread.sleep(3000);
                driver.quit();
            } catch (Exception e) {
                System.out.println("Error during teardown: " + e.getMessage());
            }
        }
    }
}