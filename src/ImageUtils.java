import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageUtils {

    // Perhitungan rata-rata warna untuk normalisasi warna
    public static Color averageColor(BufferedImage img, int x, int y, int width, int height) {
        long r = 0, g = 0, b = 0;
        int count = width * height;

        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                Color c = new Color(img.getRGB(j, i));
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }

        return new Color((int)(r / count), (int)(g / count), (int)(b / count));
    }

    // Perhitungan error
    public static double calculateError(BufferedImage img, int x, int y, int width, int height) {
        int N = width * height;
        double[] sum = new double[3];

        // Penghitungan jumlah total RGB dari semua piksel dalam blok
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                Color c = new Color(img.getRGB(j, i));
                sum[0] += c.getRed();
                sum[1] += c.getGreen();
                sum[2] += c.getBlue();
            }
        }

        // Perhitungan rata-rata warna untuk RGB
        double[] mean = { sum[0] / N, sum[1] / N, sum[2] / N };

        // Perhitungan error berdasarkan metode yang dipilih
        return switch (ImageCompressor.method) {
            case 1 -> calculateVariance(img, x, y, width, height, mean);
            case 2 -> calculateMAD(img, x, y, width, height, mean);
            case 3 -> calculateMaxDiff(img, x, y, width, height);
            case 4 -> calculateEntropy(img, x, y, width, height);
            default -> 0.0;
        };
    }

    // Metode perhitungan error: Variance
    private static double calculateVariance(BufferedImage img, int x, int y, int width, int height, double[] mean) {
        int N = width * height;
        double error = 0;

        for (int c = 0; c < 3; c++) {
            double var = 0;
            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    Color color = new Color(img.getRGB(j, i));
                    int value = (c == 0) ? color.getRed() : (c == 1) ? color.getGreen() : color.getBlue();
                    var += Math.pow(value - mean[c], 2);
                }
            }
            error += var / N; // σ_c² = (1 / N) * Σ_{i = 1 to N} (P_{i,c} - μ_c)²
        }

        return error / 3; // σ_RGB² = (σ_R² + σ_G² + σ_B²) / 3
    }

    // Metode perhitungan error: MAD
    private static double calculateMAD(BufferedImage img, int x, int y, int width, int height, double[] mean) {
        int N = width * height;
        double error = 0;

        for (int c = 0; c < 3; c++) {
            double mad = 0;
            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    Color color = new Color(img.getRGB(j, i));
                    int value = (c == 0) ? color.getRed() : (c == 1) ? color.getGreen() : color.getBlue();
                    mad += Math.abs(value - mean[c]);
                }
            }
            error += mad / N; // MAD_c = (1 / N) * Σ_{i = 1 to N} (|P_{i,c} - μ_c|)
        }

        return error / 3; // MAD_RGB = (MAD_R + MAD_G + MAD_B) / 3
    }

    // Metode perhitungan error: Max Pixel Difference
    private static double calculateMaxDiff(BufferedImage img, int x, int y, int width, int height) {
        double error = 0;

        for (int c = 0; c < 3; c++) {
            int min = 255, max = 0;
            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    Color color = new Color(img.getRGB(j, i));
                    int value = (c == 0) ? color.getRed() : (c == 1) ? color.getGreen() : color.getBlue();
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                }
            }
            error += max - min; // D_c = max(P_{i,c}) - min(P_{i,c})
        }

        return error / 3; // D_RGB = (D_R + D_G + D_B) / 3
    }

    // Metode perhitungan error: Entropy
    private static double calculateEntropy(BufferedImage img, int x, int y, int width, int height) {
        int N = width * height;
        double error = 0;

        for (int c = 0; c < 3; c++) {
            int[] hist = new int[256];
            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    Color color = new Color(img.getRGB(j, i));
                    int value = (c == 0) ? color.getRed() : (c == 1) ? color.getGreen() : color.getBlue();
                    hist[value]++;
                }
            }

            double H = 0;
            for (int h : hist) {
                if (h > 0) {
                    double p = h / (double) N;
                    H -= p * (Math.log(p) / Math.log(2));
                }
            }

            error += H; // H_c = - Σ_{i = 1 to N} (P_c(i) log_2(P_c(i)))
        }

        return error / 3; // H_RGB = (H_R + H_G + H_B) / 3
    }
}
