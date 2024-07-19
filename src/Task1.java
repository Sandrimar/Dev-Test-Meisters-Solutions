import java.util.Arrays;
import java.util.List;

public class Task1 {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("apple", "art", "axe", "A23", "sun");

        System.out.println("Strings: " + strings);
        List<String> filteredStrings = filterStrings(strings);
        System.out.println("Filtered: " + filteredStrings);
    }

    public static List<String> filterStrings(List<String> strings) {
        return strings.stream()
                .filter(str -> str.startsWith("a") && str.length() == 3)
                .toList();
    }
}
