Feature: Mengakses Halaman Laporan

  Scenario: Seller berhasil mengakses halaman Laporan dan mengekspor laporan ke Excel setelah login
    Given Seller berada di halaman login
    When Seller memasukkan email "dummyuser@example.com" dan password "admin#123"
    And Seller klik tombol masuk
    Then Seller berhasil masuk ke halaman dashboard
    When Seller mengakses halaman Laporan
    Then Halaman Laporan berhasil ditampilkan
    And Spinner loading tidak terlihat
    And Kartu laporan terlihat
    And Grafik penjualan terlihat
    When Seller mengklik tombol Export ke Excel
    Then Ekspor Excel berhasil