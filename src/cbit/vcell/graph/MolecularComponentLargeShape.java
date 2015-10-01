package cbit.vcell.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.vcell.model.rbm.ComponentStateDefinition;
import org.vcell.model.rbm.ComponentStatePattern;
import org.vcell.model.rbm.MolecularComponent;
import org.vcell.model.rbm.MolecularComponentPattern;
import org.vcell.model.rbm.MolecularType;
import org.vcell.model.rbm.RbmElementAbstract;
import org.vcell.model.rbm.SpeciesPattern;
import org.vcell.util.Displayable;
import org.vcell.util.Issue;

import cbit.vcell.client.desktop.biomodel.RbmTreeCellRenderer;
import cbit.vcell.model.RbmObservable;
import cbit.vcell.model.ReactionParticipant;
import cbit.vcell.model.ReactionRule;
import cbit.vcell.model.SpeciesContext;

public class MolecularComponentLargeShape extends AbstractComponentShape implements LargeShape, HighlightableShapeInterface {
	
	public static final int componentSeparation = 6;	// distance between components
	public static final int baseWidth = 24;				// was 16
	public static final int baseHeight = 19;			// was 17
	public static final int cornerArc = 17;
	
	final Graphics graphicsContext;
	
	private boolean pattern;					// we draw component or we draw component pattern (and perhaps a bond)
	private int xPos = 0;
	private int yPos = 0;
	private int width = baseWidth;		// with no letters inside, it's an empty circle
	private int height = baseHeight;
	
	private int textWidth = 0;			// we add this to componentDiameter to obtain the final width of the pill
	private final String displayName;
	private final MolecularComponent mc;
	private final MolecularComponentPattern mcp;
	private final Displayable owner;
	private final List <ComponentStateLargeShape> stateShapes = new ArrayList<ComponentStateLargeShape> ();
	
	//---------------------------------------------------------------------------------------------------
	public class ComponentStateLargeShape implements LargeShape, HighlightableShapeInterface {

		final static int xOffsetWidth = 16;

		final Graphics graphicsContext;
		private final Font font;
		private int xPos;
		private int yPos;
		private int width;
		private final int height;

		private ComponentStateDefinition csd = null;
		private ComponentStatePattern csp = null;
		private final Displayable owner;
		private String displayName;
		
		public ComponentStateLargeShape(int x, int y, RbmElementAbstract rea, Graphics gc, Displayable owner) {
			this.xPos = x;
			this.yPos = y;
			
			this.owner = owner;
			if(owner instanceof MolecularType) {
			this.csd = (ComponentStateDefinition)rea;
			} else {
				this.csp = (ComponentStatePattern)rea;
				this.csd = csp.getComponentStateDefinition();	// we create a large shape only if the ComponentStatePattern has a ComponentStateDefinition
			}
			this.graphicsContext = gc;
			
			if(this.csd == null) {
				this.displayName = "?";
			} else {
				this.displayName = adjustForSize(csd.getDisplayName());
			}
			this.font = deriveStateFont(gc);
			this.height = computeStateHeight(gc);
			FontMetrics fm = graphicsContext.getFontMetrics(font);
			width = fm.stringWidth(displayName) + xOffsetWidth;		// pill will be wider than the text we show inside it
		}
		
		public boolean contains(PointLocationInShapeContext locationContext) {
			Rectangle2D rect = makeStateRectangle();
			if(rect.contains(locationContext.point)) {
				locationContext.csls = this;
				return true;
			}
			return false;		// point not inside this component shape, locationContext.mcs remains null;
		}
		// the rectangle is a bit wider and somewhat centered around the text
		private Rectangle2D makeStateRectangle() {
			final int xOffsetLeft = 0;
			final int yOffset = 0;
			return new Rectangle2D.Double(xPos-xOffsetLeft, yPos+yOffset, width, height);
		}

