package com.infoshareacademy.validator;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.infoshareacademy.display.CMDCleaner.cleanConsole;

public class Validator {
    private static final Logger STDOUT = LoggerFactory.getLogger("CONSOLE_OUT");

    public Validator() {
    }

    public Optional<String> inputString(String subject) {
        String in;
        Optional<String> opt = null;
        do {
            STDOUT.info("{}", subject);
            Scanner scanner = new Scanner(System.in);
            in = scanner.nextLine();
            opt = Optional.ofNullable(in);
        } while (opt.isEmpty());
        return opt;
    }

    public LocalDateTime localDateTimeRequest(String subject) {
        LocalDateTime out = null;
        String in;
        Optional<LocalDateTime> optionalLocalDateTime = Optional.ofNullable(null);
        String patternStr = "yyyy/MM/dd HH:mm";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patternStr);
        do {
            cleanConsole();
            Scanner scanner = new Scanner(System.in);
            STDOUT.info("Wprowadź datę {} {}: ", patternStr, subject);
            in = scanner.nextLine();
            try {
                DateUtils.parseDate(in, patternStr);
            } catch (ParseException e) {
                promptError("Źle wprowadzona data!");
                continue;
            }
            Pattern p = Pattern.compile("^[1-2][0-9]{3}/[0-1][0-9]/[0-3][0-9] [0-2][0-9]:[0-5][0-9]$");
            Matcher matcher = p.matcher(in);
            boolean matches = matcher.matches();
            if (matches) {
                out = LocalDateTime.parse(in, dtf);
                optionalLocalDateTime = Optional.ofNullable(out);
            } else {
                promptError("Źle wprowadzona data!");
            }
        } while (optionalLocalDateTime.isEmpty() || in.isEmpty() || in.isBlank());
        return out;
    }

    private void promptError(String msg) {
        cleanConsole();
        STDOUT.info("{}\n\n", msg);
        STDOUT.info("Potwierdź błąd wciskając klawisz [Enter]\n");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        cleanConsole();
    }
}
