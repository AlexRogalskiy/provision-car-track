package com.provision.cartrack.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.provision.cartrack.helpers.SimpleJpaCrudController;

@Controller
@RequestMapping("/api/v1/makes")
public class MakeController extends SimpleJpaCrudController<Make, Long> {

	@Autowired
	public MakeController(MakeRepository repo) {
		super(repo);
	}
}