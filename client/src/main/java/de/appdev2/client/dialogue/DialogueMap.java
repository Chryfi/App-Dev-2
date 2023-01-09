package main.java.de.appdev2.client.dialogue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Eine Dialog-Klasse die eine Map basierend auf der Nutzereingabe erzeugt.
 *
 * @param <K> der Datentyp für die Schlüssel der Map.
 * @param <V> der Datentyp für die Werte der Map.
 */
public class DialogueMap<K, V> extends Dialogue<K, Map<K, V>> {
    /**
     * Die Schlüssel für die die Werte eingegeben werden sollen.
     */
    private Set<K> keys = new HashSet<>();
    /**
     * Dieser Dialog wird für die Eingabe der Werte für die Map genutzt.
     */
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

    /**
     * Diese Methode iteriert durch die gegebenen Schlüssel, siehe {@link #keys}, und führt für jeden Schlüssel die Methode
     * {@link Dialogue#input(Object)} von {@link #valueDialogue} aus.
     *
     * @param obj das Objekt für welches die Eingabe geschehen soll.
     *            Es wird benutzt für die Ausgabe der Anweisung und die Benachrichtigung bei einer falschen Eingabe.
     *            Darf auch null sein.
     * @return eine Map, mit den vorher definierten Schlüsseln {@link #keys} und
     * jeweiligen Werten basierend auf der Nutzereingabe und {@link #valueDialogue}.
     */
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
