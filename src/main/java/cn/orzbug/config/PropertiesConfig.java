package cn.orzbug.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: SQJ
 * @data: 2018/7/23 11:13
 * @version:
 */
@Configuration
@PropertySource(value = "classpath:config.properties",encoding = "UTF-8")
@ConfigurationProperties(prefix = "bw")
@Component
@Data
public class PropertiesConfig {

    //阿里云短信
    private String codeTemplateNo;
    private String accessKeyId;
    private String accessKeySecret;

    //默认头像地址
    private String avatar;


    //阿里云oss
    private String ossUrl;
    private String endPoint;
    private String backetName;

    //邮箱
    private String smtpHost;
    private String smtpPort;
    private String mailUser;
    private String mailPassword;
    private String mailUsername;

}
