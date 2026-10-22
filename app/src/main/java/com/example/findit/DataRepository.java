package com.example.findit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataRepository {
    private static DataRepository instance;
    private final List<Item> items;

    private DataRepository() {
        items = new ArrayList<>();
        seedInitialData();
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    private void seedInitialData() {
        items.add(new Item(
                "item-1",
                "Black Backpack",
                "LOST",
                "Bags",
                "A black backpack with books, notebooks, and a water bottle inside.",
                "University Library",
                "15 October",
                "+92 300 1234567",
                null,
                true // Part of user's reports
        ));

        items.add(new Item(
                "item-2",
                "Mobile Phone",
                "LOST",
                "Electronics",
                "Blue Samsung Galaxy smartphone with a transparent cover.",
                "Cafeteria",
                "14 October",
                "+92 312 9876543",
                null,
                false
        ));

        items.add(new Item(
                "item-3",
                "Notebook",
                "LOST",
                "Stationery",
                "Calculus spiral notebook with handwritten notes on Chapter 3.",
                "Classroom 12",
                "13 October",
                "+92 333 5551234",
                null,
                false
        ));

        items.add(new Item(
                "item-4",
                "Set of Keys",
                "FOUND",
                "Accessories",
                "3 silver keys attached to a red car keychain.",
                "Computer Lab",
                "16 October",
                "+92 345 1122334",
                null,
                true // Part of user's reports
        ));

        items.add(new Item(
                "item-5",
                "Student ID Card",
                "FOUND",
                "Documents",
                "BS Computer Science student card belonging to Ali Raza.",
                "Main Gate",
                "16 October",
                "+92 321 4455667",
                null,
                false
        ));

        items.add(new Item(
                "item-6",
                "Water Bottle",
                "FOUND",
                "Accessories",
                "Stainless steel metallic blue water bottle.",
                "Sports Ground",
                "15 October",
                "+92 301 7788990",
                null,
                false
        ));
    }

    public synchronized List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    public synchronized List<Item> getLostItems() {
        List<Item> lostList = new ArrayList<>();
        for (Item item : items) {
            if ("LOST".equalsIgnoreCase(item.getType())) {
                lostList.add(item);
            }
        }
        return lostList;
    }

    public synchronized List<Item> getFoundItems() {
        List<Item> foundList = new ArrayList<>();
        for (Item item : items) {
            if ("FOUND".equalsIgnoreCase(item.getType())) {
                foundList.add(item);
            }
        }
        return foundList;
    }

    public synchronized List<Item> getMyReports() {
        List<Item> myReports = new ArrayList<>();
        for (Item item : items) {
            if (item.isUserReport()) {
                myReports.add(item);
            }
        }
        return myReports;
    }

    public synchronized Item getItemById(String id) {
        if (id == null) return null;
        for (Item item : items) {
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public synchronized void addItem(Item item) {
        if (item.getId() == null || item.getId().trim().isEmpty()) {
            item.setId(UUID.randomUUID().toString());
        }
        items.add(0, item);
    }

    public synchronized boolean updateItem(Item updatedItem) {
        if (updatedItem == null || updatedItem.getId() == null) return false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(updatedItem.getId())) {
                items.set(i, updatedItem);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean deleteItem(String itemId) {
        if (itemId == null) return false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(itemId)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public synchronized List<Item> searchAndFilter(String keyword, String categoryType, String location) {
        List<Item> results = new ArrayList<>();
        String kw = (keyword == null) ? "" : keyword.trim().toLowerCase();
        String loc = (location == null) ? "" : location.trim().toLowerCase();

        for (Item item : items) {
            // Category / Type match
            if (categoryType != null && !categoryType.isEmpty() && !"ALL".equalsIgnoreCase(categoryType)) {
                if (!categoryType.equalsIgnoreCase(item.getType())) {
                    continue;
                }
            }

            // Keyword match (name or description)
            if (!kw.isEmpty()) {
                boolean nameMatch = item.getName() != null && item.getName().toLowerCase().contains(kw);
                boolean descMatch = item.getDescription() != null && item.getDescription().toLowerCase().contains(kw);
                if (!nameMatch && !descMatch) {
                    continue;
                }
            }

            // Location match
            if (!loc.isEmpty()) {
                if (item.getLocation() == null || !item.getLocation().toLowerCase().contains(loc)) {
                    continue;
                }
            }

            results.add(item);
        }
        return results;
    }
}
