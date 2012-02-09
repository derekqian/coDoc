/*******************************************************************************
 * Copyright (c) 2011 Boris von Loesch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Boris von Loesch - initial API and implementation
 ******************************************************************************/
package edu.pdx.svl.coDoc.editors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;


import com.sun.pdfview.ImageInfo;
import com.sun.pdfview.PDFRenderer;
import com.sun.pdfview.RefImage;
import com.sun.pdfview.Watchable;
import com.sun.pdfview.action.GoToAction;
import com.sun.pdfview.action.UriAction;
import com.sun.pdfview.annotation.LinkAnnotation;
import com.sun.pdfview.annotation.PDFAnnotation;

import edu.pdx.svl.coDoc.handlers.ToggleLinkHighlightHandler;
import edu.pdx.svl.coDoc.poppler.PopplerJNI;


/**
 * SWT Canvas which shows a whole pdf-page. It also handles click on links.
 * Since the pdf library returns an awt BufferedImage, we need to convert it
 * to an SWT image. This was avoided in {@link PDFPageViewerAWT}.
 * 
 * @author Boris von Loesch
 *
 */
public class PDFPageViewer extends Canvas implements PaintListener, IPreferenceChangeListener{
    
    /** The current PDFPage that was rendered into currentImage */
    public int currentPageNum;
    /** the current transform from device space to page space */
    AffineTransform currentXform;
    /** The horizontal offset of the image from the left edge of the panel */
    int offx;
    /** The vertical offset of the image from the top of the panel */
    int offy;
    /** the size of the image */
    Dimension prevSize;
    
    private boolean highlightLinks;
    private Rectangle2D highlight;
    
    private Display display;
    private PopplerJNI poppler;
    
    private float zoomFactor;
    
    private org.eclipse.swt.graphics.Image swtImage;

    /**
     * Create a new PagePanel.
     */
    public PDFPageViewer(Composite parent, final CDCEditor editor) {
        //super(parent, SWT.NO_BACKGROUND|SWT.NO_REDRAW_RESIZE);
    	//super(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE);
    	super(parent, SWT.NO_BACKGROUND);

    	this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
			}
			
			@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				
				if (e.button != 1) return;
				
