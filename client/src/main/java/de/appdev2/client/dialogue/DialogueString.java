package main.java.de.appdev2.client.dialogue;

import java.util.*;
import java.util.function.Function;

public class DialogueString<K> extends Dialogue<K, String> {
    /**
     * Ein Set mit den Strings die bei der Eingabe erlaubt sind.
     * Alle Strings sind lowercase.
     */
    private final Set<String> expected = new HashSet<>();

    public DialogueString(String prompt, String wrongPrompt, String... expected) {
        this((obj) -> prompt, (obj) -> wrongPrompt, expected);
    }

    public DialogueString(Function<K, String> prompt, Function<K, String> wrongPrompt, String... expected) {
        super(prompt, wrongPrompt);

        List<String> expectedList = Arrays.asList(expected);

        /*
         * Alle Strings in lowercase umwandeln, damit später case-insensitive gesucht werden kann
         */
        for (int i = 0; i < expectedList.size(); i++) {
            expectedList.set(i, expectedList.get(i).toLowerCase());
        }

        this.expected.addAll(expectedList);
    }

    public Set<String> getExpected() {
        return new HashSet<>(this.expected);
    }

    @Override
    public String input(K obj) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(this.getPrompt(obj));

        String input;
        while (!this.expected.contains(input = scanner.nextLine().toLowerCase())) {
            System.out.println(this.getWrongPrompt(obj));
        }

        return input;
    }
}
