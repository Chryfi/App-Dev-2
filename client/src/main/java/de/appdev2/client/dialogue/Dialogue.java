package main.java.de.appdev2.client.dialogue;

import java.util.function.Function;

public abstract class Dialogue<V> {
    /**
     * Ausgabe am Anfang
     */
    private String prompt;
    /**
     * Bei falscher Eingabe, folgendes ausgeben
     */
    private String wrongPrompt;

    public Dialogue(String prompt, String wrongPrompt) {
        this.prompt = prompt;
        this.wrongPrompt = wrongPrompt;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getWrongPrompt() {
        return this.wrongPrompt;
    }

    public abstract V input();
}
