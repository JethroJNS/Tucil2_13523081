import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static int method;
    public static double threshold;
    public static int minBlockSize;
    public static String inputPath;

    public static int nodeCount = 0;
    public static int maxTreeDepth = 0;

    public static void main(String[] args) throws Exception {
        // Input
        Scanner scanner = new Scanner(System.in);
        BufferedImage input = IOHandler.getInputImage(scanner);
        IOHandler.getMethodThresholdBlockSize(scanner, input);
        File inputFile = new File(inputPath);
        String outputPath = IOHandler.getOutputPath(scanner, inputFile);
        String format = outputPath.substring(outputPath.lastIndexOf('.') + 1);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Variabel untuk output
        long start = System.nanoTime();
        Node root = compress(input, output, 0, 0, input.getWidth(), input.getHeight(), 0);
        long end = System.nanoTime();
        ImageIO.write(output, format, new File(outputPath));
        File outFile = new File(outputPath);

        // Output
        System.out.printf("Waktu ekseskusi: %.2f ms\n", (end - start) / 1e6);
        System.out.printf("Ukuran sebelum: %d KB\n", inputFile.length() / 1024);
        System.out.printf("Ukuran sesudah: %d KB\n", outFile.length() / 1024);
        System.out.printf("Persentase kompresi: %.2f%%\n", 100.0 * (1 - (double) outFile.length() / inputFile.length()));
        System.out.println("Banyak simpul: " + nodeCount);
        System.out.println("Kedalaman maksimum: " + maxTreeDepth);
    }

    // Proses kompresi
    public static Node compress(BufferedImage input, BufferedImage output, int x, int y, int width, int height, int depth) {
        double error = ImageUtils.calculateError(input, x, y, width, height);

        // Basis: Jika luas blok sudah mencapai batas minimum atau sudah homogen
        if (((width * height) / 4 < minBlockSize) || height / 2 < 1 || width / 2 < 1 || error < threshold) {
            // Normalisasi warna
            Color avg = ImageUtils.averageColor(input, x, y, width, height);
            for (int i = y; i < y + height; i++) {
                for (int j = x; j < x + width; j++) {
                    output.setRGB(j, i, avg.getRGB());
                }
            }
            nodeCount++;
            maxTreeDepth = Math.max(maxTreeDepth, depth);
            return new Node(x, y, width, height, true);
        }

        // Rekursi: Pembagian blok jika melum mencapai minBlockSize dan belum homogen
        int w2 = width / 2;
        int h2 = height / 2;
        Node node = new Node(x, y, width, height, false);
        node.children = new Node[4];

        // Kompresi masing-masing blok
        node.children[0] = compress(input, output, x, y, w2, h2, depth + 1);
        node.children[1] = compress(input, output, x + w2, y, width - w2, h2, depth + 1);
        node.children[2] = compress(input, output, x, y + h2, w2, height - h2, depth + 1);
        node.children[3] = compress(input, output, x + w2, y + h2, width - w2, height - h2, depth + 1);

        return node;
    }
}
