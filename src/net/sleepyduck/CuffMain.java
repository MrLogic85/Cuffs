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
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.Serializer;

/**
 *
 * @author MetcalfPriv
 */
public class CuffMain extends javax.swing.JFrame {

	private JFileChooser fileChooser;

	/**
	 * Creates new form MuddarFrame
	 */
	public CuffMain() {
		initComponents();
		fileChooser = new JFileChooser();
		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(new FileNameExtensionFilter("XML", new String[]{"xml"}));
	}

	private void saveToFile(File theFileToSave, CuffTab cuffTab) {
		Element saveData = cuffTab.save();
		if (fileChooser.getSelectedFile() != null) {
			try {
				Document doc = new Document(saveData);
				Serializer serializer = new Serializer(new FileOutputStream(theFileToSave), "ISO-8859-1");
				serializer.setIndent(4);
				serializer.setMaxLength(64);
				serializer.write(doc);
			} catch (IOException ex) {
				Logger.getLogger(CuffMain.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPaneCuffs = new javax.swing.JTabbedPane();
        cuffTab1 = new net.sleepyduck.CuffTab();
        colors = new net.sleepyduck.Colors();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemCreateTab = new javax.swing.JMenuItem();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemSaveAs = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemCreateCuff = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 800));

        jTabbedPaneCuffs.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTabbedPaneCuffs.addTab("Tab 1", cuffTab1);

        jMenuFile.setText("File");

        jMenuItemCreateTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemCreateTab.setText("New");
        jMenuItemCreateTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCreateTabActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemCreateTab);

        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpen);

        jMenuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSave.setText("Save");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);

        jMenuItemSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSaveAs.setText("Save As");
        jMenuItemSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAsActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSaveAs);

        jMenuBar.add(jMenuFile);

        jMenuEdit.setText("Edit");

        jMenuItemCreateCuff.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemCreateCuff.setText("New Cuff");
        jMenuItemCreateCuff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCreateCuffActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemCreateCuff);

        jMenuBar.add(jMenuEdit);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPaneCuffs, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCuffs)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(569, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemCreateCuffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCreateCuffActionPerformed
		CuffTab muddTab = (CuffTab) jTabbedPaneCuffs.getSelectedComponent();
		if (muddTab != null) {
			muddTab.add(new Cuff(colors));
		}
    }//GEN-LAST:event_jMenuItemCreateCuffActionPerformed

    private void jMenuItemCreateTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCreateTabActionPerformed
		jTabbedPaneCuffs.addTab("Tab " + (jTabbedPaneCuffs.getTabCount() + 1), new CuffTab());
		jTabbedPaneCuffs.setSelectedIndex(jTabbedPaneCuffs.getComponentCount() - 1);
    }//GEN-LAST:event_jMenuItemCreateTabActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
		Component cmp = jTabbedPaneCuffs.getSelectedComponent();
		if (cmp != null && cmp instanceof CuffTab) {
			CuffTab cuffTab = (CuffTab) cmp;
			if (cuffTab.getFilePath() == null) {
				jMenuItemSaveAsActionPerformed(evt);
			} else {
				saveToFile(cuffTab.getFilePath().toFile(), cuffTab);
			}
		}
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsActionPerformed
		Component cmp = jTabbedPaneCuffs.getSelectedComponent();
		if (cmp != null && cmp instanceof CuffTab) {
			CuffTab cuffTab = (CuffTab) cmp;
			int option = fileChooser.showSaveDialog(this);
			if (option == JFileChooser.APPROVE_OPTION) {
				File theFileToSave = fileChooser.getSelectedFile();
				Path filePath = theFileToSave.toPath();
				if (!filePath.toString().endsWith(".xml")) {
					theFileToSave = new File(filePath + ".xml");
					filePath = theFileToSave.toPath();
				}
				saveToFile(theFileToSave, cuffTab);
				cuffTab.setFilePath(filePath);
				jTabbedPaneCuffs.setTitleAt(jTabbedPaneCuffs.getSelectedIndex(), filePath.getFileName().toString().split("\\.")[0]);
			}
		}
    }//GEN-LAST:event_jMenuItemSaveAsActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
		int option = fileChooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			try {
				Builder parser = new Builder();
				FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile());
				Document doc = parser.build(fis);
				Element root = doc.getRootElement();
				jTabbedPaneCuffs.addTab(fileChooser.getSelectedFile().toPath().getFileName().toString().split("\\.")[0], new CuffTab());
				jTabbedPaneCuffs.setSelectedIndex(jTabbedPaneCuffs.getComponentCount() - 1);
				((CuffTab)jTabbedPaneCuffs.getSelectedComponent()).load(root, colors);
			} catch (ParsingException | IOException ex) {
				Logger.getLogger(CuffMain.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(CuffMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(CuffMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(CuffMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(CuffMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CuffMain().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private net.sleepyduck.Colors colors;
    private net.sleepyduck.CuffTab cuffTab1;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemCreateCuff;
    private javax.swing.JMenuItem jMenuItemCreateTab;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemSaveAs;
    private javax.swing.JTabbedPane jTabbedPaneCuffs;
    // End of variables declaration//GEN-END:variables
}
