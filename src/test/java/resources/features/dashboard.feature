Feature: Seller Dashboard

  Scenario: Seller mengakses halaman dashboard setelah login berhasil
    Given Seller berada di halaman login
    When Seller memasukkan email/username/nomor telepon dan password
    And Seller klik tombol "Masuk"
    Then Seller masuk ke halaman dashboard
    When Seller mengakses halaman dashboard
    Then Halaman dashboard terlihat
    And Statistik card terlihat
    And Grafik terlihat
    And Statistik dashboard ditampilkan