package edu.pdx.svl.coDoc.refexp.referencemodel;

import java.util.Vector;

public class FunctionReference extends Reference {
	protected String functionName;
	protected String returnType;
	protected Vector<Parameter> parameters = new Vector<Parameter>();

	public class Parameter {
		public String type;
		public String name;
	}

	public void addParameter(String type, String name) {
		Parameter p = new Parameter();
		p.type = type;
		p.name = name;
		parameters.add(p);
	}

	/**
	 * @return a string that is a list of all of the input parameters (Type1,
	 *         Type2...)
	 */
	public String getParameterString() {
		//TODO implement
		return null;
	}

	@Override
	public String getType() {
		return "Function";
	}

	@Override
	public String description() {
		return null;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Vector<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(Vector<Parameter> parameters) {
		this.parameters = parameters;
	}
}
