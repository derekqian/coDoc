package edu.pdx.svl.coDoc.cdt.internal.ui.text.util;

/*******************************************************************************
 * Copyright (c) 2000 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *******************************************************************************/

import edu.pdx.svl.coDoc.cdt.internal.ui.text.ICColorConstants;
import edu.pdx.svl.coDoc.cdt.internal.ui.text.IColorManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Java color manager.
 */
public class CColorManager implements IColorManager {

	protected Map fKeyTable = new HashMap(10);

	protected Map fDisplayTable = new HashMap(2);

	public CColorManager() {
		bindColor(ICColorConstants.C_MULTI_LINE_COMMENT, new RGB(63, 127, 95));
		bindColor(ICColorConstants.C_SINGLE_LINE_COMMENT, new RGB(63, 127, 95));
		bindColor(ICColorConstants.C_KEYWORD, new RGB(127, 0, 85));
		bindColor(ICColorConstants.C_TYPE, new RGB(127, 0, 85));
		bindColor(ICColorConstants.C_STRING, new RGB(42, 0, 255));
		bindColor(ICColorConstants.C_OPERATOR, new RGB(0, 0, 0));
		bindColor(ICColorConstants.C_BRACES, new RGB(0, 0, 0));
		bindColor(ICColorConstants.C_NUMBER, new RGB(0, 0, 0));
		bindColor(ICColorConstants.C_DEFAULT, new RGB(0, 0, 0));
	}

	protected void dispose(Display display) {
		Map colorTable = (Map) fDisplayTable.get(display);
		if (colorTable != null) {
			Iterator e = colorTable.values().iterator();
			while (e.hasNext())
				((Color) e.next()).dispose();
		}
	}

	/*
	 * @see IColorManager#getColor(RGB)
	 */
	public Color getColor(RGB rgb) {

		if (rgb == null)
			return null;

		final Display display = Display.getCurrent();
		Map colorTable = (Map) fDisplayTable.get(display);
		if (colorTable == null) {
			colorTable = new HashMap(10);
			fDisplayTable.put(display, colorTable);
			display.disposeExec(new Runnable() {
				public void run() {
					dispose(display);
				}
			});
		}

		Color color = (Color) colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}

		return color;
	}

	/*
	 * @see IColorManager#dispose
	 */
	public void dispose() {
		dispose(Display.getCurrent());
	}

	/*
	 * @see IColorManager#getColor(String)
	 */
	public Color getColor(String key) {

		if (key == null)
			return null;

		RGB rgb = (RGB) fKeyTable.get(key);
		return getColor(rgb);
	}

	/*
	 * @see IColorManagerExtension#bindColor(String, RGB)
	 */
	public void bindColor(String key, RGB rgb) {
		Object value = fKeyTable.get(key);
		if (value != null)
			throw new UnsupportedOperationException();

		fKeyTable.put(key, rgb);
	}

	/*
	 * @see IColorManagerExtension#unbindColor(String)
	 */
	public void unbindColor(String key) {
		fKeyTable.remove(key);
	}
}
