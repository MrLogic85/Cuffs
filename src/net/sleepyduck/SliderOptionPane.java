/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author MetcalfPriv
 */
public class SliderOptionPane extends JOptionPane implements ChangeListener {
	private MySlider slider;

	public SliderOptionPane() {
		super();
		slider = new MySlider();
		slider.addChangeListener(this);
	}

	public JDialog createSliderDialog(Component parent, String title, int min, int max, int defaultValue) {
		slider.setMinimum(min);
		slider.setMaximum(max);
		slider.setValue(defaultValue);
		setMessage(new Object[]{"Select a value: ", slider});
		setMessageType(JOptionPane.PLAIN_MESSAGE);
		setOptionType(JOptionPane.OK_CANCEL_OPTION);
		return createDialog(parent, title);
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		JSlider theSlider = (JSlider) ce.getSource();
		if (!theSlider.getValueIsAdjusting()) {
			setInputValue(new Integer(theSlider.getValue()));
		}
	}
}
