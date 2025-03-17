import java.util.Scanner;
import java.util.Random;

public class Bai1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhap ban kinh r: ");
        double r = scanner.nextDouble();

        Random rand = new Random();
        int totalPoints = 1000000; // Số điểm để mô phỏng
        int insideCircle = 0;

        for (int i = 0; i < totalPoints; i++) {
            double x = (2 * r) * rand.nextDouble() - r; // Tọa độ x ngẫu nhiên trong [-r, r]
            double y = (2 * r) * rand.nextDouble() - r; // Tọa độ y ngẫu nhiên trong [-r, r]

            if (x * x + y * y <= r * r) { // Nếu nằm trong hình tròn
                insideCircle++;
            }
        }

        double areaApprox = (insideCircle / (double) totalPoints) * (4 * r * r); // Xấp xỉ diện tích
        System.out.println("Dien tich xap xi: " + areaApprox);

        scanner.close();
    }
}
