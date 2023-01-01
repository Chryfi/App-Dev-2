package main.java.de.appdev2.server.testing;

import main.java.de.appdev2.entities.*;
import main.java.de.appdev2.server.database.Database;
import main.java.de.appdev2.server.database.tables.EntityTable;

import java.sql.SQLException;
import java.util.*;

/**
 * Eine Klasse die zufallsgenerierte Daten testet.
 *
 * @author Christian Fritz
 */
public class TestDatabase {
    private final Database db;

    /**
     * Wieviele Bestellungen erzeugt werden sollen
     */
    private int bestellungCount;
    /**
     * Maximum an Waren pro Bestellung
     */
    private int maxWaren;
    /**
     * Wieviele Lieferanten erzeugt werden sollen
     */
    private int lieferantenCount;

    private final List<TestTable<?>> testTables = new ArrayList<>();
    private final TestTable<Lieferant> lieferanten;
    private final TestTable<Ware> waren;
    private final TestTable<Bestellung> bestellungen;
    private final TestTable<WarenBestellung> warenBestellungen;
    private final TestTable<Rechnung> rechnungen;

    public TestDatabase(Database db) {
        this(db, 10, 10, 20);
    }

    public TestDatabase(Database db, int bestellungCount, int maxWaren, int lieferantenCount) {
        this.db = db;
        this.bestellungCount = bestellungCount;
        this.maxWaren = maxWaren;
        this.lieferantenCount = lieferantenCount;

        this.lieferanten = new TestTable<>("Lieferanten" , this.db.getLieferantTable(), (lieferant) -> {
            return this.db.getLieferantTable().getLieferant(lieferant.getNr());
        });

        this.bestellungen = new TestTable<>("Bestellung", this.db.getBestellungTable(), (bestellung) -> {
            return this.db.getBestellungTable().getBestellung(bestellung.getNr(), bestellung.getLieferant().getNr());
        });

        this.warenBestellungen = new TestTable<>("WarenBestellung", this.db.getWarenBestellungTable(), (wb) -> {
            return this.db.getWarenBestellungTable().getWarenBestellung(wb.getWare().getNr(),
                    wb.getBestellung().getNr(), wb.getBestellung().getLieferant().getNr());
        });

        this.waren = new TestTable<>("Ware", this.db.getWareTable(), (ware) -> {
            return this.db.getWareTable().getWare(ware.getNr());
        });

        this.rechnungen = new TestTable<>("Rechnung", this.db.getRechnungTable(), (rechnung) -> {
            return this.db.getRechnungTable().getRechnung(rechnung.getNr());
        });

        this.testTables.add(this.lieferanten);
        this.testTables.add(this.waren);
        this.testTables.add(this.bestellungen);
        this.testTables.add(this.warenBestellungen);
        this.testTables.add(this.rechnungen);
    }

    public void generateRandomData() {
        /* Waren erstellen die keiner Bestellung angehören */
        for (int i = 0; i < this.maxWaren; i++) {
            this.waren.add(DataGenerator.getWare(10F, 50F, 500));
        }

        /* Lieferanten erstellen die später eine zufällige Bestellung erhalten können */
        for (int i = 0; i < this.lieferantenCount; i++) {
            this.lieferanten.add(new Lieferant("Zufalls Lieferant"));
        }

        for (int i = 0; i < this.bestellungCount; i++) {
            /*
             * Erstelle eine Bestellung mit einer zufälligen Anzahl an Waren
             * und einem zufälligen Lieferanten
             */
            Lieferant lieferant = this.lieferanten.get((int) (Math.random() * this.lieferantenCount));
            Bestellung bestellung = DataGenerator.getBestellung(lieferant, 2022, 2025);

            Set<Ware> bestellteWaren = DataGenerator.getWaren(1, this.maxWaren, 2F, 40F, 100);
            Set<WarenBestellung> wbs = DataGenerator.getWarenBestellungen(bestellung, bestellteWaren, 0, 50, 0, 50);

            this.waren.testData.addAll(bestellteWaren);
            this.warenBestellungen.testData.addAll(wbs);

            if (Math.random() > 0.5F) {
                Rechnung rechnung = DataGenerator.getRechnung(bestellung, 2022, 2030, Math.random() > 0.5);

                this.rechnungen.add(rechnung);
            }

            this.bestellungen.add(bestellung);
        }

        /* Erstelle Lieferanten die keine Bestellung haben */
        for (int i = 0; i < Math.round(this.lieferantenCount * 0.5F); i++) {
            this.lieferanten.add(new Lieferant("ohne Bestellung"));
        }
    }

