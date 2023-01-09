package main.java.de.appdev2.client.dialogue;

import java.util.*;
import java.util.function.Function;

/**
 * Eine Dialog-Klasse die einen Boolean basierend auf der Nutzereingabe zurückgibt.
 *
 * @param <K> Datentyp für das Objekt für welches der Input geschehen soll.
 *            Das Objekt wird für eine individuelle Ausgabe benutzt.
 */
public class DialogueBoolean<K> extends Dialogue<K, Boolean> {
    /**
     * Die eigentliche Eingabe geschieht über einen {@link DialogueString},
     * der prüft, ob die Eingabe korrekt ist.
     */
    private DialogueString<K> inputString;
    /**
     * Ein Set mit Strings die einen true Wert für die Eingabe zurückgeben sollen.
     * Die Strings sind lowercase.
     */
    private final Set<String> equalsTrue;

    /**
     * @param prompt      Aufforderung an den Nutzer am Anfang
     * @param wrongPrompt bei falscher Eingabe
     * @param equalsTrue  Strings die true ergeben sollen.
     *                    In diesem Set muss mindestens ein String enthalten sein, welches auch in expected enthalten ist.
     * @param expected    welche Eingabewerte erlaubt ist.
     * @throws IllegalArgumentException wenn in dem equalsTrue Set kein Wert enthalten ist, der auch im expected Set enthalten ist.
     */
    public DialogueBoolean(String prompt, String wrongPrompt, Set<String> equalsTrue, Set<String> expected) {
        this((obj) -> prompt, (obj) -> wrongPrompt, equalsTrue, expected);
    }

    /**
     * @param prompt      Aufforderung an den Nutzer am Anfang
     * @param wrongPrompt bei falscher Eingabe
     * @param equalsTrue  Strings die true ergeben sollen.
     *                    In diesem Set muss mindestens ein String enthalten sein, welches auch in expected enthalten ist.
     * @param expected    welche Eingabewerte erlaubt ist.
     * @throws IllegalArgumentException wenn in dem equalsTrue Set kein Wert enthalten ist, der auch im expected Set enthalten ist.
     */
    public DialogueBoolean(Function<K, String> prompt, Function<K, String> wrongPrompt, Set<String> equalsTrue, Set<String> expected) {
        super(prompt, wrongPrompt);

        /*
         * Die Strings in lowercase umwandeln damit später case-insensitiv gesucht werden kann.
         */
        this.equalsTrue = new HashSet<>();
        for (String val : equalsTrue) {
            this.equalsTrue.add(val.toLowerCase());
        }

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

    /**
     * Eingabe des Nutzers in Boolean konvertieren. Die Groß- und Kleinschreibung der Eingabe ist egal.
     *
     * @param obj das Objekt für welches die Eingabe geschehen soll.
     *            Es wird benutzt für die Ausgabe der Anweisung und die Benachrichtigung bei einer falschen Eingabe.
     *            Darf auch null sein.
     * @return falls der Nutzer eine korrekte Eingabe tätigt
     * und dieser Wert im {@link #equalsTrue} Set enthalten ist, wird true zurückgegeben.
     */
    @Override
    public Boolean input(K obj) {
        return this.equalsTrue.contains(this.inputString.input(obj));
    }
}
