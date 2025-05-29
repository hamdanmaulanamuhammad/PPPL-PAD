import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SidebarPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By sidebar = By.id("sidebar");
    private By dashboardMenu = By.xpath("//a[contains(text(), 'Dashboard')]");
    private By withdrawalMenu = By.xpath("//a[contains(text(), 'Ajukan Penarikan')]");

    // Konstruktor
    public SidebarPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verifikasi sidebar terlihat
    public boolean isSidebarVisible() {
        WebElement sidebarElement = wait.until(ExpectedConditions.visibilityOfElementLocated(sidebar));
        return sidebarElement.isDisplayed();
    }

    // Klik menu Dashboard
    public void clickDashboardMenu() {
        driver.findElement(dashboardMenu).click();
    }

    // Klik menu Ajukan Penarikan
    public void clickWithdrawalMenu() {
        driver.findElement(withdrawalMenu).click();
    }
}