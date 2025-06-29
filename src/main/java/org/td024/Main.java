package org.td024;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.td024.config.SpringConfig;
import org.td024.console.AppConsole;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        AppConsole console = applicationContext.getBean(AppConsole.class);
        console.mainMenu();
    }
}
