package com.vone.medisafe.warehouse;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class GroupItemListener implements EventListener{

	public GroupItemListener() {
		super();
	}

	public boolean isAsap() {
		return true;
	}

	public void onEvent(Event arg0) {
		Treecell tc = (Treecell) arg0.getTarget();
		Treerow treerow = (Treerow) tc.getParent();
		
		Treeitem treeitem = (Treeitem) treerow.getParent();
		boolean ceked = !treeitem.isSelected();
		treeitem.setSelected(ceked);
		
		List list1 = treeitem.getChildren();
		Iterator iter1 = list1.iterator();
		while (iter1.hasNext()) {
			Object obj = iter1.next();
			if(obj instanceof Treechildren){
				Treechildren treeChildren = (Treechildren)obj;
				List list2 = treeChildren.getChildren();
				Iterator iter2 = list2.iterator();
				while (iter2.hasNext()) {
					obj = iter2.next();
					treeitem =  (Treeitem) obj;
					treeitem.setSelected(ceked);
				}
			}
		}
	}
}
