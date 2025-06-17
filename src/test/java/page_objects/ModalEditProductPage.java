package page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ModalEditProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators untuk elemen di modal Edit Produk
    private By modalTitle = By.xpath("//h3[contains(@class, 'text-lg font-bold') and contains(text(), 'Edit Produk')]");
    private By categoryDropdown = By.id("category_id");
    private By productNameInput = By.id("productName");
    private By descriptionInput = By.id("description");
    private By unitInput = By.id("unit");
    private By thumbnailInput = By.id("thumbnail");
    private By addVariationButton = By.id("addVariationBtn");
    private By variationNameInput = By.cssSelector("input.variation-name");
    private By variationPriceInput = By.cssSelector("input.variation-price");
    private By variationStockInput = By.cssSelector("input.variation-stock");
    private By variationWeightInput = By.cssSelector("input.variation-weight");
    private By variationMinQtyInput = By.cssSelector("input.variation-min-qty");
    private By variationDefaultRadio = By.cssSelector("input.variation-default");
    private By simpanButton = By.cssSelector("button.swal2-confirm");
    // Updated locator for SweetAlert2 success message
    private By successMessage = By.cssSelector("div.swal2-html-container");
    public ModalEditProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verifikasi modal Edit Produk terlihat
    public boolean isModalVisible() {
        try {
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
            return modal.isDisplayed();
        } catch (Exception e) {
            System.out.println("Modal visibility check failed: " + e.getMessage());
            return false;
        }
    }

    // Pilih kategori produk
    public void selectCategory(String categoryId) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(categoryDropdown));
        dropdown.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[@id='category_id']/option[@value='" + categoryId + "']")));
        option.click();
    }

    // Masukkan nama produk
    public void enterProductName(String name) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInput));
        input.clear();
        input.sendKeys(name);
    }

    // Masukkan deskripsi produk
    public void enterDescription(String description) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionInput));
        input.clear();
        input.sendKeys(description);
    }

    // Masukkan unit produk
    public void enterUnit(String unit) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(unitInput));
        input.clear();
        input.sendKeys(unit);
    }

    // Unggah thumbnail produk
    public void uploadThumbnail(String filePath) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(thumbnailInput));
        input.sendKeys(filePath);
    }

    // Klik tombol Tambah Variasi
    public void clickAddVariationButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addVariationButton));
        button.click();
    }

    // Isi detail variasi produk
    public void fillVariationDetails(String name, String price, String stock, String weight, String minQty, boolean isDefault) {
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(variationNameInput));
        WebElement priceInput = wait.until(ExpectedConditions.visibilityOfElementLocated(variationPriceInput));
        WebElement stockInput = wait.until(ExpectedConditions.visibilityOfElementLocated(variationStockInput));
        WebElement weightInput = wait.until(ExpectedConditions.visibilityOfElementLocated(variationWeightInput));
        WebElement minQtyInput = wait.until(ExpectedConditions.visibilityOfElementLocated(variationMinQtyInput));
        WebElement defaultRadio = wait.until(ExpectedConditions.elementToBeClickable(variationDefaultRadio));

        nameInput.clear();
        nameInput.sendKeys(name);
        priceInput.clear();
        priceInput.sendKeys(price);
        stockInput.clear();
        stockInput.sendKeys(stock);
        weightInput.clear();
        weightInput.sendKeys(weight);
        minQtyInput.clear();
        minQtyInput.sendKeys(minQty);
        if (isDefault) {
            defaultRadio.click();
        }
    }

    // Klik tombol Simpan
    public void clickSimpanButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(simpanButton));
        button.click();
    }

    // Verifikasi pesan sukses
    public boolean isSuccessMessageVisible() {
        try {
            // Wait for the SweetAlert2 modal to be visible
            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return success.isDisplayed();
        } catch (Exception e) {
            System.out.println("Failed to detect success message: " + e.getMessage());
            System.out.println("Page source at failure: " + driver.getPageSource());
            return false;
        }
    }
}