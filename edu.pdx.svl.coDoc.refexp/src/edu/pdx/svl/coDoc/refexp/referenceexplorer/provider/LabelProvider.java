package edu.pdx.svl.coDoc.refexp.referenceexplorer.provider;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.pdx.svl.coDoc.refexp.referencemodel.*;


public class LabelProvider extends StyledCellLabelProvider {
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		StyledString text = new StyledString();

		if (element instanceof ProjectReference) {
			ProjectReference pr = (ProjectReference) element;
			text.append(pr.description());
			cell.setImage(PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_PROJECT));
			text.append(" ( " +pr.getChildrenList().size() + " ) ", StyledString.COUNTER_STYLER);
		} else if (element instanceof SourceFileReference) {
			SourceFileReference sfr = (SourceFileReference) element;
			text.append(sfr.description());
			cell.setImage(PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE));
			text.append(" ( " +sfr.getChildrenList().size() + " ) ", StyledString.COUNTER_STYLER);
		} else if (element instanceof TextSelectionReference){
				TextSelectionReference tsr = (TextSelectionReference) element;
				String tsrFormatted = tsr.description();
				text.append(tsrFormatted);
				cell.setImage(PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_ELEMENT));
		}
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		super.update(cell);
	}
}