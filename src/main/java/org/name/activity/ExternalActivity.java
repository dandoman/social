package org.name.activity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/operations")
public class ExternalActivity {
	
	@RequestMapping("/search")
	public ModelAndView searchView() {
		return new ModelAndView("search");
	}
	
	@RequestMapping("/import")
	public ModelAndView importView() {
		return new ModelAndView("import");
	}
}
