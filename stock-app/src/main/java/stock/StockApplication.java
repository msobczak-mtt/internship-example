package stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;

@SpringBootApplication
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StockApplication.class);
        
        // Check if memory DAO is configured
        String daoType = System.getProperty("stock.dao");
        if ("mem".equals(daoType)) {
            app.setAdditionalProfiles("mem");
        }
        
        app.run(args);
    }
}