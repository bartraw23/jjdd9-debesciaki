package com.infoshareacademy.service.stat;

import com.infoshareacademy.domain.entity.Event;
import com.infoshareacademy.domain.entity.User;
import com.infoshareacademy.domain.entity.ViewStat;
import com.infoshareacademy.domain.stat.ClicksPerEvent;
import com.infoshareacademy.domain.stat.ClicksPerOrganizer;
import com.infoshareacademy.repository.EventDao;
import com.infoshareacademy.repository.UserDao;
import com.infoshareacademy.repository.ViewStatDao;
import com.infoshareacademy.util.StringToLocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Stateless
public class ViewStatService {

    private static final Logger STDLOG = LoggerFactory.getLogger(ViewStatService.class.getName());

    @Inject
    private ViewStatDao viewStatDao;

    @Inject
    private UserDao userDao;

    @Inject
    private EventDao eventDao;

    public void persistSingleView(String email, Long eventId) {
        STDLOG.info("Persisting of a single view stat......");
        viewStatDao.save(joinViewStat(email, eventId));
    }


    public List<ClicksPerEvent> provideGlobalClicksPerEvent() {
        return viewStatDao.findGlobalClicksPerEvent();
    }

    public List<ClicksPerEvent> providePeriodClicksPerEvent(String date1, String date2) {
        return viewStatDao.findPeriodClicksPerEvent(StringToLocalDateTime.process(date1), StringToLocalDateTime.process(date2));
    }

    public List<ClicksPerOrganizer> provideGlobalClicksPerOrganizer() {
        return viewStatDao.findGlobalClicksPerOrganizer();
    }

    public List<ClicksPerOrganizer> providePeriodClicksPerOrganizer(String date1, String date2) {
        return viewStatDao.findPeriodClicksPerOrganizer(StringToLocalDateTime.process(date1), StringToLocalDateTime.process(date2));
    }

    private ViewStat joinViewStat(String email, Long eventId) {
        ViewStat viewStat = new ViewStat();
        STDLOG.info("Preparation of a single view stat entity......");
        Optional<User> userOptional = userDao.findByEmail(email);
        if (userOptional.isPresent()) {
            viewStat.setUser(userOptional.get());
        }
        Optional<Event> eventOptional = eventDao.findById(eventId);
        if (eventOptional.isPresent()) {
            viewStat.setEvent(eventOptional.get());
        }
        viewStat.setViewDate(LocalDateTime.now());
        return viewStat;
    }
}
