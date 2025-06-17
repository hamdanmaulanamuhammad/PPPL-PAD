Feature: Mengakses Halaman Etalase

  Scenario: Seller berhasil mengakses halaman Etalase setelah login
    Given Seller berada di halaman login
    When Seller memasukkan email "dummyuser@example.com" dan password "admin#123"
    And Seller klik tombol masuk
    Then Seller berhasil masuk ke halaman dashboard
    When Seller mengakses halaman Etalase
    Then Halaman Etalase berhasil ditampilkan
    And Spinner loading tidak terlihat