package com.board.qfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//RestController는 문자열만 반환한다..
public class HomeController {
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
}
