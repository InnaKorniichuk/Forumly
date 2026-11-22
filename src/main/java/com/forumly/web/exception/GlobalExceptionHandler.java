package com.forumly.web.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException ex,
                                             HttpServletResponse response,
                                             Model model) {
        return render(ex.getStatus().value(), ex.getMessage(), response, model);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public String handleAccessDenied(HttpServletResponse response, Model model) {
        return render(403, "You don't have permission to do this", response, model);
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public String handleDataIntegrity(HttpServletResponse response, Model model) {
        return render(409, "This action conflicts with existing data", response, model);
    }

    @ExceptionHandler(Exception.class)
    public String handleOther(Exception ex, HttpServletResponse response, Model model) {
        return render(500, "Something went wrong. Please try again later", response, model);
    }

    private String render(int status, String message, HttpServletResponse response, Model model) {
        response.setStatus(status);
        model.addAttribute("status", status);
        model.addAttribute("message", message);
        return "error/error";
    }
}