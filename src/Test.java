import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<String> languages = Arrays.asList("Java","Js");
		Collections.sort(languages, (s1,s2) -> { return s1.compareTo(s2);});
		languages.forEach(System.out::println);
	}

}
