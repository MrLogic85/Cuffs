/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

/**
 *
 * @author MetcalfPriv
 */
public class Colors extends javax.swing.JPanel implements MouseListener {

	private CuffColor lastColor;
	private CuffColor colorToChange;
	private JColorChooser colorChooser;
	private final JDialog dlg;
	private boolean colorsLoaded = false;

	/**
	 * Creates new form Colors
	 */
	public Colors() {
		initComponents();
		//colorPanel.setLayout(new WrapLayout(0, 2, 2));

		colorChooser = new JColorChooser();
		colorChooser.setPreviewPanel(new JPanel());
		dlg = JColorChooser.createDialog(this, "Choose a color", true, colorChooser, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				colorToChange.setColor(colorChooser.getColor());
			}
		}, null);

		try {
			load(); // TODO, remove
			addEraseColor();
			load();
		} catch (IOException ex) {
			Logger.getLogger(Colors.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Color getSelectedColor() {
		if (lastColor != null && !(lastColor instanceof EraseColor)) {
			return lastColor.getBackground();
		}
		return null;
	}

	private void addEraseColor() throws IOException {
		CuffColor color = new EraseColor();
		addNewColor(color, "Erase");
	}

	private void pushNewColor() {
		String text = (String) JOptionPane.showInputDialog(this, "Enter the name of the new color.", "New Color", JOptionPane.PLAIN_MESSAGE);
		if (text != null && !text.isEmpty()) {
			pushNewColor(text);
		}
	}

	private void pushNewColor(String text) {
		CuffColor color = new CuffColor();
		// <editor-fold defaultstate="collapsed" desc="Lots of colors">
		switch (text.toLowerCase()) {
			case "red":
				color.setColor(Color.RED);
				break;
			case "blue":
				color.setColor(Color.BLUE);
				break;
			case "green":
				color.setColor(Color.GREEN);
				break;
			case "yellow":
				color.setColor(Color.YELLOW);
				break;
			case "orange":
				color.setColor(Color.ORANGE);
				break;
			case "purple":
				color.setColor(new Color(255, 0, 255));
				break;
			case "black":
				color.setColor(Color.BLACK);
				break;
			case "white":
				color.setColor(Color.WHITE);
				break;
			case "grey":
			case "gray":
				color.setColor(Color.GRAY);
				break;
			case "pink":
				color.setColor(Color.PINK);
				break;
			default:
				selectColor(color);
		}
		// </editor-fold>
		addNewColor(color, text);
	}

	private void addNewColor(CuffColor color, String tooltipText) {
		color.addMouseListener(this);
		color.setToolTipText(tooltipText);
		colorPanel.add(color);
		colorPanel.revalidate();
		colorPanel.repaint();
		if (colorsLoaded)
			save();
	}

	public void removeSelectedColor() {
		if (lastColor != null && !(lastColor instanceof EraseColor)) {
			int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the " + lastColor.getToolTipText() + " color?", "Delete color", JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.YES_OPTION) {
				colorPanel.remove(lastColor);
				lastColor = null;
				colorPanel.validate();
				colorPanel.repaint();
			}
		}
	}

	private void selectColor(CuffColor color) {
		colorToChange = color;
		dlg.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getComponent() instanceof CuffColor) {
			if (me.isControlDown()) {
				if (me.getComponent() instanceof CuffColor && !(me.getComponent() instanceof EraseColor)) {
					CuffColor color = (CuffColor) me.getComponent();
					selectColor(color);
				}
			} else if (me.getButton() == MouseEvent.BUTTON1) {
				if (lastColor != null) {
					lastColor.setIsYarnSelected(false);
				}
				lastColor = (CuffColor) me.getComponent();
				lastColor.setIsYarnSelected(true);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	String geColorName(Color color) {
		CuffColor mudd;
		for (Component comp : colorPanel.getComponents()) {
			if (comp instanceof CuffColor) {
				mudd = (CuffColor) comp;
				if (mudd.getToolTipText() != null && mudd.getToolTipText().length() > 0) {
					if (mudd.getBackground() != null) {
						if (mudd.getBackground().equals(color)) {
							return mudd.getToolTipText();
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorPanel = new javax.swing.JPanel();
        addColorButton = new javax.swing.JButton();
        removeColorButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(200, 200));

        colorPanel.setLayout(new net.sleepyduck.WrapLayout());

        addColorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Plus.png"))); // NOI18N
        addColorButton.setToolTipText("Add new color");
        addColorButton.setIconTextGap(0);
        addColorButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        addColorButton.setPreferredSize(new java.awt.Dimension(18, 18));
        addColorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addColorButtonMouseClicked(evt);
            }
        });

        removeColorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Minus.png"))); // NOI18N
        removeColorButton.setToolTipText("Remove selected color");
        removeColorButton.setIconTextGap(0);
        removeColorButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        removeColorButton.setPreferredSize(new java.awt.Dimension(18, 18));
        removeColorButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeColorButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(addColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 158, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removeColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(colorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addColorButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addColorButtonMouseClicked
		pushNewColor();
    }//GEN-LAST:event_addColorButtonMouseClicked

    private void removeColorButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeColorButtonMouseClicked
		removeSelectedColor();
    }//GEN-LAST:event_removeColorButtonMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addColorButton;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JButton removeColorButton;
    // End of variables declaration//GEN-END:variables

	private void save() {
		Element cuffEle, color;
		Element root = new Element("Colors");
		Component cmp;
		CuffColor cuff;
		for (int i = 0; i < colorPanel.getComponentCount(); ++i) {
			cmp = colorPanel.getComponent(i);
			if (cmp instanceof CuffColor && !(cmp instanceof EraseColor)) {
				cuff = (CuffColor) cmp;
				cuffEle = new Element("CuffColor");
				cuffEle.addAttribute(new Attribute("name", cuff.getToolTipText()));
				color = new Element("Color");
				color.addAttribute(new Attribute("red", "" + cuff.getBackground().getRed()));
				color.addAttribute(new Attribute("green", "" + cuff.getBackground().getGreen()));
				color.addAttribute(new Attribute("blue", "" + cuff.getBackground().getBlue()));
				cuffEle.appendChild(color);
				root.appendChild(cuffEle);
			}
		}
		try {
			Document doc = new Document(root);
			File theFileToSave = new File("settings.ini");
			theFileToSave.createNewFile();
			Serializer serializer = new Serializer(new FileOutputStream(theFileToSave), "ISO-8859-1");
			serializer.setIndent(4);
			serializer.setMaxLength(64);
			serializer.write(doc);
		} catch (IOException ex) {
			Logger.getLogger(CuffMain.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void load() {
		colorsLoaded = true;
	}
}
