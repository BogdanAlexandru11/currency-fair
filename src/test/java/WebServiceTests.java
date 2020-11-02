import com.fasterxml.jackson.databind.ObjectMapper;
import currencyfair.Application;
import currencyfair.Message;
import currencyfair.MessageRepository;
import currencyfair.Service;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc


public class WebServiceTests {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Service messageService;

    @Autowired
    private MessageRepository messageRepository;

    @After
    public void clear() {
        messageRepository.deleteAll();
    }

    @Test
    public void givenGetRequestToEndpoint_expect_error() throws Exception {
//        Expect get request to /api endpoint to fail
        mvc.perform(get("/api")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void addNewMessage() throws Exception {
        Message message = new Message("12", "EUR", "GBP", 123, 129, 111, "24-JAN-18 10:27:44", "IE");
        String jsonString = mapper.writeValueAsString(message);

//        Expect get request to /api endpoint to fail
        mvc.perform(post("/api")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<Message> messages = messageService.findAllMessages();
//        Compare the message from the db with the message sent to the db
        Assert.assertEquals(message.getUserId(), messages.get(0).getUserId());
        Assert.assertEquals(message.getCurrencyFrom(), messages.get(0).getCurrencyFrom());
        Assert.assertEquals(message.getCurrencyTo(), messages.get(0).getCurrencyTo());
        Assert.assertEquals(message.getAmountSell(), messages.get(0).getAmountSell(),0);
        Assert.assertEquals(message.getAmountBuy(), messages.get(0).getAmountBuy(),0);
        Assert.assertEquals(message.getRate(), messages.get(0).getRate(),0);
        Assert.assertEquals(message.getTimePlaced(), messages.get(0).getTimePlaced());
        Assert.assertEquals(message.getOriginatingCountry(), messages.get(0).getOriginatingCountry());

    }

    @Test
    public void addingNewMessageShouldFailIfIdAlreadyExists() throws Exception {
        Message message = new Message("123", "EUR", "GBP", 123,129,111, "24-JAN-18 10:27:44", "IE");
        String jsonString = mapper.writeValueAsString(message);

//        Add a new message with the userId 123
        mvc.perform(post("/api")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Message message2 = new Message("123", "EUR", "GBP", 123,129,111, "24-JAN-18 10:27:44", "IE");
        String jsonString2 = mapper.writeValueAsString(message2);

//        Adding another message with the same userId should return a conflict
        mvc.perform(post("/api")
                .content(jsonString2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        Assert.assertEquals(1, messageService.findAllMessages().size());
    }

    @Test
    public void addingMultipleMessages() throws Exception {
        Message message = new Message("123", "EUR", "GBP", 123,129,111, "24-JAN-18 10:27:44", "IE");
        String jsonString = mapper.writeValueAsString(message);

        mvc.perform(post("/api")
                .content(jsonString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Message message2 = new Message("1234", "EUR", "GBP", 123,129,111, "24-JAN-18 10:27:44", "IE");
        String jsonString2 = mapper.writeValueAsString(message2);

        mvc.perform(post("/api")
                .content(jsonString2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Message message3 = new Message("12345", "EUR", "GBP", 123,129,111, "24-JAN-18 10:27:44", "IE");
        String jsonString3 = mapper.writeValueAsString(message3);

        mvc.perform(post("/api")
                .content(jsonString3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Assert.assertEquals(3, messageService.findAllMessages().size());
    }
}