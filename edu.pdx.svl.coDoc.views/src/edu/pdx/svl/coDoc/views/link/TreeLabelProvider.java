package edu.pdx.svl.coDoc.views.link;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.pdx.svl.coDoc.cdc.datacenter.BaseEntry;
import edu.pdx.svl.coDoc.cdc.datacenter.EntryNode;
import edu.pdx.svl.coDoc.cdc.datacenter.CategoryEntry;
import edu.pdx.svl.coDoc.cdc.datacenter.LinkEntry;


public class TreeLabelProvider extends StyledCellLabelProvider {
	@Override
	public void update(ViewerCell cell) {
		EntryNode node = (EntryNode) cell.getElement();
		BaseEntry entry = (BaseEntry) node.getData();
		StyledString text = new StyledString();

		if (entry instanceof CategoryEntry) {
			cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			text.append(((CategoryEntry) entry).name);
			text.append("[" + node.getChildren().length + "]", StyledString.COUNTER_STYLER);
		} else if (entry instanceof LinkEntry) {
			cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT));
			text.append("valid", StyledString.QUALIFIER_STYLER);
			//text.append("invalid", StyledString.DECORATIONS_STYLER);
		}
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		super.update(cell);
	}
}