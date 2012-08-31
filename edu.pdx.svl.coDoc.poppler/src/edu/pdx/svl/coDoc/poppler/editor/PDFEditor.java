package edu.pdx.svl.coDoc.poppler.editor;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.GZIPInputStream;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.INavigationLocationProvider;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;


import edu.pdx.svl.coDoc.poppler.editor.PDFFileOutline;
import edu.pdx.svl.coDoc.poppler.editor.PDFNavigationLocation;
import edu.pdx.svl.coDoc.poppler.editor.PDFPageViewer;
import edu.pdx.svl.coDoc.poppler.editor.StatusLinePageSelector;
import edu.pdx.svl.coDoc.poppler.editor.PDFPageViewer.IPDFEditor;
import edu.pdx.svl.coDoc.poppler.editor.StatusLinePageSelector.IPageChangeListener;
import edu.pdx.svl.coDoc.poppler.lib.OutlineNode;
import edu.pdx.svl.coDoc.poppler.lib.PDFDestination;
import edu.pdx.svl.coDoc.poppler.lib.PopplerJNI;


public class PDFEditor extends EditorPart implements IReusableEditor, IResourceChangeListener, INavigationLocationProvider, IPageChangeListener, IPDFEditor
{
	public static final String ID = "edu.pdx.svl.coDoc.poppler.editor.PDFEditor"; // editor id, plugin.xml
	public static final String CONTEXT_ID = "PDFViewer.editors.contextid"; // key binding, plugin.xml
	static final String PDFPOSITION_ID = "PDFPosition"; //$NON-NLS-1$
	
	private IEditorInput input;
	
	private PopplerJNI poppler = null;
	public int currentPage = 0;
	public int pageNumbers = 0;

	private ScrolledComposite sc;	
	public PDFPageViewer pv;
	private PDFFileOutline outline;
	private StatusLinePageSelector position = null;

	public PDFEditor() {
		super();
		System.out.println("PDFEditor.PDFEditor");
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		System.out.println("PDFEditor.init");
		super.setInput(input);
		this.input = input;
		setSite(site);
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
		poppler = new PopplerJNI();
	}

	@Override
	public void createPartControl(final Composite parent) {
		System.out.println("PDFEditor::createPartControl()");
		parent.setLayout(new FillLayout());
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		pv = new PDFPageViewer(sc, this, poppler);
		sc.setContent(pv);
		// Speed up scrolling when using a wheel mouse
		ScrollBar vBar = sc.getVerticalBar();
		vBar.setIncrement(10);

		IStatusLineManager statusLineM = getEditorSite().getActionBars().getStatusLineManager();
		IContributionItem[] items = statusLineM.getItems();
		for (IContributionItem item : items) {
			if (PDFPOSITION_ID.equals(item.getId())) {
				position = (StatusLinePageSelector) item;
				position.setPageChangeListener(this);
			}
		}
		if (position == null) {
			position = new StatusLinePageSelector(PDFPOSITION_ID, 15);
			position.setPageChangeListener(this);
			statusLineM.add(position);
		}
		position.setPageInfo(1, 1);

		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		
		initKeyBindingContext();
		
		setInput(input);
	}
	
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		try {
			readPdfFile();
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		pv.init();
	}
	
	public void readPdfFile() throws PartInitException {
		System.out.println("PDFEditor::readPdfFile()");
		IEditorInput input = getEditorInput();
		String pathname = null;
		URI uri = null;
		if(input instanceof FileStoreEditorInput) {
			uri = ((FileStoreEditorInput)input).getURI();
		} else if((input instanceof IFileEditorInput)) {
			uri = ((IFileEditorInput) input).getFile().getLocationURI();
		}
		if(uri != null) {
			pathname = uri.toString();
			if(poppler != null) {
				poppler.document_close();
			}
	    	//poppler.document_new_from_file("file:///home/derek/Data Check and Restore Manual.pdf", null);
			poppler.document_new_from_file(pathname, null);
			pageNumbers = poppler.document_get_n_pages();
		}
		currentPage = -1;
	}

	private void initKeyBindingContext() {
		System.out.println("PDFEditor::initKeyBindingContext()\n");
		final IContextService service = (IContextService)getSite().getService(IContextService.class);

		pv.addFocusListener(new FocusListener() {
			IContextActivation currentContext = null;
			public void focusGained(FocusEvent e) {
				if (currentContext == null) {
					currentContext = service.activateContext(CONTEXT_ID);
				}
			}

			public void focusLost(FocusEvent e) {
				if (currentContext != null) {
					service.deactivateContext(currentContext);
					currentContext = null;
				}
			}
		});
	}	
	
	@Override
	public void dispose() {
		super.dispose();
		
		System.out.println("PDFEditor::dispose()");

		if (sc != null) sc.dispose();
		if (pv != null) pv.dispose();
		if (outline != null) outline.dispose();
		
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		if (position != null) position.removePageChangeListener(this);

    	poppler.document_release_page();
    	poppler.document_close();
		poppler = null;
		
		position = null;
		outline = null;
		pv = null;
		sc = null;
	}

