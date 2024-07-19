public class Task3 {
    public static void main(String[] args) {
        int a = 42;
        int b = 7;

        System.out.printf("Before:\na = %d\nb = %d\n", a, b);

        a = a + b;
        b = a - b;
        a = a - b;

        System.out.printf("\nAfter:\na = %d\nb = %d", a, b);
    }
}
