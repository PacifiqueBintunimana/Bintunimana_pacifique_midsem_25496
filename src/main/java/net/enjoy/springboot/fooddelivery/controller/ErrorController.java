package net.enjoy.springboot.fooddelivery.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error details
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);

            // Add specific messages based on status code
            switch(statusCode) {
                case 404:
                    model.addAttribute("errorMessage", "Page not found");
                    break;
                case 500:
                    model.addAttribute("errorMessage", "Internal server error");
                    break;
                default:
                    model.addAttribute("errorMessage", "An error occurred");
            }
        }

        return "error"; // This will look for error.html in your templates directory
    }
}
