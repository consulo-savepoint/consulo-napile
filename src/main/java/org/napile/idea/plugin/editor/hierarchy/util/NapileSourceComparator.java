package org.napile.idea.plugin.editor.hierarchy.util;

import java.util.Comparator;

import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapileFile;
import org.napile.idea.plugin.projectView.NapileClassTreeNode;
import com.intellij.ide.projectView.impl.nodes.ProjectViewProjectNode;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.NodeDescriptor;

/**
 * @see com.intellij.ide.util.treeView.SourceComparator
 */
public class NapileSourceComparator implements Comparator<NodeDescriptor>
{
	public static final NapileSourceComparator INSTANCE = new NapileSourceComparator();

	private NapileSourceComparator()
	{
	}

	public int compare(NodeDescriptor nodeDescriptor1, NodeDescriptor nodeDescriptor2)
	{
		int weight1 = getWeight(nodeDescriptor1);
		int weight2 = getWeight(nodeDescriptor2);
		if(weight1 != weight2)
		{
			return weight1 - weight2;
		}
		if(!(nodeDescriptor1.getParentDescriptor() instanceof ProjectViewProjectNode))
		{
			if(nodeDescriptor1 instanceof PsiDirectoryNode || nodeDescriptor1 instanceof PsiFileNode)
			{
				return nodeDescriptor1.toString().compareToIgnoreCase(nodeDescriptor2.toString());
			}
			if(nodeDescriptor1 instanceof NapileClassTreeNode && nodeDescriptor2 instanceof NapileClassTreeNode)
			{
				if(((NapileClass)nodeDescriptor1.getElement()).getParent() instanceof NapileFile)
				{
					return nodeDescriptor1.toString().compareToIgnoreCase(nodeDescriptor2.toString());
				}
			}
		}
		int index1 = nodeDescriptor1.getIndex();
		int index2 = nodeDescriptor2.getIndex();
		if(index1 == index2)
			return 0;
		return index1 < index2 ? -1 : +1;
	}

	private static int getWeight(NodeDescriptor descriptor)
	{
		if(descriptor instanceof PsiDirectoryNode)
		{
			return ((PsiDirectoryNode) descriptor).isFQNameShown() ? 7 : 0;
		}
		else
		{
			return 2;
		}
	}
}
