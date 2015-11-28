package org.napile.idea.plugin.editor.hierarchy.tree;

import java.awt.Font;

import org.mustbe.consulo.RequiredDispatchThread;
import org.napile.compiler.lang.psi.NapileClass;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.util.CompositeAppearance;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.LayeredIcon;

/**
 * @author VISTALL
 * @since 8:41/19.04.13
 */
public class NapileTypeHierarchyNodeDescriptor extends HierarchyNodeDescriptor
{
	protected NapileTypeHierarchyNodeDescriptor(Project project, NodeDescriptor parentDescriptor, NapileClass element, boolean isBase)
	{
		super(project, parentDescriptor, element, isBase);
	}

	@RequiredDispatchThread
	@Override
	public final boolean update()
	{
		boolean changes = super.update();

		NapileClass napileClass = (NapileClass) getPsiElement();
		if(napileClass == null)
		{
			final String invalidPrefix = IdeBundle.message("node.hierarchy.invalid");
			if(!myHighlightedText.getText().startsWith(invalidPrefix))
			{
				myHighlightedText.getBeginning().addText(invalidPrefix, HierarchyNodeDescriptor.getInvalidPrefixAttributes());
			}
			return true;
		}

		if(changes && myIsBase)
		{
			final LayeredIcon icon = new LayeredIcon(2);
			icon.setIcon(getIcon(), 0);
			icon.setIcon(AllIcons.Hierarchy.Base, 1, -AllIcons.Hierarchy.Base.getIconWidth() / 2, 0);
			setIcon(icon);
		}

		final CompositeAppearance oldText = myHighlightedText;

		myHighlightedText = new CompositeAppearance();

		TextAttributes classNameAttributes = null;
		if(myColor != null)
		{
			classNameAttributes = new TextAttributes(myColor, null, null, null, Font.PLAIN);
		}
		myHighlightedText.getEnding().addText(napileClass.getName(), classNameAttributes);
		myHighlightedText.getEnding().addText(" (" + napileClass.getContainingFile().getPackage().getFqName() + ")", HierarchyNodeDescriptor.getPackageNameAttributes());
		myName = myHighlightedText.getText();

		if(!Comparing.equal(myHighlightedText, oldText))
		{
			changes = true;
		}
		return changes;
	}
}
