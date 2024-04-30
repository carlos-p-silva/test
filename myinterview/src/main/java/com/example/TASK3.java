package com.example;

import java.util.*;

/**
 * Write a list and add an aleatory number of Strings. In the end, print out how
 * many distinct itens exists on the list.
 *
 */
public class TASK3 {

    public static void main(String[] args) {

        List<String> itemList = new ArrayList<>();

        addItem(itemList, "Notebook");
        addItem(itemList, "Smartphone");
        addItem(itemList, "Tablet");
        addItem(itemList, "Mouse");
        addItem(itemList, "TV");

        System.out.println("List of items: " + itemList);

        int distinctItemCount = countDistinctItems(itemList);
        System.out.println("Number of distinct items: " + distinctItemCount);

    }

    private static void addItem(List<String> itemList, String item) {
        itemList.add(item);
    }

    private static int countDistinctItems(List<String> itemList) {
        Set<String> distinctItems = new HashSet<>(itemList);
        return distinctItems.size();
    }

  
}