				//derek List<PDFAnnotation> annos = getPage().getAnnots(PDFAnnotation.LINK_ANNOTATION);
				List<PDFAnnotation> annos = null;
            	for (PDFAnnotation a : annos) {
            		LinkAnnotation aa = (LinkAnnotation) a;
            		Rectangle2D r = convertPDF2ImageCoord(aa.getRect());
            		if (r.contains(e.x, e.y)) {
            			if (aa.getAction() instanceof GoToAction){
            				final GoToAction action = (GoToAction) aa.getAction();

            				Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									editor.gotoAction(action.getDestination());
								}
							});
            				return;
            			}
            			else if (aa.getAction() instanceof UriAction) {
            				final UriAction action = (UriAction) aa.getAction();
            				Display.getDefault().asyncExec(new Runnable() {
								
								@Override
								public void run() {
									
									try {
										String uri = action.getUri();
										if (uri.toLowerCase().indexOf("://") < 0) { //$NON-NLS-1$
											uri = "http://"+uri; //$NON-NLS-1$
										}
										PlatformUI.getWorkbench().getBrowserSupport()
										.createBrowser("PDFBrowser").openURL(new URL(uri)); //$NON-NLS-1$
									} catch (PartInitException e) {
										e.printStackTrace();
									} catch (MalformedURLException e) {
										editor.writeStatusLineError(e.getMessage());
									}
								}
							});
            				return;
            			}
            		}
				}
			}
			
			@Override
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				
				if (e.button != 1) return;
				
				final Rectangle2D r = convertImage2PDFCoord(new java.awt.Rectangle(e.x, e.y, 1, 1));

				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						//derek editor.reverseSearch(r.getX(), currentPageNum.getHeight() - r.getY());
					}
				});
			}
		});


    	display = parent.getDisplay();
    	poppler = editor.getPoppler();
        setSize(800, 600);
        zoomFactor = 1.f;
        this.addPaintListener(this);
        
        IEclipsePreferences prefs = (new InstanceScope()).getNode(edu.pdx.svl.coDoc.Activator.PLUGIN_ID);
		prefs.addPreferenceChangeListener(this);
		
		highlightLinks = prefs.getBoolean(ToggleLinkHighlightHandler.PREF_LINKHIGHTLIGHT_ID, true);
    }


    /**
     * Highlights the rectangle given by the upper left and lower right 
     * coordinates. The highlight is visible after the next redraw.
     * @param x
     * @param y
     * @param x2
     * @param y2
     */
    public void highlight(double x, double y, double x2, double y2) {
    	//derek Point p = poppler.page_get_size();
    	Rectangle2D r = new Double(x, /*derek currentPageNum.getHeight()*/0 - y2, x2-x, y2 - y);
    	highlight = convertPDF2ImageCoord(r);
    }

    /**
     * Stop the generation of any previous page, and draw the new one.
     * @param page the PDFPage to draw.
     */
    public void showPage(int page) {
    	poppler.document_get_page(page);
    	Point size = poppler.page_get_size();
    	
    	ImageData imgdata = new ImageData(size.x, size.y, 32, new PaletteData(0x0000FF00, 0x00FF0000, 0xFF000000));
    	poppler.page_render(imgdata.data);
    	if (swtImage != null) swtImage.dispose();
    	swtImage = new org.eclipse.swt.graphics.Image(display, imgdata);

    	
    	// stop drawing the previous page

    	// set up the new page
    	currentPageNum = page;

    	//Reset highlight
    	highlight = null;

    	boolean resize = false;
    	int newW = Math.round(zoomFactor*size.x);
    	int newH = Math.round(zoomFactor*size.y);

    	Point sz = getSize();

    	if (sz.x != newW || sz.y != newH) {
    		sz.x = newW;
    		sz.y = newH;
    		resize = true;
    	}

    	if (sz.x == 0 || sz.y == 0) return;

    	Dimension pageSize = new Dimension(size.x,size.y);

    	ImageInfo info = new ImageInfo(pageSize.width, pageSize.height, null, Color.WHITE);
    	
    	// calculate the transform from screen to page space
    	//derek currentXform = page.getInitialTransform(pageSize.width, pageSize.height, null);
    	//derek try {
    	//derek currentXform = currentXform.createInverse();
    	//derek } catch (NoninvertibleTransformException nte) {
    		//System.out.println(Messages.PDFPageViewer_Error1);
    	//derek nte.printStackTrace();
    	//derek }

    	prevSize = pageSize;

    	if (resize) {
    		//Resize triggers repaint
    		setSize(Math.round(zoomFactor*size.x), Math.round(zoomFactor*size.y));
    		redraw();
    	}
    }

    private Rectangle getRectangle(Rectangle2D r) {
    	return new Rectangle((int)Math.round(r.getX()), (int)Math.round(r.getY()), (int)Math.round(r.getWidth()), (int)Math.round(r.getHeight()));
    }
    
    public Rectangle2D convertPDF2ImageCoord(Rectangle2D r) {
    	//derek if (currentImage == null) return null;
    	int imwid = 0;//derek currentImage.getWidth(null);
    	int imhgt = 0;//derek currentImage.getHeight(null);
    	//derek AffineTransform t = currentPageNum.getInitialTransform(imwid, imhgt, null);
    	AffineTransform t = null;
    	Rectangle2D tr = t.createTransformedShape(r).getBounds2D();
    	tr.setFrame(tr.getX() + offx, tr.getY() + offy, tr.getWidth(), tr.getHeight());
    	return tr;    	
    }
    
    public Rectangle2D convertImage2PDFCoord(Rectangle2D r) {
    	//derek if (currentImage == null) return null;

    	r.setFrame(r.getX() - offx, r.getY() -offy, 1, 1);
    	Rectangle2D tr = currentXform.createTransformedShape(r).getBounds2D();
    	tr.setFrame(tr.getX(), tr.getY(), tr.getWidth(), tr.getHeight());
    	return tr;    	
    }
    
    /**
     * Sets the zoom factor and rerenders the current page.
     * @param factor 0 < factor < \infty
     */
    public void setZoomFactor(float factor) {
    	assert (factor > 0);
    	zoomFactor = factor;
    	showPage(currentPageNum);
    }
    
    /**
     * Returns the current used zoom factor
     * @return
     */
    public float getZoomFactor() {
    	return zoomFactor;
    }
    
    /**
     * Draw the image.
     */
    public void paintControl(PaintEvent event) {
    	GC g = event.gc;
        Point sz = getSize();
    	
        if (poppler.page_get_index() == -1) {
            g.setForeground(getBackground());
            g.fillRectangle(0, 0, sz.x, sz.y);
            g.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
            g.drawString("currentImage == NULL", sz.x / 2 - 30, sz.y / 2);
        } else {
        	Point size = poppler.page_get_size();
        	
            // draw the image
            int imwid = size.x;
            int imhgt = size.y;

            // draw it centered within the panel
            offx = (sz.x - imwid) / 2;
            offy = (sz.y - imhgt) / 2;

            if ((imwid == sz.x && imhgt <= sz.y) ||
                    (imhgt == sz.y && imwid <= sz.x)) {
            	
            	if (swtImage != null) g.drawImage(swtImage, offx, offy);

            	if (highlightLinks) {
            		//derek List<PDFAnnotation> anno = currentPageNum.getAnnots(PDFAnnotation.LINK_ANNOTATION);
            		List<PDFAnnotation> anno = null;
            		g.setForeground(display.getSystemColor(SWT.COLOR_RED));
            		for (PDFAnnotation a : anno) {
            			Rectangle r = getRectangle(convertPDF2ImageCoord(a.getRect()));
            			g.drawRectangle(r);
            		}
            	}
            	//Draw highlight frame
            	if (highlight != null) {
                	g.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
            		g.drawRectangle(getRectangle(highlight));
            	}

            } else {
                // the image is bogus.  try again, or give up.
                if (currentPageNum != -1) {
                    showPage(currentPageNum);
                }
                g.setForeground(getBackground());
                g.fillRectangle(0, 0, sz.x, sz.y);                
                g.setForeground(display.getSystemColor(SWT.COLOR_RED));
                g.drawLine(0, 0, sz.x, sz.y);
                g.drawLine(0, sz.y, sz.x, 0);
            }
        }
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
    	if (ToggleLinkHighlightHandler.PREF_LINKHIGHTLIGHT_ID.equals(event.getKey())) {
    		highlightLinks = Boolean.parseBoolean((String)(event.getNewValue()));
    		redraw();
    	}
    }

    @Override
    public void dispose() {
    	super.dispose();

    	if (swtImage != null) swtImage.dispose();
    	
    	//IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(de.vonloesch.pdf4eclipse.Activator.PLUGIN_ID);
    	IEclipsePreferences prefs = (new InstanceScope()).getNode(edu.pdx.svl.coDoc.Activator.PLUGIN_ID);
    	prefs.removePreferenceChangeListener(this);
    }
}