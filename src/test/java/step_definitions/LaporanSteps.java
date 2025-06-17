package step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page_objects.ReportPage;
import page_objects.SidebarPage;
import org.openqa.selenium.WebDriver;
import org.junit.Assert;

public class LaporanSteps {
    private final SidebarPage sidebarPage;
    private final ReportPage reportPage;

    public LaporanSteps(LoginSteps loginSteps) {
        WebDriver driver = loginSteps.getDriver();
        this.sidebarPage = new SidebarPage(driver);
        this.reportPage = new ReportPage(driver);
    }

    @When("Seller mengakses halaman Laporan")
    public void seller_mengakses_halaman_laporan() throws InterruptedException {
        System.out.println("Mengakses halaman Laporan...");
        sidebarPage.clickReportMenu();
        Thread.sleep(2000);
    }

    @Then("Halaman Laporan berhasil ditampilkan")
    public void halaman_laporan_berhasil_ditampilkan() {
        System.out.println("Halaman Laporan ditampilkan.");
        Assert.assertTrue("Halaman Laporan tidak terlihat", reportPage.isReportPageVisible());
    }

    @Then("Kartu laporan terlihat")
    public void kartu_laporan_terlihat() {
        Assert.assertTrue("Kartu laporan tidak terlihat", reportPage.isReportCardsVisible());
    }

    @Then("Grafik penjualan terlihat")
    public void grafik_penjualan_terlihat() {
        Assert.assertTrue("Grafik penjualan tidak terlihat", reportPage.isSalesChartVisible());
    }

    @When("Seller mengklik tombol Export ke Excel")
    public void seller_mengklik_tombol_export_ke_excel() throws InterruptedException {
        System.out.println("Mengklik tombol Export ke Excel...");
        reportPage.clickExportExcelButton();
        Thread.sleep(2000);
    }

    @Then("Ekspor Excel berhasil")
    public void ekspor_excel_berhasil() {
        Assert.assertTrue("Ekspor Excel tidak berhasil", reportPage.isExportSuccessful());
    }
}