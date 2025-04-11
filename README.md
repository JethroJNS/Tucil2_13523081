<h1 align="center">Tugas Kecil 2 IF2211 Strategi Algoritma</h1>
<h3 align="center">Kompresi Gambar Dengan Metode Quadtree</h3>

## Daftar Isi

- [Deskripsi](#deskripsi)
- [Kebutuhan Sistem](#kebutuhan-sistem)
- [Struktur](#struktur)
- [Cara Menjalankan](#cara-menjalankan)
- [Pengembang](#pengembang)

## Deskripsi

Program ini adalah program kompresi gambar menggunakan struktur quadtree, dengan pendekatan Divide and Conquer. Program menerima input gambar dalam format RGB, memprosesnya menjadi blok-blok yang dapat dibagi sesuai dengan tingkat error piksel di dalamnya, lalu menghasilkan gambar baru yang telah dikompresi berdasarkan variasi warna dalam blok-blok tersebut.

Pengguna dapat memilih metode pengukuran error yang akan digunakan untuk menentukan tingkat error dalam sebuah blok gambar, yaitu Variance, Mean Absolute Deviation (MAD), Max Pixel Difference, dan Entropy. Selain itu, pengguna juga dapat menentukan nilai threshold serta ukuran blok minimum, yang berfungsi sebagai parameter utama dalam mengatur proses pembagian blok pada gambar selama proses kompresi.

## Kebutuhan Sistem

* Java 8 atau versi yang lebih baru
* Sistem operasi Windows, macOS, atau Linux

## Struktur
```ssh
├── bin
│   ├── IOHandler.class
│   ├── ImageUtils.class
│   ├── Main.class
│   └── Node.class
├── doc
│   └── Tucil2_13523081_Jethro Jens Norbert Simatupang.pdf
├── src
│   ├── IOHandler.java
│   ├── ImageUtils.java
│   ├── Main.java
│   └── Node.java
├── test
│   ├── input
│   │   ├─ input1.png
│   │   ├─ input2.jpg
│   │   ├─ input3.jpg
│   │   ├─ input4.jpg
│   │   ├─ input5.jpeg
│   │   ├─ input6.jpg
│   │   ├─ input7.jpg
│   │   └─ input8.jpg
│   └── output
│       ├─ output1.png
│       ├─ output2.jpg
│       ├─ output3.jpg
│       ├─ output4.jpg
│       ├─ output5.jpeg
│       ├─ output6.jpg
│       ├─ output7.jpg
│       └─ output8.jpg
└── README.md
```

## Cara Menjalankan

1. Clone repository ini:

```bash
git clone https://github.com/JethroJNS/Tucil2_13523081.git
```

2. Navigasi ke direktori repositori dan jalankan command berikut:

```bash
javac -d bin src/*.java
```

```bash
java -cp bin Main
```

## Pengembang

| **NIM**  | **Nama Anggota**               | **Github** |
| -------- | ------------------------------ | ---------- |
| 13523081 | Jethro Jens Norbert Simatupang | [JethroJNS](https://github.com/JethroJNS) |
