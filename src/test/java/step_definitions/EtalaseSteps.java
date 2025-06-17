package step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page_objects.EtalasePage;
import page_objects.SidebarPage;
import org.openqa.selenium.WebDriver;
import org.junit.Assert;

public class EtalaseSteps {
    private final SidebarPage sidebarPage;
    private final EtalasePage etalasePage;

    public EtalaseSteps(LoginSteps loginSteps) {
        WebDriver driver = loginSteps.getDriver();
        this.sidebarPage = new SidebarPage(driver);
        this.etalasePage = new EtalasePage(driver);
    }

    @When("Seller mengakses halaman Etalase")
    public void seller_mengakses_halaman_etalase() throws InterruptedException {
        System.out.println("Mengakses halaman Etalase...");
        sidebarPage.clickEtalaseMenu();
        Thread.sleep(2000);
    }

    @Then("Halaman Etalase berhasil ditampilkan")
    public void halaman_etalase_berhasil_ditampilkan() {
        System.out.println("Halaman Etalase ditampilkan.");
        Assert.assertTrue("Halaman Etalase tidak terlihat", etalasePage.isEtalasePageVisible());
    }

    @Then("Spinner loading tidak terlihat")
    public void spinner_loading_tidak_terlihat() {
        Assert.assertTrue("Spinner loading masih terlihat", etalasePage.isLoadingSpinnerGone());
    }
}