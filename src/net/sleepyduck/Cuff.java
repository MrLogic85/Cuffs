/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ChangeListener;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 * @author MetcalfPriv
 */
public class Cuff extends javax.swing.JPanel implements MouseMotionListener, MouseListener {

	public static final int MIN_WIDTH = 115;
	private int rows = 56;
	private int worms = 54;
	private int repeatYarnH = 1;
	private int repeatYarnV = 2;
	private int repeatBeadsH = 0;
	private int repeatBeadsV = 4;
	private SliderOptionPane sop;
	private Colors colors;
	private CuffColor[][] yarn;
	private CuffColor[][] beads;
	Queue<ChangeListener> changeListeners = new LinkedList<>();

	/**
	 * Creates new form Mudd
	 */
	public Cuff(Colors colors) {
		this.colors = colors;
		sop = new SliderOptionPane();
		yarn = new CuffColor[rows][worms];
		beads = new CuffColor[rows][worms - 1];

		initComponents();
		updateLayout();

		addMouseMotionListener(this);
		addMouseListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		int gap = getGap();
		int colorSize = getColorSize();
		int maxWidth = worms * (colorSize + gap) + PreferencesData.CUFF_EDGE * 2 > MIN_WIDTH ? worms * (colorSize + gap) + PreferencesData.CUFF_EDGE * 2 : MIN_WIDTH;
		setSize(maxWidth, 0);
		return new Dimension(maxWidth, getLayout().preferredLayoutSize(this).height + rows * (colorSize + gap) + PreferencesData.CUFF_EDGE * 2);
	}

