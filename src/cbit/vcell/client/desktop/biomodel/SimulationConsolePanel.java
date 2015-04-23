package cbit.vcell.client.desktop.biomodel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.mapping.TaskCallbackMessage;
import cbit.vcell.mapping.TaskCallbackMessage.TaskCallbackStatus;
import cbit.vcell.model.Model;
import cbit.vcell.model.Model.RbmModelContainer;

public class SimulationConsolePanel extends JPanel {

	private EventHandler eventHandler = new EventHandler();
	private IssueManager fieldIssueManager;
	private SelectionManager fieldSelectionManager;

	private SimulationContext fieldSimulationContext;

	
	private JTextPane netGenConsoleText;
	
	private int currentIterationSpecies = 0;
	private int previousIterationSpecies = 0;

	
	private class EventHandler implements FocusListener, ActionListener, PropertyChangeListener {

		public void actionPerformed(ActionEvent e) {

		}

		public void focusGained(FocusEvent e) {
		}
		public void focusLost(FocusEvent e) {

		}
		
		public void propertyChange(java.beans.PropertyChangeEvent event) {
			if(event.getSource() instanceof Model && event.getPropertyName().equals(RbmModelContainer.PROPERTY_NAME_MOLECULAR_TYPE_LIST)) {
				System.out.println("received");
				refreshInterface();
			} else if(event.getSource() instanceof SimulationContext && event.getPropertyName().equals("appendToConsole")) {
				if(event.getNewValue() instanceof TaskCallbackMessage) {
					TaskCallbackMessage message = (TaskCallbackMessage)(event.getNewValue());
					appendToConsole(message);
				}
			}
		}
	}

	
	public SimulationConsolePanel() {
		super();
		initialize();
	}

	private void initialize() {
		
		netGenConsoleText = new JTextPane();
		netGenConsoleText.addFocusListener(eventHandler);

		Border loweredEtchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();
		setBorder(loweredEtchedBorder);

		JScrollPane netGenConsoleScrollPane = new JScrollPane(netGenConsoleText);
		netGenConsoleScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		TitledBorder titleConsole = BorderFactory.createTitledBorder(loweredBevelBorder, " BioNetGen Console ");
		titleConsole.setTitleJustification(TitledBorder.LEFT);
		titleConsole.setTitlePosition(TitledBorder.ABOVE_TOP);
		netGenConsoleScrollPane.setBorder(titleConsole);

		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(new GridBagLayout());		// --- bottom
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1.0;			// get all the available space
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(20, 4, 4, 10);
		add(netGenConsoleScrollPane, gbc);
		
		netGenConsoleText.setFont(new Font("monospaced", Font.PLAIN, 11));
		netGenConsoleText.setEditable(false);
	}
	
	public SimulationContext getSimulationContext() {
		return fieldSimulationContext;
	}
	public void setSimulationContext(SimulationContext sc) {
		if(this.fieldSimulationContext == sc) {
			return;
		}
		if(this.fieldSimulationContext != null) {
			this.fieldSimulationContext.removePropertyChangeListener(eventHandler);
		}
		this.fieldSimulationContext = sc;
		if(this.fieldSimulationContext != null) {
			this.fieldSimulationContext.addPropertyChangeListener(eventHandler);
		}
		refreshInterface();
	}

	private void appendToConsole(TaskCallbackMessage newCallbackMessage) {
		TaskCallbackStatus status = newCallbackMessage.getStatus();
		String string = newCallbackMessage.getText();
		StyledDocument doc = netGenConsoleText.getStyledDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		try {
		switch(status) {
		case TaskStart:			// clean console, display initialization message
			previousIterationSpecies = 0;
			currentIterationSpecies = 0;
			netGenConsoleText.setText("");
			doc.insertString(doc.getLength(), string + "\n", null);
			break;
		case TaskEnd:			// normal end

			break;
		case TaskStopped:		// stopped by user

			break;
		case Notification:		// normal notification, just display the string
			doc.insertString(doc.getLength(), string + "\n", null);
			break;
		case Detail:			// specific details, string will be processed, details extracted, formatted, etc
			String split[];
			split = string.split("\\n");
			for(String s : split) {
				if(s.startsWith("CPU TIME: total"))  {
					doc.insertString(doc.getLength(), "  " + s + "\n", null);
				} else if (s.startsWith("Iteration")) {
					String species = "species";
					s = "    " + s.substring(0, s.indexOf("species") + species.length());
					doc.insertString(doc.getLength(), s + "\n", null);
					checkMaxIterationConsistency(s);
				}
			}
			if(previousIterationSpecies != currentIterationSpecies) {
				StyleConstants.setForeground(keyWord, Color.RED);
				String s = "Warning: Max Iterations number may be insufficient.";
				doc.insertString(doc.getLength(), s + "\n", keyWord);
			}
			break;
		case Error:			// display this in red
			StyleConstants.setForeground(keyWord, Color.RED);
			doc.insertString(doc.getLength(), string + "\n", keyWord);
			break;
		default:
			break;
		}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	private void checkMaxIterationConsistency(String s) {
		Pattern pattern = Pattern.compile("\\w+");
		Matcher matcher = pattern.matcher(s);
		try {
		for(int i=0; matcher.find(); i++) {
			if(i==2) {
				previousIterationSpecies = currentIterationSpecies;
				currentIterationSpecies = Integer.parseInt(matcher.group());
			}
		}
		} catch(NumberFormatException nfe) {
			
		}
	}
	
	private void refreshInterface() {
		
		String text1 = "Simulation Console for: ";
		if(fieldSimulationContext == null) {
			netGenConsoleText.setText(text1 + "no simulation");
		} else {
			netGenConsoleText.setText(text1 + fieldSimulationContext.getName());
		}
		if(fieldSimulationContext != null) {
			fieldSimulationContext.playConsoleNotificationList();
		}
	}

}
