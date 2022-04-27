package com.github.FabioSCP0.Calculator.model;

import java.util.ArrayList;
import java.util.List;

public class Memory {
	
	private enum CommandType{
		CLEAR, SIGNAL, NUMBER, DIV, MULT, SUB, SUM, EQUAL, COMMA 
	};
	
	private static final Memory instance = new Memory();
	private final List<MemoryObserver> observers = new ArrayList<>();
	
	private CommandType lastOperation = null;
	private boolean replace = false;
	private String text = "";
	private String textBuffer = "";
	
	private Memory() {}
	
	public static Memory getInstance() {
		return instance;
	}
	
	public String getText() {
		if(text.isEmpty()) {
			return "0";
		}
		return text;
	}
	
	public void addObserver(MemoryObserver observer) {
		observers.add(observer);
	}

	public void comand(String value) {
		
		CommandType commandType = detectCommandType(value);
		
		if(commandType == null) return;
		else if(commandType == CommandType.CLEAR) {
			text = "";
			textBuffer = "";
			replace = false;
			lastOperation = null;
			}
		else if(commandType == CommandType.SIGNAL && !text.equals("0")) {
			if(text.contains("-")) text = text.substring(1);
			else text = "-" + text;
		}
		else if((commandType == CommandType.NUMBER)||(commandType == CommandType.COMMA)) {
			if(replace) text = value;
			else text += value;
			replace = false;
		}
		else {
			replace = true;
			text = getNumbersOperation();
			textBuffer = text;
			lastOperation = commandType;
		}
		
		observers.forEach(o -> o.changedValue(getText()));
	}

	private String getNumbersOperation() {
		if(lastOperation == null || lastOperation == CommandType.EQUAL) return text;
		double bufferNumber = Double.parseDouble(textBuffer.replace(",", "."));
		double textNumber = Double.parseDouble(text.replace(",", "."));
		double result = 0;
	
		if(lastOperation == CommandType.SUM) result = bufferNumber + textNumber;
		else if(lastOperation == CommandType.SUB) result = bufferNumber - textNumber;
		else if(lastOperation == CommandType.MULT) result = bufferNumber * textNumber;
		else if(lastOperation == CommandType.DIV) result = bufferNumber / textNumber;
		
		String textResult = Double.toString(result).replace(".", ",");
		boolean integer = textResult.endsWith(",0");
		return integer ? textResult.replace(",0", "") : textResult;
	}

	private CommandType detectCommandType(String value) {
		
		if(value.isEmpty() && value =="0") {
			return null;
		}
		
		try { 
			Integer.parseInt(value);
			return CommandType.NUMBER;
		} catch (NumberFormatException e) {
			if("AC".equals(value)) return CommandType.CLEAR;
			else if("+/-".equals(value) && !text.isEmpty()) return CommandType.SIGNAL;
			else if("/".equals(value)) return CommandType.DIV;
			else if("*".equals(value)) return CommandType.MULT;
			else if("+".equals(value)) return CommandType.SUM;
			else if("-".equals(value)) return CommandType.SUB;
			else if("=".equals(value)) return CommandType.EQUAL;
			else if(",".equals(value) && !text.contains(",")) return CommandType.COMMA;
		}
		return null;
	}
}
