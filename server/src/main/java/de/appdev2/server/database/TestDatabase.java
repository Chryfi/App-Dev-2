package main.java.de.appdev2.server.database;

import main.java.de.appdev2.entities.*;
import main.java.de.appdev2.server.database.tables.EntityTable;
import main.java.de.appdev2.utils.DateUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class TestDatabase {
    private final Database db;
    private final List<TestTable<?>> testData = new LinkedList<>();

    public TestDatabase(Database db) {
        this.db = db;
    }

    public void testRandomData() throws SQLException {
        int count = 15;
        int maxWaren = 10;
        int lieferantenCount = 20;

        TestTable<Lieferant> lieferanten = new TestTable<>("Lieferanten" ,
                this.db.getLieferantTable(), (lieferant) -> {
            return this.db.getLieferantTable().getLieferant(lieferant.getNr());
        });

        TestTable<Bestellung> bestellungen = new TestTable<>("Bestellung",
                this.db.getBestellungTable(), (bestellung) -> {
            return this.db.getBestellungTable().getBestellung(bestellung.getNr(), bestellung.getLieferant().getNr());
        });

        TestTable<WarenBestellung> warenBestellungen = new TestTable<>("WarenBestellung",
                this.db.getWarenBestellungTable(), (wb) -> {
            return this.db.getWarenBestellungTable().getWarenBestellung(wb.getWare().getNr(),
                    wb.getBestellung().getNr(), wb.getBestellung().getLieferant().getNr());
        });

        TestTable<Ware> waren = new TestTable<>("Ware", this.db.getWareTable(), (ware) -> {
            return this.db.getWareTable().getWare(ware.getNr());
        });

        TestTable<Rechnung> rechnungen = new TestTable<>("Rechnung", this.db.getRechnungTable(), (rechnung) -> {
            return this.db.getRechnungTable().getRechnung(rechnung.getNr());
        });

        this.testData.add(lieferanten);
        this.testData.add(waren);
        this.testData.add(bestellungen);
        this.testData.add(warenBestellungen);
        this.testData.add(rechnungen);

        for (int i = 0; i < lieferantenCount; i++) {
            lieferanten.add(new Lieferant("keine Ahnung"));
        }

        for (int i = 0; i < count; i++) {
            /*
             * Erstelle eine Bestellung mit einer zufälligen Anzahl an Waren
             * und einem zufälligen Lieferanten
             */
            int warenCount = (int) (Math.random() * maxWaren);
            Lieferant lieferant = lieferanten.get((int) (Math.random() * lieferantenCount));
            Bestellung bestellung = new Bestellung((int) (Math.random() * 100000), DateUtils.getRandomDate(2022, 2025), lieferant);

            /* erstelle Waren und teile sie der Bestellung zu */
            for (int j = 0; j < warenCount; j++) {
                Ware ware = new Ware(Math.round(Math.random() * 20 * 100) / 100F, (int) (Math.random() * 100), "coole Ware" + warenCount);
                int bestellt = (int) (Math.random() * 50);
                WarenBestellung wb = new WarenBestellung(ware, bestellung, bestellt, (int) (Math.round(Math.random() * bestellt)));

                waren.add(ware);
                warenBestellungen.add(wb);
            }

            if (Math.random() > 0.5F) {
                Rechnung rechnung = new Rechnung(bestellung, DateUtils.getRandomDate(2022, 2030), Math.random() > 0.5);

                rechnungen.add(rechnung);
            }

            bestellungen.add(bestellung);
        }


        /* Einfügen in die Datenbank */
        for (TestTable<?> test : this.testData) {
            test.insert();
        }

        boolean passed = true;
        for (TestTable<?> test : this.testData) {
            Map<?,?> wrong = test.testEquality();

            if (!wrong.isEmpty()) {
                passed = false;

                System.out.println("Data that is not equal:");

                System.out.println("\n" + test.tableName + ":");
                this.outputTestResult(wrong);
            }
        }

        if (passed) System.out.println("Test passed");
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

            for (T test : testData) {
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
