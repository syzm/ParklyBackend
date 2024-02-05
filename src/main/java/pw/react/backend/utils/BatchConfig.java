package pw.react.backend.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Profile({"batch", "*mysql*"})
public class BatchConfig {

    private final DataSource dataSource;

    public BatchConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}