	private void updateLayout() {
		widthButton.setText("Width (" + worms + ")");
		heightButton.setText("Height (" + rows + ")");
		repeatYarnHButton.setText("Repeat Yarn H (" + repeatYarnH + ")");
		repeatYarnVButton.setText("Repeat Yarn V (" + repeatYarnV + ")");
		repeatBeadsHButton.setText("Repeat Beads H (" + repeatBeadsH + ")");
		repeatBeadsVButton.setText("Repeat Beads V (" + repeatBeadsV + ")");

		if (name.getWidth() > getVisibleRect().width - ((WrapLayout) getLayout()).getHgap() * 2) {
			name.setSize(getVisibleRect().width - ((WrapLayout) getLayout()).getHgap() * 2, name.getHeight());
		}
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		if ((me.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK)) > 0 || me.getButton() == MouseEvent.BUTTON1 || me.getButton() == MouseEvent.BUTTON3) {
			int gap = getGap();
			int colorSize = getColorSize();
			if (me.getY() > getHeight() - rows * (colorSize + gap) - PreferencesData.CUFF_EDGE) { // TODO, check square
				if ((me.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0 || me.getButton() == MouseEvent.BUTTON1) {
					int repeatRows = (repeatYarnV == 0 || repeatYarnV > rows) ? rows : repeatYarnV;
					int repeatCols = (repeatYarnH == 0 || repeatYarnH > worms) ? worms : repeatYarnH;
					int row = (me.getY() - getHeight() + rows * (colorSize + gap) + PreferencesData.CUFF_EDGE) / (colorSize + gap);
					int col = (me.getX() - PreferencesData.CUFF_EDGE) / (colorSize + gap);
					if (row < rows && col < worms) {
						Element before = getSaveData();
						setColor(yarn, colors.getSelectedColor(), row % repeatRows, col % repeatCols, repeatRows, repeatCols);
						Element after = getSaveData();
						if (before.toXML().compareTo(after.toXML()) != 0) {
							notfiyChangeListeners(before);
						}
						repaint();
					}
				} else if ((me.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0 || me.getButton() == MouseEvent.BUTTON3) {
					int repeatRows = (repeatBeadsV == 0 || repeatBeadsV > rows) ? rows : repeatBeadsV;
					int repeatCols = (repeatBeadsH == 0 || repeatBeadsH > worms - 1) ? worms - 1 : repeatBeadsH;
					int row = (me.getY() - getHeight() + rows * (colorSize + gap) + PreferencesData.CUFF_EDGE) / (colorSize + gap);
					int col = (me.getX() - PreferencesData.CUFF_EDGE - colorSize / 2) / (colorSize + gap);
					if (row < rows && col < worms) {
						Element before = getSaveData();
						setColor(beads, colors.getSelectedColor(), row % repeatRows, col % repeatCols, repeatRows, repeatCols);
						Element after = getSaveData();
						if (before.toXML().compareTo(after.toXML()) != 0) {
							notfiyChangeListeners(before);
						}
						repaint();
					}
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent me) {
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		mouseDragged(me);
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

	void addChangeListener(ChangeListener changeListener) {
		changeListeners.add(changeListener);
	}

	private void notfiyChangeListeners(Element ele) {
		for (ChangeListener cl : changeListeners) {
			cl.stateChanged(new DataChangeEvent(this, ele));
		}
	}

	private int getGap() {
		return editCheckBox.isSelected() ? PreferencesData.CUFF_COLOR_GAP : 0;
	}

	private int getColorSize() {
		return editCheckBox.isSelected() ? PreferencesData.CUFF_COLOR_SIZE_EDIT : PreferencesData.CUFF_COLOR_SIZE;
	}

	private void designSizeChanged() {
		CuffColor[][] newYarn = new CuffColor[rows][worms];
		for (int row = 0; row < newYarn.length && row < yarn.length; ++row) {
			for (int col = 0; col < newYarn[row].length && col < yarn[row].length; ++col) {
				newYarn[row][col] = yarn[row][col];
			}
		}
		yarn = newYarn;
		CuffColor[][] newBeads = new CuffColor[rows][worms - 1];
		for (int row = 0; row < newBeads.length && row < beads.length; ++row) {
			for (int col = 0; col < newBeads[row].length && col < beads[row].length; ++col) {
				newBeads[row][col] = beads[row][col];
			}
		}
		beads = newBeads;
		designRepeatChanged();
	}

	private void designRepeatChanged() {
		int repeatRows = (repeatYarnV == 0 || repeatYarnV > rows) ? rows : repeatYarnV;
		int repeatCols = (repeatYarnH == 0 || repeatYarnH > worms) ? worms : repeatYarnH;
		for (int row = 0; row < repeatRows; ++row) {
			for (int col = 0; col < repeatCols; ++col) {
				setColor(yarn, yarn[row][col], row, col, repeatRows, repeatCols);
			}
		}
		repeatRows = (repeatBeadsV == 0 || repeatBeadsV > rows) ? rows : repeatBeadsV;
		repeatCols = (repeatBeadsH == 0 || repeatBeadsH > worms - 1) ? worms - 1 : repeatBeadsH;
		for (int row = 0; row < repeatRows; ++row) {
			for (int col = 0; col < repeatCols; ++col) {
				setColor(beads, beads[row][col], row, col, repeatRows, repeatCols);
			}
		}
		updateLayout();
	}

	private void setColor(CuffColor[][] colors, CuffColor color, int row, int col, int repeatRow, int repeatCol) {
		int ROW = 0, COL = 0, _row = row, _col = col;
		while (_row < colors.length) {
			while (_col < colors[_row].length) {
				colors[_row][_col] = color;
				COL++;
				_col += repeatCol;
			}
			COL = 0;
			_col = col;
			ROW++;
			_row += repeatRow;
		}
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		int gap = getGap();
		int colorSize = getColorSize();
		int hGap = getHeight() - rows * (colorSize + gap) - PreferencesData.CUFF_EDGE;

		for (int row = 0; row < yarn.length; ++row) {
			for (int col = 0; col < yarn[row].length; ++col) {
				if (yarn[row][col] != null) {
					graphics.setColor(yarn[row][col].getColor());
				} else {
					graphics.setColor(Color.LIGHT_GRAY);
				}
				graphics.fillRect(col * (colorSize + gap) + PreferencesData.CUFF_EDGE, row * (colorSize + gap) + hGap, colorSize, colorSize);
			}
		}
		int _x, _y;
		for (int row = 0; row < beads.length; ++row) {
			for (int col = 0; col < beads[row].length; ++col) {
				if (beads[row][col] != null) {
					graphics.setColor(beads[row][col].getColor());
					_x = col * (colorSize + gap) + PreferencesData.CUFF_EDGE + colorSize / 2;
					_y = row * (colorSize + gap) + hGap;
					graphics.setColor(beads[row][col].getColor());
					graphics.fillOval(_x + 1, _y + 1, colorSize - 2, colorSize - 2);
				}
			}
		}
	}

	public Element getSaveData() {
		Element tmp, pos, color;
		Element root = new Element("Cuff");
		root.addAttribute(new Attribute("name", name.getText()));
		root.addAttribute(new Attribute("worms", "" + worms));
		root.addAttribute(new Attribute("rows", "" + rows));

		tmp = new Element("Yarn");
		tmp.addAttribute(new Attribute("repeatH", "" + repeatYarnH));
		tmp.addAttribute(new Attribute("repeatV", "" + repeatYarnV));
		for (int row = 0; row < yarn.length && (repeatYarnV == 0 || row < repeatYarnV); ++row) {
			for (int col = 0; col < yarn[row].length && (repeatYarnH == 0 || col < repeatYarnH); ++col) {
				if (yarn[row][col] != null) {
					pos = new Element("Pos");
					pos.addAttribute(new Attribute("row", "" + row));
					pos.addAttribute(new Attribute("col", "" + col));
					color = yarn[row][col].getSaveData();
					pos.appendChild(color);
					tmp.appendChild(pos);
				}
			}
		}
		root.appendChild(tmp);

		tmp = new Element("Beads");
		tmp.addAttribute(new Attribute("repeatH", "" + repeatBeadsH));
		tmp.addAttribute(new Attribute("repeatV", "" + repeatBeadsV));
		for (int row = 0; row < beads.length && (repeatBeadsV == 0 || row < repeatBeadsV); ++row) {
			for (int col = 0; col < beads[row].length && (repeatBeadsH == 0 || col < repeatBeadsH); ++col) {
				if (beads[row][col] != null) {
					pos = new Element("Pos");
					pos.addAttribute(new Attribute("row", "" + row));
					pos.addAttribute(new Attribute("col", "" + col));
					color = beads[row][col].getSaveData();
					pos.appendChild(color);
					tmp.appendChild(pos);
				}
			}
		}
		root.appendChild(tmp);

		return root;
	}

	void load(Element root) {
		Elements elements;
		Element tmp, pos;
		int row, col;

		name.setText(root.getAttributeValue("name"));
		worms = Integer.parseInt(root.getAttributeValue("worms"));
		rows = Integer.parseInt(root.getAttributeValue("rows"));

		yarn = new CuffColor[rows][worms];
		beads = new CuffColor[rows][worms - 1];

		tmp = root.getFirstChildElement("Yarn");
		repeatYarnH = Integer.parseInt(tmp.getAttributeValue("repeatH"));
		repeatYarnV = Integer.parseInt(tmp.getAttributeValue("repeatV"));
		elements = tmp.getChildElements("Pos");
		for (int i = 0; i < elements.size(); ++i) {
			pos = elements.get(i);
			row = Integer.parseInt(pos.getAttributeValue("row"));
			col = Integer.parseInt(pos.getAttributeValue("col"));
			yarn[row][col] = new CuffColor(pos.getFirstChildElement("CuffColor"));
			yarn[row][col] = colors.tryReplace(yarn[row][col]);
		}

		tmp = root.getFirstChildElement("Beads");
		repeatBeadsH = Integer.parseInt(tmp.getAttributeValue("repeatH"));
		repeatBeadsV = Integer.parseInt(tmp.getAttributeValue("repeatV"));
		elements = tmp.getChildElements("Pos");
		for (int i = 0; i < elements.size(); ++i) {
			pos = elements.get(i);
			row = Integer.parseInt(pos.getAttributeValue("row"));
			col = Integer.parseInt(pos.getAttributeValue("col"));
			beads[row][col] = new CuffColor(pos.getFirstChildElement("CuffColor"));
			beads[row][col] = colors.tryReplace(beads[row][col]);
		}
		designSizeChanged();
	}

	@Override
	public String toString() {
		return name.getText();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        name = new javax.swing.JTextField();
        editCheckBox = new javax.swing.JCheckBox();
        widthButton = new javax.swing.JButton();
        heightButton = new javax.swing.JButton();
        repeatYarnHButton = new javax.swing.JButton();
        repeatYarnVButton = new javax.swing.JButton();
        repeatBeadsHButton = new javax.swing.JButton();
        repeatBeadsVButton = new javax.swing.JButton();
        getBeadPatternButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new net.sleepyduck.WrapLayout());

        name.setText("Name");
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameKeyReleased(evt);
            }
        });
        add(name);

        editCheckBox.setText("Edit Mode");
        editCheckBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editCheckBoxMouseClicked(evt);
            }
        });
        add(editCheckBox);

        widthButton.setText("Width (20)");
        widthButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                widthButtonMouseClicked(evt);
            }
        });
        add(widthButton);

        heightButton.setText("Height (10)");
        heightButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                heightButtonMouseClicked(evt);
            }
        });
        add(heightButton);

        repeatYarnHButton.setText("Repeat Yarn H (5)");
        repeatYarnHButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                repeatYarnHButtonMouseClicked(evt);
            }
        });
        add(repeatYarnHButton);

        repeatYarnVButton.setText("Repeat Yarn V (0)");
        repeatYarnVButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                repeatYarnVButtonMouseClicked(evt);
            }
        });
        add(repeatYarnVButton);

        repeatBeadsHButton.setText("Repeat Beads H (5)");
        repeatBeadsHButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                repeatBeadsHButtonMouseClicked(evt);
            }
        });
        add(repeatBeadsHButton);

        repeatBeadsVButton.setText("Repeat Beads V (0)");
        repeatBeadsVButton.setActionCommand("Repeat Yarn V (0)");
        repeatBeadsVButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                repeatBeadsVButtonMouseClicked(evt);
            }
        });
        add(repeatBeadsVButton);

        getBeadPatternButton.setText("Get Bead Order");
        getBeadPatternButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                getBeadPatternButtonMouseClicked(evt);
            }
        });
        add(getBeadPatternButton);
    }// </editor-fold>//GEN-END:initComponents

    private void widthButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_widthButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Width", 15, 70, worms);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			notfiyChangeListeners(getSaveData());
			worms = (Integer) sop.getInputValue();
		}
		designSizeChanged();
    }//GEN-LAST:event_widthButtonMouseClicked

    private void heightButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_heightButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Height", 15, 70, rows);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			notfiyChangeListeners(getSaveData());
			rows = (Integer) sop.getInputValue();
		}
		designSizeChanged();
    }//GEN-LAST:event_heightButtonMouseClicked

    private void repeatYarnHButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatYarnHButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat H", 0, 70, repeatYarnH);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			notfiyChangeListeners(getSaveData());
			repeatYarnH = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatYarnHButtonMouseClicked

    private void repeatYarnVButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatYarnVButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat V", 0, 70, repeatYarnV);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			notfiyChangeListeners(getSaveData());
			repeatYarnV = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatYarnVButtonMouseClicked

    private void repeatBeadsHButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatBeadsHButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat H", 0, 70, repeatBeadsH);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			notfiyChangeListeners(getSaveData());
			repeatBeadsH = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatBeadsHButtonMouseClicked

    private void repeatBeadsVButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatBeadsVButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat V", 0, 70, repeatBeadsV);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			notfiyChangeListeners(getSaveData());
			repeatBeadsV = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatBeadsVButtonMouseClicked

    private void getBeadPatternButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_getBeadPatternButtonMouseClicked
		LinkedList<BeadCount> list = new LinkedList<>();
		for (int row = 0; row < beads.length; ++row) {
			for (int worm = beads[row].length - 1; worm >= 0; --worm) {
				if (beads[row][worm] != null) {
					if (list.peekLast() != null && list.peekLast().color.compareTo(beads[row][worm]) == 0) {
						list.getLast().count++;
					} else {
						list.add(new BeadCount(beads[row][worm]));
					}
				}
			}
		}
		BeadsCheckForm checkForm = new BeadsCheckForm(list);
		checkForm.setVisible(true);
    }//GEN-LAST:event_getBeadPatternButtonMouseClicked

    private void nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyReleased
		if (Character.isAlphabetic(evt.getKeyChar()) || Character.isDigit(evt.getKeyChar()) || evt.getKeyChar() == ' ') {
			notfiyChangeListeners(getSaveData());
			updateLayout();
		} else {
			evt.consume();
		}
		revalidate();
    }//GEN-LAST:event_nameKeyReleased

    private void editCheckBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editCheckBoxMouseClicked
		revalidate();
		repaint();
    }//GEN-LAST:event_editCheckBoxMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox editCheckBox;
    private javax.swing.JButton getBeadPatternButton;
    private javax.swing.JButton heightButton;
    private javax.swing.JTextField name;
    private javax.swing.JButton repeatBeadsHButton;
    private javax.swing.JButton repeatBeadsVButton;
    private javax.swing.JButton repeatYarnHButton;
    private javax.swing.JButton repeatYarnVButton;
    private javax.swing.JButton widthButton;
    // End of variables declaration//GEN-END:variables
}
