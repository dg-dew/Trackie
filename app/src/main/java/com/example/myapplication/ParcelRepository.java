package com.example.myapplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ParcelRepository {

    private static ParcelRepository instance;
    private final List<ParcelModel> allParcels   = new ArrayList<>();
    private final List<ParcelModel> arrivedToday = new ArrayList<>();
    private boolean preloaded = false;

    private ParcelRepository() {}

    public static ParcelRepository getInstance() {
        if (instance == null) instance = new ParcelRepository();
        return instance;
    }

    // ── Seed data ────────────────────────────────────────────────────────────
    public void preloadIfNeeded() {
        if (preloaded) return;
        preloaded = true;

        // Helper to make a date N days ago
        // Parcels older than 10 days → will appear in Reminders

        // e-commerce
        allParcels.add(makeOld("Rahul Sharma",  "9876543210", "Serenity New Boys",    "204", "e-commerce",            "Amazon",          0));
        allParcels.add(makeOld("Priya Nair",    "9845012345", "Serenity New Girls",   "112", "e-commerce",            "Flipkart",        3));
        allParcels.add(makeOld("Arjun Kapoor",  "9901234567", "Serenity Empire",      "305", "e-commerce",            "Myntra",         12)); // reminder
        allParcels.add(makeOld("Ananya Sen",    "9823456789", "Serenity Homes",       "408", "e-commerce",            "Ajio",           15)); // reminder
        allParcels.add(makeOld("Rohan Verma",   "9812345678", "Serenity Vel",         "101", "e-commerce",            "Nykaa",           5));

        // Food items
        allParcels.add(makeOld("Sara Johnson",  "9867890123", "Serenity LNG 3",       "203", "Food items",            "Swiggy Instamart", 1));
        allParcels.add(makeOld("John Matthew",  "9756789012", "Serenity Spencer",     "310", "Food items",            "Zomato",          11)); // reminder
        allParcels.add(makeOld("Dew Sharma",    "9745678901", "Serenity Tulip",       "102", "Food items",            "BigBasket",        2));

        // instants
        allParcels.add(makeOld("Priya Pillai",  "9934567890", "Serenity Co-Living PG","205", "instants",              "Blinkit",          0));
        allParcels.add(makeOld("Rahul Gupta",   "9923456789", "Serenity Girls Hostel","307", "instants",              "Zepto",           13)); // reminder
        allParcels.add(makeOld("Ananya Roy",    "9912345678", "Serenity Mounts View", "403", "instants",              "Dunzo",            4));

        // handle with care
        allParcels.add(makeOld("Arjun Nair",    "9889012345", "Serenity Christ View", "108", "handle with care items","Apple Store",      6));
        allParcels.add(makeOld("John Abraham",  "9878901234", "Serenity LNG 2",       "212", "handle with care items","Samsung",         20)); // reminder
        allParcels.add(makeOld("Rohan Kumar",   "9867890124", "Serenity Corner House","306", "handle with care items","OnePlus",          8));
    }

    // Builds a parcel with dateAdded = today minus daysAgo
    private ParcelModel makeOld(String name, String phone, String building,
                                String room, String type, String brand, int daysAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -daysAgo);
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.getTime());
        ParcelModel p = new ParcelModel(name, phone, building, room, type, brand);
        p.dateAdded = date;
        return p;
    }

    // Called when admin adds a new parcel — goes into BOTH lists
    public void addParcel(ParcelModel parcel) {
        allParcels.add(0, parcel);
        arrivedToday.add(0, parcel);
    }

    // Pending Packages = everything
    public List<ParcelModel> getAllParcels() {
        return new ArrayList<>(allParcels);
    }

    // Arrived Today = only parcels added this session via addParcel()
    public List<ParcelModel> getArrivedToday() {
        return new ArrayList<>(arrivedToday);
    }

    // Reminders = parcels whose dateAdded is more than 10 days ago
    public List<ParcelModel> getReminders() {
        List<ParcelModel> result = new ArrayList<>();
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        for (ParcelModel p : allParcels) {
            try {
                Date added = sdf.parse(p.dateAdded);
                Date now   = sdf.parse(today);
                if (added != null && now != null) {
                    long diffMs   = now.getTime() - added.getTime();
                    long diffDays = diffMs / (1000 * 60 * 60 * 24);
                    if (diffDays >= 10) result.add(p);
                }
            } catch (Exception ignored) {}
        }
        return result;
    }

    // ── Model ────────────────────────────────────────────────────────────────
    public static class ParcelModel {
        public String residentName;
        public String phone;
        public String building;
        public String room;
        public String type;
        public String brand;
        public String dateAdded;

        public ParcelModel(String residentName, String phone, String building,
                           String room, String type, String brand) {
            this.residentName = residentName;
            this.phone        = phone;
            this.building     = building;
            this.room         = room;
            this.type         = type;
            this.brand        = brand;
            this.dateAdded    = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        }

        // backward compat
        public ParcelModel(String residentName, String phone, String building,
                           String room, String type) {
            this(residentName, phone, building, room, type, type);
        }
    }
}