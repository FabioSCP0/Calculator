package com.github.FabioSCP0.Calculator.vision;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.FabioSCP0.Calculator.model.Memory;
import com.github.FabioSCP0.Calculator.model.MemoryObserver;

@SuppressWarnings("serial")
public class Display extends JPanel implements MemoryObserver{
	private final JLabel label;
	public Display() {
		Memory.getInstance().addObserver(this);
		
		setBackground(new Color(46,49,50));
		label = new JLabel(Memory.getInstance().getText());
		label.setForeground(Color.WHITE);
		label.setFont(new Font("courier",Font.PLAIN,30));
		setLayout(new FlowLayout(FlowLayout.RIGHT,10,25));
		add(label);
	}
	@Override
	public void changedValue(String newValue) {
		label.setText(newValue);
		
	}
}
