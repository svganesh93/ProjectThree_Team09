package ser516.project3.client.controller;


import ser516.project3.client.view.GraphView;
import ser516.project3.client.view.PerformanceMetricView;
import ser516.project3.model.GraphModel;
import ser516.project3.model.PerformanceMetricModel;
import ser516.project3.constants.ClientConstants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;

public class PerformanceMetricController implements PerformanceMetricInterface{
  private PerformanceMetricModel performanceMetricModel;
  private PerformanceMetricView performanceMetricView;

  private GraphControllerInterface graphController;

  public PerformanceMetricController(PerformanceMetricModel performanceMetricModel, PerformanceMetricView performanceMetricView) {
    this.performanceMetricModel = performanceMetricModel;
    this.performanceMetricView = performanceMetricView;
    initializeGraph();
    performanceMetricView.initializePerformanceMetricUI(graphController.getGraphView());
    this.performanceMetricView.addEmotionButtonsListener(new EmotionButtonsListener());
    this.performanceMetricView.addDisplayLengthListener(new DisplayLengthKeyListener(), new DisplayLengthDocumentListener());
  }

  private void initializeGraph() {
    GraphModel graphModel = new GraphModel();
    GraphView graphView = new GraphView();
    graphController = new GraphControllerImpl(graphModel, graphView);
    graphController.setNoOfChannels(6);
    graphController.setXLength(performanceMetricModel.getDisplayLength());
    Color channelColors[] = {
        performanceMetricModel.getInterestColor(), performanceMetricModel.getEngagementColor(),
        performanceMetricModel.getStressColor(), performanceMetricModel.getRelaxationColor(),
        performanceMetricModel.getExcitementColor(), performanceMetricModel.getFocusColor()};
    graphController.setChannelColors(channelColors);
    graphController.updateGraphView();
  }

  /**
   * @inheritDoc
   */
  public PerformanceMetricView getPerformanceMetricView() {
    return performanceMetricView;
  }

  /**
   * @inheritDoc
   */
  public GraphControllerInterface getGraphController() {
    return graphController;
  }

  public class EmotionButtonsListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      Color initialBackground;
      Color newBackground;
      switch(e.getActionCommand()) {
        case ClientConstants.INTEREST :
          initialBackground = performanceMetricModel.getInterestColor();
          newBackground = JColorChooser.showDialog(null, "Change " + ClientConstants.INTEREST + " Color",
              initialBackground);
          if (newBackground != null) {
            performanceMetricModel.setInterestColor(newBackground);
          }
          break;
        case ClientConstants.ENGAGEMENT :
          initialBackground = performanceMetricModel.getEngagementColor();
          newBackground = JColorChooser.showDialog(null, "Change " + ClientConstants.ENGAGEMENT + " Color",
              initialBackground);
          if (newBackground != null) {
            performanceMetricModel.setEngagementColor(newBackground);
          }
          break;
        case ClientConstants.STRESS :
          initialBackground = performanceMetricModel.getStressColor();
          newBackground = JColorChooser.showDialog(null, "Change " + ClientConstants.STRESS + " Color",
              initialBackground);
          if (newBackground != null) {
            performanceMetricModel.setStressColor(newBackground);
          }
          break;
        case ClientConstants.RELAXATION :
          initialBackground = performanceMetricModel.getRelaxationColor();
          newBackground = JColorChooser.showDialog(null, "Change " + ClientConstants.RELAXATION + " Color",
              initialBackground);
          if (newBackground != null) {
            performanceMetricModel.setRelaxationColor(newBackground);
          }
          break;
        case ClientConstants.EXCITEMENT :
          initialBackground = performanceMetricModel.getExcitementColor();
          newBackground = JColorChooser.showDialog(null, "Change " + ClientConstants.EXCITEMENT + " Color",
              initialBackground);
          if (newBackground != null) {
            performanceMetricModel.setExcitementColor(newBackground);
          }
          break;
        case ClientConstants.FOCUS :
          initialBackground = performanceMetricModel.getFocusColor();
          newBackground = JColorChooser.showDialog(null, "Change " + ClientConstants.FOCUS + " Color",
              initialBackground);
          if (newBackground != null) {
            performanceMetricModel.setFocusColor(newBackground);
          }
          break;
      }

      Color channelColors[] = {
          performanceMetricModel.getInterestColor(), performanceMetricModel.getEngagementColor(),
          performanceMetricModel.getStressColor(), performanceMetricModel.getRelaxationColor(),
          performanceMetricModel.getExcitementColor(), performanceMetricModel.getFocusColor()};
      graphController.setChannelColors(channelColors);
      graphController.updateGraphView();

      performanceMetricView.updatePerformanceMetricView(performanceMetricModel);
      performanceMetricView.revalidate();
      performanceMetricView.repaint();
    }
  }

  public class DisplayLengthKeyListener extends KeyAdapter{
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        graphController.updateGraphView();
        performanceMetricView.updatePerformanceMetricView(performanceMetricModel);
        performanceMetricView.revalidate();
        performanceMetricView.repaint();
      }
    }
  }

  class DisplayLengthDocumentListener implements DocumentListener {
    @Override
    public void removeUpdate(DocumentEvent e) {
      try {
        if(e.getDocument().getLength() == 0) {
          performanceMetricModel.setDisplayLength(1);
          graphController.setXLength(1);
        } else {
          performanceMetricModel.setDisplayLength(Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())));
          graphController.setXLength(performanceMetricModel.getDisplayLength());
        }
      } catch(BadLocationException ex) {
        System.out.println(ex);
      }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      try {
        if(Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())) == 0) {
          performanceMetricModel.setDisplayLength(1);
          graphController.setXLength(1);
        } else {
          performanceMetricModel.setDisplayLength(Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())));
          graphController.setXLength(performanceMetricModel.getDisplayLength());
        }
      } catch(BadLocationException ex) {
        System.out.println(ex);
      }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
  }
}
