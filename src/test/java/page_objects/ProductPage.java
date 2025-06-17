package page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for Product Management page
    private By productManagementTitle = By.xpath("//h2[contains(text(), 'Manajemen Produk')]");
    private By statusFilterDropdown = By.cssSelector("select.w-60.p-2.border.border-gray-300.rounded-md");
    private By productTable = By.cssSelector("table.w-full.table-auto.border-collapse");
    private By loadingSpinner = By.cssSelector("div.animate-spin.h-8.w-8.border-4.border-red-600");
    private By addProductButton = By.cssSelector("button.bg-red-600");
    private By statusDropdown = By.cssSelector("td:nth-child(9) select.status-dropdown");
    private By confirmStatusButton = By.cssSelector(".swal2-confirm");
    private By successMessageText = By.xpath("//div[contains(@class, 'swal2-success')]//h2[contains(text(), 'Berhasil!') and following-sibling::div[contains(text(), 'Status produk berhasil diubah')]]");

    // Locators for Add/Edit Product Modal
    private By addModalTitle = By.cssSelector(".swal2-title");
    private By editModalTitle = By.xpath("//h3[contains(@class, 'text-lg font-bold') and contains(text(), 'Edit Produk')]");
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
    private By cancelButton = By.cssSelector(".swal2-cancel");
    private By successMessage = By.cssSelector("div.swal2-html-container");
    private By errorMessage = By.cssSelector(".swal2-validation-message");

    // Constructor
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // --- Product Management Page Methods ---

    public boolean isProductPageVisible() {
        try {
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productManagementTitle));
            return titleElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoadingSpinnerGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProductTableVisible() {
        try {
            WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productTable));
            return tableElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectStatusFilter(String status) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(statusFilterDropdown));
        dropdown.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[contains(@class, 'w-60 p-2 border border-gray-300 rounded-md')]/option[@value='" + status + "']")));
        option.click();
    }

    public boolean isStatusProductsVisible(String status) {
        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(productTable));
            List<WebElement> statusCells = table.findElements(By.cssSelector("td:nth-child(9) select"));
            if (statusCells.isEmpty()) {
                return status.equals("all") ? true : false;
            }
            return status.equals("all") || statusCells.stream().allMatch(cell -> cell.getAttribute("value").equalsIgnoreCase(status));
        } catch (Exception e) {
            return false;
        }
    }

    public void changeProductStatus(String productName, String newStatus) {
        WebElement productRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody/tr[td[2][text()='" + productName + "']]")));
        WebElement statusDropdownElement = productRow.findElement(By.cssSelector("select.status-dropdown"));
        wait.until(ExpectedConditions.elementToBeClickable(statusDropdownElement));
        statusDropdownElement.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tbody/tr[td[2][text()='" + productName + "']]//select[contains(@class, 'status-dropdown')]/option[@value='" + newStatus + "']")));
        option.click();
        try {
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(confirmStatusButton));
            confirmButton.click();
        } catch (Exception e) {
            System.out.println("Modal konfirmasi tidak muncul atau sudah dikonfirmasi: " + e.getMessage());
        }
    }

    public boolean isStatusChangeSuccessVisible() {
        try {
            WebElement successElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageText));
            return successElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getProductStatus(String productName) {
        WebElement productRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody/tr[td[2][text()='" + productName + "']]")));
        WebElement statusDropdownElement = productRow.findElement(By.cssSelector("select.status-dropdown"));
        return statusDropdownElement.getAttribute("value");
    }

    public Map<String, String> getProductDetails(String productName) {
        WebElement productRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody/tr[td[2][text()='" + productName + "']]")));
        Map<String, String> details = new HashMap<>();
        details.put("category", productRow.findElement(By.cssSelector("td:nth-child(1)")).getText());
        details.put("name", productRow.findElement(By.cssSelector("td:nth-child(2)")).getText());
        details.put("description", productRow.findElement(By.cssSelector("td:nth-child(3)")).getText());
        details.put("stock", productRow.findElement(By.cssSelector("td:nth-child(4)")).getText());
        details.put("price", productRow.findElement(By.cssSelector("td:nth-child(5)")).getText());
        details.put("variant", productRow.findElement(By.cssSelector("td:nth-child(6)")).getText());
        details.put("options", productRow.findElement(By.cssSelector("td:nth-child(7)")).getText());
        details.put("status", productRow.findElement(By.cssSelector("td:nth-child(9) select.status-dropdown")).getAttribute("value"));
        return details;
    }

    // --- Add Product Modal Methods ---

    public void clickAddProductButton() {
        // Ensure any existing modals are closed
        closeAnyOpenModal();
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addProductButton));
        button.click();
    }

    public boolean isAddModalVisible() {
        try {
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(addModalTitle));
            return titleElement.isDisplayed() && titleElement.getText().equals("Tambah Produk Baru");
        } catch (Exception e) {
            return false;
        }
    }

    // --- Edit Product Modal Methods ---

    public void clickEditProductButton(String productName) {
        WebElement editButton = driver.findElement(By.xpath(
                "//tbody/tr[td[2][text()='" + productName + "']]//button[contains(@class, 'text-black-500') and .//i[contains(@class, 'fa-pen-to-square')]]"));
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
    }

    public boolean isEditModalVisible() {
        try {
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(editModalTitle));
            return modal.isDisplayed();
        } catch (Exception e) {
            System.out.println("Modal visibility check failed: " + e.getMessage());
            return false;
        }
    }

    // --- Shared Modal Methods ---

    public void selectCategory(String categoryId) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(categoryDropdown));
        dropdown.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[@id='category_id']/option[@value='" + categoryId + "']")));
        option.click();
    }

    public void enterProductName(String name) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(productNameInput));
        input.clear();
        input.sendKeys(name);
    }

    public void enterDescription(String description) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(descriptionTextarea));
        input.clear();
        input.sendKeys(description);
    }

    public void enterUnit(String unit) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(unitInput));
        input.clear();
        input.sendKeys(unit);
    }

    public void uploadThumbnail(String filePath) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(thumbnailInput));
        input.sendKeys(filePath);
    }

    public void clickAddVariationButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(addVariationButton));
        button.click();
    }

    public void fillVariationDetails(String name, String price, String stock, String weight, String minQty, boolean isDefault) {
        WebElement variationName = wait.until(ExpectedConditions.visibilityOfElementLocated(variationNameInput));
        variationName.sendKeys(name);

        WebElement variationPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(variationPriceInput));
        variationPrice.clear();
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

    public void clickSaveButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        button.click();
    }

    public void clickCancelButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
            button.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(cancelButton));
        } catch (Exception e) {
            System.out.println("No cancel button found or modal already closed: " + e.getMessage());
        }
    }

    public boolean isSuccessMessageVisible() {
        try {
            WebElement success = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return success.isDisplayed();
        } catch (Exception e) {
            System.out.println("Failed to detect success message: " + e.getMessage());
            return false;
        }
    }

    public boolean isErrorMessageVisible() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void closeAnyOpenModal() {
        try {
            WebElement modalBackdrop = driver.findElement(By.cssSelector(".swal2-backdrop-show"));
            if (modalBackdrop.isDisplayed()) {
                clickCancelButton();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".swal2-container")));
            }
        } catch (Exception e) {
            // No modal open, proceed
        }
    }
}