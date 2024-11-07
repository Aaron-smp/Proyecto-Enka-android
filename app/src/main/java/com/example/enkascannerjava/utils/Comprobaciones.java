package com.example.enkascannerjava.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comprobaciones {

    public boolean verificarFecha(String cadena) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean cumplePatron = true;
        try {
            LocalDate fecha = LocalDate.parse(cadena, formato);
        } catch (DateTimeParseException e) {
            cumplePatron = false;
        }
        return cumplePatron;
    }

    public boolean verificarNumIde(String cadena) {
        String patron = "^ES\\d{12}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(cadena);
        return matcher.matches();
    }
}
