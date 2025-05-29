import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By dashboardTitle = By.xpath("//h2[contains(text(), 'Dashboard')]");
    private By statCardContainer = By.xpath("//div[contains(@class, 'grid') and contains(@class, 'grid-cols')]");
    private By statCardTitle = By.xpath(".//h3");
    private By statCardValue = By.xpath(".//p[contains(@class, 'text-red-600')]");
    private By chartContainer = By.id("orderChart");

    // Konstruktor
    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Verifikasi halaman dashboard terlihat
    public boolean isDashboardPageVisible() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardTitle));
            return title.isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memverifikasi halaman dashboard: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi statistik card terlihat
    public boolean isStatCardVisible() {
        try {
            WebElement statTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(statCardTitle));
            return statTitle.isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memverifikasi statistik card: " + e.getMessage());
            return false;
        }
    }

    // Verifikasi grafik terlihat
    public boolean isChartVisible() {
        try {
            WebElement chart = wait.until(ExpectedConditions.visibilityOfElementLocated(chartContainer));
            return chart.isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memverifikasi grafik: " + e.getMessage());
            return false;
        }
    }

    // Metode untuk mengambil dan mencetak semua data StatCard ke terminal
    public void printDashboardStats() {
        try {
            // Tunggu hingga container StatCard terlihat
            WebElement statCardGrid = wait.until(ExpectedConditions.visibilityOfElementLocated(statCardContainer));

            // Ambil semua StatCard di dalam grid
            List<WebElement> statCards = statCardGrid.findElements(By.xpath(".//div[contains(@class, 'stat-card')]"));

            System.out.println("=== Statistik Dashboard ===");
            for (WebElement card : statCards) {
                WebElement title = card.findElement(statCardTitle);
                WebElement value = card.findElement(statCardValue);
                String titleText = title.getText().trim();
                String valueText = value.getText().trim();
                System.out.println(titleText + ": " + valueText);
            }
            System.out.println("===========================");
        } catch (Exception e) {
            System.out.println("Gagal mengambil data StatCard: " + e.getMessage());
        }
    }
}