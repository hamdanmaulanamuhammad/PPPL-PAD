package step_definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import java.time.Duration;

public class LoginSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

    public LoginSteps() {
        // Inisiasi WebDriver seperti di SellerFlowTest
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        driver.get("http://localhost:5173/login");
    }

    @Given("Seller berada di halaman login")
    public void sellerOnLoginPage() {
        Assert.assertTrue(loginPage.isLoginPageVisible(), "Halaman login tidak terlihat");
    }

    @When("Seller memasukkan email/username/nomor telepon dan password")
    public void sellerEntersCredentials() {
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("admin#123");
    }

    @When("Seller memasukkan email/username/nomor telepon, tanpa password")
    public void sellerEntersCredentialsWithoutPassword() {
        loginPage.enterEmailOrPhone("dummyuser@example.com");
        loginPage.enterPassword("");
    }

    @When("Seller klik tombol \"Masuk\"")
    public void sellerClicksLogin() {
        loginPage.clickLoginButton();
        try {
            Thread.sleep(2000); // Menunggu respons, sesuaikan dengan kebutuhan
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("Seller masuk ke halaman dashboard")
    public void sellerOnDashboard() {
        // Asumsi Anda memiliki metode di SidebarPage atau DashboardPage
        SidebarPage sidebarPage = new SidebarPage(driver); // Inisiasi tambahan jika perlu
        Assert.assertTrue(sidebarPage.isSidebarVisible(), "Sidebar tidak terlihat setelah login berhasil");
    }

    @Then("Sistem menampilkan pesan error dan memberikan penanda pada kolom password")
    public void systemShowsErrorOnPasswordField() {
        Assert.assertTrue(loginPage.isErrorMessageVisible(), "Pesan error tidak terlihat setelah login gagal");
        // Penanda pada kolom password bisa dicek dengan logika tambahan jika ada
    }

    @Then("Seller gagal login")
    public void sellerFailsLogin() {
        Assert.assertFalse(sidebarPage.isSidebarVisible(), "Login seharusnya gagal, tetapi sidebar terlihat");
    }

    @Then("Seller masuk ke halaman detail produk setelah mengakses salah satu produk")
    public void sellerEntersDetailProduct() {
        // Asumsi ada logika untuk akses produk (bisa ditambahkan di DashboardPage atau Page lain)
        DashboardPage dashboardPage = new DashboardPage(driver); // Inisiasi tambahan jika perlu
        dashboardPage.clickProductLink(); // Metode hipotetis, sesuaikan dengan POM Anda
        Assert.assertTrue(dashboardPage.isProductDetailDisplayed(), "Halaman detail produk tidak terlihat");
    }

    // Cleanup setelah setiap skenario (opsional, bisa dipindah ke hook)
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}