		@Override
		public void setX(int xPos) {
			this.xPos = xPos;
		}
		@Override
		public int getX() {
			return xPos;
		}
		@Override
		public void setY(int yPos) {
			this.yPos = yPos;
		}
		@Override
		public int getY() {
			return yPos;
		}
		@Override
		public int getWidth() {
			return width;
		}
		@Override
		public int getHeight() {
			return height;
		}
		@Override
		public Rectangle getLabelOutline() {
			Font font = getLabelFont();
			FontMetrics fm = graphicsContext.getFontMetrics(font);
			int stringWidth = fm.stringWidth(getFullName());
			Rectangle labelOutline = new Rectangle(xPos+2, yPos, stringWidth+8, fm.getHeight()+5);
			return labelOutline;
		}
		@Override
		public Font getLabelFont() {
			return font;
		}
		@Override
		public String getFullName() {
			if(csd != null) {
				return csd.getDisplayName();
			} else {
				return "?";
			}
		}
		public void forceDifferentWidth(int width) {
			// use this to force a different with, for instance if we want all the rectangles used
			// for contains() to be equal
			// we don't allow reducing the width below the width of the text
			FontMetrics fm = graphicsContext.getFontMetrics(font);
			int minWidth = fm.stringWidth(displayName);
			if(width > minWidth) {
				// we compared the widths of the strings, but we set the width of the "pill", so we add xOffsetWidth
				this.width = width + xOffsetWidth;
			}
		}

		@Override
		public void paintSelf(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			Font fontOld = g.getFont();
			Color colorOld = g.getColor();
			Paint paintOld = g2.getPaint();

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color paleBlue = Color.getHSBColor(0.6f, 0.03f, 1.0f);
			Color darkerBlue = Color.getHSBColor(0.6f, 0.18f, 1.0f);	// a bit darker for border
			Rectangle2D rect = makeStateRectangle();
			
			if(owner instanceof MolecularType) {
				// we deal with highlighting only for Molecular Type States - because only here we have more
				// than one and highlighting them individually makes sense
//				if(isHighlighted()) {
//					g2.setPaint(paleBlue);
//					g2.fill(rect);
//					g2.setColor(darkerBlue);
//					g2.draw(rect);
//				} else {
//					g2.setPaint(Color.white);
//					g2.fill(rect);
//					g2.setColor(Color.white);
//					g2.draw(rect);
//				}
			}
			
			// =========================================================
			RoundRectangle2D normalRectangle = new RoundRectangle2D.Float(xPos, yPos, width, height, cornerArc, cornerArc);
			g2.setColor(componentYellow);
			g2.fill(normalRectangle);
			g.setColor(Color.black);
			g2.draw(normalRectangle);
			// =========================================================
			
			g.setFont(font);
			g.setColor(Color.black);
//			g.drawString(displayName, xPos, yPos);
			g.drawString(displayName, xPos+7, yPos+computeStateHeight(g)-4);
			
			g2.setPaint(paintOld);
			g.setFont(fontOld);
			g.setColor(colorOld);
		}

		@Override
		public boolean isHighlighted() {
			
			if(owner instanceof RbmObservable) {
				return csp.isHighlighted();
			} else if(owner instanceof MolecularType) {
				return csd.isHighlighted();
			} else if(owner instanceof SpeciesContext) {
				return csp.isHighlighted();
			} else if(owner instanceof ReactionRule) {
				return csp.isHighlighted();
			}
			return false;
		}
		@Override
		public void setHighlight(boolean b) {
			if(owner instanceof RbmObservable) {
				csp.setHighlighted(b);
			} else if(owner instanceof MolecularType) {
				csd.setHighlighted(b);
			} else if(owner instanceof SpeciesContext) {
				csp.setHighlighted(b);
			} else if(owner instanceof ReactionRule) {
				csp.setHighlighted(b);
			}
		}
		@Override
		public void turnHighlightOffRecursive(Graphics g) {
			boolean oldHighlight = isHighlighted();
			setHighlight(false);
			if(oldHighlight == true) {
				paintSelf(g);			// paint self not highlighted if previously highlighted
			}
		}

		public Displayable getOwner() {
			return owner;
		}
		public ComponentStateDefinition getComponentStateDefinition() {
			return csd;
		}
		public ComponentStatePattern getComponentStatePattern() {
			return csp;
		}
	}	// --- end class ComponentStateLargeShape ---------------------------------------------------------------

