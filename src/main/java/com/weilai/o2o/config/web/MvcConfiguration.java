package com.weilai.o2o.config.web;

import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.weilai.o2o.interceptor.shop.ShopLoginInterceptor;
import com.weilai.o2o.interceptor.shop.ShopPermissionInterceptor;

/**
 * 开始MVC，自动注入spring容器，WebMvcConfigurerAdapter：配置视图解析器
 * 当一个类实现了这个接口（ApplicationContextAware）之后，这个类
 * 就方便获得ApplicationContext中所有的bean
 * @author ASUS
 *
 */
@Configuration
//相当于<mvc:annotation-driven>，开启spring MVC注解模式
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {
	//spring 容器
	private ApplicationContext applicationContext;
	
	//获取spring容器
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	/**
	 * 静态资源的配置
	 * 静态资源默认servlet配置 加入对静态资源的处理：js,gif,png (2)允许使用"/"做整体映射
	 * 即拦截 /resources/**的请求，将其转移到类路径下的classpaht:/resources/**
	 * <mvc:resources mapping="/resources/**" location="/resources/" />
	 */
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/**");
		//该变图片的获取路径
		registry.addResourceHandler("/upload/**").addResourceLocations("file:///D:/projectdev/image/upload/");
	}
	
	/**
	 * 定义默认的请求处理器
	 * 	
	 *	<mvc:default-servlet-handler />
	 * @param condiConfigurer
	 */
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer condiConfigurer) {
		condiConfigurer.enable();
	}
	
	/**
	 * 定义视图解析器
	 * <bean id="viewResolver"
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/html/"></property>
		<property name="suffix" value=".html"></property>
	   </bean>
	 * @return
	 */
	@Bean(name = "viewResolver")
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		//设置spring容器
		viewResolver.setApplicationContext(this.applicationContext);
		//取消缓存
		viewResolver.setCache(false);
		//设置解析的前缀
		viewResolver.setPrefix("/WEB-INF/html/");
		//设置解析的后缀
		viewResolver.setSuffix(".html");
		return viewResolver;
	}
	
	/**
	 * 定义文件上传解析器
	 * @return
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		//1024*1024*20=20M
		multipartResolver.setMaxUploadSize(20971520);
		multipartResolver.setMaxInMemorySize(20971520);
		return multipartResolver;
		
	}
	
	/**
	 * #Kaptcha 验证码
		kaptcha.boder=no
		kaptcha.textproducer.font.color=red
		kaptcha.image.width=135
		kaptcha.textproducer.char.string=ACDEFHKPRSTRWX345679
		kaptcha.image.height=50
		kaptcha.textproducer.font.size=43
		kaptcha.textproducer.char.length=4
		kaptcha.textproducer.font.names=Arial
		kaptcha.noise.color=black
	 */
	@Value("${kaptcha.boder}")
	private String boder;
	
	@Value("${kaptcha.textproducer.font.color}")
	private String fcolor;
	
	@Value("${kaptcha.image.width}")
	private String width;
	
	@Value("${kaptcha.textproducer.char.string}")
	private String cString;
	
	@Value("${kaptcha.image.height}")
	private String height;
	
	@Value("${kaptcha.textproducer.font.size}")
	private String fsize;
	
	@Value("${kaptcha.textproducer.char.length}")
	private String clength;
	
	@Value("${kaptcha.textproducer.font.names}")
	private String fnames;
	
	@Value("${kaptcha.noise.color}")
	private String ncolor;
	
	/**
	 * 由于web.xml不生效了，需要在这里配置Kaptcha
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() throws ServletException{
		ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/Kaptcha");
		servlet.addInitParameter("kaptcha.boder", boder);//无边框
		servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor);//字体颜色
		servlet.addInitParameter("kaptcha.image.width", width);//图片宽度
		servlet.addInitParameter("kaptcha.textproducer.char.string", cString);//使用哪些字
		servlet.addInitParameter("kaptcha.image.height", height);//图片高度
		servlet.addInitParameter("kaptcha.textproducer.font.size", fsize);//字体大小
		servlet.addInitParameter("kaptcha.textproducer.char.length", clength);//字符个数
		servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);//字体
		servlet.addInitParameter("kaptcha.noise.color", ncolor);//干扰色
		return servlet;
		
	}
	
	/**
	 * 定义拦截器
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		String interceptPath = "/shopadmin/**";
		String interceptPath1 = "/shop/**";
		//注册拦截器
		InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
		//配置拦截的路径
		loginIR.addPathPatterns(interceptPath);
		//注册拦截器
		InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
		//配置拦截的路径
		permissionIR.addPathPatterns(interceptPath);
		//配置拦截的路径
		permissionIR.addPathPatterns(interceptPath1);
		//配置不拦截的路径
		permissionIR.excludePathPatterns("/shopadmin/shoplist");
		permissionIR.excludePathPatterns("/shop/getshopList");
		permissionIR.excludePathPatterns("/shop/getshopinitinfo");
		permissionIR.excludePathPatterns("/shop/registershop");
		permissionIR.excludePathPatterns("/shopadmin/shopoperation");
		permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
		permissionIR.excludePathPatterns("/shop/getshopmanageInfo");
		permissionIR.excludePathPatterns("/shop/getshopbyid");
		permissionIR.excludePathPatterns("/shop/modifyshop");
		permissionIR.excludePathPatterns("/shop/getproductcategorylist");
		permissionIR.excludePathPatterns("/shopadmin/productmanagement");
		permissionIR.excludePathPatterns("/shop/removeproductcategory");
		permissionIR.excludePathPatterns("/shopadmin/addproduct");
		permissionIR.excludePathPatterns("/shopadmin/getproductbyid");
		permissionIR.excludePathPatterns("/shopadmin/getproductlistbyshop");
		permissionIR.excludePathPatterns("/shopadmin/modifyproduct");
		permissionIR.excludePathPatterns("/shopadmin/productcategorymanagement");
		permissionIR.excludePathPatterns("/shopadmin/productoperation");
		
	}

	
	
	
	
	
	
}
