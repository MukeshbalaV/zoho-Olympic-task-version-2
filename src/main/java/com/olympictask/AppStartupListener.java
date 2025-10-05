package com.olympictask;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            OlympicsDao dao = new OlympicsDao();
            dao.resetOlympicPlayersTable();  // runs only once at startup
            System.out.println("Olympic table reset at server startup.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // optional cleanup code
    }
}