    public void testSetGelieferteMenge() {
        System.out.println("Test WarenBestellungTable setGelieferteMenge");

        try {
            for (WarenBestellung wb : this.warenBestellungen.testData) {
                if (!this.db.getWarenBestellungTable().setGelieferteMenge(wb, (int) (Math.random() * 1000))) {
                    System.out.println("WarenBestellung ware=" + wb.getWare().getNr()
                            + ", bestellungnr=" + wb.getBestellung().getNr()
                            + ", lieferantennr=" + wb.getBestellung().getLieferant().getNr() + " konnte nicht geupdated werden.");
                }
            }

            this.readAndCompare();
        } catch (SQLException e) {
            System.out.println("Test not passed: SQLException occurred!");

            e.printStackTrace();
        }
    }

    public void testWareUpdateStueckzahl() {
        System.out.println("Test WarenTable updateStueckzahl");

        try {
            for (Ware ware : this.waren.testData) {
                if (!this.db.getWareTable().updateStueckzahl(ware, (int) (Math.random() * 1000))) {
                    System.out.println("ware " + ware.getNr() + " konnte nicht geupdated werden.");
                }
            }

            this.readAndCompare();
        } catch (SQLException e) {
            System.out.println("Test not passed: SQLException occurred!");

            e.printStackTrace();
        }
    }

    public void testGetWarenBestellungen() {
        System.out.println("Test getWarenBestellungen from Bestellung");

        try {
            boolean passed = true;

            for (Bestellung bestellung : this.bestellungen.testData) {
                Set<WarenBestellung> wbSet = this.db.getWarenBestellungTable().getWarenBestellungen(bestellung);

                int equaled = 0;
                for (WarenBestellung wb : this.warenBestellungen.testData) {
                    for (WarenBestellung wbDB : wbSet) {
                        if (wbDB.equals(wb)) {
                            equaled++;
                            break;
                        }
                    }
                }

                if (wbSet.size() != equaled) {
                    System.out.println("Test not passed: " + equaled + " are equal while " + wbSet.size() + " were read from databse.");
                    passed = false;
                }
            }

            if (passed) {
                System.out.println("Test passed");
            }
        } catch (SQLException e) {
            System.out.println("Test not passed: SQLException occurred!");

            e.printStackTrace();
        }
    }

    public void testRechnungSetOffen() {
        System.out.println("Test RechnungTable setOffen");

        try {
            for (Rechnung rechnung : this.rechnungen.testData) {
                if (!this.db.getRechnungTable().setOffen(rechnung, Math.random() > 0.5)) {
                    System.out.println("rechnung " + rechnung.getNr() + " konnte nicht geupdated werden.");
                }
            }

            this.readAndCompare();
        } catch (SQLException e) {
            System.out.println("Test not passed: SQLException occurred!");

            e.printStackTrace();
        }
    }

    public void testInsertion() {
        System.out.println("Test insertion and read");

        try {
            /* Einfügen in die Datenbank */
            for (TestTable<?> test : this.testTables) {
                test.insert();
            }

            /*
             * Testen, ob die ausgelesenen Daten aus der Datenbank
             * den Objekten hier entsprechen
             */
            this.readAndCompare();
        } catch (SQLException e) {
            System.out.println("Test not passed: SQLException occurred!");

            e.printStackTrace();
        }
    }

    private void readAndCompare() throws SQLException {
        boolean passed = true;
        boolean empty = false;

        for (TestTable<?> testTable : this.testTables) {
            empty = empty || testTable.testData.isEmpty();

            Map<?,?> wrong = testTable.testEquality();

            if (!wrong.isEmpty()) {
                passed = false;

                System.out.println("Data that is not equal:");

                System.out.println("\n" + testTable.tableName + ":");
                this.outputTestResult(wrong);
            }
        }

        if (empty) {
            System.out.println("One or more of the test data is empty!!!!!");
        }
        else if (passed) {
            System.out.println("Test passed");
        }
    }

    private void outputTestResult(Map<?,?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println("Test data that was inserted: " + entry.getKey());
            System.out.println("Data from database:          " + entry.getValue() + "\n");
        }
    }

    private static class TestTable<T> {
        private String tableName;
        private SqlSupplier<T> retrieve;
        private EntityTable<T> table;
        private final List<T> testData = new ArrayList<>();

        public TestTable(String tableName, EntityTable<T> table, SqlSupplier<T> retrieve) {
            this.table = table;
            this.retrieve = retrieve;
            this.tableName = tableName;
        }

        public T get(int i) {
            return this.testData.get(i);
        }

        public void add(T test) {
            this.testData.add(test);
        }

        public void insert() throws SQLException {
            for (T test : this.testData) {
                this.table.insert(test);
            }
        }

        public Map<T, T> testEquality() throws SQLException {
            Map<T, T> notEqual = new HashMap<>();

            for (T test : this.testData) {
                T fromDB = this.retrieve.execute(test);

                if (!test.equals(fromDB)) {
                    notEqual.put(test, fromDB);
                }
            }

            return notEqual;
        }
    }

    @FunctionalInterface
    private interface SqlSupplier<T> {
        T execute(T data) throws SQLException;
    }
}
