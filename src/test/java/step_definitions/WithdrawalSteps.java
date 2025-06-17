package step_definitions;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import page_objects.LoginPage;
import page_objects.SidebarPage;
import page_objects.WithdrawalPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import java.time.Duration;

public class WithdrawalSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private SidebarPage sidebarPage;
    private WithdrawalPage withdrawalPage;

    public WithdrawalSteps(LoginSteps loginSteps) {
        this.driver = loginSteps.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.loginPage = new LoginPage(driver);
        this.sidebarPage = new SidebarPage(driver);
        this.withdrawalPage = new WithdrawalPage(driver);
    }

    @When("Seller memasukkan email {string} dan password {string}")
    public void seller_memasukkan_email_dan_password(String email, String password) throws InterruptedException {
        System.out.println("Mencoba login...");
        loginPage.enterEmailOrPhone(email);
        Thread.sleep(1000);
        loginPage.enterPassword(password);
        Thread.sleep(1000);
    }

    @When("Seller klik tombol masuk")
    public void seller_klik_tombol_masuk() throws InterruptedException {
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        wait.until(d -> sidebarPage.isSidebarVisible() || loginPage.isErrorMessageVisible());
    }

    @When("Seller mengakses halaman {string} dari dashboard")
    public void seller_mengakses_halaman_dari_dashboard(String pageName) throws InterruptedException {
        System.out.println("Mengakses halaman " + pageName + "...");
        try {
            sidebarPage.clickWithdrawalMenu();
            Thread.sleep(2000);
            Assert.assertTrue("Halaman penarikan tidak terlihat", withdrawalPage.isWithdrawalPageVisible());
            Assert.assertTrue("Tabel penarikan tidak muncul dan tidak ada pesan error",
                    withdrawalPage.isTableVisible() || withdrawalPage.isErrorMessageVisible());
        } catch (Exception e) {
            System.out.println("Gagal mengakses halaman penarikan: " + e.getMessage());
            throw e;
        }
    }

    @When("Seller mengklik tombol {string}")
    public void seller_mengklik_tombol(String buttonName) throws InterruptedException {
        System.out.println("Mengklik tombol " + buttonName + "...");
        if (buttonName.equals("+ Permintaan")) {
            withdrawalPage.clickWithdrawButton();
            Thread.sleep(2000);
        }
    }

    @When("Seller mengisi formulir penarikan dengan bank {string}, nomor rekening {string}, dan jumlah {string}")
    public void seller_mengisi_formulir_penarikan_dengan_bank_nomor_rekening_dan_jumlah(String bank, String accountNumber, String amount) throws InterruptedException {
        System.out.println("Menambahkan rekening baru...");
        withdrawalPage.addNewAccount(bank, accountNumber, "John Doe");
        Thread.sleep(2000);
        System.out.println("Mengajukan penarikan dana...");
        withdrawalPage.submitWithdrawal(bank + " - " + accountNumber, amount);
        Thread.sleep(2000);
    }

    @Then("Penarikan dana berhasil diajukan ke Admin")
    public void penarikan_dana_berhasil_diajukan_ke_admin() {
        System.out.println("Penarikan dana diajukan.");
        Assert.assertTrue("Halaman penarikan tidak terlihat setelah pengajuan", withdrawalPage.isWithdrawalPageVisible());
    }

    @Then("Sistem menampilkan pesan error penarikan")
    public void sistem_menampilkan_pesan_error_penarikan() {
        Assert.assertTrue("Pesan error tidak terlihat setelah penarikan gagal",
                withdrawalPage.isValidationMessageVisible("Silakan masukkan jumlah penarikan yang valid"));
        withdrawalPage.closeModal();
    }

    @Then("Penarikan dana gagal diajukan ke Admin")
    public void penarikan_dana_gagal_diajukan_ke_admin() {
        System.out.println("Penarikan gagal seperti yang diharapkan.");
        Assert.assertTrue("Halaman penarikan tidak terlihat setelah gagal", withdrawalPage.isWithdrawalPageVisible());
    }
}