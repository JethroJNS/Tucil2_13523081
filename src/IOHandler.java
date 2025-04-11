import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class IOHandler {

    // Input alamat absolut gambar yang akan dikompresi
    public static BufferedImage getInputImage(Scanner scanner) {
        BufferedImage img = null;
    
        while (true) {
            System.out.print("Alamat absolut gambar: ");
            String path = scanner.nextLine().trim().replace("\"", "");
            File file = new File(path);
    
            if (!file.isAbsolute()) {
                System.out.println("Harap masukkan path absolut (misalnya: C:\\Users\\Acer\\Documents\\input.png).");
                continue;
            }
    
            if (!file.exists()) {
                System.out.println("File tidak ditemukan. Silakan coba lagi.");
                continue;
            }
    
            try {
                img = ImageIO.read(file);
                if (img == null) {
                    System.out.println("File tidak bisa dibaca sebagai gambar. Silakan coba lagi.");
                    continue;
                }
                Main.inputPath = path;
                return img;
            } catch (IOException e) {
                System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
            }
        }
    }    

    // Input pilihan metode perhitungan variansi, ambang batas, dan ukuran blok minimum
    public static void getMethodThresholdBlockSize(Scanner scanner, BufferedImage inputImage) {
        System.out.println("PILIH METODE PERHITUNGAN VARIANSI");
        System.out.println("1. Variance");
        System.out.println("2. Mean Absolute Deviation (MAD)");
        System.out.println("3. Max Pixel Difference");
        System.out.println("4. Entropy");

        // Validasi metode
        while (true) {
            System.out.print("Pilihan metode: ");
            try {
                int m = Integer.parseInt(scanner.nextLine());
                if (m >= 1 && m <= 4) {
                    Main.method = m;
                    break;
                } else {
                    System.out.println("Input tidak valid. Masukkan angka antara 1 hingga 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Harap masukkan angka.");
            }
        }

        // Validasi threshold
        while (true) {
            System.out.print("Threshold: ");
            try {
                double t = Double.parseDouble(scanner.nextLine());

                boolean isValid = switch (Main.method) {
                    case 4 -> t >= 0.0 && t <= 8.0;
                    default -> t >= 0.0 && t <= 16256.25;
                };

                if (isValid) {
                    Main.threshold = t;
                    break;
                } else {
                    System.out.println(Main.method == 4
                        ? "Threshold untuk Entropy harus antara 0.0 dan 8.0."
                        : "Threshold harus antara 0.0 dan 16256.25.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Harap masukkan angka untuk threshold.");
            }
        }

        // Validasi minimum block size
        int maxBlockSize = inputImage.getWidth() * inputImage.getHeight();
        while (true) {
            System.out.print("Minimum block size: ");
            try {
                int size = Integer.parseInt(scanner.nextLine());
                if (size >= 1 && size <= maxBlockSize) {
                    Main.minBlockSize = size;
                    break;
                } else {
                    System.out.println("Ukuran blok harus antara 1 dan " + maxBlockSize + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Harap masukkan angka untuk ukuran blok.");
            }
        }
    }

    // Input alamat absolut gambar hasil kompresi
    public static String getOutputPath(Scanner scanner, File inputFile) {
        while (true) {
            System.out.print("Alamat output: ");
            String out = scanner.nextLine().trim().replace("\"", "");
            File outputFile = new File(out);
    
            if (!outputFile.isAbsolute()) {
                System.out.println("Harap masukkan path absolut (misalnya: C:\\Users\\Acer\\Documents\\output.png).");
                continue;
            }
    
            try {
                if (outputFile.getCanonicalPath().equals(inputFile.getCanonicalPath())) {
                    System.out.println("Alamat output tidak boleh sama dengan alamat input.");
                    continue;
                }
            } catch (IOException e) {
                System.out.println("Gagal memverifikasi path output. Coba lagi.");
                continue;
            }
    
            if (!out.contains(".")) {
                System.out.println("Path output harus menyertakan ekstensi.");
                continue;
            }
    
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                System.out.println("Direktori tidak ditemukan: " + parentDir.getAbsolutePath());
                continue;
            }
    
            String ext = out.substring(out.lastIndexOf('.') + 1).toLowerCase();
            boolean supported = false;
            for (String format : ImageIO.getWriterFormatNames()) {
                if (format.equalsIgnoreCase(ext)) {
                    supported = true;
                    break;
                }
            }
    
            if (!supported) {
                System.out.println("Format file tidak didukung: " + ext);
                continue;
            }
    
            return out;
        }
    }
}