	// rightPos is rightmost corner of the ellipse, we compute the xPos based on the text width
	public MolecularComponentLargeShape(int rightPos, int y, MolecularComponent mc, Graphics graphicsContext, Displayable owner) {
		this.owner = owner;
		this.pattern = false;
		this.mcp = null;
		this.mc = mc;
		this.graphicsContext = graphicsContext;
		displayName = adjustForSize(mc.getDisplayName());
		
		Font font = deriveStateFont(graphicsContext);
		int stateHeight = getStringHeight(font)+2;	// we reserve 2 extra pixels height as separation between states
		String longestStateName = getLongestStateName(mc);
		
		// we reserve enough space for component name or state name, whichever is longer
		// TODO: this is not an exact science because so far we checked just for the number of characters
		textWidth = getStringWidth(displayName) > getStateStringWidth(longestStateName, graphicsContext) ? getStringWidth(displayName) : getStateStringWidth(longestStateName, graphicsContext);
		width = baseWidth + textWidth;
		xPos = rightPos-width;
		yPos = y;
		for(int i=0; i<mc.getComponentStateDefinitions().size(); i++) {
			ComponentStateDefinition csd = mc.getComponentStateDefinitions().get(i);
			// align the end of the state shape with the end of the component shape
			int xPosState = xPos + width - getStateStringWidth(longestStateName, graphicsContext) - ComponentStateLargeShape.xOffsetWidth;
			ComponentStateLargeShape csls = new ComponentStateLargeShape(xPosState, yPos - computeStateHeight(graphicsContext)*(i+1)+1, csd, graphicsContext, owner);
			csls.forceDifferentWidth(getStateStringWidth(longestStateName, graphicsContext));
			stateShapes.add(csls);
		}
	}
	public MolecularComponentLargeShape(int rightPos, int y, MolecularComponentPattern mcp, Graphics graphicsContext, Displayable owner) {
		this.owner = owner;
		this.pattern = true;
		this.mcp = mcp;
		this.mc = mcp.getMolecularComponent();
		this.graphicsContext = graphicsContext;
		
		displayName = adjustForSize(mc.getDisplayName());
		String stateName = "";
		ComponentStatePattern csp = mcp.getComponentStatePattern();
		if(csp != null && !csp.isAny() && mcp.getComponentStatePattern().getComponentStateDefinition() != null) {
			ComponentStateDefinition csd = mcp.getComponentStatePattern().getComponentStateDefinition();
			stateName = adjustForSize(csd.getDisplayName());
		}
		String longestStateName = getLongestStateName(mc);
		int displayNameWidth = getStringWidth(displayName);
		int longestStateNameWidth = getStateStringWidth(longestStateName, graphicsContext);
		
		textWidth = displayNameWidth > longestStateNameWidth ? displayNameWidth : longestStateNameWidth;
		width = width + textWidth;
		xPos = rightPos-width;
		yPos = y;
//		if(csp != null && !csp.isAny() && mcp.getComponentStatePattern().getComponentStateDefinition() != null) {
		if(csp != null) {
			// align the end of the state shape with the end of the component shape
			int xPosState = xPos + width - longestStateNameWidth - ComponentStateLargeShape.xOffsetWidth;
			ComponentStateLargeShape csls = new ComponentStateLargeShape(xPosState, yPos-computeStateHeight(graphicsContext)+1, csp, graphicsContext, owner);
			csls.forceDifferentWidth(getStateStringWidth(longestStateName, graphicsContext));
			stateShapes.add(csls);
		}
	}
	private String getLongestStateName(MolecularComponent mc) {
		String longestStateName = "";
		for(int i=0; i<mc.getComponentStateDefinitions().size(); i++) {
			ComponentStateDefinition csd = mc.getComponentStateDefinitions().get(i);
			String stateName = csd.getDisplayName();
			stateName = adjustForSize(stateName);
			longestStateName = stateName.length() > longestStateName.length() ? stateName : longestStateName;
		}
		return longestStateName;
	}

