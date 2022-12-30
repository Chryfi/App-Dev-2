package main.java.de.appdev2.client.dialogue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

public class DialogueString extends Dialogue<String> {
    private final Set<String> expected = new HashSet<>();

    public DialogueString(String prompt, String wrongPrompt, String... expected) {
        super(prompt, wrongPrompt);

        this.expected.addAll(Arrays.asList(expected));
    }

    @Override
    public String input() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(this.getPrompt());

        String eingabe;
        while (!this.expected.contains(eingabe = scanner.nextLine())) {
            System.out.println(this.getWrongPrompt());
        }

        return eingabe;
    }
}
