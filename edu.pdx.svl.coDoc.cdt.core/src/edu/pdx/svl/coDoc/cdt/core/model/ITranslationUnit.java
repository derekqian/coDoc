/*******************************************************************************
 * Copyright (c) 2000, 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package edu.pdx.svl.coDoc.cdt.core.model;

import edu.pdx.svl.coDoc.cdt.internal.core.model.IBufferFactory;
import org.eclipse.core.runtime.IProgressMonitor;
/**
 * Represents an entire C translation unit (<code>.c</code> source file).
 * The children are of type <code>IStructureElement</code>,
 * <code>IInclude</code>, etc..
 * and appear in the order in which they are declared in the source.
 * If a <code>.c</code> file cannot be parsed, its structure remains unknown.
 * Use <code>ICElement.isStructureKnown</code> to determine whether this is 
 * the case.
 */
public interface ITranslationUnit extends ICElement, IParent, IOpenable,
  ISourceReference, ISourceManipulation {  

	/**
	 * Finds the shared working copy for this element, given a <code>IBuffer</code> factory. 
	 * If no working copy has been created for this element associated with this
	 * buffer factory, returns <code>null</code>.
	 * <p>
	 * Users of this method must not destroy the resulting working copy. 
	 * 
	 * @param bufferFactory the given <code>IBuffer</code> factory
	 * @return the found shared working copy for this element, <code>null</code> if none
	 * @see IBufferFactory
	 * @since 2.0
	 */
	IWorkingCopy findSharedWorkingCopy(IBufferFactory bufferFactory);

	/**
	 * Returns the contents of a translation unit as a char[]
	 * @return char[]
	 */
	char[] getContents();

	/**
	 * Returns the smallest element within this translation unit that 
	 * includes the given source position (that is, a method, field, etc.), or
	 * <code>null</code> if there is no element other than the translation
	 * unit itself at the given position, or if the given position is not
	 * within the source range of this translation unit.
	 *
	 * @param line a position inside the translation unit
	 * @return the innermost C element enclosing a given source position or <code>null</code>
	 *	if none (excluding the translation unit).
	 * @exception CModelException if the translation unit does not exist or if an
	 *		exception occurs while accessing its corresponding resource
	 */
	ICElement getElementAtLine(int line) throws CModelException;

	/**
	 * Returns the smallest element within this translation unit that 
	 * includes the given source position (that is, a method, field, etc.), or
	 * <code>null</code> if there is no element other than the translation
	 * unit itself at the given position, or if the given position is not
	 * within the source range of this translation unit.
	 *
	 * @param position a source position inside the translation unit
	 * @return the innermost C element enclosing a given source position or <code>null</code>
	 *	if none (excluding the translation unit).
	 * @exception CModelException if the translation unit does not exist or if an
	 *		exception occurs while accessing its corresponding resource
	 */
	ICElement getElementAtOffset(int offset) throws CModelException;

	/**
	 * Returns the elements within this translation unit that 
	 * includes the given source position (that is, a method, field, etc.), or
	 * an empty array if there are no elements other than the translation
	 * unit itself at the given position, or if the given position is not
	 * within the source range of this translation unit.
	 * You have this behavior when at expansion of a macro.
	 *
	 * @param position a source position inside the translation unit
	 * @return the innermost C element enclosing a given source position or <code>null</code>
	 *	if none (excluding the translation unit).
	 * @exception CModelException if the translation unit does not exist or if an
	 *		exception occurs while accessing its corresponding resource
	 */
	ICElement[] getElementsAtOffset(int offset) throws CModelException;

	ICElement getElement(String name) throws CModelException;

	/**
	 * Returns the include declaration in this translation unit with the given name.
	 *
	 * @param the name of the include to find (For example: <code>"stdio.h"</code> 
	 * 	or <code>"sys/types.h"</code>)
	 * @return a handle onto the corresponding include declaration. The include declaration may or may not exist.
	 */
	IInclude getInclude(String name);

	/**
	 * Returns the include declarations in this translation unit
	 * in the order in which they appear in the source.
	 *
	 * @exception CModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource
	 */
	IInclude[] getIncludes() throws CModelException;

	/**
	 * Returns a shared working copy on this element using the given factory to create
	 * the buffer, or this element if this element is already a working copy.
	 * This API can only answer an already existing working copy if it is based on the same
	 * original translation unit AND was using the same buffer factory (i.e. as
	 * defined by <code>Object#equals</code>).
	 * <p>
	 * The life time of a shared working copy is as follows:
	 * <ul>
	 * <li>The first call to <code>getSharedWorkingCopy(...)</code> creates a new working copy for this
	 *     element</li>
	 * <li>Subsequent calls increment an internal counter.</li>
	 * <li>A call to <code>destroy()</code> decrements the internal counter.</li>
	 * <li>When this counter is 0, the working copy is destroyed.
	 * </ul>
	 * So users of this method must destroy exactly once the working copy.
	 * <p>
	 * Note that the buffer factory will be used for the life time of this working copy, i.e. if the 
	 * working copy is closed then reopened, this factory will be used.
	 * The buffer will be automatically initialized with the original's compilation unit content
	 * upon creation.
	 * <p>
	 * When the shared working copy instance is created, an ADDED ICElementDelta is reported on this
	 * working copy.
	 *
	 * @param monitor a progress monitor used to report progress while opening this compilation unit
	 *                 or <code>null</code> if no progress should be reported 
	 * @param factory the factory that creates a buffer that is used to get the content of the working copy
	 *                 or <code>null</code> if the internal factory should be used
	 * @param problemRequestor a requestor which will get notified of problems detected during
	 * 	reconciling as they are discovered. The requestor can be set to <code>null</code> indicating
	 * 	that the client is not interested in problems.
	 * @exception CModelException if the contents of this element can   not be
	 * determined. Reasons include:
	 * <ul>
	 * <li> This C element does not exist (ELEMENT_DOES_NOT_EXIST)</li>
	 * </ul>
	 * @return a shared working copy on this element using the given factory to create
	 * the buffer, or this element if this element is already a working copy
	 * @see IBufferFactory
	 * @see IProblemRequestor
	 * @since 2.0
	 */
	
	IWorkingCopy getSharedWorkingCopy(IProgressMonitor monitor,	IBufferFactory factory) throws CModelException;

	/**
	 * Returns the first using in this translation unit with the name
	 * This is a handle-only method. The namespace declaration may or may not exist.
	 *
	 * @param name the name of the namespace declaration (For example, <code>"std"</code>)
	 */
	IUsing getUsing(String name);

	/**
	 * Returns the usings in this translation unit
	 * in the order in which they appear in the source.
	 *
	 * @return an array of namespace declaration (normally of size one)
	 *
	 * @exception CModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource
	 */
	IUsing[] getUsings() throws CModelException;

	/**
	 * Returns the first namespace declaration in this translation unit with the given name
	 * This is a handle-only method. The namespace declaration may or may not exist.
	 *
	 * @param name the name of the namespace declaration (For example, <code>"std"</code>)
	 */
	INamespace getNamespace(String name);

	/**
	 * Returns the namespace declarations in this translation unit
	 * in the order in which they appear in the source.
	 *
	 * @return an array of namespace declaration (normally of size one)
	 *
	 * @exception CModelException if this element does not exist or if an
	 *		exception occurs while accessing its corresponding resource
	 */
	INamespace[] getNamespaces() throws CModelException;

	/**
	 * True if its a header.
	 * @return boolean
	 */
	boolean isHeaderUnit();

	/**
	 * True it is a source file.
	 * @return boolean
	 */
	boolean isSourceUnit();

	/**
	 * True if the code is C
	 * @return
	 */
	boolean isCLanguage();

	/**
	 * True if the code is C++
	 * 
	 * @return
	 */
	boolean isCXXLanguage();

	/**
	 * Returns a new working copy for the Translation Unit.
	 * @return IWorkingCopy
	 */
	IWorkingCopy getWorkingCopy() throws CModelException;

	/**
	 * Returns a new working copy for the Translation Unit.
	 * @return IWorkingCopy
	 */
	IWorkingCopy getWorkingCopy(IProgressMonitor monitor, IBufferFactory factory) throws CModelException;

	/**
	 * Return the contentType id for this file.
	 * @return String - contentType id
	 */
	String getContentTypeId();

	/**
	 * Checks if this is a working copy.
	 * @return boolean
	 */
	boolean isWorkingCopy();
	
	/**
	 * Used by contributed languages' model builders to indicate whether or
	 * not the parse of a translation unit was successful.
	 * 
	 * @param wasSuccessful
	 * 
	 */
	public void setIsStructureKnown(boolean wasSuccessful);
}

