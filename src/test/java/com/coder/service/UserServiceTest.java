package com.coder.service;

import org.apache.log4j.Logger;  
import org.junit.Test;  
import org.springframework.beans.factory.annotation.Autowired;  
  
import com.coder.baseTest.SpringTestCase;  
import com.coder.domain.User;  
  

public class UserServiceTest extends SpringTestCase {  
    @Autowired  
    private UserService userService;  
    Logger logger = Logger.getLogger(UserServiceTest.class);  
      
    @Test  
    public void selectUserByIdTest() throws Exception{  
        User user = userService.selectUserById(1);  
        logger.debug("²éÕÒ½á¹û" + user);  
    }  
      
  
}  
