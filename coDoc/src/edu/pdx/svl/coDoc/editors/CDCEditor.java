package edu.pdx.svl.coDoc.editors;

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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFDestination;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFPage;

import edu.pdx.svl.coDoc.editors.StatusLinePageSelector.IPageChangeListener;
import edu.pdx.svl.coDoc.poppler.PopplerJNI;


public class CDCEditor extends EditorPart implements IResourceChangeListener, INavigationLocationProvider, IPageChangeListener
{

	public static final String ID = "edu.pdx.svl.coDoc.editors.CDCEditor";
	public static final String CONTEXT_ID = "PDFViewer.editors.contextid";

	public static final int FORWARD_SEARCH_OK = 0;
	public static final int FORWARD_SEARCH_NO_SYNCTEX = -1;
	public static final int FORWARD_SEARCH_FILE_NOT_FOUND = -2;
	public static final int FORWARD_SEARCH_POS_NOT_FOUND = -3;
	public static final int FORWARD_SEARCH_UNKNOWN_ERROR = -4;

	static final String PDFPOSITION_ID = "PDFPosition"; //$NON-NLS-1$
	
	public PDFPageViewer pv;
	
	private PopplerJNI poppler;

	private PDFFile f;
	private ScrolledComposite sc;
	int currentPage;
	private int pageNumbers;
	private PDFFileOutline outline;
	private StatusLinePageSelector position;

	public CDCEditor() {
		super();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if (sc != null) sc.dispose();
		if (pv != null) pv.dispose();
		if (outline != null) outline.dispose();
		
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		if (position != null) position.removePageChangeListener(this);

		f = null;
		pv = null;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		setSite(site);
		setInput(input);
		currentPage = 1;
		setPartName(input.getName());
		readPdfFile();
	}

	@Override
	public void pageChange(int pageNr) {
		showPage(pageNr);
		setOrigin(sc.getOrigin().x, 0);
	}
	
