//package TODOListApp.gateway_service.Config;
//
//import TODOListApp.gateway_service.Filter.AuthFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//
//    @Autowired
//    private AuthFilter authFilter;
//
//    @Bean
//    public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
//        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(authFilter);
//        registrationBean.addUrlPatterns("/task-service/*", "/user-service/*");
//        return registrationBean;
//    }
//}