	public MolecularComponentPattern getMolecularComponentPattern() {
		return mcp;
	}
	public MolecularComponent getMolecularComponent() {
		return mc;
	}
	@Override
	public int getX(){
		return xPos;
	}
	@Override
	public void setX(int xPos) {
		this.xPos = xPos;
	}
	@Override
	public int getY(){
		return yPos;
	}
	@Override
	public void setY(int yPos) {
		this.yPos = yPos;
	}
	@Override
	public int getWidth(){
		return width;
	} 
	@Override
	public int getHeight(){
		return height;
	}
	@Override
	public Rectangle getLabelOutline() {
		Font font = getLabelFont();
		FontMetrics fm = graphicsContext.getFontMetrics(font);
		int stringWidth = fm.stringWidth(getFullName());
		Rectangle labelOutline = new Rectangle(xPos+6, yPos+2, stringWidth+18, fm.getHeight()+2);
		return labelOutline;
	}
	@Override
	public Font getLabelFont() {
		return MolecularComponentLargeShape.deriveComponentFontBold(graphicsContext);
	}
	@Override
	public String getFullName() {
		if(mc != null) {
			return mc.getDisplayName();
		} else {
			return "?";
		}
	}
	
	private String adjustForSize(String input) {
		int len = input.length();
		if(len > 8) {
			return(input.substring(0,4) + ".." + input.substring(len-2, len));
		} else {
			return(input);
		}
	}

	public static Font deriveComponentFontBold(Graphics gc) {
		Font fontOld = gc.getFont();
		Font font = fontOld.deriveFont((float) (baseHeight*3/5)).deriveFont(Font.BOLD);
		return font;
	}
	private static Font deriveStateFont(Graphics gc) {
		Font fontOld = gc.getFont();
		Font font = fontOld.deriveFont((float) ((float)baseHeight*3.0/6.0));
		return font;
	}
	public static int computeStateHeight(Graphics gc) {
		Font font = deriveStateFont(gc);
		FontMetrics fm = gc.getFontMetrics(font);
		return fm.getHeight() + 4;		// we allow 2 pixels above and below the state text
	}
	private static int getStateStringWidth(String s, Graphics gc) {
		Font font = deriveStateFont(gc);
		FontMetrics fm = gc.getFontMetrics(font);
		return fm.stringWidth(s);
	}
	
	private int getStringWidth(String s) {
//		Font font = graphicsContext.getFont().deriveFont(Font.BOLD);
		Font font = deriveComponentFontBold(graphicsContext);
		FontMetrics fm = graphicsContext.getFontMetrics(font);
		int stringWidth = fm.stringWidth(s);
		return stringWidth;
	}
	private int getStringHeight(Font font) {
		FontMetrics fm = graphicsContext.getFontMetrics(font);
		int stringHeight = fm.getHeight();
		return stringHeight;
	}

	private Color setComponentColor() {
		boolean highlight = isHighlighted();
		if(owner == null) {
			return highlight == true ? componentBad.brighter() : componentBad;
		}
		Color componentColor = highlight == true ? componentBad.brighter() : componentBad;
		if(owner instanceof MolecularType) {
			componentColor = highlight == true ? componentGreen.brighter() : componentGreen;
		} else if(owner instanceof SpeciesContext) {
			componentColor = highlight == true ? componentGreen.brighter() : componentGreen;
		} else if(mcp != null && owner instanceof RbmObservable) {
			componentColor = highlight == true ? componentHidden.brighter() : componentHidden;
			if(mcp.isbVisible()) {
				componentColor = highlight == true ? componentGreen.brighter() : componentGreen;
			}
			ComponentStatePattern csp = mcp.getComponentStatePattern();
			if(csp != null && !csp.isAny()) {
				componentColor = highlight == true ? componentGreen.brighter() : componentGreen;
			}
		} else if(owner instanceof ReactionRule) {
			componentColor = highlight == true ? componentHidden.brighter() : componentHidden;
			if(mcp.isbVisible()) {
				componentColor = highlight == true ? componentGreen.brighter() : componentGreen;
			}
			ComponentStatePattern csp = mcp.getComponentStatePattern();
			if(csp != null && !csp.isAny()) {
				componentColor = highlight == true ? componentGreen.brighter() : componentGreen;
			}
		}
		if(AbstractComponentShape.hasIssues(owner, mcp, mc)) {
			componentColor = highlight == true ? componentBad.brighter() : componentBad;
		}
		return componentColor;
	}

