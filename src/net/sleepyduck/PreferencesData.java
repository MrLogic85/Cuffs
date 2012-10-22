/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sleepyduck;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
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
public class PreferencesData {

	public static int CUFF_COLOR_SIZE = 10;
	public static int CUFF_COLOR_SIZE_EDIT = 10;
	public static int CUFF_COLOR_GAP = 1;
	public static int CUFF_EDGE = 5;
	public static int PALETTE_BORDER_THICKNESS = 2;
	public static int PALETTE_COLOR_SIZE = 12;
	public static final int CUFF_COLOR_SIZE_DEFAULT = 10;
	public static final int CUFF_COLOR_SIZE_EDIT_DEFAULT = 10;
	public static final int CUFF_COLOR_GAP_DEFAULT = 1;
	public static final int CUFF_EDGE_DEFAULT = 5;
	public static final int PALETTE_BORDER_THICKNESS_DEFAULT = 2;
	public static final int PALETTE_COLOR_SIZE_DEFAULT = 12;

	private PreferencesData() {
	}

	public static void load() {
		try {
			File file = new File("preferences.ini");
			if (file.exists()) {
				Builder parser = new Builder();
				FileInputStream fis = new FileInputStream(file);
				Document doc = parser.build(fis);

				Element root = doc.getRootElement();
				CUFF_COLOR_SIZE = Integer.parseInt(root.getAttributeValue("CUFF_COLOR_SIZE"));
				CUFF_COLOR_SIZE_EDIT = Integer.parseInt(root.getAttributeValue("CUFF_COLOR_SIZE_EDIT"));
				CUFF_COLOR_GAP = Integer.parseInt(root.getAttributeValue("CUFF_COLOR_GAP"));
				CUFF_EDGE = Integer.parseInt(root.getAttributeValue("CUFF_EDGE"));
				PALETTE_BORDER_THICKNESS = Integer.parseInt(root.getAttributeValue("PALETTE_BORDER_THICKNESS"));
				PALETTE_COLOR_SIZE = Integer.parseInt(root.getAttributeValue("PALETTE_COLOR_SIZE"));
			}
		} catch (ParsingException | IOException ex) {
			Logger.getLogger(CuffMain.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void save() {
			Element root = new Element("Preferences");
			root.addAttribute(new Attribute("CUFF_COLOR_SIZE", "" + CUFF_COLOR_SIZE));
			root.addAttribute(new Attribute("CUFF_COLOR_SIZE_EDIT", "" + CUFF_COLOR_SIZE_EDIT));
			root.addAttribute(new Attribute("CUFF_COLOR_GAP", "" + CUFF_COLOR_GAP));
			root.addAttribute(new Attribute("CUFF_EDGE", "" + CUFF_EDGE));
			root.addAttribute(new Attribute("PALETTE_BORDER_THICKNESS", "" + PALETTE_BORDER_THICKNESS));
			root.addAttribute(new Attribute("PALETTE_COLOR_SIZE", "" + PALETTE_COLOR_SIZE));
			try {
				Document doc = new Document(root);
				File file = new File("preferences.ini");
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
