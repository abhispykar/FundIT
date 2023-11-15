package com.example.fundit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
        public boolean validateName(String name) {
            // Define a regular expression pattern for alphabetic characters
            String regex = "^[a-zA-Z]+$";

            // Create a Pattern object
            Pattern pattern = Pattern.compile(regex);

            // Create a Matcher object
            Matcher matcher = pattern.matcher(name);

            // Use the find() method to check if the input string matches the pattern
            return matcher.find();
        }
}
