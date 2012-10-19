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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
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
	private static final int RED = 0;
	private static final int GREEN = 1;
	private static final int BLUE = 2;

	/**
	 * Creates new form Colors
	 */
	public Colors() {
		initComponents();
		//colorPanel.setLayout(new WrapLayout(0, 2, 2));

		colorChooser = new JColorChooser();
		colorChooser.setPreviewPanel(new PreviewPanel());
		dlg = JColorChooser.createDialog(this, "Choose a color", true, colorChooser, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				colorToChange.setColor(colorChooser.getColor());
				save();
			}
		}, null);

		try {
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
				changeColor(color);
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
		save();
	}

	public void removeSelectedColor() {
		if (lastColor != null && !(lastColor instanceof EraseColor)) {
			int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the " + lastColor + " color?", "Delete color", JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.YES_OPTION) {
				colorPanel.remove(lastColor);
				lastColor = null;
				colorPanel.validate();
				colorPanel.repaint();
				save();
			}
		}
	}

	private void changeColor(CuffColor color) {
		colorToChange = color;
		dlg.setVisible(true);
	}

	private void sortColors(Comparator<Component> comparator) {
		Component[] components = colorPanel.getComponents();
		Arrays.sort(components, comparator);
		for (Component cmp : components) {
			colorPanel.add(cmp);
		}
		revalidate();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getComponent() instanceof CuffColor) {
			if (me.isControlDown()) {
				if (me.getComponent() instanceof CuffColor && !(me.getComponent() instanceof EraseColor)) {
					CuffColor color = (CuffColor) me.getComponent();
					changeColor(color);
					revalidate();
					repaint();
					save();
				}
			} else if (me.isShiftDown()) {
				if (me.getComponent() instanceof CuffColor && !(me.getComponent() instanceof EraseColor)) {
					CuffColor color = (CuffColor) me.getComponent();
					String text = (String) JOptionPane.showInputDialog(this, "Enter the name of the new color.", "New Color", JOptionPane.PLAIN_MESSAGE, null, null, color.toString());
					if (text != null && !text.isEmpty()) {
						color.setToolTipText(text);
						revalidate();
						repaint();
						save();
					}
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
				if (mudd.toString() != null && mudd.toString().length() > 0) {
					if (mudd.getBackground() != null) {
						if (mudd.getBackground().equals(color)) {
							return mudd.toString();
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
        jButtonOrderName = new javax.swing.JButton();
        jButtonOrderColor = new javax.swing.JButton();

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

        jButtonOrderName.setText("Order By Name");
        jButtonOrderName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonOrderNameMouseClicked(evt);
            }
        });

        jButtonOrderColor.setText("Order By Color");
        jButtonOrderColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonOrderColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(colorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonOrderName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOrderColor)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removeColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(colorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOrderName)
                    .addComponent(jButtonOrderColor)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addColorButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addColorButtonMouseClicked
		pushNewColor();
    }//GEN-LAST:event_addColorButtonMouseClicked

    private void removeColorButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeColorButtonMouseClicked
		removeSelectedColor();
    }//GEN-LAST:event_removeColorButtonMouseClicked

    private void jButtonOrderNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonOrderNameMouseClicked
		sortColors(new Comparator<Component>() {
			@Override
			public int compare(Component t1, Component t2) {
				if (t1 instanceof EraseColor) {
					return -1;
				} else if (t2 instanceof EraseColor) {
					return 1;
				} else {
					return t1.toString().compareTo(t2.toString());
				}
			}
		});
    }//GEN-LAST:event_jButtonOrderNameMouseClicked

    private void jButtonOrderColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonOrderColorMouseClicked
		sortColors(new Comparator<Component>() {
			@Override
			public int compare(Component t1, Component t2) {
				if (t1 instanceof EraseColor) {
					return -1;
				} else if (t2 instanceof EraseColor) {
					return 1;
				} else {
					final int[] colort1 = new int[]{t1.getBackground().getRed(), t1.getBackground().getGreen(), t1.getBackground().getBlue()};
					final int[] colort2 = new int[]{t2.getBackground().getRed(), t2.getBackground().getGreen(), t2.getBackground().getBlue()};
					int dominantT1 = getBiggest(colort1);
					int dominantT2 = getBiggest(colort2);
					if (dominantT1 != dominantT2) {
						return dominantT1 - dominantT2;
					} else {
						if (dominantT1 == RED) {
							return (colort2[RED] - colort1[RED]) * 3 + (colort2[BLUE] - colort1[BLUE]) * 2 + (colort2[GREEN] - colort1[GREEN]);
						} else if (dominantT1 == BLUE) {
							return (colort2[RED] - colort1[RED]) + (colort2[BLUE] - colort1[BLUE]) * 3 + (colort2[GREEN] - colort1[GREEN] * 2);
						} else {
							return (colort2[RED] - colort1[RED]) * 2 + (colort2[BLUE] - colort1[BLUE]) + (colort2[GREEN] - colort1[GREEN] * 3);
						}
					}
				}
			}

			private int getBiggest(int[] colors) {
				int[] colorsSorted = Arrays.copyOf(colors, colors.length);
				Arrays.sort(colorsSorted);
				int maxVal = colorsSorted[colorsSorted.length - 1];
				for (int i = 0; i < colors.length; ++i) {
					if (colors[i] == maxVal) {
						return i;
					}
				}
				return 0;
			}
		});
    }//GEN-LAST:event_jButtonOrderColorMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addColorButton;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JButton jButtonOrderColor;
    private javax.swing.JButton jButtonOrderName;
    private javax.swing.JButton removeColorButton;
    // End of variables declaration//GEN-END:variables

	private void save() {
		if (colorsLoaded) {
			Element cuffEle, color;
			Element root = new Element("Colors");
			Component cmp;
			CuffColor cuff;
			for (int i = 0; i < colorPanel.getComponentCount(); ++i) {
				cmp = colorPanel.getComponent(i);
				if (cmp instanceof CuffColor && !(cmp instanceof EraseColor)) {
					cuff = (CuffColor) cmp;
					cuffEle = new Element("CuffColor");
					cuffEle.addAttribute(new Attribute("name", cuff.toString()));
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
				File file = new File("settings.ini");
				file.createNewFile();
				Serializer serializer = new Serializer(new FileOutputStream(file), "ISO-8859-1");
				serializer.setIndent(4);
				serializer.setMaxLength(64);
				serializer.write(doc);
			} catch (IOException ex) {
				Logger.getLogger(CuffMain.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void load() {
		try {
			File file = new File("settings.ini");
			if (file.exists()) {
				Builder parser = new Builder();
				FileInputStream fis = new FileInputStream(file);
				Document doc = parser.build(fis);

				Element cuffEle, color;
				int r, g, b;
				Element root = doc.getRootElement();
				CuffColor cuff;
				Elements elements = root.getChildElements("CuffColor");
				for (int i = 0; i < elements.size(); ++i) {
					cuffEle = elements.get(i);
					cuff = new CuffColor();
					color = cuffEle.getFirstChildElement("Color");
					r = Integer.parseInt(color.getAttributeValue("red"));
					g = Integer.parseInt(color.getAttributeValue("green"));
					b = Integer.parseInt(color.getAttributeValue("blue"));
					cuff.setColor(new Color(r, g, b));
					addNewColor(cuff, cuffEle.getAttributeValue("name"));
				}
			}
			colorsLoaded = true;
		} catch (ParsingException | IOException ex) {
			Logger.getLogger(CuffMain.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
