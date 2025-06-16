package page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SidebarPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for sidebar elements
    private By sidebarContainer = By.id("sidebar");
    private By sidebarToggleButton = By.cssSelector("button[aria-label='Toggle Sidebar'], button#sidebar-toggle, button.navbar-burger");
    private By dashboardMenu = By.xpath("//a[contains(translate(normalize-space(text()), ' ', ''), 'Dashboard')]");
    private By withdrawalMenu = By.xpath("//a[contains(translate(normalize-space(text()), ' ', ''), 'AjukanPenarikan')]");
    private By etalaseMenu = By.xpath("//a[contains(translate(normalize-space(text()), ' ', ''), 'Etalase')]");
    private By reportMenu = By.xpath("//a[contains(translate(normalize-space(text()), ' ', ''), 'Laporan')]");
    private By productMenu = By.xpath("//a[contains(translate(normalize-space(text()), ' ', ''), 'ManajemenProduk')]");

    // Constructor
    public SidebarPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Verify sidebar is visible
    public boolean isSidebarVisible() {
        try {
            WebElement sidebar = wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarContainer));
            return sidebar.isDisplayed();
        } catch (Exception e) {
            System.out.println("Sidebar not visible: " + e.getMessage());
            return false;
        }
    }

    // Toggle sidebar if on mobile view
    public void toggleSidebarIfMobile() {
        try {
            WebElement toggleButton = driver.findElement(sidebarToggleButton);
            if (toggleButton.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(toggleButton)).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(sidebarContainer));
                System.out.println("Sidebar toggled open.");
            }
        } catch (Exception e) {
            System.out.println("No sidebar toggle button found or not needed: " + e.getMessage());
        }
    }

    // Dismiss overlay if present
    public void dismissOverlayIfPresent() {
        try {
            WebElement overlay = driver.findElement(By.cssSelector("div.fixed.inset-0.bg-opacity-20.z-20"));
            if (overlay.isDisplayed()) {
                overlay.click();
                wait.until(ExpectedConditions.invisibilityOf(overlay));
                System.out.println("Overlay dismissed.");
            }
        } catch (Exception e) {
            System.out.println("No overlay found: " + e.getMessage());
        }
    }

    // Click the Dashboard menu
    public void clickDashboardMenu() {
        try {
            if (!isSidebarVisible()) {
                toggleSidebarIfMobile();
            }
            dismissOverlayIfPresent();
            WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(dashboardMenu));
            System.out.println("Dashboard menu found: " + menu.getText());
            menu.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Dashboard menu: " + e.getMessage(), e);
        }
    }

    // Click the Withdrawal menu
    public void clickWithdrawalMenu() {
        try {
            if (!isSidebarVisible()) {
                toggleSidebarIfMobile();
            }
            dismissOverlayIfPresent();
            WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(withdrawalMenu));
            System.out.println("Withdrawal menu found: " + menu.getText());
            menu.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Withdrawal menu: " + e.getMessage(), e);
        }
    }

    // Click the Etalase menu
    public void clickEtalaseMenu() {
        try {
            if (!isSidebarVisible()) {
                toggleSidebarIfMobile();
            }
            dismissOverlayIfPresent();
            WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(etalaseMenu));
            System.out.println("Etalase menu found: " + menu.getText());
            menu.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Etalase menu: " + e.getMessage(), e);
        }
    }

    // Click the Report menu
    public void clickReportMenu() {
        try {
            if (!isSidebarVisible()) {
                toggleSidebarIfMobile();
            }
            dismissOverlayIfPresent();
            WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(reportMenu));
            System.out.println("Report menu found: " + menu.getText());
            menu.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Report menu: " + e.getMessage(), e);
        }
    }

    // Click the Product Management menu
    public void clickProductMenu() {
        try {
            if (!isSidebarVisible()) {
                System.out.println("Sidebar not visible, attempting to toggle...");
                toggleSidebarIfMobile();
            }
            dismissOverlayIfPresent();
            WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(productMenu));
            System.out.println("Product menu found: " + menu.getText());
            menu.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Product Management menu: " + e.getMessage(), e);
        }
    }
}