package main.java.de.appdev2.client.dialogue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DialogueMap<K, V> extends Dialogue<K, Map<K, V>> {
    private Set<K> keys = new HashSet<>();
    private Dialogue<K, V> valueDialogue;

    public DialogueMap(String prompt, Dialogue<K, V> valueDialogue) {
        this((obj) -> prompt, valueDialogue);
    }

    public DialogueMap(Function<K, String> prompt, Dialogue<K, V> valueDialogue) {
        super(prompt, (obj) -> "");

        this.valueDialogue = valueDialogue;
    }

    public DialogueMap(Function<K, String> prompt, Set<K> keys, Dialogue<K, V> valueDialogue) {
        super(prompt, (obj) -> "");

        this.keys = keys;
        this.valueDialogue = valueDialogue;
    }

    public void setKeys(Set<K> keys) {
        this.keys = keys;
    }

    public Set<K> getKeys() {
        return this.keys;
    }

    @Override
    public Map<K, V> input(K obj) {
        Map<K, V> map = new HashMap<>();

        System.out.println(this.getPrompt(obj));

        for (K key : this.keys) {
            map.put(key, this.valueDialogue.input(key));
        }

        return map;
    }
}
