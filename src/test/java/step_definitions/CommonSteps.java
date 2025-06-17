package step_definitions;

import io.cucumber.java.en.Then;
import page_objects.DashboardPage;
import page_objects.SidebarPage;
import org.openqa.selenium.WebDriver;
import org.junit.Assert;

public class CommonSteps {
    private final SidebarPage sidebarPage;
    private final DashboardPage dashboardPage;

    public CommonSteps(LoginSteps loginSteps) {
        WebDriver driver = loginSteps.getDriver();
        this.sidebarPage = new SidebarPage(driver);
        this.dashboardPage = new DashboardPage(driver);
    }

    @Then("Seller berhasil masuk ke halaman dashboard")
    public void seller_berhasil_masuk_ke_halaman_dashboard() throws InterruptedException {
        Assert.assertTrue("Sidebar tidak terlihat setelah login berhasil", sidebarPage.isSidebarVisible());
        sidebarPage.toggleSidebarIfMobile();
        sidebarPage.dismissOverlayIfPresent();
        Assert.assertTrue("Halaman dashboard tidak terlihat", dashboardPage.isDashboardPageVisible());
        Thread.sleep(2000);
    }
}