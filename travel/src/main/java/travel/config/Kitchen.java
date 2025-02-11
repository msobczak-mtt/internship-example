package travel.config;

import org.springframework.beans.factory.FactoryBean;

import java.util.List;

public class Kitchen implements FactoryBean<List<String>> {

    @Override
    public List<String> getObject() throws Exception {
        return List.of("pomidorowa", "schabowy", "sernik");
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }
}
