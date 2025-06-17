package page_objects;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WithdrawalPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Lokator elemen
    private By withdrawalTitle = By.xpath("//h2[contains(text(), 'Penarikan Dana')]");
    private By withdrawButton = By.xpath("//button[span[contains(text(), 'Tarik Dana')]]");
    private By withdrawalModal = By.cssSelector("div.swal2-popup");
    private By addAccountButton = By.id("add-account-btn");
    private By newBankSelect = By.id("new-bank");
    private By newAccountNumber = By.id("new-account-number");
    private By newAccountName = By.id("new-account-name");
    private By saveAccountButton = By.xpath("//button[contains(text(), 'Simpan')]");
    private By accountSelect = By.id("account-select");
    private By amountInput = By.id("amount");
    private By submitWithdrawalButton = By.xpath("//button[contains(text(), 'Ajukan Penarikan')]");
    private By cancelButton = By.xpath("//button[contains(text(), 'Batal')]");
    private By statusFilter = By.cssSelector("select");
    private By tableRows = By.cssSelector("table tbody tr");
    private By errorMessageLocator = By.xpath("//div[contains(@class, 'bg-red-100')]");
    private By validationMessageLocator = By.cssSelector("div.swal2-validation-message"); // Lokator baru untuk pesan error modal

    // Konstruktor
    public WithdrawalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Verifikasi halaman penarikan terlihat
    public boolean isWithdrawalPageVisible() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(withdrawalTitle));
            return title.isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memverifikasi halaman penarikan: " + e.getMessage());
            return false;
        }
    }

    // Klik tombol Tarik Dana
    public void clickWithdrawButton() throws InterruptedException {
        try {
            System.out.println("Mencoba mengklik tombol Tarik Dana...");
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(withdrawButton));
            button.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(withdrawalModal));
            System.out.println("Tombol Tarik Dana berhasil diklik.");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Gagal mengklik tombol Tarik Dana: " + e.getMessage());
            throw e;
        }
    }

    // Pilih status dari dropdown
    public void selectStatus(String status) {
        try {
            WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(statusFilter));
            new Select(dropdown).selectByVisibleText(status);
        } catch (Exception e) {
            System.out.println("Gagal memilih status: " + e.getMessage());
            throw e;
        }
    }

    // Ambil jumlah baris tabel
    public boolean isTableVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tableRows)).size() > 0;
        } catch (Exception e) {
            System.out.println("Gagal memverifikasi tabel: " + e.getMessage());
            return false;
        }
    }

    // Ambil pesan error pada halaman
    public boolean isErrorMessageVisible() {
        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            System.out.println("Gagal memeriksa pesan error: " + e.getMessage());
            return false;
        }
    }

    // Ambil pesan validasi pada modal
    public boolean isValidationMessageVisible(String expectedMessage) {
        try {
            WebElement validationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(validationMessageLocator));
            String actualMessage = validationMessage.getText();
            System.out.println("Pesan validasi ditemukan: " + actualMessage);
            return validationMessage.isDisplayed() && actualMessage.contains(expectedMessage);
        } catch (Exception e) {
            System.out.println("Gagal memeriksa pesan validasi: " + e.getMessage());
            return false;
        }
    }

    // Metode untuk simulasi slow typing
    private void slowType(WebElement element, String text) throws InterruptedException {
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            Thread.sleep(100);
        }
    }

    // Tambah rekening baru
    public void addNewAccount(String bankName, String accountNumber, String accountName) throws InterruptedException {
        try {
            System.out.println("Mengklik tombol + Tujuan Baru...");
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addAccountButton));
            addButton.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bankAccountForm")));
            Thread.sleep(1000);

            System.out.println("Memilih bank: " + bankName);
            Select bankDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(newBankSelect)));
            bankDropdown.selectByVisibleText(bankName);
            Thread.sleep(500);

            System.out.println("Mengisi nomor rekening: " + accountNumber);
            WebElement accountNumberField = driver.findElement(newAccountNumber);
            slowType(accountNumberField, accountNumber);
            Thread.sleep(500);

            System.out.println("Mengisi nama pemilik rekening: " + accountName);
            WebElement accountNameField = driver.findElement(newAccountName);
            slowType(accountNameField, accountName);
            Thread.sleep(500);

            System.out.println("Menyimpan rekening baru...");
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveAccountButton));
            saveButton.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("bankAccountForm")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(accountSelect));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Gagal menambahkan rekening baru: " + e.getMessage());
            throw e;
        }
    }

    // Ajukan penarikan dana
    public void submitWithdrawal(String accountOption, String amount) throws InterruptedException {
        try {
            System.out.println("Memilih rekening tujuan: " + accountOption);
            Select accountDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(accountSelect)));
            accountDropdown.selectByVisibleText(accountOption);
            Thread.sleep(500);

            System.out.println("Mengisi jumlah penarikan: " + amount);
            WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(amountInput));
            slowType(amountField, amount);
            Thread.sleep(500);

            System.out.println("Mengklik tombol Ajukan Penarikan...");
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(submitWithdrawalButton));
            submitButton.click();

            // Cek apakah pesan validasi muncul
            try {
                WebElement validationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(validationMessageLocator));
                if (validationMessage.isDisplayed()) {
                    System.out.println("Pesan validasi ditemukan: " + validationMessage.getText());
                    Thread.sleep(1000); // Tunggu sebentar untuk stabilitas
                    return; // Biarkan modal terbuka untuk validasi di WithdrawalSteps
                }
            } catch (Exception e) {
                System.out.println("Tidak ada pesan validasi, lanjutkan...");
            }

            // Jika tidak ada pesan validasi, tunggu modal hilang
            wait.until(ExpectedConditions.invisibilityOfElementLocated(withdrawalModal));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Gagal mengajukan penarikan: " + e.getMessage());
            throw e;
        }
    }

    // Tutup modal (opsional)
    public void closeModal() {
        try {
            WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(this.cancelButton));
            cancelButton.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(withdrawalModal));
        } catch (Exception e) {
            System.out.println("Gagal menutup modal: " + e.getMessage());
        }
    }
}