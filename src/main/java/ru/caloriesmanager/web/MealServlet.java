package ru.caloriesmanager.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.caloriesmanager.model.Meal;
import ru.caloriesmanager.transferObject.UserMealWithExcess;
import ru.caloriesmanager.util.DateTimeUtil;
import ru.caloriesmanager.web.meal.MealRestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class MealServlet extends HttpServlet {
    private ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
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
            List<UserMealWithExcess> filteredMealToList = mealRestController
                    .getBetween(DateTimeUtil.MIN_DATE, LocalTime.MIN, DateTimeUtil.MAX_DATE, LocalTime.MAX);
            request.setAttribute("meals", filteredMealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }
        switch (action) {
            case "create": {
                Meal meal = new Meal(LocalDateTime.now(), "", 1000);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("mealForm.jsp").forward(request, response);
                break;
            }
            case "update": {
                int id = Integer.parseInt(Objects.requireNonNull(request.getParameter("id")));
                Meal meal = mealRestController.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("mealForm.jsp").forward(request, response);
                break;
            }
            case "delete": {
                int id = Integer.parseInt(Objects.requireNonNull(request.getParameter("id")));
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            }
            case "filter" : {
                LocalDate startD =  DateTimeUtil.parseToLocalDate(request.getParameter("startDate"), DateTimeUtil.MIN_DATE);
                LocalDate endD =  DateTimeUtil.parseToLocalDate(request.getParameter("endDate"), DateTimeUtil.MAX_DATE);
                LocalTime startT = DateTimeUtil.parseToLocalTime(request.getParameter("startTime"), LocalTime.MIN);
                LocalTime endT = DateTimeUtil.parseToLocalTime(request.getParameter("endTime"), LocalTime.MAX);
                List<UserMealWithExcess> filteredMealToList = mealRestController.getBetween(startD, startT, endD, endT);
                request.setAttribute("meals", filteredMealToList);
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String actonType = request.getParameter("action");
        switch (actonType) {
            case "edit": {
                request.setCharacterEncoding("UTF-8");
                String id = request.getParameter("id");
                LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                String description = request.getParameter("description");
                int calories = Integer.parseInt(request.getParameter("calories"));
                Meal meal = new Meal(id.isEmpty() ? null : Integer.parseInt(id), dateTime, description, calories);
                String action = (meal.isNew() ? "create meal {}" : "update meal {}");
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