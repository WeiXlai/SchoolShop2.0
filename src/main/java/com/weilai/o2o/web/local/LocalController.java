package com.weilai.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author cheng
 *         2018/4/17 13:13
 */
@Controller
@RequestMapping("/local")
public class LocalController {

    /**
     * 绑定账号页路由
     *
     * @return
     */
	@RequestMapping(value = "/accountbind",method = RequestMethod.GET)
    private String accountBind() {
        return "local/accountbind";
    }

    /**
     * 修改密码页路由
     *
     * @return
     */
	@RequestMapping(value ="/changepwd",method = RequestMethod.GET)
    private String changePwd() {
        return "local/changepwd";
    }

    /**
     * 登录页路由
     *
     * @return
     */
    @RequestMapping(value ="/login",method = RequestMethod.GET)
    private String login() {
        return "local/login";
    }
}
