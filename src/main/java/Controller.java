import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;



@RestController
@EnableAutoConfiguration
public class Controller {

    private static SessionIDGenerator sessionIDGenerator;

    @RequestMapping("/")
    String welcome()
    {
        return "Welcome home";
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:Beans.xml");
        sessionIDGenerator = (SessionIDGenerator) context.getBean("SessionIDGenerator");
        SpringApplication.run(Controller.class, args);
    }

    @RequestMapping(value = "/getRange/{clientId}", method = RequestMethod.GET)
    ResponseObject getRange(@PathVariable("clientId") String clientId)
    {
        //sessionIDGenerator=new SessionIDGenerator();
         ResponseObject responseObject = new ResponseObject();
         if(clientId==null)
         {
             clientId=sessionIDGenerator.generateClientId();
         }
         responseObject.setSessionIds(sessionIDGenerator.giveRange(clientId));
         responseObject.setClientId(clientId);
         return responseObject;

    }

    @RequestMapping(value = "/releaseSessions/{clientId}", method = RequestMethod.PUT)
    ResponseObject releaseSessions(@PathVariable("clientId") String clientId)
    {
        ResponseObject responseObject = new ResponseObject();
        sessionIDGenerator.releaseSessions(clientId);
        return responseObject;

    }

}
