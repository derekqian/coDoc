/**
 * 
 */
package edu.pdx.svl.coDoc.cdt.core.dom;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author dschaefer
 * 
 */
public interface IPDOMIndexerTask {

	/**
	 * Run the sub job progress to the main job.
	 * 
	 * @param mainJob
	 */
	public void run(IProgressMonitor monitor);

	public IPDOMIndexer getIndexer();

}
