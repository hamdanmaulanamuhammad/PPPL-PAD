Feature: Seller Dashboard

  Scenario: Seller mengakses halaman dashboard setelah login berhasil
    Given Seller berada di halaman login untuk dashboard
    When Seller memasukkan kredensial valid untuk dashboard
    And Seller klik tombol masuk untuk dashboard
    Then Seller berhasil masuk ke halaman dashboard
    When Seller mengakses halaman dashboard
    Then Halaman dashboard terlihat
    And Statistik card terlihat
    And Grafik terlihat
    And Statistik dashboard ditampilkan