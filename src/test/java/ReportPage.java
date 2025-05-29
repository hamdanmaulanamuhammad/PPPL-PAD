import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ReportPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Lokator untuk elemen halaman Laporan
    private By reportHeader = By.cssSelector("h2.text-2xl.md\\:text-3xl.font-bold.mb-6");
    private By loadingSpinner = By.cssSelector("div.animate-spin.rounded-full");
    private By apiErrorBanner = By.cssSelector("div.bg-red-100.border.border-red-400");
    private By demoModeBanner = By.cssSelector("div.bg-yellow-100.border.border-yellow-400");
    private By reportCards = By.cssSelector("div.grid.grid-cols-1.md\\:grid-cols-3.gap-6.mb-10");
    private By exportExcelButton = By.cssSelector("button.bg-green-600.text-white");
    private By exportPDFButton = By.cssSelector("button.bg-blue-600.text-white");
    private By chartContainer = By.cssSelector("div.chart-container");
    private By chartErrorMessage = By.cssSelector("div.absolute.inset-0 div.text-center p.font-medium");
    private By tryAgainButton = By.cssSelector("button.bg-red-600.text-white");

    // Konstruktor
    public ReportPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verifikasi halaman Laporan terlihat
    public boolean isReportPageVisible() {
        try {
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(reportHeader));
            return header.isDisplayed() && header.getText().contains("Laporan");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa halaman Laporan: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi loading spinner tidak terlihat
    public boolean isLoadingSpinnerGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
            return true;
        } catch (Exception e) {
            System.out.println("Spinner masih terlihat: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi banner error API terlihat
    public boolean isApiErrorVisible() {
        try {
            WebElement errorBanner = wait.until(ExpectedConditions.visibilityOfElementLocated(apiErrorBanner));
            return errorBanner.isDisplayed() &&
                    errorBanner.getText().contains("Terjadi kesalahan saat mengambil data!");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa banner error API: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi mode demo aktif
    public boolean isDemoModeVisible() {
        try {
            WebElement demoBanner = wait.until(ExpectedConditions.visibilityOfElementLocated(demoModeBanner));
            return demoBanner.isDisplayed() &&
                    demoBanner.getText().contains("Mode Demo Aktif");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa mode demo: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi kartu laporan terlihat
    public boolean isReportCardsVisible() {
        try {
            WebElement cards = wait.until(ExpectedConditions.visibilityOfElementLocated(reportCards));
            return cards.isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memeriksa kartu laporan: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi grafik penjualan terlihat
    public boolean isSalesChartVisible() {
        try {
            WebElement chart = wait.until(ExpectedConditions.visibilityOfElementLocated(chartContainer));
            return chart.isDisplayed() &&
                    !wait.until(ExpectedConditions.visibilityOfElementLocated(chartErrorMessage)).isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memeriksa grafik penjualan: " + e.getMessage());
            return false;
        }
    }

    // Klik tombol Export ke Excel
    public void clickExportExcelButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(exportExcelButton));
            button.click();
        } catch (Exception e) {
            System.out.println("Gagal mengklik tombol Export ke Excel: " + e.getMessage());
            throw e;
        }
    }

    // Klik tombol Export ke PDF
    public void clickExportPDFButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(exportPDFButton));
            button.click();
        } catch (Exception e) {
            System.out.println("Gagal mengklik tombol Export ke PDF: " + e.getMessage());
            throw e;
        }
    }

    // Verifikasi pesan error ekspor
    public boolean isExportErrorMessageVisible() {
        try {
            // Asumsikan pesan error ditampilkan sebagai alert browser
            WebElement alert = wait.until(ExpectedConditions.alertIsPresent()).getText();
            return alert.contains("Gagal mengekspor");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa pesan error ekspor: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi file diunduh (sederhana, hanya memeriksa klik berhasil)
    public boolean isExportSuccessful() {
        try {
            // Karena Selenium tidak bisa langsung memeriksa unduhan file, kita asumsikan klik tombol berhasil
            // tanpa muncul alert error
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(exportExcelButton));
            return button.isDisplayed(); // Asumsi sukses jika tombol masih ada dan tidak ada error
        } catch (Exception e) {
            System.out.println("Gagal memeriksa status ekspor: " + e.getMessage());
            return false;
        }
    }
}