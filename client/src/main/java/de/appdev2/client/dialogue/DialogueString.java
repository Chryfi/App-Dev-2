package main.java.de.appdev2.client.dialogue;

import java.util.*;
import java.util.function.Function;

/**
 * Eine Dialog-Klasse die einen String basierend auf der Nutzereingabe zurückgibt.
 * Es wird vorher definiert, welche Strings als Eingabe erlaubt sind.
 *
 * @param <K> Datentyp für das Objekt für welches der Input geschehen soll.
 *            Das Objekt wird für eine individuelle Ausgabe benutzt.
 */
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

        /*
         * Alle Strings in lowercase umwandeln, damit später case-insensitive gesucht werden kann
         */
        for (int i = 0; i < expected.length; i++) {
            this.expected.add(expected[i].toLowerCase());
        }
    }

    public Set<String> getExpected() {
        return new HashSet<>(this.expected);
    }

    /**
     * Eingabe des Nutzers prüfen, ob sie in {@link #expected} vorkommt. Die Groß- und Kleinschreibung der Eingabe ist egal.
     *
     * @param obj das Objekt für welches die Eingabe geschehen soll.
     *            Es wird benutzt für die Ausgabe der Anweisung und die Benachrichtigung bei einer falschen Eingabe.
     *            Darf auch null sein.
     * @return den String, den der Nutzer eingegeben hat, in lowercase.
     */
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
