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
import ru.caloriesmanager.service.UserService;
import ru.caloriesmanager.web.user.AbstractUserController;
import ru.caloriesmanager.web.user.AdminRestController;
import ru.caloriesmanager.web.user.ProfileRestController;

import java.io.IOException;


public class UserServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(UserServlet.class);
    private ConfigurableApplicationContext appCtx;
//    private UserService userService;
    private ProfileRestController profileRestController;
    private AdminRestController adminRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        profileRestController = appCtx.getBean(ProfileRestController.class);
        adminRestController = appCtx.getBean(AdminRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("redirect to user");
        response.sendRedirect("users");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("select_user");
        LOG.info("current user {}", userId);
        SecurityUtil.setUserId(Integer.parseInt(userId));
        response.sendRedirect("meals");
    }
}
