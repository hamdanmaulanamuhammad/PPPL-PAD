package step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import page_objects.LoginPage;
import page_objects.SidebarPage;
import page_objects.DashboardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import java.time.Duration;

public class DashboardSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private DashboardPage dashboardPage;

    public DashboardSteps() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        sidebarPage = new SidebarPage(driver);
        dashboardPage = new DashboardPage(driver);
        driver.get("http://localhost:5173/login");
    }

    @Given("Seller berada di halaman login")
    public void sellerOnLoginPage() {
        System.out.println("Memeriksa halaman login...");
        Assert.assertTrue("Halaman login tidak terlihat", loginPage.isLoginPageVisible());
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @When("Seller memasukkan email\\/username\\/nomor telepon dan password")
    public void sellerMemasukkanEmailUsernameNomorTeleponDanPassword() {
        System.out.println("Mencoba login berhasil...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        loginPage.enterPassword("admin#123");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @When("Seller klik tombol \"Masuk\"")
    public void sellerClicksLogin() {
        loginPage.clickLoginButton();
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
        wait.until(d -> sidebarPage.isSidebarVisible() || loginPage.isErrorMessageVisible());
    }

    @Then("Seller masuk ke halaman dashboard")
    public void sellerOnDashboard() {
        if (sidebarPage.isSidebarVisible()) {
            sidebarPage.toggleSidebarIfMobile();
            sidebarPage.dismissOverlayIfPresent();
            Assert.assertTrue("Sidebar tidak terlihat setelah login berhasil", sidebarPage.isSidebarVisible());
        } else {
            System.out.println("Sidebar tidak tersedia di halaman ini.");
        }
    }

    @When("Seller mengakses halaman dashboard")
    public void sellerMengaksesHalamanDashboard() {
        System.out.println("Mengakses halaman dashboard...");
        sidebarPage.clickDashboardMenu();
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Then("Halaman dashboard terlihat")
    public void halamanDashboardTerlihat() {
        Assert.assertTrue("Halaman dashboard tidak terlihat", dashboardPage.isDashboardPageVisible());
    }

    @Then("Statistik card terlihat")
    public void statistikCardTerlihat() {
        Assert.assertTrue("Statistik card tidak terlihat", dashboardPage.isStatCardVisible());
    }

    @Then("Grafik terlihat")
    public void grafikTerlihat() {
        Assert.assertTrue("Grafik tidak terlihat", dashboardPage.isChartVisible());
    }

    @Then("Statistik dashboard ditampilkan")
    public void statistikDashboardDitampilkan() {
        dashboardPage.printDashboardStats();
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @After
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}