	// ----------------------------------------------------------------------------------------------
	@Override
	public boolean contains(PointLocationInShapeContext locationContext) {
		
		// first we check if the point is inside a subcomponent of "this"
		for(ComponentStateLargeShape csls : stateShapes) {
			boolean found = csls.contains(locationContext);
			if(found) {
				// since point is inside one of our components it's also inside "this"
				locationContext.mcs = this;
				return true;	// if the point is inside a ComponentStateLargeShape there's no need to check others
			}
		}
		// even if the point it's not inside one of our subcomponents it may still be inside "this"
		RoundRectangle2D rect = new RoundRectangle2D.Float(xPos, yPos, width, baseHeight, cornerArc, cornerArc);
		if(rect.contains(locationContext.point)) {
			locationContext.mcs = this;
			return true;
		}
		return false;		// point not inside this component shape, locationContext.mcs remains null;
	}
	
	public void paintSelf(Graphics g) {
		paintComponent(g);
	}
	private void paintComponent(Graphics g) {
		
		boolean hidden = AbstractComponentShape.isHidden(owner, mcp);
		Graphics2D g2 = (Graphics2D)g;
		Font fontOld = g.getFont();
		Color colorOld = g.getColor();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Color componentColor = setComponentColor();
		g2.setColor(componentColor);
		
		RoundRectangle2D normalRectangle = new RoundRectangle2D.Float(xPos, yPos, width, baseHeight, cornerArc, cornerArc);
		RoundRectangle2D innerRectangle = new RoundRectangle2D.Float(xPos+1, yPos+1, width-2, baseHeight-2, cornerArc-2, cornerArc-2);
		g2.fill(normalRectangle);
		if(AbstractComponentShape.hasIssues(owner, mcp, mc)) {
			g2.setColor(Color.red);
		} else {
			if(hidden == false) {
				g.setColor(Color.black);
			} else {
				g.setColor(Color.gray);
			}
		}
		g2.draw(normalRectangle);
		if(isHighlighted()) {
			g2.setColor(Color.black);
			g2.draw(innerRectangle);
			g2.setColor(componentColor);
		}

		Font font = deriveComponentFontBold(graphicsContext);
		g.setFont(font);
		if(hidden == false) {
			g.setColor(Color.black);
		} else {
			g.setColor(Color.gray);
		}
		g2.drawString(displayName, xPos+1+baseWidth/2, yPos+5+baseHeight/2);

		for(ComponentStateLargeShape csls : stateShapes) {	// text of the State(s), if any
			csls.paintSelf(g);
		}
		
		// the smaller "State" yellow circle
//		if(mc.getComponentStateDefinitions().size()>0) {
//			g2.setColor(componentYellow);
//			g2.fillOval(xPos + width - 12, yPos-6, 10, 10);
//			g.setColor(Color.gray);
//			g2.drawOval(xPos + width - 12, yPos-6, 10, 10);
//		}
		
		g.setFont(fontOld);
		g.setColor(colorOld);
	}
	
	@Override
	public void setHighlight(boolean b) {
		if(owner instanceof RbmObservable) {
			mcp.setHighlighted(b);
		} else if(owner instanceof MolecularType) {
			mc.setHighlighted(b);
		} else if(owner instanceof SpeciesContext) {
			mcp.setHighlighted(b);
		} else if(owner instanceof ReactionRule) {
			mcp.setHighlighted(b);
		}
	}
	@Override
	public boolean isHighlighted() {
		if(owner instanceof RbmObservable) {
			return mcp.isHighlighted();
		} else if(owner instanceof MolecularType) {
			return mc.isHighlighted();
		} else if(owner instanceof SpeciesContext) {
			return mcp.isHighlighted();
		} else if(owner instanceof ReactionRule) {
			return mcp.isHighlighted();
		}
		return false;
	}
	@Override
	public void turnHighlightOffRecursive(Graphics g) {	// not really recursive, no subchildren (for now)
		boolean oldHighlight = isHighlighted();
		setHighlight(false);
		if(oldHighlight == true) {
			paintSelf(g);			// paint self not highlighted if previously highlighted
		}
		for(ComponentStateLargeShape css : stateShapes) {
			css.turnHighlightOffRecursive(g);
		}
	}

}
