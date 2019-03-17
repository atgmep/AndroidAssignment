package com.mobile.makefive.common;


public class Validator {
    public String checkFullName(String name) {
        try {
            String s = Vietnamese.toLower(name.trim());
            if (!s.matches("^[^0-9]+$")) {
                return null;
            }
            return Vietnamese.toUpperName(s);
        } catch (Exception e) {
            return null;
        }
    }

    public String checkUsername(String input) {
        String s = input.replace(" ", "").toLowerCase();
        try {
            if (!s.matches("^.*[a-z].*$")) {
                System.out.println("1");
                return null;
            }
            if (!s.matches("^[a-z0-9]{3,20}$")) {
                System.out.println("2");
                return null;
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String checkPhone(String phone) {
        try {
            String s = phone.replaceAll("\\s", "");
            if (!s.matches("^[0-9]{9,13}+$")) {
                return null;
            }
            return s;
        } catch (Exception e) {
            return null;
        }
    }

    public String checkNumber(String input) {
        try {
            String s = input.replaceAll("\\s", "");
            if (!s.matches("^[0-9]+$")) {
                return null;
            }
            return s;
        } catch (Exception e) {
            return null;
        }
    }

    public String nullProof(String input) {
        if (input == null) {
            return "";
        }
        input = input.trim();
        if (input.equals("undefined")) {
            return "";
        }
        return input;
    }

}
