package com.infoshareacademy.favourites;

import com.infoshareacademy.display.DisplayEvents;
import com.infoshareacademy.parser.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ShowUpcoming {
    private final static Logger STDOUT = LoggerFactory.getLogger("CONSOLE_OUT");
    ShowFavourites showFavourites = new ShowFavourites();
    List<Event> listFavourites = showFavourites.getEvents();
    DisplayEvents display = new DisplayEvents();

    public ShowUpcoming() throws IOException {
        run();
    }

    public void run() {
        if (listFavourites.isEmpty()) {
            STDOUT.info("BRAK INFOMRACJI O NAJBLIŻSZYM ULUBIONYM WYDARZENIU\n");
        } else {
            Event upcomingEvent = listFavourites.get(0);
            for (int i = 0; i < listFavourites.size(); i++) {
                if (listFavourites.get(i).getStartDate().isBefore(upcomingEvent.getStartDate())) {
                    upcomingEvent = listFavourites.get(i);
                }
            }
            display.consolePrintSingleEventScheme(upcomingEvent);
        }
    }
}
