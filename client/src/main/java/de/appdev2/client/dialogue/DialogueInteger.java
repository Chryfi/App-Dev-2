package main.java.de.appdev2.client.dialogue;

import main.java.de.appdev2.exceptions.IllegalInputException;

import java.util.Scanner;
import java.util.function.Function;

/**
 * Eine Dialog-Klasse die einen Integer basierend auf der Nutzereingabe zurückgibt.
 *
 * @param <K> Datentyp für das Objekt für welches der Input geschehen soll.
 *            Das Objekt wird für eine individuelle Ausgabe benutzt.
 */
public class DialogueInteger<K> extends Dialogue<K, Integer> {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public DialogueInteger(String prompt, String wrongPrompt) {
        super(prompt, wrongPrompt);
    }

    public DialogueInteger(String prompt, String wrongPrompt, int min, int max) {
        this((obj) -> prompt, (obj) -> wrongPrompt, min, max);
    }

    public DialogueInteger(Function<K, String> prompt, Function<K, String> wrongPrompt, int min, int max) {
        super(prompt, wrongPrompt);

        this.min = min;
        this.max = max;
    }

    public DialogueInteger(Function<K, String> prompt, Function<K, String> wrongPrompt, int min) {
        super(prompt, wrongPrompt);

        this.min = min;
    }

    public DialogueInteger(String prompt, String wrongPrompt, int min) {
        this((obj) -> prompt, (obj) -> wrongPrompt, min);
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    /**
     * Eingabe des Nutzers in Integer konvertieren.
     *
     * @param obj das Objekt für welches die Eingabe geschehen soll.
     *            Es wird benutzt für die Ausgabe der Anweisung und die Benachrichtigung bei einer falschen Eingabe.
     *            Darf auch null sein.
     * @return ein Integer, falls die Eingabe korrekt war z.B. innerhalb des Definitionsbereichs.
     */
    @Override
    public Integer input(K obj) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(this.getPrompt(obj));

        Integer eingabe;

        while (true) {
            try {
                eingabe = this.format(scanner.nextLine());

                break;
            } catch (IllegalInputException e) {
                System.out.println(this.getWrongPrompt(obj));
                System.out.println(e.getMessage());
            }
        }

        return eingabe;
    }

    /**
     * Konvertiere den String in einen Integer, falls der String im richtigen Format ist.
     *
     * @param scan
     * @return Integer konvertiert aus dem String.
     * @throws IllegalInputException falls der String nicht als ganze Zahl gelesen werden konnte,
     *                               oder er außerhalb des Definitionsbereichs ist (siehe {@link #min} und {@link #max})
     */
    private Integer format(String scan) throws IllegalInputException {
        Integer eingabe;

        try {
            eingabe = Integer.parseInt(scan);
        } catch (NumberFormatException e) {
            throw new IllegalInputException("Der String konnte nicht eingelesen werden als Integer!");
        }

        if (eingabe < this.min || eingabe > this.max) {
            throw new IllegalInputException("Außerhalb vom Definitionsbereich!");
        }

        return eingabe;
    }
}
