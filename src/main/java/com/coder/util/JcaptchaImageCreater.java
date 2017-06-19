package com.coder.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
public class JcaptchaImageCreater {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	
	@RequestMapping("/captcha")
	public void handleRequest(HttpServletRequest request,HttpServletResponse response){
		ByteArrayOutputStream jpegOutPutStream = new ByteArrayOutputStream();
		String captchaId = request.getSession().getId();
		BufferedImage challenge = imageCaptchaService.getImageChallengeForID(captchaId,request.getLocale());
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setContentType("image/jpeg");
        
        try {
			ImageIO.write(challenge, "jpeg", jpegOutPutStream);
			byte[] captchaChallengeAsJpeg = jpegOutPutStream.toByteArray();
			ServletOutputStream respOs = response.getOutputStream();
            respOs.write(captchaChallengeAsJpeg);
            respOs.flush();
            respOs.close();
		} catch (IOException e) {
			logger.error("generate captcha image error");
			e.printStackTrace();
		}
        
	}
}
