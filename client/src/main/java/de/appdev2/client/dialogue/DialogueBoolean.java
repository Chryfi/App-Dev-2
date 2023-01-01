package main.java.de.appdev2.client.dialogue;

import java.util.*;
import java.util.function.Function;

public class DialogueBoolean<K> extends Dialogue<K, Boolean> {
    /**
     * Die eigentliche Eingabe geschieht über einen DialogueString,
     * der prüft, ob die Eingabe korrekt ist.
     */
    private DialogueString<K> inputString;
    /**
     * Ein Set mit Strings die einen true Wert für die Eingabe zurückgeben sollen.
     * Die Strings sind lowercase.
     */
    private final Set<String> equalsTrue;

    public DialogueBoolean(String prompt, String wrongPrompt, Set<String> equalsTrue, Set<String> expected) {
        this((obj) -> prompt, (obj) -> wrongPrompt, equalsTrue, expected);
    }

    public DialogueBoolean(Function<K, String> prompt, Function<K, String> wrongPrompt, Set<String> equalsTrue, Set<String> expected) {
        super(prompt, wrongPrompt);

        /*
         * Die Strings in lowercase umwandeln damit später case-insensitiv gesucht werden kann.
         */
        List<String> equalsTrueList = new ArrayList<>(equalsTrue);
        for (int i = 0; i < equalsTrueList.size(); i++) {
            equalsTrueList.set(i, equalsTrueList.get(i).toLowerCase());
        }
        this.equalsTrue = new HashSet<>(equalsTrueList);

        /*
         * Testen, ob im equalsTrue Set mindestens ein String vorhanden ist, der auch im expected Set liegt.
         */
        boolean intersects = false;
        for (String expect : expected) {
            if (this.equalsTrue.contains(expect.toLowerCase())) {
                intersects = true;
                break;
            }
        }

        if (!intersects) {
            throw new IllegalArgumentException("The equalsTrue Set needs to have at least one common keyword with the expected Set!");
        }

        this.inputString = new DialogueString<>(prompt, wrongPrompt, expected.toArray(new String[expected.size()]));
    }

    public Set<String> getExpected() {
        return this.inputString.getExpected();
    }

    public Set<String> getEqualsTrue() {
        return new HashSet<>(this.equalsTrue);
    }

    @Override
    public Boolean input(K obj) {
        return this.equalsTrue.contains(this.inputString.input(obj));
    }
}
