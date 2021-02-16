package edu.human.com.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {
	@RequestMapping(value="/admin/home.do", method=RequestMethod.GET)
	public String admin() throws Exception {
		return "admin/home";
	}
}
