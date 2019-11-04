package com.raylee.tryjd.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raylee.tryjd.utils.HttpRequestUtils;

@Component
public class CookiesJFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	@Autowired
	ReptileJFrame reptileJFrame;

	private JLabel label = new JLabel("Cookies:");
	private JTextArea textArea = new JTextArea();
	private JButton button = new JButton("提交");

	public void init() {
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().add(label, BorderLayout.NORTH);
		this.getContentPane().add(textArea, BorderLayout.CENTER);
		this.getContentPane().add(button, BorderLayout.SOUTH);

		textArea.setLineWrap(true);

		button.addActionListener(this);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cookies = textArea.getText();
		if (StringUtils.isEmpty(cookies)) {
			JOptionPane.showMessageDialog(null, "cookies不能为空!", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		HttpRequestUtils.setCookies(cookies);
		this.setVisible(false);
		reptileJFrame.init();
	}

}
