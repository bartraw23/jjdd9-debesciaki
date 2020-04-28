package com.infoshareacademy.display;

import com.infoshareacademy.parser.Event;
import com.infoshareacademy.parser.Place;
import com.infoshareacademy.properties.PropertiesRepository;
import com.infoshareacademy.repository.EventRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.infoshareacademy.display.CMDCleaner.cleanConsole;

public class Display {
    private final static Logger STDOUT = LoggerFactory.getLogger("CONSOLE_OUT");
    Integer qty;
    Integer elemPerPage;
    boolean firstStart;

    public void displayCurrentEvents() {
        cleanConsole();
        Optional<Integer> compQty;
        Optional<Integer> pageMaxElements;
        firstStart = true;
        do {
            if (!firstStart) {
                cleanConsole();
                STDOUT.info("Podano zerowe lub ujemne wartości parametrów, proszę wprowadzić je ponownie.\n");
            }
            firstStart = false;
            compQty = inputInteger("Ile nadchodzących wydarzeń chcesz zobaczyć łącznie? ");
            pageMaxElements = inputInteger("Ile wydarzeń chcesz zobaczyć na jednej stronie? ");
            if (compQty.isPresent()) {
                qty = compQty.get();
            }
            if (pageMaxElements.isPresent()) {
                elemPerPage = pageMaxElements.get();
            }
        } while (qty <= 0 || elemPerPage <= 0);
        List<Event> eventList = selectedList(qty);
        displayPages(qty, elemPerPage, eventList);
    }

    public Optional<Integer> inputInteger(String subject) {
        Integer qty = null;
        Optional<Integer> opt = Optional.ofNullable(qty);
        String in;
        do {
            STDOUT.info("{}", subject);
            Scanner scanner = new Scanner(System.in);
            if (NumberUtils.isDigits(in = scanner.nextLine())) {
                qty = Integer.parseInt(in);
            }
            opt = Optional.ofNullable(qty);
            if (!NumberUtils.isDigits(in)) {
                cleanConsole();
                STDOUT.info("Źle wprowadzone dane, spróbuj ponownie!\n");
            }
        } while (opt.isEmpty());
        return opt;
    }

    public Map<Integer, Event> selectedMap(int qty) {
        //Selective filling
        Map<Integer, Event> eventMap = new HashMap<>();
        for (Event e : EventRepository.getAllEvents()) {
            if (eventMap.size() < qty) {
                if (isAfterNow(e.getEndDate())) eventMap.put(e.getId(), e);
            }
        }
        return eventMap;
    }

    public List<Event> selectedList(int qty) {
        List<Event> eventList = new ArrayList<>();
        for (Event e : EventRepository.getAllEvents()) {
            if (eventList.size() < qty && eventList.size() < EventRepository.getAllEvents().size()) {
               if (isAfterNow(e.getEndDate())) eventList.add(e);
            }
        }
        return eventList;
    }

    public boolean isAfterNow(LocalDateTime eventTime) {
        return eventTime.isAfter(LocalDateTime.now());
    }

    public void displayPages(Integer qty, Integer elemPerPage, List<Event> eventList) {
        Optional<Integer> decision = null;
        double pageCountd = Math.ceil((double) qty / elemPerPage);
        Integer pageCount = (int) pageCountd;
        int limU = 0, limD = elemPerPage, actual = 1;
        do {
            cleanConsole();
            for (int i = limU; i < limD; i++) {
                if (i < eventList.size()) {
                    Event e = eventList.get(i);
                    consolePrintEventScheme(e, PropertiesRepository.getInstance().getProperty("date-format"));
                }
            }
            if (actual == 1 && pageCount > 1) {
                decision = inputInteger("2 - Następna\n0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + "\nTwój wybór to: ");
            }
            if (actual == pageCount && actual != 1) {
                decision = inputInteger("1 - Poprzednia\n0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + "\nTwój wybór to: ");
            }
            if (actual > 1 && actual < pageCount) {
                decision = inputInteger("2 - Następna\n1 - Poprzednia\n0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + "\nTwój wybór to: ");
            }
            if (actual == 1 && pageCount == 1) {
                decision = inputInteger("0 - Wyjdź\nStrona nr " + actual + " z " + pageCount + "\nTwój wybór to: ");
            }
            int dec = 0;
            if (decision.isPresent()) {
                dec = decision.get();
            }
            if (actual > 1 && dec == 1) {
                actual--;
                limU -= elemPerPage;
                limD -= elemPerPage;
            } else if (actual < pageCount && dec == 2) {
                actual++;
                limU += elemPerPage;
                limD += elemPerPage;
            } else if (dec == 0) {
                break;
            }
        } while (decision.get() != 0);
    }

    public void consolePrintEventScheme(Event e, String pattern) {
        boolean firstTime = true;
        Place p = e.getPlace();
        String eventTimeFormatted = null;
        Optional<String> opt = Optional.ofNullable(eventTimeFormatted);
        do {
            if (!firstTime) {
                pattern = PropertiesRepository.getInstance().getProperty("date-format");
            }
            if (pattern.isBlank() || pattern.isEmpty()) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }
            try {
                firstTime = false;
                eventTimeFormatted = configureDate(e.getEndDate(), pattern);
                opt = Optional.ofNullable(eventTimeFormatted);
            } catch (IllegalMonitorStateException exception) {
                promptError();
            } catch (UnsupportedOperationException exception) {
                promptError();
            } catch (IllegalArgumentException exception) {
                promptError();
            } catch (DateTimeException exception) {
                promptError();
            }

        } while (opt.isEmpty());

        STDOUT.info("Nazwa: {}{}{}\nLokalizacja: {}{}{}\nData zakończenia: {}{}{}\n \n",
                ConsoleColor.RED_UNDERLINED, e.getName(), ConsoleColor.RESET,
                ConsoleColor.BLUE, p.getName(), ConsoleColor.RESET,
                ConsoleColor.BLUE, eventTimeFormatted, ConsoleColor.RESET);
    }

    private void promptError() {
        cleanConsole();
        STDOUT.info("Niepoprawny format daty w pliku konfiguracyjnym, proszę popraw konfigurację. \nZmianę potwierdź klawiszem enter\n");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public String configureDate(LocalDateTime eventTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return eventTime.format(formatter);
    }
}