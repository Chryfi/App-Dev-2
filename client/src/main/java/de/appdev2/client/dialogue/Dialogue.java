package main.java.de.appdev2.client.dialogue;

import java.util.function.Function;

/**
 * Eine Dialog-Klasse. Der Dialog geschieht für ein Objekt des Typs K woraufhin der Input vom Typ V zurückgegeben wird.
 * Es muss nicht zwangsläufig ein Objekt vom Typ K gegeben sein, es kann auch null sein.
 * Das Objekt vom Typ K wird für individuelle Ausgaben benutzt.
 *
 * @param <K> Datentyp für das Objekt für welches der Input geschehen soll.
 *            Das Objekt wird für eine individuelle Ausgabe benutzt.
 * @param <V> Datentyp der Rückgabe basierend auf dem Input des Users.
 */
public abstract class Dialogue<K, V> {
    /**
     * Ausgabe am Anfang
     */
    private Function<K, String> prompt;
    /**
     * Bei falscher Eingabe, folgendes ausgeben
     */
    private Function<K, String> wrongPrompt;

    public Dialogue(String prompt, String wrongPrompt) {
        this.prompt = (obj) -> prompt;
        this.wrongPrompt = (obj) -> wrongPrompt;
    }

    public Dialogue(Function<K, String> prompt, Function<K, String> wrongPrompt) {
        this.prompt = prompt;
        this.wrongPrompt = wrongPrompt;
    }

    public String getPrompt(K obj) {
        return this.prompt.apply(obj);
    }

    public String getPrompt() {
        return this.prompt.apply(null);
    }

    public String getWrongPrompt(K obj) {
        return this.wrongPrompt.apply(obj);
    }

    public String getWrongPrompt() {
        return this.wrongPrompt.apply(null);
    }

    /**
     * Eingabe des Nutzers abfragen, auslesen und konvertieren um den korrekten Datentyp zurückzugeben.
     *
     * @param obj das Objekt für welches die Eingabe geschehen soll.
     *            Es wird benutzt für die Ausgabe der Anweisung und die Benachrichtigung bei einer falschen Eingabe.
     *            Darf auch null sein.
     * @return die Nutzereingabe wird ausgelesen, geprüft und konvertiert um daraufhin einen Wert des Typs V zurückzugeben.
     */
    public abstract V input(K obj);

    public V input() {
        return this.input(null);
    }
}