	public void readPdfFile() throws PartInitException{
		IEditorInput input = getEditorInput();
		String pathname = null;
		URI uri = null;
		if (input instanceof FileStoreEditorInput) {
			uri = ((FileStoreEditorInput)input).getURI();
		}
		else if ((input instanceof IFileEditorInput)) {
			uri = ((IFileEditorInput) input).getFile().getLocationURI();
		}
		else {
			throw new PartInitException("Messages.PDFEditor_ErrorMsg1");
		}
		pathname = uri.toString();
		poppler = null;
		poppler = new PopplerJNI();
		poppler.document_new_from_file(pathname, null);
		pageNumbers = poppler.document_get_n_pages();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}


	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if(event.getType() == IResourceChangeEvent.POST_CHANGE){
			try {

				if (!(getEditorInput() instanceof IFileEditorInput)) return;

				final IFile currentfile = ((IFileEditorInput) getEditorInput()).getFile();
				if (event.getDelta().findMember(currentfile.getFullPath()) != null){
					readPdfFile();
					final OutlineNode n = f.getOutline();
					Display.getDefault().asyncExec(new Runnable() {										
						@Override
						public void run() {
							if (pv != null && !pv.isDisposed()) {
								showPage(currentPage);
								if (outline != null) outline.setInput(n);		
								pv.redraw();
							}
						}
					});
				}
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}				
	}

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new FillLayout());
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		//ScrolledComposite sc2 = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		pv = new PDFPageViewer(sc, this);
		//pv = new PDFPageViewerAWT(sc, this);
		sc.setContent(pv);
		// Speed up scrolling when using a wheel mouse
		ScrollBar vBar = sc.getVerticalBar();
		vBar.setIncrement(10);

		pv.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				int height = sc.getClientArea().height;
				int pInc = 3* height / 4;
				int lInc = height / 20;
				int hInc = sc.getClientArea().width / 20;
				int pheight = sc.getContent().getBounds().height;
				Point p = sc.getOrigin();
				if (e.keyCode == SWT.PAGE_DOWN) {
					if (p.y < pheight - height) {
						int y = p.y + pInc;
						if (y > pheight - height) {
							y = pheight - height;
						}
						sc.setOrigin(sc.getOrigin().x, y);
					}
					else {
						//We are at the end of the page
						if (currentPage < pageNumbers) {
							showPage(currentPage + 1);
							setOrigin(sc.getOrigin().x, 0);
						}
					}
				}
				else if (e.keyCode == SWT.PAGE_UP) {
					if (p.y > 0) {
						int y = p.y - pInc;
						if (y < 0) y = 0;
						sc.setOrigin(sc.getOrigin().x, y);
					}
					else {
						//We are at the top of the page
						if (currentPage > 1) {
							showPage(currentPage - 1);
							setOrigin(sc.getOrigin().x, pheight);
						}
					}					
				}
				else if (e.keyCode == SWT.ARROW_DOWN) {
					if (p.y < pheight - height) {
						sc.setOrigin(sc.getOrigin().x, p.y + lInc);
					}					
				}
				else if (e.keyCode == SWT.ARROW_UP) {
					if (p.y > 0) {
						int y = p.y - lInc;
						if (y < 0) y = 0;
						sc.setOrigin(sc.getOrigin().x, y);
					}					
				}
				else if (e.keyCode == SWT.ARROW_RIGHT) {
					if (p.x < sc.getContent().getBounds().width - sc.getClientArea().width) {
						sc.setOrigin(p.x + hInc, sc.getOrigin().y);
					}
				}
				else if (e.keyCode == SWT.ARROW_LEFT) {
					if (p.x > 0) {
						int x = p.x - hInc;
						if (x < 0) x = 0;
						sc.setOrigin(x, sc.getOrigin().y);
					}					
				}
				else if (e.keyCode == SWT.HOME) {
					showPage(1);
					setOrigin(sc.getOrigin().x, 0);
				}
				else if (e.keyCode == SWT.END) {
					showPage(pageNumbers);
					setOrigin(sc.getOrigin().x, pheight);
				}	

			}
		});

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

		if (f != null) {
			showPage(currentPage);
		}
		initKeyBindingContext();
	}

	private void initKeyBindingContext() {
		final IContextService service = (IContextService)
				getSite().getService(IContextService.class);

		pv.addFocusListener(new FocusListener() {
			IContextActivation currentContext = null;
			public void focusGained(FocusEvent e) {
				if (currentContext == null)
					currentContext = service.activateContext(CONTEXT_ID);
			}

			public void focusLost(FocusEvent e) {
				if (currentContext != null) {
					service.deactivateContext(currentContext);
					currentContext = null;
				}
			}
		});
	}	

	/**
	 * Starts a forward search in the current pdf-editor. The editor
	 * searches for the SyncTeX file and displays the position given by the user.
	 * 
	 * @param file The TeX file 
	 * @param lineNr The line number in the TeX file
	 * @return One of {@link FORWARD_SEARCH_OK}, 
	 * 		{@link FORWARD_SEARCH_NO_SYNCTEX}, {@link FORWARD_SEARCH_FILE_NOT_FOUND},
	 * 		{@link FORWARD_SEARCH_POS_NOT_FOUND}, {@link FORWARD_SEARCH_UNKNOWN_ERROR}
	 */
	public int forwardSearch(String file, int lineNr) {
		int page = 1;
		showPage(page);
		pv.highlight(0, 0, 30, 4);
		Rectangle2D re = pv.convertPDF2ImageCoord(new Rectangle(0, 0, 1, 1));
		int x = sc.getOrigin().x;
		if (re.getX() < sc.getOrigin().x) x = (int)Math.round(re.getX() - 10);
		setOrigin(x, (int)Math.round(re.getY() - sc.getBounds().height / 4.));
		//System.out.println("Page: "+page);
		try {
			this.getSite().getPage().openEditor(this.getEditorInput(), CDCEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
			return FORWARD_SEARCH_UNKNOWN_ERROR;
		}
		this.setFocus();
		return FORWARD_SEARCH_OK;
	}

	public void reverseSearch(double pdfX, double pdfY) {
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

	private void showPage (PDFObject page) {
		try {	
			int pageNr = f.getPageNumber(page)+1;
			if (pageNr < 1) pageNr = 1;
			if (pageNr > pageNumbers) pageNr = pageNumbers;
			PDFPage pager = f.getPage(pageNr);
			currentPage = pageNr;
			pv.showPage(pager);
			updateStatusLine();
		} catch (IOException e) {
			System.err.println("Messages.PDFEditor_ErrorMsg5");
		}
	}

	public void showPage(int pageNr) {
		if (pageNr < 1) pageNr = 1;
		if (pageNr > pageNumbers) pageNr = pageNumbers;
		poppler.document_get_page(pageNr);
		currentPage = pageNr;
		pv.showPage(page);
		updateStatusLine();
	}

	@Override
	public void setFocus() {
		sc.setFocus();
		updateStatusLine();
		position.setPageChangeListener(this);
	}

	/**
	 * Shows the given page and reveals the destination
	 * @param dest
	 */
	public void gotoAction(PDFDestination dest){
		PDFObject page = dest.getPage();
		if (page == null) {
			return;
		}

		IWorkbenchPage wpage = getSite().getPage();
		wpage.getNavigationHistory().markLocation(this);

		showPage(page);

		Rectangle2D re = pv.convertPDF2ImageCoord(new Rectangle((int)Math.round(dest.getLeft()), (int)Math.round(dest.getTop()), 
				1, 1));
		int x = sc.getOrigin().x;
		if (re.getX() < sc.getOrigin().x) x = (int)Math.round(re.getX() - 10);
		setOrigin(x, (int)Math.round(re.getY() - sc.getBounds().height / 4.));

		wpage.getNavigationHistory().markLocation(this);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (outline == null) {
				try {
					OutlineNode n = f.getOutline();
					if (n == null) return null;
					outline = new PDFFileOutline(this);
					outline.setInput(n);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else return outline;
		}
		return super.getAdapter(required);
	}

	@Override
	public INavigationLocation createEmptyNavigationLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INavigationLocation createNavigationLocation() {
		return new PDFNavigationLocation(this);
	}

	private void updateStatusLine() {
		position.setPageInfo(currentPage, pageNumbers);
	}

	public void fitHorizontal() {
		int w = sc.getClientArea().width;
		pv.setZoomFactor((1.0f*w)/pv.getPage().getWidth());
	}

	public void fit() {
		float w = 1.f * sc.getClientArea().width;
		float h = 1.f * sc.getClientArea().height;
		float pw = pv.getPage().getWidth();
		float ph = pv.getPage().getHeight();
		if (w/pw < h/ph) pv.setZoomFactor(w/pw);
		else pv.setZoomFactor(h/ph);
	}

	/**
	 * Writes an error message to the status line and deletes it after five seconds.
	 * @param text
	 */
	public void writeStatusLineError(String text) {
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

	Point getOrigin() {
		if (!sc.isDisposed()) return sc.getOrigin();
		else return null;
	}

	private void setOrigin(int x, int y) {
		sc.setRedraw(false);
		sc.setOrigin(x, y);
		sc.setRedraw(true);
	}

	void setOrigin(Point p) {
		sc.setRedraw(false);
		if (p != null) sc.setOrigin(p);
		sc.setRedraw(true);
	}

}
