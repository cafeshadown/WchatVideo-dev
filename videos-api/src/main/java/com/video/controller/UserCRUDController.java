package com.video.controller;

import com.video.pojo.Users;
import com.video.crud.UserCRUDService;
import com.video.utils.KJSONResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/crud")
public class UserCRUDController {
	
	@Resource
	private UserCRUDService userCRUDService;
	
	@RequestMapping("/save")
	public KJSONResult save() {
		
		Users user = new Users("1001", "test-saveuser-1001",
				"123456", "/path", "慕课网", null, null, null);
		userCRUDService.saveUser(user);
		
		return KJSONResult.ok();
	}
	
	@RequestMapping("/update")
	public KJSONResult update() {
		
		Users user = new Users("1001", "test-saveuser-1111",
				"77777", "/path000", "慕课网好牛~", null, null, null);
		userCRUDService.updateUser(user);
		
		return KJSONResult.ok();
	}
	
	@RequestMapping("/update2")
	public KJSONResult update2() {
		
		Users user = new Users("1001", null,
				"9999", "/path000999", "慕课网好牛十分牛~", null, null, null);
		userCRUDService.updateUser(user);
		
		return KJSONResult.ok();
	}
	
	@RequestMapping("/delUser")
	public KJSONResult delUser() {
		
		userCRUDService.delete();
		
		return KJSONResult.ok();
	}
}
