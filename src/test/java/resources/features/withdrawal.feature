Feature: Manajemen Penarikan Seller

  Scenario: Seller mengajukan penarikan dengan data valid
    Given Seller berada di halaman login
    When Seller memasukkan email "dummyuser@example.com" dan password "admin#123"
    And Seller klik tombol masuk
    Then Seller berhasil masuk ke halaman dashboard
    When Seller mengakses halaman "Ajukan Penarikan" dari dashboard
    And Seller mengklik tombol "+ Permintaan"
    And Seller mengisi formulir penarikan dengan bank "minima", nomor rekening "1234567890", dan jumlah "1000000"
    And Seller mengklik tombol "Ajukan Penarikan"
    Then Penarikan dana berhasil diajukan ke Admin

  Scenario: Seller mencoba mengajukan penarikan dengan jumlah minus
    Given Seller berada di halaman login
    When Seller memasukkan email "dummyuser@example.com" dan password "admin#123"
    And Seller klik tombol masuk
    Then Seller berhasil masuk ke halaman dashboard
    When Seller mengakses halaman "Ajukan Penarikan" dari dashboard
    And Seller mengklik tombol "+ Permintaan"
    And Seller mengisi formulir penarikan dengan bank "minima", nomor rekening "1234567890", dan jumlah "-200000"
    And Seller mengklik tombol "Ajukan Penarikan"
    Then Sistem menampilkan pesan error penarikan
    Then Penarikan dana gagal diajukan ke Admin