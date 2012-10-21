/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import javax.swing.event.ChangeEvent;

/**
 *
 * @author MetcalfPriv
 */
public class DataChangeEvent extends ChangeEvent {

	private Object data;
	
	public DataChangeEvent(Object source, Object data) {
		super(source);
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
}
