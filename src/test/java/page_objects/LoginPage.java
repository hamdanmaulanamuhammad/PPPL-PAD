package page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By emailOrPhoneField = By.id("phone");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[contains(text(), 'Masuk')]");
    private By errorMessageLocator = By.cssSelector("div.swal2-html-container");
    private By tryAgainButton = By.cssSelector("button.swal2-confirm.bg-red-600");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isLoginPageVisible() {
        try {
            WebElement loginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailOrPhoneField));
            return loginInput.isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memeriksa halaman login: " + e.getMessage());
            return false;
        }
    }

    public void enterEmailOrPhone(String emailOrPhone) {
        try {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(emailOrPhoneField));
            field.clear();
            field.sendKeys(emailOrPhone);
        } catch (Exception e) {
            System.out.println("Gagal memasukkan email/phone: " + e.getMessage());
        }
    }

    public void enterPassword(String password) {
        try {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
            field.clear();
            field.sendKeys(password);
        } catch (Exception e) {
            System.out.println("Gagal memasukkan password: " + e.getMessage());
        }
    }

    public void clickLoginButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            button.click();
        } catch (Exception e) {
            System.out.println("Gagal mengklik tombol login: " + e.getMessage());
            throw e;
        }
    }

    public boolean isErrorMessageVisible() {
        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            return errorMessage.isDisplayed() &&
                    errorMessage.getText().contains("Periksa kembali email/username dan password Anda.");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa pesan error: " + e.getMessage());
            return false;
        }
    }

    public void clickTryAgainButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(tryAgainButton));
            button.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.swal2-container")));
        } catch (Exception e) {
            System.out.println("Gagal mengklik tombol 'Coba Lagi': " + e.getMessage());
            throw e;
        }
    }

    // Metode baru untuk memverifikasi penanda pada kolom password
    public boolean isPasswordFieldHighlighted() {
        try {
            WebElement errorModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.swal2-html-container")));
            return errorModal.isDisplayed() && errorModal.getText().contains("Periksa kembali email/username dan password Anda.");
        } catch (Exception e) {
            System.out.println("Gagal memeriksa modal error: " + e.getMessage());
            return false;
        }
    }
}