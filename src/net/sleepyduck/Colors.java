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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	private static final int GREY = 0;
	private static final int WHITERED = 1;
	private static final int REDBLACK = 2;
	private static final int WHITEGREEN = 3;
	private static final int GREENBLACK = 4;
	private static final int WHITEBLUE = 5;
	private static final int BLUEBLACK = 6;
	private static final int WHITEYELLOW = 7;
	private static final int YELLOWBLACK = 8;
	private static final int WHITEPURPLE = 9;
	private static final int PURPLEBLACK = 10;
	private static final int WHITETURQUOISE = 11;
	private static final int TURQUOISEBLACK = 12;

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
				getTopLevelAncestor().repaint();
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

	public CuffColor getSelectedColor() {
		if (lastColor != null && !(lastColor instanceof EraseColor)) {
			return lastColor;
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
		colorChooser.setColor(colorToChange.getColor());
		dlg.setVisible(true);
	}

	private void sortColors(Comparator<Component> comparator) {
		Component[] components = colorPanel.getComponents();
		Arrays.sort(components, comparator);
		colorPanel.removeAll();
		for (Component cmp : components) {
			colorPanel.add(cmp);
		}
		revalidate();
		repaint();
		save();
	}

	@Override
	public void revalidate() {
		if (colorPanel != null) {
			for (Component cmp : colorPanel.getComponents()) {
				cmp.revalidate();
			}
		}
		super.revalidate();
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
					lastColor.setIsSelected(false);
				}
				lastColor = (CuffColor) me.getComponent();
				lastColor.setIsSelected(true);
				if (!(lastColor instanceof EraseColor)) {
					previewPanel.setColor(lastColor.getColor());
				} else {
					previewPanel.setColor(null);
				}
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
					if (mudd.getColor() != null) {
						if (mudd.getColor().equals(color)) {
							return mudd.toString();
						}
					}
				}
			}
		}
		return null;
	}

	public CuffColor findColor(Color color) {
		for (Component cmp : colorPanel.getComponents()) {
			if (cmp instanceof CuffColor && !(cmp instanceof EraseColor)) {
				if (((CuffColor) cmp).compareTo(color) == 0) {
					return (CuffColor) cmp;
				}
			}
		}
		return null;
	}

	public CuffColor findColor(UUID id) {
		for (Component cmp : colorPanel.getComponents()) {
			if (cmp instanceof CuffColor && !(cmp instanceof EraseColor)) {
				if (((CuffColor) cmp).getId().compareTo(id) == 0) {
					return (CuffColor) cmp;
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

        addColorButton = new javax.swing.JButton();
        removeColorButton = new javax.swing.JButton();
        jButtonOrderName = new javax.swing.JButton();
        jButtonOrderColor = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        colorPanel = new javax.swing.JPanel();
        previewPanel = new net.sleepyduck.ColorsPreview();

        setMinimumSize(new java.awt.Dimension(200, 200));

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

        colorPanel.setLayout(new net.sleepyduck.WrapLayout());
        jScrollPane1.setViewportView(colorPanel);

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonOrderName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOrderColor))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOrderName)
                    .addComponent(jButtonOrderColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeColorButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(previewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
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
					final int[] color1 = new int[]{t1.getBackground().getRed(), t1.getBackground().getGreen(), t1.getBackground().getBlue()};
					final int[] color2 = new int[]{t2.getBackground().getRed(), t2.getBackground().getGreen(), t2.getBackground().getBlue()};
					final int[] weight1 = getMaxColorAndWeight(color1);
					final int[] weight2 = getMaxColorAndWeight(color2);
					if (weight1[0] == weight2[0]) {
						return weight1[1] - weight2[1];
					} else {
						return weight1[0] - weight2[0];
					}
				}
			}

			// <editor-fold defaultstate="collapsed" desc="Sorting Functions">	
			private int[] getBiggest(int[] numbers) {
				int length = numbers.length;
				int[] colorsSorted = Arrays.copyOf(numbers, length);
				int[] ret = new int[length];
				Arrays.fill(ret, -1);
				Arrays.sort(colorsSorted);
				for (int colId = 0; colId < length; ++colId) {
					int maxVal = colorsSorted[length - 1 - colId];
					for (int i = 0; i < length; ++i) {
						if (numbers[i] == maxVal && !contains(ret, i)) {
							ret[colId] = i;
							break;
						}
					}
				}
				return ret;
			}

			private boolean contains(int[] numbers, int number) {
				for (int i = 0; i < numbers.length; ++i) {
					if (numbers[i] == number) {
						return true;
					}
				}
				return false;
			}

			private int[] getMaxColorAndWeight(int[] color) {
				int[][] weights = new int[2][13];
				int[] weight;
				weight = getGreyscaleWeights(color);
				weights[0][GREY] = weight[0];
				weights[1][GREY] = weight[1];
				weight = getComboColorWeightHigh(color[0], color[1], color[2]);
				weights[0][WHITERED] = weight[0];
				weights[1][WHITERED] = weight[1];
				weight = getColorWeightHigh(color[0], color[1], color[2]);
				weights[0][REDBLACK] = weight[0];
				weights[1][REDBLACK] = weight[1];
				weight = getComboColorWeightHigh(color[1], color[0], color[2]);
				weights[0][WHITEGREEN] = weight[0];
				weights[1][WHITEGREEN] = weight[1];
				weight = getColorWeightHigh(color[1], color[0], color[2]);
				weights[0][GREENBLACK] = weight[0];
				weights[1][GREENBLACK] = weight[1];
				weight = getComboColorWeightHigh(color[2], color[0], color[1]);
				weights[0][WHITEBLUE] = weight[0];
				weights[1][WHITEBLUE] = weight[1];
				weight = getColorWeightHigh(color[2], color[0], color[1]);
				weights[0][BLUEBLACK] = weight[0];
				weights[1][BLUEBLACK] = weight[1];
				weight = getColorWeightLow(color[2], color[0], color[1]);
				weights[0][WHITEYELLOW] = weight[0];
				weights[1][WHITEYELLOW] = weight[1];
				weight = getComboColorWeightLow(color[2], color[0], color[1]);
				weights[0][YELLOWBLACK] = weight[0];
				weights[1][YELLOWBLACK] = weight[1];
				weight = getColorWeightLow(color[0], color[2], color[1]);
				weights[0][WHITEPURPLE] = weight[0];
				weights[1][WHITEPURPLE] = weight[1];
				weight = getComboColorWeightLow(color[0], color[2], color[1]);
				weights[0][PURPLEBLACK] = weight[0];
				weights[1][PURPLEBLACK] = weight[1];
				weight = getColorWeightLow(color[1], color[2], color[0]);
				weights[0][WHITETURQUOISE] = weight[0];
				weights[1][WHITETURQUOISE] = weight[1];
				weight = getComboColorWeightLow(color[1], color[2], color[0]);
				weights[0][TURQUOISEBLACK] = weight[0];
				weights[1][TURQUOISEBLACK] = weight[1];

				// REMOVE SOME COLORS IF PALETTE IS SMALL
				if (colorPanel.getComponents().length < 10) {
					weights[0][WHITEYELLOW] = Integer.MAX_VALUE;
					weights[0][YELLOWBLACK] = Integer.MAX_VALUE;
					weights[0][WHITEPURPLE] = Integer.MAX_VALUE;
					weights[0][PURPLEBLACK] = Integer.MAX_VALUE;
					weights[0][WHITETURQUOISE] = Integer.MAX_VALUE;
					weights[0][TURQUOISEBLACK] = Integer.MAX_VALUE;
				}

				int maxColor = getBiggest(weights[0])[12];
				return new int[]{maxColor, weights[1][maxColor]};
			}

			private int[] getGreyscaleWeights(int[] color) {
				int[] weights = new int[2];
				weights[0] = (Math.abs(color[0] - color[1]) + Math.abs(color[1] - color[2]) + Math.abs(color[2] - color[0])) * 3;
				weights[1] = -color[0] - color[1] - color[2];
				return weights;
			}

			private int[] getColorWeightHigh(int any, int low1, int low2) {
				int[] weights = new int[2];
				weights[0] = 1 + (low1 + low2); //1 Because 0 0 0 should yield geyscale
				weights[1] = -low1 - low2 - any;
				return weights;
			}

			private int[] getColorWeightLow(int any, int high1, int high2) {
				int[] weights = new int[2];
				weights[0] = 1 + (255 - high1 + 255 - high2); //1 Because 0 0 0 should yield geyscale
				weights[1] = -high1 - high2 - any;
				return weights;
			}

			private int[] getComboColorWeightHigh(int high, int any1, int any2) {
				int[] weights = new int[2];
				weights[0] = 1 + Math.abs(any1 - any2) + (255 - high);
				weights[1] = -any1 - any2 - high;
				return weights;
			}

			private int[] getComboColorWeightLow(int low, int any1, int any2) {
				int[] weights = new int[2];
				weights[0] = 1 + Math.abs(any1 - any2) + low;
				weights[1] = -any1 - any2 - low;
				return weights;
			}
			// </editor-fold>
		});
    }//GEN-LAST:event_jButtonOrderColorMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addColorButton;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JButton jButtonOrderColor;
    private javax.swing.JButton jButtonOrderName;
    private javax.swing.JScrollPane jScrollPane1;
    private net.sleepyduck.ColorsPreview previewPanel;
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
					cuffEle = cuff.getSaveData();
					root.appendChild(cuffEle);
				}
			}
			try {
				Document doc = new Document(root);
				File file = new File("palette.ini");
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
			File file = new File("palette.ini");
			if (file.exists()) {
				Builder parser = new Builder();
				FileInputStream fis = new FileInputStream(file);
				Document doc = parser.build(fis);

				Element cuffEle;
				Element root = doc.getRootElement();
				CuffColor cuff;
				Elements elements = root.getChildElements("CuffColor");
				for (int i = 0; i < elements.size(); ++i) {
					cuffEle = elements.get(i);
					cuff = new CuffColor(cuffEle);
					addNewColor(cuff, cuff.toString());
				}
			}
			colorsLoaded = true;
		} catch (ParsingException | IOException ex) {
			Logger.getLogger(CuffMain.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	CuffColor tryReplace(CuffColor cuffColor) {
		CuffColor foundColor = findColor(cuffColor.getId());
		if (foundColor != null) {
			return foundColor;
		} else {
			return cuffColor;
		}
	}
}
