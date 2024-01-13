package ru.caloriemanager.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.caloriemanager.model.Meal;
import ru.caloriemanager.model.MealTo;
import ru.caloriemanager.util.DateTimeUtil;
import ru.caloriemanager.web.meal.MealRestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            LOG.info("getAll");
            List<MealTo> filteredMealToList = mealRestController.getBetween(LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX);
            request.setAttribute("meals", filteredMealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }
        switch (action) {
            case "create": {
                LOG.info("create meal");
                Meal meal = new Meal(LocalDateTime.now(), "", 1000);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("mealForm.jsp").forward(request, response);
                break;
            }
            case "update": {
                int id = Integer.parseInt(Objects.requireNonNull(request.getParameter("id")));
                LOG.info("update meal {}", id);
                Meal meal = mealRestController.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("mealForm.jsp").forward(request, response);
                break;
            }
            case "delete": {
                int id = Integer.parseInt(Objects.requireNonNull(request.getParameter("id")));
                LOG.info("delete meal {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String actonType = request.getParameter("action");
        switch (actonType) {
            case "filter": {
                LOG.info("filter");
                LocalDate startD =  DateTimeUtil.parseToLocalDate(request.getParameter("fromDate"), DateTimeUtil.MIN_DATE);
                LocalDate endD =  DateTimeUtil.parseToLocalDate(request.getParameter("toDate"), DateTimeUtil.MAX_DATE);
                LocalTime startT = DateTimeUtil.parseToLocalTime(request.getParameter("fromTime"), LocalTime.MIN);
                LocalTime endT = DateTimeUtil.parseToLocalTime(request.getParameter("toTime"), LocalTime.MAX);
                List<MealTo> filteredMealToList = mealRestController.getBetween(startD, endD, startT, endT);
                request.setAttribute("meals", filteredMealToList);
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                break;
            }
            case "edit": {
                request.setCharacterEncoding("UTF-8");
                String id = request.getParameter("id");
                LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                String description = request.getParameter("description");
                int calories = Integer.parseInt(request.getParameter("calories"));
                Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id), dateTime, description, calories);
                String action = (meal.isNew() ? "create meal {}" : "update meal {}");
                LOG.info(action, meal);
                if (meal.isNew())
                    mealRestController.create(meal);
                else
                    mealRestController.update(meal);
                response.sendRedirect("meals");
                break;
            }
        }
    }
}
