package page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ModalAddProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for the modal elements
    private By modalTitle = By.cssSelector(".swal2-title");
    private By categoryDropdown = By.id("category_id");
    private By productNameInput = By.id("productName");
    private By descriptionTextarea = By.id("description");
    private By unitInput = By.id("unit");
    private By thumbnailInput = By.id("thumbnail");
    private By addVariationButton = By.id("addVariationBtn");
    private By variationNameInput = By.cssSelector(".variation-box:last-child .variation-name");
    private By variationPriceInput = By.cssSelector(".variation-box:last-child .variation-price");
    private By variationStockInput = By.cssSelector(".variation-box:last-child .variation-stock");
    private By variationWeightInput = By.cssSelector(".variation-box:last-child .variation-weight");
    private By variationMinQtyInput = By.cssSelector(".variation-box:last-child .variation-min-qty");
    private By variationDefaultRadio = By.cssSelector(".variation-box:last-child .variation-default");
    private By confirmButton = By.cssSelector(".swal2-confirm");
    private By successMessage =By.cssSelector("div.swal2-html-container");
    // Constructor
    public ModalAddProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verify modal is open
    public boolean isModalVisible() {
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
        return titleElement.isDisplayed() && titleElement.getText().equals("Tambah Produk Baru");
    }

    // Select category from dropdown
    public void selectCategory(String categoryId) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(categoryDropdown));
        dropdown.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[@id='category_id']/option[@value='" + categoryId + "']")));
        option.click();
    }

    // Enter product name
    public void enterProductName(String name) {
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInput));
        nameInput.clear();
        nameInput.sendKeys(name);
    }

    // Enter product description
    public void enterDescription(String description) {
        WebElement descInput = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionTextarea));
        descInput.clear();
        descInput.sendKeys(description);
    }

    // Enter unit
    public void enterUnit(String unit) {
        WebElement unitInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(unitInput));
        unitInputElement.clear();
        unitInputElement.sendKeys(unit);
    }

    // Upload thumbnail
    public void uploadThumbnail(String filePath) {
        WebElement thumbnailInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(thumbnailInput));
        thumbnailInputElement.sendKeys(filePath);
    }

    // Click Add Variation button
    public void clickAddVariationButton() {
        WebElement addVariationBtn = wait.until(ExpectedConditions.elementToBeClickable(addVariationButton));
        addVariationBtn.click();
    }

    // Fill variation details
    public void fillVariationDetails(String name, String price, String stock, String weight, String minQty, boolean isDefault) {
        WebElement variationName = wait.until(ExpectedConditions.visibilityOfElementLocated(variationNameInput));
        variationName.sendKeys(name);

        WebElement variationPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(variationPriceInput));
        variationPrice.sendKeys(price);

        WebElement variationStock = wait.until(ExpectedConditions.visibilityOfElementLocated(variationStockInput));
        variationStock.sendKeys(stock);

        WebElement variationWeight = wait.until(ExpectedConditions.visibilityOfElementLocated(variationWeightInput));
        variationWeight.sendKeys(weight);

        WebElement variationMinQty = wait.until(ExpectedConditions.visibilityOfElementLocated(variationMinQtyInput));
        variationMinQty.sendKeys(minQty);

        if (isDefault) {
            WebElement variationDefault = wait.until(ExpectedConditions.elementToBeClickable(variationDefaultRadio));
            variationDefault.click();
        }
    }

    // Click Tambah button
    public void clickTambahButton() {
        WebElement tambahButton = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        tambahButton.click();
    }

    // Verify success message
    public boolean isSuccessMessageVisible() {
        try {
            WebElement successElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return successElement.isDisplayed() && successElement.getText().contains("Berhasil");
        } catch (Exception e) {
            return false;
        }
    }
}