package adql.query.operand.function.geometry;

/*
 * This file is part of ADQLLibrary.
 * 
 * ADQLLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ADQLLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ADQLLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2012 - UDS/Centre de Données astronomiques de Strasbourg (CDS)
 */

import adql.query.ADQLObject;

import adql.query.operand.ADQLOperand;
import adql.query.operand.ADQLColumn;

/**
 * <p>It represents the CONTAINS function of the ADQL language.</p>
 * 
 * <p>This numeric function determines if a geometry is wholly contained within another.
 * This is most commonly used to express the "point-in-shape" condition.</p>
 * 
 * <p><i><u>Example:</u><br />
 * CONTAINS(POINT('ICRS GEOCENTER', 25.0, -19.5), CIRCLE('ICRS GEOCENTER', 25.4, -20.0, 1)) = 1<br />
 * In this example the function determines if the point (25.0,-19.5) is within a circle of
 * one degree radius centered in a position of (25.4,-20.0).</i></p>
 * 
 * <p><b><u>Warning:</u>
 * <ul><li>The CONTAINS function returns 1 (true) if the first argument is in or on the boundary of the circle and 0 (false) otherwise.</li>
 * <li>Since the two argument geometries may be expressed in different coordinate systems, the function is responsible for converting one (or both).
 * If it can not do so, it SHOULD throw an error message, to be defined by the service making use of ADQL.</li></ul>
 * </b></p>
 * 
 * @author Gr&eacute;gory Mantelet (CDS)
 * @version 06/2011
 */
public class ContainsFunction extends GeometryFunction {

	/** The first geometry. */
	private GeometryValue<GeometryFunction> leftParam;

	/** The second geometry. */
	private GeometryValue<GeometryFunction> rightParam;

	/**
	 * Builds a CONTAINS function.
	 * 
	 * @param left					Its first geometry (the one which must be included the second).
	 * @param right					Its second geometry (the one which must include the first).
	 * @throws NullPointerException	If one parameter is <i>null</i>.
	 */
	public ContainsFunction(GeometryValue<GeometryFunction> left, GeometryValue<GeometryFunction> right) throws NullPointerException{
		super();
		if (left == null || right == null)
			throw new NullPointerException("A CONTAINS function must have two parameters different from NULL !");

		leftParam = left;
		rightParam = right;
	}

	/**
	 * Builds a CONTAINS function by copying the given one.
	 * 
	 * @param toCopy		The CONTAINS function to copy.
	 * @throws Exception	If there is an error during the copy.
	 */
	@SuppressWarnings("unchecked")
	public ContainsFunction(ContainsFunction toCopy) throws Exception{
		super();
		leftParam = (GeometryValue<GeometryFunction>)(toCopy.leftParam.getCopy());
		rightParam = (GeometryValue<GeometryFunction>)(toCopy.rightParam.getCopy());
	}

	public ADQLObject getCopy() throws Exception{
		return new ContainsFunction(this);
	}

	public String getName(){
		return "CONTAINS";
	}

	public boolean isNumeric(){
		return true;
	}

	public boolean isString(){
		return false;
	}

	/**
	 * @return The leftParam.
	 */
	public final GeometryValue<GeometryFunction> getLeftParam(){
		return leftParam;
	}

	/**
	 * @param leftParam The leftParam to set.
	 */
	public final void setLeftParam(GeometryValue<GeometryFunction> leftParam){
		if (leftParam != null)
			this.leftParam = leftParam;
	}

	/**
	 * @return The rightParam.
	 */
	public final GeometryValue<GeometryFunction> getRightParam(){
		return rightParam;
	}

	/**
	 * @param rightParam The rightParam to set.
	 */
	public final void setRightParam(GeometryValue<GeometryFunction> rightParam){
		if (rightParam != null)
			this.rightParam = rightParam;
	}

	@Override
	public ADQLOperand[] getParameters(){
		return new ADQLOperand[]{leftParam,rightParam};
	}

	@Override
	public int getNbParameters(){
		return 2;
	}

	@Override
	public ADQLOperand getParameter(int index) throws ArrayIndexOutOfBoundsException{
		if (index == 0)
			return leftParam.getValue();
		else if (index == 1)
			return rightParam.getValue();
		else
			throw new ArrayIndexOutOfBoundsException("No " + index + "-th parameter for the function \"" + getName() + "\" !");
	}

	@Override
	@SuppressWarnings("unchecked")
	public ADQLOperand setParameter(int index, ADQLOperand replacer) throws ArrayIndexOutOfBoundsException, NullPointerException, Exception{
		if (replacer == null)
			throw new NullPointerException("Impossible to remove one parameter from the " + getName() + " function !");
		else if (!(replacer instanceof GeometryValue || replacer instanceof ADQLColumn || replacer instanceof GeometryFunction))
			throw new Exception("Impossible to replace GeometryValue/Column/GeometryFunction by a " + replacer.getClass().getName() + " (" + replacer.toADQL() + ") !");

		ADQLOperand replaced = null;
		if (index == 0){
			replaced = leftParam;
			if (replacer instanceof GeometryValue)
				leftParam = (GeometryValue<GeometryFunction>)replacer;
			else if (replacer instanceof ADQLColumn)
				leftParam.setColumn((ADQLColumn)replacer);
			else if (replacer instanceof GeometryFunction)
				leftParam.setGeometry((GeometryFunction)replacer);
		}else if (index == 1){
			replaced = rightParam;
			if (replacer instanceof GeometryValue)
				rightParam = (GeometryValue<GeometryFunction>)replacer;
			else if (replacer instanceof ADQLColumn)
				rightParam.setColumn((ADQLColumn)replacer);
			else if (replacer instanceof GeometryFunction)
				rightParam.setGeometry((GeometryFunction)replacer);
		}else
			throw new ArrayIndexOutOfBoundsException("No " + index + "-th parameter for the function \"" + getName() + "\" !");
		return replaced;
	}

}