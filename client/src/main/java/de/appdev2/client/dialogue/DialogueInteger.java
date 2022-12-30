package main.java.de.appdev2.client.dialogue;

import java.util.Scanner;
import java.util.function.Function;

public class DialogueInteger extends Dialogue<Integer> {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public DialogueInteger(String prompt, String wrongPrompt) {
        super(prompt, wrongPrompt);
    }

    public DialogueInteger(String prompt, String wrongPrompt, int min, int max) {
        super(prompt, wrongPrompt);

        this.min = min;
        this.max = max;
    }

    public DialogueInteger(String prompt, String wrongPrompt, int min) {
        super(prompt, wrongPrompt);

        this.min = min;
    }

    @Override
    public Integer input() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(this.getPrompt());

        int eingabe;

        while (true) {
            try {
                eingabe = this.format(scanner.nextLine());

                break;
            } catch (NumberFormatException e) {
                System.out.println(this.getWrongPrompt());
                System.out.println(e.getMessage());
            }
        }

        return eingabe;
    }

    private Integer format(String scan) {
        Integer eingabe;

        eingabe = Integer.parseInt(scan);

        if (eingabe < this.min || eingabe > this.max) {
            throw new NumberFormatException("Außerhalb vom Definitionsbereich!");
        }

        return eingabe;
    }
}
