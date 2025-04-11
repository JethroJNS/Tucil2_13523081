public class Node {
    // Struktur data untuk node
    int x, y, width, height;
    boolean isLeaf;
    Node[] children;

    public Node(int x, int y, int width, int height, boolean isLeaf) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isLeaf = isLeaf;
    }
}
