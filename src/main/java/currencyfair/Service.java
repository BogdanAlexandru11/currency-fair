package currencyfair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {
    Logger logger = LoggerFactory.getLogger(Service.class);
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findAllMessages() {
//        Retrieving all the messages stored in the DB and adding them to a list
        List<Message> messages = new ArrayList<>();
        messageRepository.findAll().forEach(messages::add);
//        Return list of messages
        return messages;
    }

    public HttpStatus saveMessage(Message message) {
//        Check if the Message with the given userId already exist
        Optional<Message> alreadyExists = messageRepository.findById(message.getUserId());
        if (alreadyExists.isEmpty()) {
//            If it doesn't exist, try adding it to our embedded database
            try {
                messageRepository.save(message);
                logger.info("Message saved sucessfully into the database");
                return HttpStatus.CREATED;
            } catch (Exception e) {
                logger.error("Error thrown while saving Message object into the db: " + e);
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
//            If a message using the given userId already exists, return a Conflict response code
            logger.warn("Message was not added as a message with the given userId already exists");
            return HttpStatus.CONFLICT;
        }
    }
}
