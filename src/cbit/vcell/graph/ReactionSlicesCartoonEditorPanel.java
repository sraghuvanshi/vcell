package cbit.vcell.graph;
/*
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.vcell.util.gui.ButtonGroupCivilized;
import org.vcell.util.gui.JToolBarToggleButton;

import cbit.gui.graph.CartoonTool.Mode;
import cbit.gui.graph.GraphEmbeddingManager;
import cbit.gui.graph.GraphPane;
import cbit.vcell.clientdb.DocumentManager;
import cbit.vcell.model.Membrane;
import cbit.vcell.model.Model;
import cbit.vcell.model.Structure;

@SuppressWarnings("serial")
public class ReactionSlicesCartoonEditorPanel extends JPanel 
implements ActionListener, PropertyChangeListener {
	private static final Dimension TOOL_BAR_SEPARATOR_SIZE = new Dimension(15,0);
	public static final String PROPERTY_NAME_FLOATING_REQUESTED = "floatingRequested";
	private static final Dimension TOOLBAR_BUTTON_SIZE = new Dimension(28, 28);
	private JPanel featureSizePanel = null;
	private GraphPane graphPane = null;
	private boolean connPtoP1Aligning = false;
	private JPanel panel1 = null;
	private JToolBar toolBar1 = null;
	private ButtonModel selection = null;
	private JToolBarToggleButton fluxButton = null;
	private JToolBarToggleButton lineDirectedButton = null;
	private JToolBarToggleButton lineCatalystButton = null;
	private JToolBarToggleButton selectButton = null;
	private JToolBarToggleButton stepButton = null;
	private ButtonGroupCivilized buttonGroupCivilized = null;
	private JScrollPane scrollPane1 = null;
	private JButton annealLayoutButton = null;
	private JButton circleLayoutButton = null;
	private JButton levellerLayoutButton = null;
	private JButton randomLayoutButton = null;
	private JButton relaxerLayoutButton = null;
	private JButton zoomInButton = null;
	private JButton zoomOutButton = null;
	private JButton glgLayoutJButton = null;
	private Structure fieldStructure = null;
	private JToolBarToggleButton speciesButton = null;
	private DocumentManager fieldDocumentManager = null;
	private ReactionSlicesCartoon reactionCartoon = null;
	private ReactionSlicesCartoonTool reactionCartoonTool = null;
	private Model fieldModel = null;

	private boolean bFloatingRequested = false;
	private JButton floatButton = null;

	public ReactionSlicesCartoonEditorPanel() {
		super();
		initialize();
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == getRandomLayoutButton())
				getReactionCartoonTool1().layout(GraphEmbeddingManager.RANDOMIZER);
			else if (e.getSource() == getAnnealLayoutButton())
				getReactionCartoonTool1().layout(GraphEmbeddingManager.ANNEALER);
			else if (e.getSource() == getCircleLayoutButton())
				getReactionCartoonTool1().layout(GraphEmbeddingManager.CIRCULARIZER);
			else if (e.getSource() == getRelaxerLayoutButton())
				getReactionCartoonTool1().layout(GraphEmbeddingManager.RELAXER);
			else if (e.getSource() == getLevellerLayoutButton())
				getReactionCartoonTool1().layout(GraphEmbeddingManager.LEVELLER);
			else if (e.getSource() == getZoomInButton())
				this.zoomInButton_ActionPerformed();
			else if (e.getSource() == getZoomOutButton())
				this.zoomOutButton_ActionPerformed();
			else if (e.getSource() == getGlgLayoutJButton())
				getReactionCartoonTool1().layoutGlg();
			else if (e.getSource() == getFloatButton()) {
				setFloatingRequested(!bFloatingRequested);
			}
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public void cleanupOnClose() {
		getReactionCartoon().cleanupAll();
	}

	private JButton getAnnealLayoutButton() {
		if (annealLayoutButton == null) {
			try {
				annealLayoutButton = new JButton();
				annealLayoutButton.setName("AnnealLayoutButton");
				annealLayoutButton.setToolTipText("Layout Annealing");
				annealLayoutButton.setText("anl");
				annealLayoutButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				annealLayoutButton.setActionCommand("AnnealLayout");
				annealLayoutButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				annealLayoutButton.setFont(new Font("Arial", 1, 10));
				annealLayoutButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				annealLayoutButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return annealLayoutButton;
	}

	private ButtonGroupCivilized getButtonGroupCivilized() {
		if (buttonGroupCivilized == null) {
			try {
				buttonGroupCivilized = new ButtonGroupCivilized();
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return buttonGroupCivilized;
	}

	private JButton getCircleLayoutButton() {
		if (circleLayoutButton == null) {
			try {
				circleLayoutButton = new JButton();
				circleLayoutButton.setName("CircleLayoutButton");
				circleLayoutButton.setToolTipText("Layout Circular");
				circleLayoutButton.setText("crc");
				circleLayoutButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				circleLayoutButton.setActionCommand("CircleLayout");
				circleLayoutButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				circleLayoutButton.setFont(new Font("Arial", 1, 10));
				circleLayoutButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				circleLayoutButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return circleLayoutButton;
	}

	public DocumentManager getDocumentManager() {
		return fieldDocumentManager;
	}

	private javax.swing.JPanel getFeatureSizePanel() {
		if (featureSizePanel == null) {
			try {
				featureSizePanel = new javax.swing.JPanel();
				featureSizePanel.setName("FeatureSizePanel");
				featureSizePanel.setPreferredSize(new Dimension(22, 396));
				featureSizePanel.setLayout(new java.awt.BorderLayout());
//				featureSizePanel.setMinimumSize(new Dimension(22, 396));
				getFeatureSizePanel().add(getJPanel1(), "South");
				getFeatureSizePanel().add(getJScrollPane1(), "Center");
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return featureSizePanel;
	}

	private JToolBarToggleButton getFluxButton() {
		if (fluxButton == null) {
			try {
				fluxButton = new JToolBarToggleButton();
				fluxButton.setName("FluxButton");
				fluxButton.setToolTipText("Flux Tool");
				fluxButton.setText("");
				fluxButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				fluxButton.setActionCommand(Mode.FLUX.getActionCommand());
				fluxButton.setIcon(new ImageIcon(getClass().getResource("/images/flux.gif")));
				fluxButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				fluxButton.setEnabled(true);
				fluxButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return fluxButton;
	}

	private JButton getGlgLayoutJButton() {
		if (glgLayoutJButton == null) {
			try {
				glgLayoutJButton = new JButton();
				glgLayoutJButton.setName("GlgLayoutJButton");
				glgLayoutJButton.setToolTipText("Layout GLG");
				glgLayoutJButton.setText("glg");
				glgLayoutJButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				glgLayoutJButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				glgLayoutJButton.setFont(new Font("Arial", 1, 10));
				glgLayoutJButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				glgLayoutJButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return glgLayoutJButton;
	}

	private GraphPane getGraphPane() {
		if (graphPane == null) {
			try {
				graphPane = new GraphPane();
				graphPane.setName("GraphPane");
				graphPane.setBounds(0, 0, 372, 364);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return graphPane;
	}

	private JPanel getJPanel1() {
		if (panel1 == null) {
			try {
				panel1 = new javax.swing.JPanel();
				panel1.setName("JPanel1");
				panel1.setLayout(new java.awt.GridBagLayout());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return panel1;
	}

	private JScrollPane getJScrollPane1() {
		if (scrollPane1 == null) {
			try {
				scrollPane1 = new javax.swing.JScrollPane();
				scrollPane1.setName("JScrollPane1");
				scrollPane1.setPreferredSize(new Dimension(22, 396));
				scrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//				scrollPane1.setMinimumSize(new Dimension(22, 396));
				getJScrollPane1().setViewportView(getGraphPane());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return scrollPane1;
	}

	private JToolBar getJToolBar1() {
		if (toolBar1 == null) {
			try {
				toolBar1 = new javax.swing.JToolBar();
				toolBar1.setName("JToolBar1");
				toolBar1.setFloatable(false);
				toolBar1.setBorder(new javax.swing.border.EtchedBorder());
				toolBar1.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
				getJToolBar1().addSeparator(TOOL_BAR_SEPARATOR_SIZE);
				getJToolBar1().add(getSelectButton(), getSelectButton().getName());
				getJToolBar1().add(getSpeciesButton(), getSpeciesButton().getName());
				getJToolBar1().add(getStepButton(), getStepButton().getName());
				getJToolBar1().add(getFluxButton(), getFluxButton().getName());
				getJToolBar1().add(getLineDirectedButton(), getLineDirectedButton().getName());
				getJToolBar1().add(getLineCatalystButton(), getLineCatalystButton().getName());
				getJToolBar1().addSeparator(TOOL_BAR_SEPARATOR_SIZE);
				getJToolBar1().add(getZoomInButton(), getZoomInButton().getName());
				getJToolBar1().add(getZoomOutButton(), getZoomOutButton().getName());
				getJToolBar1().addSeparator(TOOL_BAR_SEPARATOR_SIZE);
				getJToolBar1().add(getRandomLayoutButton(), getRandomLayoutButton().getName());
				getJToolBar1().add(getCircleLayoutButton(), getCircleLayoutButton().getName());
				getJToolBar1().add(getAnnealLayoutButton(), getAnnealLayoutButton().getName());
				getJToolBar1().add(getLevellerLayoutButton(), getLevellerLayoutButton().getName());
				getJToolBar1().add(getRelaxerLayoutButton(), getRelaxerLayoutButton().getName());
				getJToolBar1().add(getGlgLayoutJButton(), getGlgLayoutJButton().getName());
				getJToolBar1().addSeparator(TOOL_BAR_SEPARATOR_SIZE);
				getJToolBar1().add(getFloatButton(), getFloatButton().getName());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return toolBar1;
	}

	private JButton getLevellerLayoutButton() {
		if (levellerLayoutButton == null) {
			try {
				levellerLayoutButton = new JButton();
				levellerLayoutButton.setName("LevellerLayoutButton");
				levellerLayoutButton.setToolTipText("Layout Leveler");
				levellerLayoutButton.setText("lev");
				levellerLayoutButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				levellerLayoutButton.setActionCommand("LevellerLayout");
				levellerLayoutButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				levellerLayoutButton.setFont(new Font("Arial", 1, 10));
				levellerLayoutButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				levellerLayoutButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return levellerLayoutButton;
	}

	private JToolBarToggleButton getLineDirectedButton() {
		if (lineDirectedButton == null) {
			try {
				lineDirectedButton = new JToolBarToggleButton();
				lineDirectedButton.setName("LineButton");
				lineDirectedButton.setToolTipText("RX Connection Tool");
				lineDirectedButton.setText("");
				lineDirectedButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				lineDirectedButton.setActionCommand(Mode.LINEDIRECTED.getActionCommand());
				lineDirectedButton.setIcon(
						new ImageIcon(getClass().getResource("/images/lineDirected.gif")));
				lineDirectedButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				lineDirectedButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return lineDirectedButton;
	}

	private JToolBarToggleButton getLineCatalystButton() {
		if (lineCatalystButton == null) {
			try {
				lineCatalystButton = new JToolBarToggleButton();
				lineCatalystButton.setName("LineCatalystButton");
				lineCatalystButton.setToolTipText("Set a catalyst");
				lineCatalystButton.setText("");
				lineCatalystButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				lineCatalystButton.setActionCommand(Mode.LINECATALYST.getActionCommand());
				lineCatalystButton.setIcon(new ImageIcon(
						getClass().getResource("/images/lineCatalyst.gif")));
				lineCatalystButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				lineCatalystButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return lineCatalystButton;
	}

	public Model getModel() {
		return fieldModel;
	}

	private JButton getRandomLayoutButton() {
		if (randomLayoutButton == null) {
			try {
				randomLayoutButton = new JButton();
				randomLayoutButton.setName("RandomLayoutButton");
				randomLayoutButton.setToolTipText("Layout Random");
				randomLayoutButton.setText("rnd");
				randomLayoutButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				randomLayoutButton.setActionCommand("RandomLayout");
				randomLayoutButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				randomLayoutButton.setFont(new Font("Arial", 1, 10));
				randomLayoutButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				randomLayoutButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return randomLayoutButton;
	}

	private ReactionSlicesCartoon getReactionCartoon() {
		if (reactionCartoon == null) {
			try {
				reactionCartoon = new ReactionSlicesCartoon();
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return reactionCartoon;
	}

	private ReactionSlicesCartoonTool getReactionCartoonTool1() {
		if (reactionCartoonTool == null) {
			try {
				reactionCartoonTool = new ReactionSlicesCartoonTool();
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return reactionCartoonTool;
	}

	private JButton getRelaxerLayoutButton() {
		if (relaxerLayoutButton == null) {
			try {
				relaxerLayoutButton = new JButton();
				relaxerLayoutButton.setName("RelaxerLayoutButton");
				relaxerLayoutButton.setToolTipText("Layout Relaxer");
				relaxerLayoutButton.setText("rlx");
				relaxerLayoutButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				relaxerLayoutButton.setActionCommand("RelaxerLayout");
				relaxerLayoutButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				relaxerLayoutButton.setFont(new Font("Arial", 1, 10));
				relaxerLayoutButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				relaxerLayoutButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return relaxerLayoutButton;
	}

	private JToolBarToggleButton getSelectButton() {
		if (selectButton == null) {
			try {
				selectButton = new JToolBarToggleButton();
				selectButton.setName("SelectButton");
				selectButton.setToolTipText("Select Tool");
				selectButton.setText("");
				selectButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				selectButton.setActionCommand(Mode.SELECT.getActionCommand());
				selectButton.setSelected(true);
				selectButton.setIcon(new ImageIcon(getClass().getResource("/images/select.gif")));
				selectButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				selectButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return selectButton;
	}

	private ButtonModel getSelection() {
		return selection;
	}

	private JToolBarToggleButton getSpeciesButton() {
		if (speciesButton == null) {
			try {
				speciesButton = new JToolBarToggleButton();
				speciesButton.setName("SpeciesButton");
				speciesButton.setToolTipText("Species Tool");
				speciesButton.setText("");
				speciesButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				speciesButton.setActionCommand(Mode.SELECT.getActionCommand());
				speciesButton.setIcon(new ImageIcon(getClass().getResource("/images/species.gif")));
				speciesButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				speciesButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return speciesButton;
	}

	private JToolBarToggleButton getStepButton() {
		if (stepButton == null) {
			try {
				stepButton = new JToolBarToggleButton();
				stepButton.setName("StepButton");
				stepButton.setToolTipText("Reaction Tool");
				stepButton.setText("");
				stepButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				stepButton.setActionCommand(Mode.STEP.getActionCommand());
				stepButton.setIcon(new ImageIcon(getClass().getResource("/images/step.gif")));
				stepButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				stepButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return stepButton;
	}

	public Structure getStructure() {
		return fieldStructure;
	}

	private JButton getZoomInButton() {
		if (zoomInButton == null) {
			try {
				zoomInButton = new JButton();
				zoomInButton.setName("ZoomInButton");
				zoomInButton.setToolTipText("Zoom In");
				zoomInButton.setText("");
				zoomInButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				zoomInButton.setActionCommand("ZoomIn");
				zoomInButton.setIcon(new ImageIcon(getClass().getResource("/images/zoomin.gif")));
				zoomInButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				zoomInButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				zoomInButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return zoomInButton;
	}
	
	private JButton getFloatButton() {
		if (floatButton == null) {
			try {
				floatButton = new JButton("\u21b1");
				floatButton.setName("FloatingButton");
				floatButton.setToolTipText("\u21b1 Float");
				floatButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				floatButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				floatButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
				floatButton.setMargin(new Insets(2, 2, 2, 2));
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return floatButton;
	}

	private JButton getZoomOutButton() {
		if (zoomOutButton == null) {
			try {
				zoomOutButton = new JButton();
				zoomOutButton.setName("ZoomOutButton");
				zoomOutButton.setToolTipText("Zoom Out");
				zoomOutButton.setText("");
				zoomOutButton.setMaximumSize(TOOLBAR_BUTTON_SIZE);
				zoomOutButton.setActionCommand("ZoomOut");
				zoomOutButton.setIcon(new ImageIcon(getClass().getResource("/images/zoomout.gif")));
				zoomOutButton.setPreferredSize(TOOLBAR_BUTTON_SIZE);
				zoomOutButton.setMinimumSize(TOOLBAR_BUTTON_SIZE);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return zoomOutButton;
	}

	// TODO centralize exception handling
	private void handleException(Throwable exception) {
		System.out.println("--------- UNCAUGHT EXCEPTION --------- in CartoonPanel");
		exception.printStackTrace(System.out);
	}

	private void initConnections() throws java.lang.Exception {
		getButtonGroupCivilized().addPropertyChangeListener(this);
		getRandomLayoutButton().addActionListener(this);
		getAnnealLayoutButton().addActionListener(this);
		getCircleLayoutButton().addActionListener(this);
		getRelaxerLayoutButton().addActionListener(this);
		getLevellerLayoutButton().addActionListener(this);
		getZoomInButton().addActionListener(this);
		getZoomOutButton().addActionListener(this);
		getGlgLayoutJButton().addActionListener(this);
		getFloatButton().addActionListener(this);
		this.addPropertyChangeListener(this);
		try {
			if (connPtoP1Aligning == false) {
				connPtoP1Aligning = true;
				setSelection(getButtonGroupCivilized().getSelection());
				connPtoP1Aligning = false;
			}
		} catch (Throwable ivjExc) {
			connPtoP1Aligning = false;
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
		/* Set the target from the source */
		try {
			if ((getSelection() != null)) {
				getReactionCartoonTool1().setModeString(getSelection().getActionCommand());
			}
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private void initialize() {
		try {
			setName("ReactionSlicesCartoonPanel");
//			setPreferredSize(new Dimension(54, 425));
			setLayout(new java.awt.BorderLayout());
			setSize(472, 422);
//			setMinimumSize(new Dimension(54, 425));
			add(getFeatureSizePanel(), BorderLayout.CENTER);
			add(getJToolBar1(), BorderLayout.NORTH);
			initConnections();
			getButtonGroupCivilized().add(getStepButton());
			getButtonGroupCivilized().add(getFluxButton());
			getButtonGroupCivilized().add(getLineDirectedButton());
			getButtonGroupCivilized().add(getLineCatalystButton());
			getButtonGroupCivilized().add(getSelectButton());
			getButtonGroupCivilized().add(getSpeciesButton());
			getReactionCartoonTool1().setReactionCartoon(getReactionCartoon());
			getReactionCartoonTool1().setGraphPane(getGraphPane());
			getReactionCartoonTool1().setButtonGroup(getButtonGroupCivilized());
			getGraphPane().setGraphModel(getReactionCartoon());
		} catch (Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	private boolean isMembrane(Structure structure) {
		return (structure instanceof Membrane);
	}

	public static void main(java.lang.String[] args) {
		try {
			java.awt.Frame frame = new java.awt.Frame();
			ReactionSlicesCartoonEditorPanel aReactionCartoonEditorPanel;
			aReactionCartoonEditorPanel = new ReactionSlicesCartoonEditorPanel();
			frame.add("Center", aReactionCartoonEditorPanel);
			frame.setSize(aReactionCartoonEditorPanel.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of java.awt.Panel");
			exception.printStackTrace(System.out);
		}
	}

	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		if (evt.getSource() == getButtonGroupCivilized() && (evt.getPropertyName().equals("selection"))) {
			try {
				if (connPtoP1Aligning == false) {
					connPtoP1Aligning = true;
					setSelection(getButtonGroupCivilized().getSelection());
					connPtoP1Aligning = false;
				}
			} catch (Throwable ivjExc1) {
				connPtoP1Aligning = false;
				handleException(ivjExc1);
			}
		} else if (evt.getSource() == this && (evt.getPropertyName().equals("structure"))) {
			try {
				getReactionCartoon().setStructure(this.getStructure());
				try {
					getFluxButton().setEnabled(this.isMembrane(getReactionCartoon().getStructure()));
				} catch (Throwable ivjExc) {
					handleException(ivjExc);
				}
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		} else if (evt.getSource() == this && (evt.getPropertyName().equals("documentManager"))) {
			try {
				getReactionCartoonTool1().setDocumentManager(this.getDocumentManager());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		} else if (evt.getSource() == this && (evt.getPropertyName().equals("model"))) {
			try {
				getReactionCartoon().setModel(this.getModel());
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		} else if (evt.getSource() == this && (evt.getPropertyName().equals(PROPERTY_NAME_FLOATING_REQUESTED))) {
			floatButton.setText((Boolean)evt.getNewValue() ? "\u21b5" : "\u21b1");
			floatButton.setToolTipText((Boolean)evt.getNewValue() ? "\u21b5 Dock" : "\u21b1 Float");
		}
	}

	public void setDocumentManager(DocumentManager documentManager) {
		DocumentManager oldValue = fieldDocumentManager;
		fieldDocumentManager = documentManager;
		firePropertyChange("documentManager", oldValue, documentManager);
	}

	public void setModel(Model model) {
		Model oldValue = fieldModel;
		fieldModel = model;
		firePropertyChange("model", oldValue, model);
	}

	private void setSelection(javax.swing.ButtonModel newValue) {
		if (selection != newValue) {
			try {
				selection = newValue;
				/* Set the source from the target */
				try {
					if (connPtoP1Aligning == false) {
						connPtoP1Aligning = true;
						if ((getSelection() != null)) {
							getButtonGroupCivilized().setSelection(getSelection());
						}
						connPtoP1Aligning = false;
					}
				} catch (Throwable ivjExc) {
					connPtoP1Aligning = false;
					handleException(ivjExc);
				}
				/* Set the target from the source */
				try {
					if ((getSelection() != null)) {
						getReactionCartoonTool1().setModeString(getSelection().getActionCommand());
					}
				} catch (Throwable ivjExc) {
					handleException(ivjExc);
				}
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		};
	}

	public void setStructure(Structure structure) {
		Structure oldValue = fieldStructure;
		fieldStructure = structure;
		firePropertyChange("structure", oldValue, structure);
	}

	private void zoomInButton_ActionPerformed() {
		if (getReactionCartoon()!=null){
			switch (getReactionCartoon().getZoomPercent()){
			case 195: {
				// already at top, do nothing
				break;
			}
			case 156: {
				getReactionCartoon().setZoomPercent(195);
				break;
			}
			case 125: {
				getReactionCartoon().setZoomPercent(156);
				break;
			}
			case 100: {
				getReactionCartoon().setZoomPercent(125);
				break;
			}
			case 80: {
				getReactionCartoon().setZoomPercent(100);
				break;
			}
			case 64: {
				getReactionCartoon().setZoomPercent(80);
				break;
			}
			case 50: {
				getReactionCartoon().setZoomPercent(64);
				break;
			}
			case 40: {
				getReactionCartoon().setZoomPercent(50);
				break;
			}
			case 30: {
				getReactionCartoon().setZoomPercent(40);
				break;
			}
			case 20: {
				getReactionCartoon().setZoomPercent(30);
				break;
			}
			case 10: {
				getReactionCartoon().setZoomPercent(20);
				break;
			}
			default: {
				getReactionCartoon().setZoomPercent(100);
				break;
			}
			}
		}
	}

	private void zoomOutButton_ActionPerformed() {
		if (getReactionCartoon()!=null){
			switch (getReactionCartoon().getZoomPercent()){
			case 195: {
				getReactionCartoon().setZoomPercent(156);
				break;
			}
			case 156: {
				getReactionCartoon().setZoomPercent(125);
				break;
			}
			case 125: {
				getReactionCartoon().setZoomPercent(100);
				break;
			}
			case 100: {
				getReactionCartoon().setZoomPercent(80);
				break;
			}
			case 80: {
				getReactionCartoon().setZoomPercent(64);
				break;
			}
			case 64: {
				getReactionCartoon().setZoomPercent(50);
				break;
			}
			case 50: {
				getReactionCartoon().setZoomPercent(40);
				break;
			}
			case 40: {
				getReactionCartoon().setZoomPercent(30);
				break;
			}
			case 30: {
				getReactionCartoon().setZoomPercent(20);
				break;
			}
			case 20: {
				getReactionCartoon().setZoomPercent(10);
				break;
			}
			case 10: {
				// can't zoom out any further
				break;
			}
			default: {
				getReactionCartoon().setZoomPercent(100);
				break;
			}
			}
		}
	}

	private final void setFloatingRequested(boolean newValue) {
		boolean oldValue = bFloatingRequested;
		this.bFloatingRequested = newValue;
		firePropertyChange(PROPERTY_NAME_FLOATING_REQUESTED, oldValue, newValue);
	}
}