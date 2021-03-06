package ser516.project3.client.controller;

import ser516.project3.client.service.ClientConnectionServiceImpl;
import ser516.project3.client.service.ClientConnectionServiceInterface;
import ser516.project3.client.view.ClientView;
import ser516.project3.client.view.ExpressionsView;
import ser516.project3.client.view.HeaderView;
import ser516.project3.client.view.PerformanceMetricView;
import ser516.project3.constants.ClientConstants;
import ser516.project3.model.ExpressionsModel;
import ser516.project3.model.HeaderModel;
import ser516.project3.model.PerformanceMetricModel;
import ser516.project3.server.view.ServerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main Client controller which controls the main client view rendering
 * and acts like a mediator between the view classes, service classes
 * and model classes.
 *
 * @author Varun Srivastava, Adhiraj Tikku, Vishakha Singal
 */

public class ClientControllerImpl implements ClientControllerInterface {

	private static ClientControllerImpl instance;

	private boolean connected = false;
	private ClientConnectionServiceInterface clientConnectionService;
	private ClientView clientView;
	private ServerView serverDialog;
	private HeaderController headerController;
	private ExpressionsController expressionsController;
	private PerformanceMetricController performanceMetricController;

	/**
	 * Getter for performanceMetricController
	 *
	 * @return returns the controller object
	 */
	public PerformanceMetricController getPerformanceMetricController() {
		return performanceMetricController;
	}


	public ClientControllerImpl() {
		HeaderModel headerModel = new HeaderModel();
		HeaderView headerView = new HeaderView(headerModel);
		headerController = new HeaderController(headerModel, headerView);

		ExpressionsModel expressionsModel = new ExpressionsModel();
		ExpressionsView expressionsView = new ExpressionsView(expressionsModel);
		expressionsController = new ExpressionsController(expressionsModel, expressionsView);

		PerformanceMetricModel performanceMetricModel = new PerformanceMetricModel();
		PerformanceMetricView performanceMetricView = new PerformanceMetricView(performanceMetricModel);
		performanceMetricController = new PerformanceMetricController(performanceMetricModel, performanceMetricView);
	}

	
	/**
	 * Creates a singleton instance . If exists, returns it, else creates it.
	 * 
	 * @return instance of the ClientControllerImpl
	 */
	public static ClientControllerImpl getInstance() {
		if (instance == null) {
			instance = new ClientControllerImpl();
		}
		return instance;
	}

	@Override
	public void startClient() {
		// TODO: start data reception form server

		// Registering the observers on client start
		PerformanceMetricGraphObserver performanceMetricObserver = new PerformanceMetricGraphObserver();
		PerformanceMetricDataObservable.getInstance().addObserver(performanceMetricObserver);

		FaceViewObserver faceObserver = new FaceViewObserver();
		ExpressionsDataObservable.getInstance().addObserver(faceObserver);

		ExpressionsGraphObserver expGraphObserver = new ExpressionsGraphObserver();
		ExpressionsDataObservable.getInstance().addObserver(expGraphObserver);

	}

	@Override
	public void toggleConnectionToServer(String ipAddress, int port) {
		if (connected) {
			clientConnectionService.stopClientConnection();
			connected = false;
			// TODO: Find a way to stop the client container
		} else {
			clientConnectionService = new ClientConnectionServiceImpl();
			clientConnectionService.createClientConnection(ipAddress, port, ClientConstants.ENDPOINT);
			connected = true;
		}
	}

	@Override
	public void stopClientConnector() {
		clientConnectionService.stopClientConnection();
		connected = false;
	}

	@Override
	public void initializeClientView() {
		clientView = ClientView.getClientView();
		clientView.initializeClientUI(headerController.getHeaderView(),
				performanceMetricController.getPerformanceMetricView(),
				expressionsController.getExpressionsView());
		clientView.addServerMenuItemListener(new ServerMenuItemListener());
	}

	/**
	 * Updates the connectionStatus for the controller classes and
	 * also, updates the UI
	 *
	 * @param connectionStatus true if connected successfully else not.
	 */
	public void setConnectionStatus(boolean connectionStatus) {
		connected = connectionStatus;
		headerController.setConnectionStatus(connectionStatus);
	}

	class ServerMenuItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(serverDialog == null) {
				serverDialog = new ServerView();
			} else {
				serverDialog.setVisible(true);
			}
		}
	}
}
