package openweathermap.test_task.controllersException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 @author Zhurenko Evgeniy
 */


@Controller
public class MyErrorController implements ErrorController {


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "Непредвиденная ситуация";
        Exception ex = (Exception)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if(ex != null)
            message = ex.toString().split(":")[ex.toString().split(":").length-1];

        getGuestUserAdminProp(status, message, model);
        return "error";

    }

    private void getGuestUserAdminProp(Object status, Object message,
                                       Model model){
        Integer statusCode = Integer.valueOf(status.toString());

        if(statusCode == 500) {
            String msg = " INTERNAL_SERVER_ERROR";
            model.addAttribute("message", "Ошибка " + " " + statusCode + " " + msg + " " + message);
        } else if(statusCode == 404){
            String msg = " NOT FOUND";
            model.addAttribute("message", "Ошибка " + " " + statusCode + " " + msg + " " + message);
        } else if(statusCode == 403){
            String msg = " FORBIDDEN";
            model.addAttribute("message", "Ошибка " + " " + statusCode + " " + msg + " " + message);
        } else {
            model.addAttribute("message", "Ошибка " + " " + statusCode + " " + message);
        }
    }

}
