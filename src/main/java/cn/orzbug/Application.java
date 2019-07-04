package cn.orzbug;


import cn.orzbug.base.config.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: SQJ
 * @data: 2018/6/29 09:34
 * @version:
 */
@EnableTransactionManagement
@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class,scanBasePackages = {"cn.orzbug"})
@EnableJpaRepositories(basePackages = {"cn.orzbug"},repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableAsync
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    public Application() {
        super();
        setRegisterErrorPageFilter(false);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Application.class,args);
    }
}
