package com.example;
import java.util.Scanner;
/**
 * 
 *
 * Task here is to implement a function that says if a given string is
 * palindrome.
 * 
 * 
 * 
 * Definition=> A palindrome is a word, phrase, number, or other sequence of
 * characters which reads the same backward as forward, such as madam or
 * racecar.
 */
public class TASK1 {

    public Boolean isPalindrome(String phrase) {
        StringBuilder sb = new StringBuilder(phrase);
        String palindrome =  sb.reverse().toString();
        return phrase.equals(palindrome);
    }
 
}