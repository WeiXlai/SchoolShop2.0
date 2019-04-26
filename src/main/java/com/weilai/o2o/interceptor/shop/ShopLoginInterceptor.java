package com.weilai.o2o.interceptor.shop;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.weilai.o2o.entity.PersonInfo;
/**
 * 店家管理系统登录验证拦截器
 * @author ASUS
 *
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 主要做事前拦截（即在执行controller方法之前执行拦截），
	 * 即用户操作发生前，改写preHandle里的逻辑，进行拦截
	 * 
	 * request  请求参数
	 * response  回写接收到请求的处理后的相应
	 * handler  即需要拦截的controller
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//从session中取出用户信息表
		Object userObj = request.getSession().getAttribute("user");
		if (userObj != null) {
			//若用户信息不为空则将session里的用户信息转换成personInfo实体类对象
			PersonInfo user = (PersonInfo) userObj;
			//做空值判断，确保userId不为空并且该账户的可用状态为1，并且用户类型为店家
			if (user != null && user.getUserId() != null
					&& user.getUserId() > 0 && user.getEnableStatus() == 1
					&& user.getUserId() == 1)
				//若通过验证则返回true，拦截器返回true之后，用户接下来的操作得以正常执行
				return true;
		}
		//若不满足登录验证，则直接跳转到账户登录页面
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open ('" + request.getContextPath()
				+ "/local/login?userType=2','_self')");
		out.println("</script>");
		out.println("</html>");
		return false;
	}
}
