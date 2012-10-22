/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.UUID;
import javax.swing.JTabbedPane;
import javax.swing.plaf.BorderUIResource;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 *
 * @author MetcalfPriv
 */
public class CuffColor extends javax.swing.JPanel implements Comparable<Object> {

	private boolean _isSelected;
	private UUID _id;
	private static BorderUIResource.LineBorderUIResource borderSelected;
	private BorderUIResource.LineBorderUIResource borderNotSelected;

	public CuffColor() {
		_isSelected = false;
		setColor(Color.WHITE);
		_id = UUID.randomUUID();
	}

	CuffColor(Element cuffEle) {
		_isSelected = false;
		String id = cuffEle.getAttributeValue("id");
		if (id != null) {
			_id = UUID.fromString(id);
		} else {
			_id = UUID.randomUUID();
		}
		Element color = cuffEle.getFirstChildElement("Color");
		int r = Integer.parseInt(color.getAttributeValue("red"));
		int g = Integer.parseInt(color.getAttributeValue("green"));
		int b = Integer.parseInt(color.getAttributeValue("blue"));
		int a = 255;
		String alpha = color.getAttributeValue("alpha");
		if (alpha != null) {
			a = Integer.parseInt(alpha);
		}
		setColor(new Color(r, g, b, a));
		setToolTipText(cuffEle.getAttributeValue("name"));
	}

	public Element getSaveData() {
		Element cuffEle = new Element("CuffColor");
		cuffEle.addAttribute(new Attribute("name", toString()));
		cuffEle.addAttribute(new Attribute("id", getId().toString()));
		Element color = new Element("Color");
		color.addAttribute(new Attribute("red", "" + getColor().getRed()));
		color.addAttribute(new Attribute("green", "" + getColor().getGreen()));
		color.addAttribute(new Attribute("blue", "" + getColor().getBlue()));
		color.addAttribute(new Attribute("alpha", "" + getColor().getAlpha()));
		cuffEle.appendChild(color);
		return cuffEle;
	}

	public void setIsSelected(boolean b) {
		_isSelected = b;
		updateBorder();
	}

	public UUID getId() {
		return _id;
	}

	@Override
	public Dimension getSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PreferencesData.PALETTE_COLOR_SIZE, PreferencesData.PALETTE_COLOR_SIZE);
	}

	/**
	 *
	 * @param color
	 */
	public final void setColor(Color color) {
		setBackground(color);
		borderNotSelected = new BorderUIResource.LineBorderUIResource(color, PreferencesData.PALETTE_BORDER_THICKNESS);
		updateBorder();
	}

	@Override
	public String toString() {
		return getToolTipText();
	}

	@Override
	public void revalidate() {
		borderSelected = new BorderUIResource.LineBorderUIResource(Color.BLACK, PreferencesData.PALETTE_BORDER_THICKNESS);
		borderNotSelected = new BorderUIResource.LineBorderUIResource(getColor(), PreferencesData.PALETTE_BORDER_THICKNESS);
		updateBorder();
		super.revalidate();
	}

	private void updateBorder() {
		setBorder(_isSelected ? borderSelected : borderNotSelected);
	}

	@Override
	public int compareTo(Object t) {
		if (t instanceof CuffColor) {
			if (_id.compareTo(((CuffColor) t)._id) == 0) {
				return 0;
			} else {
				return -1;
			}
		} else if (t instanceof Color) {
			if (getColor().getRed() == ((Color) t).getRed()
					&& getColor().getGreen() == ((Color) t).getGreen()
					&& getColor().getBlue() == ((Color) t).getBlue()) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	Color getColor() {
		return getBackground();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
