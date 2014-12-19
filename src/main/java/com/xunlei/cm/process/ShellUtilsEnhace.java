package com.xunlei.cm.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ShellUtilsEnhace {
	
	public static final ExecuteResult executeShell(String cmd, OutputStream out)
			throws IOException, InterruptedException {
		if (cmd == null || cmd.trim().length() == 0) {
			return null;
		}
		if (out != null) {
			out.write(cmd.getBytes());
			out.write("\r\n".getBytes());
		}
		Process process = Runtime.getRuntime().exec(cmd);
		return executeInner(process,out);
	}
	
	
	public static final ExecuteResult executeShell(String[] cmds, OutputStream out)
			throws IOException, InterruptedException {
		Process process = Runtime.getRuntime().exec(cmds);
		return executeInner(process,out);
	}

	private static ExecuteResult executeInner(Process process,OutputStream out)
			throws IOException, InterruptedException {
		StreamGobber error = new StreamGobber(process.getErrorStream(),
				"error", out);
		StreamGobber output = new StreamGobber(process.getInputStream(),
				"output", out);
		error.start();
		output.start();
		int exitValue = process.waitFor();
		ExecuteResult executeResult = new ExecuteResult(exitValue, output.msg, error.msg);
		return executeResult;
	}
	
	public static class ExecuteResult {
		private int code ;
		private List<String> outputList;
		private List<String> errorList;
		public ExecuteResult(int code, List<String> outputList,
				List<String> errorList) {
			this.code = code;
			this.outputList = outputList;
			this.errorList = errorList;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public List<String> getOutputList() {
			return outputList;
		}
		public void setOutputList(List<String> outputList) {
			this.outputList = outputList;
		}
		public List<String> getErrorList() {
			return errorList;
		}
		public void setErrorList(List<String> errorList) {
			this.errorList = errorList;
		}
	}
	

	static class StreamGobber extends Thread {
		List<String> msg = new ArrayList<String>();
		InputStream ins;

		String type;

		OutputStream out;

		StreamGobber(InputStream ins, String type) {
			this(ins, type, null);
		}

		StreamGobber(InputStream ins, String type, OutputStream redirect) {
			this.ins = ins;
			this.type = type;
			this.out = redirect;
		}

		public void run() {
			try {
				PrintWriter writer = null;
				if (out != null) {
					writer = new PrintWriter(out);
				}
				InputStreamReader isr = new InputStreamReader(ins);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					if (writer != null) {
						writer.println(line);
						writer.flush();
					}
					msg.add(line);
				}
				if (writer != null) {
					writer.flush();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
