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

    @Given("Seller berada di halaman login untuk dashboard")
    public void seller_berada_di_halaman_login_untuk_dashboard() {
        System.out.println("Memeriksa halaman login untuk akses dashboard...");
        Assert.assertTrue("Halaman login tidak terlihat", loginPage.isLoginPageVisible());
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @When("Seller memasukkan kredensial valid untuk dashboard")
    public void seller_memasukkan_kredensial_valid_untuk_dashboard() {
        System.out.println("Mencoba login berhasil...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        loginPage.enterPassword("admin#123");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @When("Seller klik tombol masuk untuk dashboard")
    public void seller_klik_tombol_masuk_untuk_dashboard() {
        loginPage.clickLoginButton();
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
        wait.until(d -> sidebarPage.isSidebarVisible() || loginPage.isErrorMessageVisible());
        Assert.assertTrue("Sidebar tidak terlihat setelah login berhasil", sidebarPage.isSidebarVisible());
    }

    @Then("Seller berhasil masuk ke halaman dashboard")
    public void sellerSuccessfullyOnDashboard() {
        if (sidebarPage.isSidebarVisible()) {
            sidebarPage.toggleSidebarIfMobile();
            sidebarPage.dismissOverlayIfPresent();
            Assert.assertTrue("Sidebar tidak terlihat setelah login berhasil", sidebarPage.isSidebarVisible());
        } else {
            System.out.println("Sidebar tidak tersedia di halaman ini.");
        }
    }

    @When("Seller mengakses halaman dashboard")
    public void sellerAccessesDashboardPage() {
        System.out.println("Mengakses halaman dashboard...");
        sidebarPage.clickDashboardMenu();
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Then("Halaman dashboard terlihat")
    public void dashboardPageIsVisible() {
        Assert.assertTrue("Halaman dashboard tidak terlihat", dashboardPage.isDashboardPageVisible());
    }

    @Then("Statistik card terlihat")
    public void statCardIsVisible() {
        Assert.assertTrue("Statistik card tidak terlihat", dashboardPage.isStatCardVisible());
    }

    @Then("Grafik terlihat")
    public void chartIsVisible() {
        Assert.assertTrue("Grafik tidak terlihat", dashboardPage.isChartVisible());
    }

    @Then("Statistik dashboard ditampilkan")
    public void dashboardStatsAreDisplayed() {
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