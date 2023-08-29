package app.util.feature;

import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.util.feature.NodeServer;
import app.util.feature.ServiceLocator;

@CrossOrigin
@RestController
public class NodeController {
   private static String PROPERTIES_NAME="server.properties";
   private ServiceLocator serviceLocator;

   @Autowired
	private IncomingCallHandler incomingCallHandler;

   @Autowired
	private OutgoingCallHandler outgoingCallHandler;

   @Autowired
	private CallProcessor callProcessor;

   @Autowired
	private NodeServer nodeServer;

   public NodeController(String[] serverUrls) {
      String properties_location = System.getProperty(PROPERTIES_NAME);
		serviceLocator = new ServiceLocator(properties_location);
   }

   @PostConstruct
   public void initialise() {
      outgoingCallHandler.initialise(serviceLocator, nodeServer);
      incomingCallHandler.initialise(serviceLocator, nodeServer, callProcessor, outgoingCallHandler);
   }

   @RequestMapping(value = "/ingest", method = RequestMethod.POST)
   public void doTask(@RequestBody String parcel) {
      TextBlock block = new TextBlock();
      String content="{test}";
      String reply = incomingCallHandler.handleCall(content);
   }

    
}