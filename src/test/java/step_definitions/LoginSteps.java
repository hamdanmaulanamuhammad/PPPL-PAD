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
        Assert.assertTrue("Halaman login tidak terlihat", loginPage.isLoginPageVisible());
    }

    @When("Seller memasukkan email/username/nomor telepon dan password")
    public void sellerEntersCredentials() {
        // Hardcode kredensial untuk skenario login berhasil
        // Ganti dengan kredensial yang valid sesuai aplikasi Anda
        loginPage.enterEmailOrPhone("valid@example.com");
        loginPage.enterPassword("validpassword123");
    }

    @When("Seller memasukkan email/username/nomor telepon, tanpa password")
    public void sellerEntersCredentialsWithoutPassword() {
        loginPage.enterEmailOrPhone("invalid@example.com");
        loginPage.enterPassword("");
    }

    @When("Seller klik tombol \"Masuk\"")
    public void sellerClicksLogin() {
        loginPage.clickLoginButton();
        // Tunggu hingga sidebar terlihat (login berhasil) atau pesan error muncul (login gagal)
        wait.until(d -> sidebarPage.isSidebarVisible() || loginPage.isErrorMessageVisible());
    }

    @Then("Seller masuk ke halaman dashboard")
    public void sellerOnDashboard() {
        sidebarPage.toggleSidebarIfMobile(); // Pastikan sidebar terlihat di mobile
        sidebarPage.dismissOverlayIfPresent(); // Tutup overlay jika ada
        Assert.assertTrue("Sidebar tidak terlihat setelah login berhasil", sidebarPage.isSidebarVisible());
        Assert.assertTrue("Halaman dashboard tidak terlihat", dashboardPage.isDashboardPageVisible());
    }

    @Then("Sistem menampilkan pesan error dan memberikan penanda pada kolom password")
    public void systemShowsErrorOnPasswordField() {
        Assert.assertTrue("Pesan error tidak terlihat setelah login gagal", loginPage.isErrorMessageVisible());
        // Verifikasi penanda pada kolom password (misalnya, border merah)
        Assert.assertTrue("Kolom password tidak memiliki penanda error", loginPage.isPasswordFieldHighlighted());
        // Tutup modal error
        loginPage.clickTryAgainButton();
    }

    @Then("Seller gagal login")
    public void sellerFailsLogin() {
        Assert.assertFalse("Sidebar terlihat padahal login gagal", sidebarPage.isSidebarVisible());
        Assert.assertTrue("Halaman login masih harus terlihat", loginPage.isLoginPageVisible());
    }

    @After
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}