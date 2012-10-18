/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import javax.swing.JDialog;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;

/**
 *
 * @author MetcalfPriv
 */
public class Cuff extends javax.swing.JPanel implements MouseMotionListener, MouseListener {

	private static int UNIT_SQARE = 10;
	private static int UNIT_GAP = 1;
	private static int UNIT_EDGE = 5;
	private static int MIN_WIDTH = 115;
	private int rows = 54;
	private int worms = 56;
	private int repeatYarnH = 1;
	private int repeatYarnV = 2;
	private int repeatBeadsH = 0;
	private int repeatBeadsV = 4;
	private SliderOptionPane sop;
	private Colors colors;
	private Color[][] yarn;
	private Color[][] beads;

	/**
	 * Creates new form Mudd
	 */
	public Cuff(Colors colors) {
		this.colors = colors;
		sop = new SliderOptionPane();
		yarn = new Color[rows][worms];
		beads = new Color[rows][worms - 1];

		initComponents();
		updateLayout();

		addMouseMotionListener(this);
		addMouseListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		int maxWidth = worms * (UNIT_SQARE + UNIT_GAP) + UNIT_EDGE * 2 > MIN_WIDTH ? worms * (UNIT_SQARE + UNIT_GAP) + UNIT_EDGE * 2 : MIN_WIDTH;
		setSize(maxWidth, 0);
		return new Dimension(maxWidth, getLayout().preferredLayoutSize(this).height + rows * (UNIT_SQARE + UNIT_GAP) + UNIT_EDGE * 2);
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
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		if ((me.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK)) > 0 || me.getButton() == MouseEvent.BUTTON1 || me.getButton() == MouseEvent.BUTTON3) {
			if (me.getY() > getHeight() - rows * (UNIT_SQARE + UNIT_GAP) - UNIT_EDGE) { // TODO, check square
				if ((me.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0 || me.getButton() == MouseEvent.BUTTON1) {
					int repeatRows = (repeatYarnV == 0 || repeatYarnV > rows) ? rows : repeatYarnV;
					int repeatCols = (repeatYarnH == 0 || repeatYarnH > worms) ? worms : repeatYarnH;
					int row = (me.getY() - getHeight() + rows * (UNIT_SQARE + UNIT_GAP) + UNIT_EDGE) / (UNIT_SQARE + UNIT_GAP);
					int col = (me.getX() - UNIT_EDGE) / (UNIT_SQARE + UNIT_GAP);
					if (row < rows && col < worms) {
						setYarnColor(colors.getSelectedColor(), row % repeatRows, col % repeatCols, repeatRows, repeatCols);
						repaint();
					}
				} else if ((me.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0 || me.getButton() == MouseEvent.BUTTON3) {
					int repeatRows = (repeatBeadsV == 0 || repeatBeadsV > rows) ? rows : repeatBeadsV;
					int repeatCols = (repeatBeadsH == 0 || repeatBeadsH > worms - 1) ? worms - 1 : repeatBeadsH;
					int row = (me.getY() - getHeight() + rows * (UNIT_SQARE + UNIT_GAP) + UNIT_EDGE) / (UNIT_SQARE + UNIT_GAP);
					int col = (me.getX() - UNIT_EDGE - UNIT_SQARE / 2) / (UNIT_SQARE + UNIT_GAP);
					if (row < rows && col < worms) {
						setBeadColor(colors.getSelectedColor(), row % repeatRows, col % repeatCols, repeatRows, repeatCols);
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

	private void designSizeChanged() {
		Color[][] newYarn = new Color[rows][worms];
		for (int row = 0; row < newYarn.length && row < yarn.length; ++row) {
			for (int col = 0; col < newYarn[row].length && col < yarn[row].length; ++col) {
				newYarn[row][col] = yarn[row][col];
			}
		}
		yarn = newYarn;
		Color[][] newBeads = new Color[rows][worms - 1];
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
				setYarnColor(yarn[row][col], row, col, repeatRows, repeatCols);
			}
		}
		repeatRows = (repeatBeadsV == 0 || repeatBeadsV > rows) ? rows : repeatBeadsV;
		repeatCols = (repeatBeadsH == 0 || repeatBeadsH > worms - 1) ? worms - 1 : repeatBeadsH;
		for (int row = 0; row < repeatRows; ++row) {
			for (int col = 0; col < repeatCols; ++col) {
				setBeadColor(beads[row][col], row, col, repeatRows, repeatCols);
			}
		}
		updateLayout();
		repaint();
	}

	private void setYarnColor(Color color, int row, int col, int repeatRow, int repeatCol) {
		int ROW = 0, COL = 0, _row = row, _col = col;
		while (_row < yarn.length) {
			while (_col < yarn[_row].length) {
				yarn[_row][_col] = color;
				COL++;
				if (mirrorYarnHCheckbox.isSelected()) {
					_col += (COL % 2 == 1) ? /*ODD*/ (repeatCol - col) * 2 : /*EVEN*/ col * 2;
				} else {
					_col += repeatCol;
				}
			}
			COL = 0;
			_col = col;
			ROW++;
			if (mirrorYarnVCheckbox.isSelected()) {
				_row += (ROW % 2 == 1) ? /*ODD*/ (repeatRow - row) * 2 : /*EVEN*/ row * 2;
			} else {
				_row += repeatRow;
			}
		}
	}

	private void setBeadColor(Color color, int row, int col, int repeatRow, int repeatCol) {
		int ROW = 0, COL = 0, _row = row, _col = col;
		while (_row < beads.length) {
			while (_col < beads[_row].length) {
				beads[_row][_col] = color;
				COL++;
				if (mirrorBeadsHCheckbox.isSelected()) {
					_col += (COL % 2 == 1) ? /*ODD*/ (repeatCol - col) * 2 : /*EVEN*/ col * 2;
				} else {
					_col += repeatCol;
				}
			}
			COL = 0;
			_col = col;
			ROW++;
			if (mirrorBeadsVCheckbox.isSelected()) {
				_row += (ROW % 2 == 1) ? /*ODD*/ (repeatRow - row) * 2 : /*EVEN*/ row * 2;
			} else {
				_row += repeatRow;
			}
		}
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		int hGap = getHeight() - rows * (UNIT_SQARE + UNIT_GAP) - UNIT_EDGE;
		for (int row = 0; row < yarn.length; ++row) {
			for (int col = 0; col < yarn[row].length; ++col) {
				if (yarn[row][col] != null) {
					graphics.setColor(yarn[row][col]);
				} else {
					graphics.setColor(Color.LIGHT_GRAY);
				}
				graphics.fillRect(col * (UNIT_SQARE + UNIT_GAP) + UNIT_EDGE, row * (UNIT_SQARE + UNIT_GAP) + hGap, UNIT_SQARE, UNIT_SQARE);
			}
		}
		for (int row = 0; row < beads.length; ++row) {
			for (int col = 0; col < beads[row].length; ++col) {
				if (beads[row][col] != null) {
					graphics.setColor(beads[row][col]);
					graphics.fillOval(col * (UNIT_SQARE + UNIT_GAP) + UNIT_EDGE + UNIT_SQARE / 2, row * (UNIT_SQARE + UNIT_GAP) + hGap, UNIT_SQARE, UNIT_SQARE);
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
		tmp.addAttribute(new Attribute("mirrorH", Boolean.toString(mirrorYarnHCheckbox.isSelected())));
		tmp.addAttribute(new Attribute("mirrorV", Boolean.toString(mirrorYarnVCheckbox.isSelected())));
		for (int row = 0; row < yarn.length && (repeatYarnV == 0 || row < repeatYarnV); ++row) {
			for (int col = 0; col < yarn[row].length && (repeatYarnH == 0 || col < repeatYarnH); ++col) {
				if (yarn[row][col] != null) {
					pos = new Element("Pos");
					pos.addAttribute(new Attribute("row", "" + row));
					pos.addAttribute(new Attribute("col", "" + col));
					color = new Element("Color");
					color.addAttribute(new Attribute("red", "" + yarn[row][col].getRed()));
					color.addAttribute(new Attribute("green", "" + yarn[row][col].getGreen()));
					color.addAttribute(new Attribute("blue", "" + yarn[row][col].getBlue()));
					pos.appendChild(color);
					tmp.appendChild(pos);
				}
			}
		}
		root.appendChild(tmp);

		tmp = new Element("Beads");
		tmp.addAttribute(new Attribute("repeatH", "" + repeatBeadsH));
		tmp.addAttribute(new Attribute("repeatV", "" + repeatBeadsV));
		tmp.addAttribute(new Attribute("mirrorH", Boolean.toString(mirrorBeadsHCheckbox.isSelected())));
		tmp.addAttribute(new Attribute("mirrorV", Boolean.toString(mirrorBeadsVCheckbox.isSelected())));
		for (int row = 0; row < beads.length && (repeatBeadsV == 0 || row < repeatBeadsV); ++row) {
			for (int col = 0; col < beads[row].length && (repeatBeadsH == 0 || col < repeatBeadsH); ++col) {
				if (beads[row][col] != null) {
					pos = new Element("Pos");
					pos.addAttribute(new Attribute("row", "" + row));
					pos.addAttribute(new Attribute("col", "" + col));
					color = new Element("Color");
					color.addAttribute(new Attribute("red", "" + beads[row][col].getRed()));
					color.addAttribute(new Attribute("green", "" + beads[row][col].getGreen()));
					color.addAttribute(new Attribute("blue", "" + beads[row][col].getBlue()));
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
		Element tmp, pos, color;
		int row, col, red, green, blue;
		
		name.setText(root.getAttributeValue("name"));
		worms = Integer.parseInt(root.getAttributeValue("worms"));
		rows = Integer.parseInt(root.getAttributeValue("rows"));
		
		yarn = new Color[rows][worms];
		beads = new Color[rows][worms-1];

		tmp = root.getFirstChildElement("Yarn");
		repeatYarnH = Integer.parseInt(tmp.getAttributeValue("repeatH"));
		repeatYarnV = Integer.parseInt(tmp.getAttributeValue("repeatV"));
		mirrorYarnHCheckbox.setSelected(Boolean.parseBoolean(tmp.getAttributeValue("mirrorH")));
		mirrorYarnVCheckbox.setSelected(Boolean.parseBoolean(tmp.getAttributeValue("mirrorV")));
		elements = tmp.getChildElements("Pos");
		for (int i = 0; i < elements.size(); ++i) {
				pos = elements.get(i);
				row = Integer.parseInt(pos.getAttributeValue("row"));
				col = Integer.parseInt(pos.getAttributeValue("col"));
				color = pos.getFirstChildElement("Color");
				red = Integer.parseInt(color.getAttributeValue("red"));
				green = Integer.parseInt(color.getAttributeValue("green"));
				blue = Integer.parseInt(color.getAttributeValue("blue"));
				yarn[row][col] = new Color(red, green, blue);
		}

		tmp = root.getFirstChildElement("Beads");
		repeatBeadsH = Integer.parseInt(tmp.getAttributeValue("repeatH"));
		repeatBeadsV = Integer.parseInt(tmp.getAttributeValue("repeatV"));
		mirrorBeadsHCheckbox.setSelected(Boolean.parseBoolean(tmp.getAttributeValue("mirrorH")));
		mirrorBeadsVCheckbox.setSelected(Boolean.parseBoolean(tmp.getAttributeValue("mirrorV")));
		elements = tmp.getChildElements("Pos");
		for (int i = 0; i < elements.size(); ++i) {
				pos = elements.get(i);
				row = Integer.parseInt(pos.getAttributeValue("row"));
				col = Integer.parseInt(pos.getAttributeValue("col"));
				color = pos.getFirstChildElement("Color");
				red = Integer.parseInt(color.getAttributeValue("red"));
				green = Integer.parseInt(color.getAttributeValue("green"));
				blue = Integer.parseInt(color.getAttributeValue("blue"));
				beads[row][col] = new Color(red, green, blue);
		}
		designSizeChanged();
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
        widthButton = new javax.swing.JButton();
        heightButton = new javax.swing.JButton();
        repeatYarnHButton = new javax.swing.JButton();
        repeatYarnVButton = new javax.swing.JButton();
        mirrorYarnHCheckbox = new javax.swing.JCheckBox();
        mirrorYarnVCheckbox = new javax.swing.JCheckBox();
        repeatBeadsHButton = new javax.swing.JButton();
        repeatBeadsVButton = new javax.swing.JButton();
        mirrorBeadsHCheckbox = new javax.swing.JCheckBox();
        mirrorBeadsVCheckbox = new javax.swing.JCheckBox();
        getBeadPatternButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new net.sleepyduck.WrapLayout());

        name.setText("Name");
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameKeyTyped(evt);
            }
        });
        add(name);

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

        mirrorYarnHCheckbox.setText("Mirror Yarn H");
        mirrorYarnHCheckbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mirrorYarnHCheckboxMouseClicked(evt);
            }
        });
        add(mirrorYarnHCheckbox);

        mirrorYarnVCheckbox.setText("Mirror Yarn V");
        mirrorYarnVCheckbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mirrorYarnVCheckboxMouseClicked(evt);
            }
        });
        add(mirrorYarnVCheckbox);

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

        mirrorBeadsHCheckbox.setText("Mirror Beads H");
        mirrorBeadsHCheckbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mirrorBeadsHCheckboxMouseClicked(evt);
            }
        });
        add(mirrorBeadsHCheckbox);

        mirrorBeadsVCheckbox.setText("Mirror Beads V");
        mirrorBeadsVCheckbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mirrorBeadsVCheckboxMouseClicked(evt);
            }
        });
        add(mirrorBeadsVCheckbox);

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
			worms = (Integer) sop.getInputValue();
		}
		designSizeChanged();
    }//GEN-LAST:event_widthButtonMouseClicked

    private void heightButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_heightButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Height", 15, 70, rows);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			rows = (Integer) sop.getInputValue();
		}
		designSizeChanged();
    }//GEN-LAST:event_heightButtonMouseClicked

    private void repeatYarnHButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatYarnHButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat H", 0, 70, repeatYarnH);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			repeatYarnH = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatYarnHButtonMouseClicked

    private void repeatYarnVButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatYarnVButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat V", 0, 70, repeatYarnV);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			repeatYarnV = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatYarnVButtonMouseClicked

    private void nameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyTyped
		if (Character.isAlphabetic(evt.getKeyChar()) || Character.isDigit(evt.getKeyChar()) || evt.getKeyChar() == ' ') {
			updateLayout();
			revalidate();
		} else {
			evt.consume();
		}
    }//GEN-LAST:event_nameKeyTyped

    private void mirrorYarnHCheckboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mirrorYarnHCheckboxMouseClicked
		designRepeatChanged();
    }//GEN-LAST:event_mirrorYarnHCheckboxMouseClicked

    private void mirrorYarnVCheckboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mirrorYarnVCheckboxMouseClicked
		designRepeatChanged();
    }//GEN-LAST:event_mirrorYarnVCheckboxMouseClicked

    private void repeatBeadsHButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatBeadsHButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat H", 0, 70, repeatBeadsH);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			repeatBeadsH = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatBeadsHButtonMouseClicked

    private void repeatBeadsVButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_repeatBeadsVButtonMouseClicked
		JDialog dlg = sop.createSliderDialog(this, "Repeat V", 0, 70, repeatBeadsV);
		dlg.setVisible(true);
		if (sop.getValue() == SliderOptionPane.OK_OPTION && sop.getInputValue() instanceof Integer) {
			repeatBeadsV = (Integer) sop.getInputValue();
		}
		designRepeatChanged();
    }//GEN-LAST:event_repeatBeadsVButtonMouseClicked

    private void mirrorBeadsHCheckboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mirrorBeadsHCheckboxMouseClicked
		designRepeatChanged();
    }//GEN-LAST:event_mirrorBeadsHCheckboxMouseClicked

    private void mirrorBeadsVCheckboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mirrorBeadsVCheckboxMouseClicked
		designRepeatChanged();
    }//GEN-LAST:event_mirrorBeadsVCheckboxMouseClicked

    private void getBeadPatternButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_getBeadPatternButtonMouseClicked
		LinkedList<BeadCount> list = new LinkedList<>();
		for (int row = 0; row < beads.length; ++row) {
			for (int worm = beads[row].length - 1; worm >= 0; --worm) {
				if (beads[row][worm] != null) {
					if (list.peekLast() != null && list.peekLast().color == beads[row][worm]) {
						list.getLast().count++;
					} else {
						list.add(new BeadCount(beads[row][worm], colors.geColorName(beads[row][worm])));
					}
				}
			}
		}
		BeadsCheckForm checkForm = new BeadsCheckForm(list);
		checkForm.setVisible(true);
    }//GEN-LAST:event_getBeadPatternButtonMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton getBeadPatternButton;
    private javax.swing.JButton heightButton;
    private javax.swing.JCheckBox mirrorBeadsHCheckbox;
    private javax.swing.JCheckBox mirrorBeadsVCheckbox;
    private javax.swing.JCheckBox mirrorYarnHCheckbox;
    private javax.swing.JCheckBox mirrorYarnVCheckbox;
    private javax.swing.JTextField name;
    private javax.swing.JButton repeatBeadsHButton;
    private javax.swing.JButton repeatBeadsVButton;
    private javax.swing.JButton repeatYarnHButton;
    private javax.swing.JButton repeatYarnVButton;
    private javax.swing.JButton widthButton;
    // End of variables declaration//GEN-END:variables
}
