package org.example.luxuryhotel.framework;

import org.example.luxuryhotel.framework.web.HandlerMapping;

public class FrameworkApplication {
    public  static void run(Class<?> appClass){
        AppContext.appClass = appClass;
        System.out.println("Старт фреймворк апп");
    }
}
