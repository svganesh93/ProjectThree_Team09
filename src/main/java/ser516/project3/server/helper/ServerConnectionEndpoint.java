package ser516.project3.server.helper;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import ser516.project3.model.Message;
import ser516.project3.model.MessageEncoder;
import ser516.project3.utilities.ServerCommonData;

/**
 * The Web server socket endpoint class for the server application
 * 
 * @author User
 *
 */
@ServerEndpoint(value = "/server", encoders = { MessageEncoder.class })
public class ServerConnectionEndpoint {

	final static Logger logger = Logger.getLogger(ServerConnectionEndpoint.class);

	@OnOpen
	public void onOpen(final Session session) throws IOException {
		try {
			// TODO: Here the logic is to start sending the message json based on the value
			// of auto send flag
			// TODO: If the flag is false, just send the json once, else keep sending based
			// on the interval
			logger.info("New Client connected :::: " + session.getBasicRemote());
			if (ServerCommonData.getInstance().isAutoRepeat()) {
				while(true) {
					session.getBasicRemote().sendObject(new Message());
					Thread.sleep(ServerCommonData.getInstance().getInterval()*1000);
				}
			} else {
				session.getBasicRemote().sendObject(new Message());
			}

		} catch (IOException | EncodeException | InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurred in onOpen method :::: " + e.getStackTrace());
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		// TODO: We have to write logic of what to do when we receive message from the
		// client. Or not!
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info("onClose: " + closeReason);
		try {
			session.getBasicRemote().sendText("Connection closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurred while sending text::::" + e.getStackTrace());
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		logger.error("Error occurred in Server Endpoint");
	}
}