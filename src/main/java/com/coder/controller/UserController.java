package com.coder.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.coder.domain.User;
import com.coder.realm.ShiroDbRealm;
import com.coder.service.UserService;
import com.coder.util.CipherUtil;
import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	
	@Resource
	private UserService userService;
	
	@Autowired  
    private ImageCaptchaService imageCaptchaService; 
	
	@RequestMapping("/hello")
	public ModelAndView getIndex() throws Exception{
		ModelAndView mav = new ModelAndView("index");
		User user = userService.selectUserById(2);
		mav.addObject("user",user);
		return mav;
	}
	
	/** 
     * ��ʼ��½���� 
     * @param request 
     * @return 
     */  
    @RequestMapping("/login")  
    public String tologin(HttpServletRequest request, HttpServletResponse response, Model model){  
        logger.debug("����IP[" + request.getRemoteHost() + "]�ķ���");  
        return "login";  
    }
    
    /** 
     * ��֤�û��������� 
     * @param request 
     * @return 
     */  
    @RequestMapping("/checkLogin")  
    public String login(HttpServletRequest request,String captcha) {  
        String result = "login";
        //��֤��
        System.out.println("captchaing...");
        Boolean isResponseCorrect = imageCaptchaService.validateResponseForID(request.getSession().getId(), captcha);
        if (!isResponseCorrect) {
			return result;
		}
        // ȡ���û���  
        String username = request.getParameter("username");  
        //ȡ�� ���룬����MD5����  
        String password = CipherUtil.generatePassword(request.getParameter("password"));  
        //String password = request.getParameter("password");  
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);  
          
        Subject currentUser = SecurityUtils.getSubject();  
        try {  
            System.out.println("----------------------------");  
            if (!currentUser.isAuthenticated()){//ʹ��shiro����֤  
                token.setRememberMe(true);  
                currentUser.login(token);//��֤��ɫ��Ȩ��  
            }  
            System.out.println("result: " + result);
            result = "login";//��֤ʧ��  
        } catch (Exception e) {  
            logger.error(e.getMessage());  
            result = "login";//��֤ʧ��  
        }  
        return result;  
    }
    
    /** 
     * �˳� 
     * @return 
     */  
    @RequestMapping(value = "/logout")    
    @ResponseBody    
    public String logout() {    
    
        Subject currentUser = SecurityUtils.getSubject();    
        String result = "logout";    
        currentUser.logout();    
        return result;    
    }
}
