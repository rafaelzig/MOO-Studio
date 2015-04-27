package uk.co.blogspot.rafaelzig.gui;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * A subclass of JDialog which displays information regarding this application.
 *
 * @author Rafael da Silva Costa - BSc Computer Science 3rd Year
 */
class AboutWindow extends JDialog
{
	private static final long	serialVersionUID	= -7576386131682930786L;

	AboutWindow(JFrame owner)
	{
		super(owner);
		setIconImage(new ImageIcon(getClass().getResource("res/icon.png")).getImage());
		
		setTitle("About this Application");
		StringBuilder bdr = new StringBuilder();
		bdr.append("<html><center>MoO Studio v1.0.0 - By Rafael da Silva Costa<center>");
		bdr.append("Licensed under the GNU Lesser General Public License v3<br><br>");
		bdr.append("Additional libraries utilised:<br><br>");
		bdr.append("MOEA Framework v2.4<br>");
		bdr.append("Licensed under the GNU Lesser General Public License v3<br><br>");
		bdr.append("Javaluator v3.0.0<br>");
		bdr.append("Licensed under the GNU Lesser General Public License v3<br><br>");
		bdr.append("Google Gson v2.3.1<br>");
		bdr.append("Licensed under the Apache License v2<html>");
		JLabel lbl = new JLabel(bdr.toString(), JLabel.CENTER);
		lbl.setFont(new Font("Arial", Font.BOLD, 20));
		add(lbl);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(owner);
		setVisible(true);
	}
}
