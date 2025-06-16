package page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class EtalasePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Lokator untuk elemen halaman Etalase
    private By etalaseHeader = By.cssSelector("h2.text-2xl.md\\:text-3xl.lg\\:text-3xl.font-bold.mb-6");
    private By categoryTitles = By.cssSelector("h2.text-xl.font-semibold");
    private By seeMoreButton = By.xpath("//button[contains(text(), 'Lihat Lainnya')]");
    private By productCards = By.cssSelector("article.bg-red-600.text-white.p-3.rounded-lg");
    private By emptyCategoryMessage = By.cssSelector("div.bg-gray-50.rounded-lg.p-4.text-center");
    private By loadingSpinner = By.cssSelector("div.animate-spin.rounded-full");

    // Konstruktor
    public EtalasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verifikasi halaman Etalase terlihat
    public boolean isEtalasePageVisible() {
        try {
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(etalaseHeader));
            return header.isDisplayed() && header.getText().contains("Etalase");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa halaman Etalase: " + e.getMessage());
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

    // Klik tombol "Lihat Lainnya" untuk kategori tertentu
    public void clickSeeMoreButton(String categoryName) throws Exception {
        try {
            // Cari semua elemen kategori
            List<WebElement> categories = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(categoryTitles));
            for (WebElement category : categories) {
                if (category.getText().equals(categoryName)) {
                    // Asumsikan tombol "Lihat Lainnya" berada di dekat judul kategori
                    WebElement seeMore = category.findElement(By.xpath("following::button[contains(text(), 'Lihat Lainnya')]"));
                    wait.until(ExpectedConditions.elementToBeClickable(seeMore)).click();
                    return;
                }
            }
            throw new Exception("Kategori " + categoryName + " tidak ditemukan.");
        } catch (Exception e) {
            System.out.println("Gagal mengklik tombol 'Lihat Lainnya' untuk kategori " + categoryName + ": " + e.getMessage());
            throw e;
        }
    }

    // Verifikasi produk dari kategori tertentu ditampilkan
    public boolean isCategoryProductsVisible(String categoryName) {
        try {
            // Asumsikan setelah klik "Lihat Lainnya", URL atau halaman berubah untuk menampilkan produk kategori
            // Verifikasi produk ditampilkan dengan memeriksa elemen artikel
            List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
            return !products.isEmpty();
        } catch (Exception e) {
            System.out.println("Gagal memeriksa produk untuk kategori " + categoryName + ": " + e.getMessage());
            return false;
        }
    }

    // Verifikasi pesan kategori kosong (opsional)
    public boolean isEmptyCategoryMessageVisible() {
        try {
            WebElement emptyMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(emptyCategoryMessage));
            return emptyMessage.isDisplayed() && emptyMessage.getText().contains("Tidak ada produk dalam kategori ini");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa pesan kategori kosong: " + e.getMessage());
            return false;
        }
    }
}