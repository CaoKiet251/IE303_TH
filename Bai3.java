import java.util.Arrays;
import java.util.Stack;

class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Bai3 {

    static Point p0;

    // Utility function to find next to top in stack
    static Point nextToTop(Stack<Point> stk) {
        Point p = stk.pop();
        Point res = stk.peek();
        stk.push(p);
        return res;
    }

    // Utility function to swap two points
    static void swap(Point[] points, int i, int j) {
        Point temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }

    // Return square of distance between p1 and p2
    static int distSq(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) +
               (p1.y - p2.y) * (p1.y - p2.y);
    }

    // Find orientation of triplet (p, q, r)
    // 0 -> collinear, 1 -> clockwise, 2 -> counterclockwise
    static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) -
                  (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0; // collinear
        return (val > 0) ? 1 : 2; // clock or counterclockwise
    }

    // Comparator function for sorting points by polar angle with p0
    static int compare(Point p1, Point p2) {
        int o = orientation(p0, p1, p2);
        if (o == 0) {
            return (distSq(p0, p2) >= distSq(p0, p1)) ? -1 : 1;
        }
        return (o == 2) ? -1 : 1;
    }

    // Function to find convex hull
    static void convexHull(Point[] points, int n) {
        // Find the bottom-most point
        int min = 0;
        for (int i = 1; i < n; i++) {
            if (points[i].y < points[min].y ||
               (points[i].y == points[min].y && points[i].x < points[min].x)) {
                min = i;
            }
        }

        // Swap the bottom-most point with the first point
        swap(points, 0, min);
        p0 = points[0];

        // Sort points based on polar angle with p0
        Arrays.sort(points, 1, n, Bai3::compare);

        // Remove collinear points, keeping only the farthest
        int m = 1;
        for (int i = 1; i < n; i++) {
            while (i < n - 1 && orientation(p0, points[i],
                                            points[i + 1]) == 0) {
                i++;
            }
            points[m] = points[i];
            m++;
        }

        // If there are less than 3 unique points, convex hull is not possible
        if (m < 3) return;

        // Create stack and push first three points
        Stack<Point> stk = new Stack<>();
        stk.push(points[0]);
        stk.push(points[1]);
        stk.push(points[2]);

        // Process remaining points
        for (int i = 3; i < m; i++) {
            while (stk.size() > 1 && orientation(nextToTop(stk), 
                                            stk.peek(), points[i]) != 2) {
                stk.pop();
            }
            stk.push(points[i]);
        }

        // Print the convex hull points
        while (!stk.isEmpty()) {
            Point p = stk.pop();
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }

    // Driver code
    public static void main(String[] args) {
      
        Point[] points = { new Point(0, 3), new Point(1, 1), new Point(2, 2),
                           new Point(4, 4), new Point(0, 0), new Point(1, 2),
                           new Point(3, 1), new Point(3, 3) };
        int n = points.length;
        convexHull(points, n);
    }
}
