package com.raylee.tryjd.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raylee.tryjd.service.TryJDService;
import com.raylee.tryjd.thread.ReptileThread;

@Component
public class ReptileJFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	@Autowired
	private TryJDService tryJDService;

	@Autowired
	private ReptileThread reptileThread;

	private JButton button = new JButton("开始");
	private JProgressBar progressBar = new JProgressBar();

	public void init() {
		this.setSize(400, 80);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		progressBar.setVisible(false);
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(true);
		progressBar.setString("加载中...");

		System.out.println("---------------CIDS:" + tryJDService.getCIds().toString());
		System.out.println("---------------TYPES:" + tryJDService.getTypes().toString());
		System.out.println("---------------STATES:" + tryJDService.getStates().toString());

		this.getContentPane().add(button, BorderLayout.NORTH);
		this.getContentPane().add(progressBar, BorderLayout.CENTER);

		button.addActionListener(this);
		this.setVisible(true);

		reptileThread.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (reptileThread.isSuspend()) {
			reptileThread.onPlay();
			button.setText("暂停");
			progressBar.setVisible(true);
		} else {
			reptileThread.onPause();
			button.setText("开始");
			progressBar.setVisible(false);
		}
	}
}
