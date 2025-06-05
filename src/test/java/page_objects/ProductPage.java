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

    // Locators for elements on the Product Management page
    private By productManagementTitle = By.xpath("//h2[contains(text(), 'Manajemen Produk')]");
    private By statusFilterDropdown = By.cssSelector("select.w-60.p-2.border.border-gray-300.rounded-md");
    private By productTable = By.cssSelector("table.w-full.table-auto.border-collapse");
    private By loadingSpinner = By.cssSelector("div.animate-spin.h-8.w-8.border-4.border-red-600");
    private By statusDropdown = By.cssSelector("td:nth-child(9) select.status-dropdown");
    private By confirmStatusButton = By.cssSelector(".swal2-confirm");
    private By successMessageText = By.xpath("//div[@class='swal2-success']//div[@class='swal2-title' and contains(text(), 'Status produk berhasil diubah')]");

    // Constructor
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verify Product Management page is visible
    public boolean isProductPageVisible() {
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productManagementTitle));
        return titleElement.isDisplayed();
    }

    // Verify loading spinner is not visible
    public boolean isLoadingSpinnerGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Verify product table is visible
    public boolean isProductTableVisible() {
        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productTable));
        return tableElement.isDisplayed();
    }

    // Select status from the status filter dropdown
    public void selectStatusFilter(String status) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(statusFilterDropdown));
        dropdown.click();
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[contains(@class, 'w-60 p-2 border border-gray-300 rounded-md')]/option[@value='" + status + "']")));
        option.click();
    }

    // Verify products displayed match the selected status
    public boolean isStatusProductsVisible(String status) {
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(productTable));
        List<WebElement> statusCells = table.findElements(By.cssSelector("td:nth-child(9) select"));
        if (statusCells.isEmpty()) {
            return status.equals("all") ? true : false; // If no products, valid for 'all' filter
        }
        return status.equals("all") || statusCells.stream().allMatch(cell -> cell.getAttribute("value").equalsIgnoreCase(status));
    }

    // Change the status of a product by product name
    public void changeProductStatus(String productName, String newStatus) {
        // Find the product row by name
        WebElement productRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody/tr[td[2][text()='" + productName + "']]")));
        WebElement statusDropdownElement = productRow.findElement(By.cssSelector("td:nth-child(9) select.status-dropdown"));

        // Ensure dropdown is interactable
        wait.until(ExpectedConditions.elementToBeClickable(statusDropdownElement));
        statusDropdownElement.click();

        // Select the new status
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[contains(@class, 'status-dropdown')]/option[@value='" + newStatus + "']")));
        option.click();

        // Wait for and confirm the status change in the SweetAlert2 modal
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(confirmStatusButton));
        confirmButton.click();

        // Wait for the success message to ensure the status change is complete
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageText));
    }

    // Verify status change success message
    public boolean isStatusChangeSuccessVisible() {
        try {
            WebElement successElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessageText));
            return successElement.isDisplayed() && successElement.getText().contains("Status produk berhasil diubah");
        } catch (Exception e) {
            return false;
        }
    }

    // Get the current status of a product by name
    public String getProductStatus(String productName) {
        WebElement productRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody/tr[td[2][text()='" + productName + "']]")));
        WebElement statusDropdownElement = productRow.findElement(By.cssSelector("td:nth-child(9) select.status-dropdown"));
        return statusDropdownElement.getAttribute("value");
    }

    // Get product details by name to verify other columns
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
}