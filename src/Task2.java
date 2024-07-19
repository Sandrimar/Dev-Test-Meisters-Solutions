public class Task2 {
    public static void main(String[] args) {
        String str = "This is a string";

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ') {
                result.append(c);
            }
        }

        System.out.println("Before: " + str);
        System.out.println("After: " + result);
    }
}
