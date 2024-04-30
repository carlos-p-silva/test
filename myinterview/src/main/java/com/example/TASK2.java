package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Task here is to write a list. Each element must know the element before and
 * after it. Print out your list and them remove the element in the middle of
 * the list. Print out again.
 *
 */


public class TASK2 {

    public static void main(String[] args) {

        List<String> names = new ArrayList<>();
        names.add("John Lennon");
        names.add("Ringo Starr");
        names.add("George Harrison");
        names.add("Paul McCartney");

        System.out.println("First: ");
        print(names);

        removeMiddleName(names);

        System.out.println("Após remoção: ");
        print(names);

    }

    public static void print(List<String> names) {
        names.forEach(System.out::println);
    }

    public static void removeMiddleName(List<String> names) {
        if (names.size() % 2 == 0 && !names.isEmpty()) {
            int index = names.size() / 2;
            names.remove(index);
        } else {
            System.out.println("Não há elemento no meio da lista para remoção");
        }
    }


}