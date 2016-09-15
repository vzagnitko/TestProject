package ua.test.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author vzagnitko
 */
@Controller
public class SwaggerController {

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String redirectToSwagger() {
        return "redirect:/rest/swagger-ui.html";
    }

}
