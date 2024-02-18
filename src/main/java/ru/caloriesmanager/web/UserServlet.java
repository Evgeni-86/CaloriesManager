package ru.caloriesmanager.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.caloriesmanager.model.User;
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.transferObject.UserTO;
import ru.caloriesmanager.web.meal.MealRestController;
import ru.caloriesmanager.web.user.AbstractUserController;
import ru.caloriesmanager.web.user.AdminRestController;
import ru.caloriesmanager.web.user.ProfileRestController;

import java.io.IOException;


public class UserServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(UserServlet.class);
    private WebApplicationContext appCtx;
    private ProfileRestController profileRestController;
    private AdminRestController adminRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        profileRestController = appCtx.getBean(ProfileRestController.class);
        adminRestController = appCtx.getBean(AdminRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userId = request.getParameter("select_user");
        User user = adminRestController.get(Integer.parseInt(userId));
        LOG.info("current user {}", userId);
        SecurityUtil.setUserId(Integer.parseInt(userId));
        request.setAttribute("user", UserTO.getTransferObject(user));
        request.getRequestDispatcher("users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String userId = request.getParameter("select_user");
//        User user = adminRestController.get(Integer.parseInt(userId));
//        LOG.info("current user {}", userId);
//        SecurityUtil.setUserId(Integer.parseInt(userId));
//        response.sendRedirect("meals");
    }
}
