/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import nu.xom.Element;

/**
 *
 * @author MetcalfPriv
 */
public class CuffState {
	public Cuff cuff;
	public Element state;

	public CuffState(Cuff cuff, Element state) {
		this.cuff = cuff;
		this.state = state;
	}
}
