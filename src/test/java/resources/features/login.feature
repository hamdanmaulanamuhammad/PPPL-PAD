Feature: Seller Login

  Scenario: Seller melakukan login dengan data yang sesuai dan lengkap
    Given Seller berada di halaman login
    When Seller memasukkan email/username/nomor telepon dan password
    And Seller klik tombol "Masuk"
    Then Seller masuk ke halaman dashboard

  Scenario: Seller melakukan login dengan data yang tidak sesuai dan tidak lengkap
    Given Seller berada di halaman login
    When Seller memasukkan email/username/nomor telepon, tanpa password
    And Seller klik tombol "Masuk"
    Then Sistem menampilkan pesan error dan memberikan penanda pada kolom password
    And Seller gagal login
    And Seller masuk ke halaman detail produk setelah mengakses salah satu produk