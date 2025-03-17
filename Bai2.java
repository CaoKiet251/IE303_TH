import java.util.Random;

public class Bai2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int totalPoints = 1000000;
        int insideCircle = 0;

        for (int i = 0; i < totalPoints; i++) {
            double x = 2  * rand.nextDouble() - 1;
            double y = 2 * rand.nextDouble() - 1;

            if (x * x + y * y <= 1) { 
                insideCircle++;
            }
        }

        double areaApprox = (insideCircle / (double) totalPoints) * 4; 
        System.out.println("So pi xap xi: " + areaApprox);

    }
}
