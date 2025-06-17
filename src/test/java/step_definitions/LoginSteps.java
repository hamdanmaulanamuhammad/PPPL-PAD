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

public class LoginSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private DashboardPage dashboardPage;

    public LoginSteps() {
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

    @When("Seller memasukkan email\\/username\\/nomor telepon, tanpa password")
    public void sellerMemasukkanEmailUsernameNomorTeleponTanpaPassword() {
        System.out.println("Mencoba login gagal...");
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        loginPage.enterPassword("");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
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

    @Then("Sistem menampilkan pesan error dan memberikan penanda pada kolom password")
    public void systemShowsErrorOnPasswordField() {
        Assert.assertTrue("Pesan error tidak terlihat setelah login gagal", loginPage.isErrorMessageVisible());
        loginPage.clickTryAgainButton();
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    @Then("Seller gagal login")
    public void sellerFailsLogin() {
        Assert.assertFalse("Sidebar terlihat padahal login gagal", sidebarPage.isSidebarVisible());
        Assert.assertTrue("Halaman login masih harus terlihat", loginPage.isLoginPageVisible());
    }

    @Then("Seller masuk ke halaman dashboard")
    public void sellerOnDashboard() {
        // Kosong karena dicek di CommonSteps
    }

    public WebDriver getDriver() {
        return driver;
    }

    @After
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}