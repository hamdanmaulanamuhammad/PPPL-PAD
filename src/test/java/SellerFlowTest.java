import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page_objects.*;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private ModalAddProductPage modalAddProductPage;

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
        modalAddProductPage = new ModalAddProductPage(driver);
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

        // Langkah 6: Simulasi penarikan dana (commented out)
        // Positive Test Case - Normal Withdrawal
        System.out.println("=== POSITIVE TEST CASE ===");
        System.out.println("Memulai simulasi penarikan dana...");
        withdrawalPage.clickWithdrawButton();
        Thread.sleep(2000);
        System.out.println("Menambahkan rekening baru...");
        withdrawalPage.addNewAccount("minima", "1234567890", "John Doe");
        Thread.sleep(2000);
        System.out.println("Mengajukan penarikan dana...");
        withdrawalPage.submitWithdrawal("minima - 1234567890", "1000000");
        Thread.sleep(2000);
        System.out.println("Penarikan dana diajukan.");

        System.out.println("\n=== NEGATIVE TEST CASE ===");
        System.out.println("Memulai simulasi penarikan dana dengan jumlah negatif...");
        withdrawalPage.clickWithdrawButton();
        Thread.sleep(2000);
        System.out.println("Mengajukan penarikan dana dengan jumlah negatif...");
        try {
            withdrawalPage.submitWithdrawal("minima - 1234567890", "-200000");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Penarikan gagal seperti yang diharapkan: " + e.getMessage());
        } finally {
            System.out.println("Menutup modal...");
            withdrawalPage.closeModal();
        }

        // Langkah 7: Akses halaman Etalase
        System.out.println("Mengakses halaman Etalase...");
        sidebarPage.clickEtalaseMenu();
        Thread.sleep(2000);
        assertTrue(etalasePage.isEtalasePageVisible(), "Halaman Etalase tidak terlihat");
        assertTrue(etalasePage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");

        // Langkah 8: Klik tombol "Lihat Lainnya" untuk kategori
        String categoryName = "Merchandise Custom";
        System.out.println("Memeriksa produk untuk kategori " + categoryName + "...");
        assertTrue(etalasePage.isCategoryProductsVisible(categoryName),
                "Produk untuk kategori " + categoryName + " tidak terlihat");

        // Langkah 9: Akses halaman Laporan
        System.out.println("Mengakses halaman Laporan...");
        sidebarPage.clickReportMenu();
        Thread.sleep(2000);
        assertTrue(reportPage.isReportPageVisible(), "Halaman Laporan tidak terlihat");
        assertTrue(reportPage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");
        assertTrue(reportPage.isReportCardsVisible(), "Kartu laporan tidak terlihat");
        assertTrue(reportPage.isSalesChartVisible(), "Grafik penjualan tidak terlihat");

        // Klik tombol Export ke Excel
        System.out.println("Mengklik tombol Export ke Excel...");
        reportPage.clickExportExcelButton();
        Thread.sleep(2000);
        assertTrue(reportPage.isExportSuccessful(), "Ekspor Excel tidak berhasil");

        // Langkah 10: Akses halaman Laporan
        System.out.println("Mengakses halaman Laporan...");
        sidebarPage.clickProductMenu();
        Thread.sleep(2000);
        assertTrue(reportPage.isReportPageVisible(), "Halaman Laporan tidak terlihat");
        assertTrue(reportPage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");
        assertTrue(reportPage.isReportCardsVisible(), "Kartu laporan tidak terlihat");
        assertTrue(reportPage.isSalesChartVisible(), "Grafik penjualan tidak terlihat");

        // Langkah 11: Tambah produk baru
        System.out.println("Mengklik tombol + Produk...");
        WebElement addProductButton = driver.findElement(By.cssSelector("button.bg-red-600"));
        wait.until(ExpectedConditions.elementToBeClickable(addProductButton)).click();
        Thread.sleep(2000);
        assertTrue(modalAddProductPage.isModalVisible(), "Modal Tambah Produk tidak terlihat");

        System.out.println("Mengisi data pada modal Tambah Produk Baru...");
        modalAddProductPage.selectCategory("1");
        modalAddProductPage.enterProductName("Custom T-Shirt");
        modalAddProductPage.enterDescription("High-quality custom printed T-shirt");
        modalAddProductPage.enterUnit("unit");
        modalAddProductPage.uploadThumbnail("C:\\Users\\VICTUS\\Downloads\\home.png");
        Thread.sleep(1000);

        System.out.println("Menambahkan variasi produk...");
        modalAddProductPage.clickAddVariationButton();
        modalAddProductPage.fillVariationDetails(
                "Medium Size", "150000", "50", "200", "1", true
        );
        Thread.sleep(1000);

        System.out.println("Mengklik tombol Tambahkan Produk...");
        modalAddProductPage.clickTambahButton();
        Thread.sleep(2000);

        System.out.println("Memeriksa pesan sukses...");
        assertTrue(modalAddProductPage.isSuccessMessageVisible(), "Pesan sukses tidak terlihat setelah menambahkan produk");

        System.out.println("Memeriksa produk baru di tabel...");
        Thread.sleep(2000);
        assertTrue(productPage.isProductTableVisible(), "Tabel produk tidak terlihat");
//
//        // Langkah 12: Ubah status produk
//        String productName = "Custom T-Shirt";
//        String newStatus = "inactive";
//        System.out.println("Mengubah status produk '" + productName + "' menjadi '" + newStatus + "'...");
//        // Simpan detail produk sebelum perubahan status
//        Map<String, String> beforeDetails = productPage.getProductDetails(productName);
//        productPage.changeProductStatus(productName, newStatus);
//        Thread.sleep(2000);
//
//        System.out.println("Memeriksa pesan sukses perubahan status...");
//        assertTrue(productPage.isStatusChangeSuccessVisible(), "Pesan sukses perubahan status tidak terlihat");
//
//        System.out.println("Memeriksa status produk di tabel...");
//        String currentStatus = productPage.getProductStatus(productName);
//        assertTrue(currentStatus.equals(newStatus),
//                "Status produk tidak berubah menjadi '" + newStatus + "', ditemukan: " + currentStatus);
//
//        // Langkah 13: Verifikasi bahwa hanya status yang berubah
//        System.out.println("Memeriksa bahwa hanya status yang berubah di tabel...");
//        Map<String, String> afterDetails = productPage.getProductDetails(productName);
//        assertEquals(beforeDetails.get("category"), afterDetails.get("category"),
//                "Kategori produk berubah setelah update status");
//        assertEquals(beforeDetails.get("name"), afterDetails.get("name"),
//                "Nama produk berubah setelah update status");
//        assertEquals(beforeDetails.get("description"), afterDetails.get("description"),
//                "Deskripsi produk berubah setelah update status");
//        assertEquals(beforeDetails.get("stock"), afterDetails.get("stock"),
//                "Stok produk berubah setelah update status");
//        assertEquals(beforeDetails.get("price"), afterDetails.get("price"),
//                "Harga produk berubah setelah update status");
//        assertEquals(beforeDetails.get("variant"), afterDetails.get("variant"),
//                "Variasi produk berubah setelah update status");
//        assertEquals(beforeDetails.get("options"), afterDetails.get("options"),
//                "Opsi tambahan produk berubah setelah update status");
    }

    @Test
    public void testAddProductWithCompleteData() throws Exception {
        assertTrue(loginPage.isLoginPageVisible(), "Halaman login tidak terlihat");
        Thread.sleep(1000);

        System.out.println("Mencoba login berhasil...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        Thread.sleep(1000);
        loginPage.enterPassword("admin#123");
        Thread.sleep(1000);
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login berhasil");

        // Step 2: Navigate to Manajemen Produk page
        System.out.println("Mengakses halaman Manajemen Produk...");
        sidebarPage.toggleSidebarIfMobile();
        sidebarPage.clickProductMenu();
        Thread.sleep(2000);
        assertTrue(productPage.isProductPageVisible(), "Halaman Manajemen Produk tidak terlihat");
        assertTrue(productPage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");

        // Step 3: Click "+ Produk" button
        System.out.println("Mengklik tombol + Produk...");
        WebElement addProductButton = driver.findElement(By.cssSelector("button.bg-red-600"));
        wait.until(ExpectedConditions.elementToBeClickable(addProductButton)).click();
        Thread.sleep(2000);
        assertTrue(modalAddProductPage.isModalVisible(), "Modal Tambah Produk tidak terlihat");

        // Step 4: Fill in product details
        System.out.println("Mengisi data pada modal Tambah Produk Baru...");
        modalAddProductPage.selectCategory("1");
        modalAddProductPage.enterProductName("Custom T-Shirt");
        modalAddProductPage.enterDescription("High-quality custom printed T-shirt");
        modalAddProductPage.enterUnit("unit");
        modalAddProductPage.uploadThumbnail("C:\\Users\\VICTUS\\Downloads\\home.png");
        Thread.sleep(1000);

        // Add variation
        System.out.println("Menambahkan variasi produk...");
        modalAddProductPage.clickAddVariationButton();
        modalAddProductPage.fillVariationDetails(
                "Medium Size", "150000", "50", "200", "1", true
        );
        Thread.sleep(1000);

        // Step 5: Click Tambahkan Produk button
        System.out.println("Mengklik tombol Tambahkan Produk...");
        modalAddProductPage.clickTambahButton();
        Thread.sleep(2000);

        // Step 6: Verify success
        System.out.println("Memeriksa pesan sukses...");
        assertTrue(modalAddProductPage.isSuccessMessageVisible(), "Pesan sukses tidak terlihat setelah menambahkan produk");

        // Verify the product appears in the table
        System.out.println("Memeriksa produk baru di tabel...");
        Thread.sleep(2000);
        assertTrue(productPage.isProductTableVisible(), "Tabel produk tidak terlihat");
        try {
            WebElement productRow = driver.findElement(By.xpath("//tbody/tr[td[2][text()='Custom T-Shirt']]"));
            assertTrue(productRow.isDisplayed(), "Produk 'Custom T-Shirt' tidak terlihat di tabel");
        } catch (Exception e) {
            throw new RuntimeException("Failed to find product 'Custom T-Shirt' in table: " + e.getMessage(), e);
        }
    }

    @Test
    public void testChangeProductStatus() throws Exception {
        // Langkah 1: Verifikasi halaman login terlihat
        System.out.println("Memeriksa halaman login...");
        assertTrue(loginPage.isLoginPageVisible(), "Halaman login tidak terlihat");
        Thread.sleep(1000);

        // Langkah 2: Login berhasil
        System.out.println("Mencoba login berhasil...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        Thread.sleep(1000);
        loginPage.enterPassword("admin#123");
        Thread.sleep(1000);
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login berhasil");

        // Langkah 3: Akses halaman Manajemen Produk
        System.out.println("Mengakses halaman Manajemen Produk...");
        sidebarPage.toggleSidebarIfMobile();
        sidebarPage.clickProductMenu();
        Thread.sleep(2000);
        assertTrue(productPage.isProductPageVisible(), "Halaman Manajemen Produk tidak terlihat");
        assertTrue(productPage.isLoadingSpinnerGone(), "Spinner loading masih terlihat");
        assertTrue(productPage.isProductTableVisible(), "Tabel produk tidak terlihat");

        // Langkah 4: Pilih status filter "active" untuk memastikan produk aktif tersedia
        String filterStatus = "active";
        System.out.println("Memilih status '" + filterStatus + "' dari dropdown filter...");
        productPage.selectStatusFilter(filterStatus);
        Thread.sleep(2000);
        assertTrue(productPage.isStatusProductsVisible(filterStatus),
                "Produk untuk status '" + filterStatus + "' tidak terlihat");

        // Langkah 5: Ubah status produk tertentu menjadi "inactive"
        String productName = "Custom T-Shirt";
        String newStatus = "inactive";
        System.out.println("Mengubah status produk '" + productName + "' menjadi '" + newStatus + "'...");
        Map<String, String> beforeDetails = productPage.getProductDetails(productName);
        productPage.changeProductStatus(productName, newStatus);
        Thread.sleep(2000);

        // Langkah 6: Verifikasi pesan sukses
        System.out.println("Memeriksa pesan sukses perubahan status...");
        assertTrue(productPage.isStatusChangeSuccessVisible(), "Pesan sukses perubahan status tidak terlihat");

        // Langkah 7: Verifikasi status produk telah berubah
        System.out.println("Memeriksa status produk di tabel...");
        String currentStatus = productPage.getProductStatus(productName);
        assertEquals(newStatus, currentStatus,
                "Status produk tidak berubah menjadi '" + newStatus + "', ditemukan: " + currentStatus);

        // Langkah 8: Verifikasi bahwa hanya status yang berubah
        System.out.println("Memeriksa bahwa hanya status yang berubah di tabel...");
        Map<String, String> afterDetails = productPage.getProductDetails(productName);
        assertEquals(beforeDetails.get("category"), afterDetails.get("category"),
                "Kategori produk berubah setelah update status");
        assertEquals(beforeDetails.get("name"), afterDetails.get("name"),
                "Nama produk berubah setelah update status");
        assertEquals(beforeDetails.get("description"), afterDetails.get("description"),
                "Deskripsi produk berubah setelah update status");
        assertEquals(beforeDetails.get("stock"), afterDetails.get("stock"),
                "Stok produk berubah setelah update status");
        assertEquals(beforeDetails.get("price"), afterDetails.get("price"),
                "Harga produk berubah setelah update status");
        assertEquals(beforeDetails.get("variant"), afterDetails.get("variant"),
                "Variasi produk berubah setelah update status");
        assertEquals(beforeDetails.get("options"), afterDetails.get("options"),
                "Opsi tambahan produk berubah setelah update status");

        // Langkah 9: Kembalikan status ke "active" dan verifikasi
        System.out.println("Mengubah status produk '" + productName + "' kembali ke 'active'...");
        beforeDetails = productPage.getProductDetails(productName);
        productPage.changeProductStatus(productName, "active");
        Thread.sleep(2000);

        System.out.println("Memeriksa pesan sukses perubahan status...");
        assertTrue(productPage.isStatusChangeSuccessVisible(), "Pesan sukses perubahan status tidak terlihat");

        System.out.println("Memeriksa status produk di tabel...");
        currentStatus = productPage.getProductStatus(productName);
        assertEquals("active", currentStatus,
                "Status produk tidak berubah menjadi 'active', ditemukan: " + currentStatus);

        // Langkah 10: Verifikasi bahwa hanya status yang berubah
        System.out.println("Memeriksa bahwa hanya status yang berubah di tabel...");
        afterDetails = productPage.getProductDetails(productName);
        assertEquals(beforeDetails.get("category"), afterDetails.get("category"),
                "Kategori produk berubah setelah update status");
        assertEquals(beforeDetails.get("name"), afterDetails.get("name"),
                "Nama produk berubah setelah update status");
        assertEquals(beforeDetails.get("description"), afterDetails.get("description"),
                "Deskripsi produk berubah setelah update status");
        assertEquals(beforeDetails.get("stock"), afterDetails.get("stock"),
                "Stok produk berubah setelah update status");
        assertEquals(beforeDetails.get("price"), afterDetails.get("price"),
                "Harga produk berubah setelah update status");
        assertEquals(beforeDetails.get("variant"), afterDetails.get("variant"),
                "Variasi produk berubah setelah update status");
        assertEquals(beforeDetails.get("options"), afterDetails.get("options"),
                "Opsi tambahan produk berubah setelah update status");
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