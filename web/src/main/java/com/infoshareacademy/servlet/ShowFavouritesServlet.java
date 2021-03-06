package com.infoshareacademy.servlet;

import com.infoshareacademy.context.ContextHolder;
import com.infoshareacademy.domain.view.EventView;
import com.infoshareacademy.freemarker.TemplateProvider;
import com.infoshareacademy.service.event.EventViewService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/show-favourites")
public class ShowFavouritesServlet extends HttpServlet {
    private static final Logger STDLOG = LoggerFactory.getLogger(ShowFavouritesServlet.class.getName());

    @Inject
    private TemplateProvider templateProvider;

    @EJB
    private EventViewService eventViewService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Template template = templateProvider.getTemplate(getServletContext(), "showFavourites.ftlh");
        ContextHolder contextHolder = new ContextHolder(req.getSession());
        Map<String, Object> dataModel = new HashMap<>();


        String emailFav;
        String emailQuery;
        Optional<String> emailOpt = Optional.ofNullable(contextHolder.getEmail());
        if (emailOpt.isPresent() && !emailOpt.isEmpty()) {
            emailFav = "\"" + emailOpt.get() + "\"";
            emailQuery = emailOpt.get();
        } else {
            emailFav = "\"placeholder\"";
            emailQuery = "placeholder";
        }

        Integer actPage = Integer.parseInt(req.getParameter("page"));
        Integer listSize = eventViewService.getFavouritesCount(emailQuery);
        Integer numberOfPages = (listSize % 20 != 0) ? listSize / 20 + 1 : listSize / 20;

        List<EventView> listEvents = eventViewService.prepareFavouriteEvents((actPage - 1) * 20, emailQuery);

        dataModel.put("role", contextHolder.getRole());
        dataModel.put("events", listEvents);
        dataModel.put("actPage", actPage);
        dataModel.put("numberOfPages", numberOfPages);
        dataModel.put("numberOfEvents", listSize);
        dataModel.put("emailFav", emailFav);
        dataModel.put("name", "favourites");
        dataModel.put("action", "");

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();

        try {
            template.process(dataModel, pw);
        } catch (TemplateException e) {
            STDLOG.error("Template for Show All Events page error");
        }
    }
}
