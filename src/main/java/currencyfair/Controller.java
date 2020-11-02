package currencyfair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    private final HashMap<String, String> map = new HashMap<>();

    @Autowired
    private Service messageService;

    @PostMapping("/api")
    public ResponseEntity addMessages(@RequestBody Message message){
        HttpStatus status = messageService.saveMessage(message);
        map.put("message", status.getReasonPhrase());
        return ResponseEntity.status(status).body(map);

    }

    @GetMapping("/messages")
    public ModelAndView getMessages(){
        List<Message> messages = messageService.findAllMessages();
        Map<String, Object> params = new HashMap<>();
        params.put("messages", messages);
        return new ModelAndView("messages", params);
    }
}