	@Override
	public void pageChange(int pageNr) {
		System.out.println("PDFEditor::pageChange()\n");
		showPage(pageNr);
		pv.setOrigin(sc.getOrigin().x, 0);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		System.out.println("PDFEditor::resourceChanged()");
		if(event.getType() == IResourceChangeEvent.POST_CHANGE) {
			try {
				if (!(getEditorInput() instanceof IFileEditorInput)) {
					return;
				}

				final IFile currentfile = ((IFileEditorInput) getEditorInput()).getFile();
				if (event.getDelta().findMember(currentfile.getFullPath()) != null) {
					readPdfFile();
					final OutlineNode n = poppler.getOutline();
					Display.getDefault().asyncExec(new Runnable() {										
						@Override
						public void run() {
							if (pv != null && !pv.isDisposed()) {
								showPage(currentPage);
								if (outline != null) { 
									outline.setInput(n);		
								}
								pv.redraw();
							}
						}
					});
				}
			} 
			catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}				
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		System.out.println("PDFEditor::doSave()");
	}
	@Override
	public void doSaveAs() {
		System.out.println("PDFEditor::doSaveAs()");
	}
	@Override
	public boolean isDirty() {
		System.out.println("PDFEditor::isDirty()");
		return false;
	}
	@Override
	public boolean isSaveAsAllowed() {
		System.out.println("PDFEditor::isSaveAsAllowed()");
		return false;
	}

	public void reverseSearch(double pdfX, double pdfY) {
		System.out.println("PDFEditor::reverseSearch()\n");
		String path="";
		IFileStore fileStore = EFS.getLocalFileSystem().fromLocalFile(new File(path));
		if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
			IWorkbenchPage page=  this.getSite().getPage();
			try {
				IEditorPart part = IDE.openEditorOnFileStore(page, fileStore);
				if (part instanceof AbstractTextEditor) {
					AbstractTextEditor t = (AbstractTextEditor) part;
					IDocument doc = t.getDocumentProvider().getDocument(t.getEditorInput());
					t.selectAndReveal(doc.getLineOffset(2 - 1), doc.getLineLength(2 - 1));
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				writeStatusLineError(NLS.bind("Messages.PDFEditor_SynctexMsg3", 2 - 1));
			}
		} else {
			writeStatusLineError(NLS.bind("Messages.PDFEditor_SynctexMsg4", path));
		}

	}
	
	public PDFPageViewer getPDFPageViewer() {
		return pv;
	}

	public void showPage(int pageNr) {
		System.out.println("PDFEditor::showPage()\n");
		if (pageNr < 1) pageNr = 1;
		if (pageNr > pageNumbers) pageNr = pageNumbers;
		//if(currentPage != pageNr)
		//{
			if(currentPage != -1)
			{
				poppler.document_release_page();
			}
			poppler.document_get_page(pageNr);
			currentPage = pageNr;
			pv.showPage(pageNr);
			updateStatusLine();
		//}
	}

	@Override
	public void setFocus() {
		System.out.println("PDFEditor::setFocus()");
		sc.setFocus();
		updateStatusLine();
		position.setPageChangeListener(this);
	}

	/**
	 * Shows the given page and reveals the destination
	 * @param dest
	 */
	public void gotoAction(PDFDestination dest) {
		System.out.println("PDFEditor::gotoAction()\n");
		int page = dest.getPage();
		if (page == -1) {
			return;
		}

		IWorkbenchPage wpage = getSite().getPage();
		wpage.getNavigationHistory().markLocation(this);

		showPage(page);

		Rectangle2D re = pv.convertPDF2ImageCoord(new Rectangle((int)Math.round(dest.getLeft()), (int)Math.round(dest.getTop()), 1, 1));
		int x = sc.getOrigin().x;
		if (re.getX() < sc.getOrigin().x) x = (int)Math.round(re.getX() - 10);
		pv.setOrigin(x, (int)Math.round(re.getY() - sc.getBounds().height / 4.));

		wpage.getNavigationHistory().markLocation(this);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {
		System.out.println("PDFEditor::getAdapter()\n");
		if (IContentOutlinePage.class.equals(required)) {
			if (outline == null) {
				OutlineNode n = poppler.getOutline();
				if (n == null) return null;
				outline = new PDFFileOutline(this);
				outline.setInput(n);
			}
			else return outline;
		}
		return super.getAdapter(required);
	}

	@Override
	public INavigationLocation createEmptyNavigationLocation() {
		// TODO Auto-generated method stub
		System.out.println("PDFEditor::createEmptyNavigationLocation()\n");
		return null;
	}

	@Override
	public INavigationLocation createNavigationLocation() {
		System.out.println("PDFEditor::createNavigationLocation()\n");
		return new PDFNavigationLocation(this);
	}

	private void updateStatusLine() {
		System.out.println("PDFEditor::updateStatusLine()\n");
		position.setPageInfo(currentPage, pageNumbers);
	}

	public void fitHorizontal() {
		System.out.println("PDFEditor::fitHorizontal()\n");
		pv.fitHorizontal();
	}

	public void fit() {
		System.out.println("PDFEditor::fit()\n");
		pv.fit();
	}

	/**
	 * Writes an error message to the status line and deletes it after five seconds.
	 * @param text
	 */
	public void writeStatusLineError(String text) {
		System.out.println("PDFEditor::writeStatusLineError()\n");
		final IStatusLineManager statusLineM = getEditorSite().getActionBars().getStatusLineManager();
		statusLineM.setErrorMessage(text);
		//FIXME: Should not be executed if there was another message in between the five secs.
		Display.getDefault().timerExec(5000, new Runnable() {

			@Override
			public void run() {
				statusLineM.setErrorMessage("");				 //$NON-NLS-1$
			}
		});
	}

	public Point getOrigin() {
		System.out.println("PDFEditor::getOrigin()\n");
		if (!sc.isDisposed()) return sc.getOrigin();
		else return null;
	}

	public void setOrigin(Point p) {
		System.out.println("PDFEditor::setOrigin()\n");
		sc.setRedraw(false);
		if (p != null) sc.setOrigin(p);
		sc.setRedraw(true);
	}

}
