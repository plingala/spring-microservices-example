package org.plingala.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NonSecuredController {

	@RequestMapping("/nonsecured")
	public ResponseEntity<String> listAll() {
		return new ResponseEntity<String>("Response from Non-Secured Controller!!!", HttpStatus.OK);
	}
}
