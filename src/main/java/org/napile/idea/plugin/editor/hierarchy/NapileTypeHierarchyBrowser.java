package org.napile.idea.plugin.editor.hierarchy;

import java.util.Comparator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapilePsiUtil;
import org.napile.idea.plugin.editor.hierarchy.tree.NapileSubtypesHierarchyTreeStructure;
import org.napile.idea.plugin.editor.hierarchy.tree.NapileSupertypesHierarchyTreeStructure;
import org.napile.idea.plugin.editor.hierarchy.tree.NapileTypeHierarchyNodeDescriptor;
import org.napile.idea.plugin.editor.hierarchy.tree.NapileTypeHierarchyTreeStructure;
import org.napile.idea.plugin.editor.hierarchy.util.NapileSourceComparator;
import com.intellij.ide.hierarchy.HierarchyBrowserManager;
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.ide.hierarchy.TypeHierarchyBrowserBase;
import com.intellij.ide.util.treeView.AlphaComparator;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.ui.PopupHandler;

/**
 * @author VISTALL
 * @since 8:36/19.04.13
 */
public class NapileTypeHierarchyBrowser extends TypeHierarchyBrowserBase
{
	public NapileTypeHierarchyBrowser(Project project, PsiElement element)
	{
		super(project, element);
	}

	@Override
	protected boolean isInterface(PsiElement psiElement)
	{
		return false;
	}

	@Override
	protected boolean canBeDeleted(PsiElement psiElement)
	{
		return true;
	}

	@Override
	protected String getQualifiedName(PsiElement psiElement)
	{
		return NapilePsiUtil.getFQName((NapileClass) psiElement).toString();
	}

	@Nullable
	@Override
	protected PsiElement getElementFromDescriptor(@NotNull HierarchyNodeDescriptor descriptor)
	{
		return descriptor instanceof NapileTypeHierarchyNodeDescriptor ? ((NapileTypeHierarchyNodeDescriptor) descriptor).getPsiClass() : null;
	}

	@Override
	protected void createTrees(@NotNull Map<String, JTree> trees)
	{
		ActionGroup group = (ActionGroup) ActionManager.getInstance().getAction(IdeActions.GROUP_TYPE_HIERARCHY_POPUP);
		final BaseOnThisTypeAction baseOnThisTypeAction = new BaseOnThisTypeAction();
		final JTree tree1 = createTree(true);
		PopupHandler.installPopupHandler(tree1, group, ActionPlaces.TYPE_HIERARCHY_VIEW_POPUP, ActionManager.getInstance());
		baseOnThisTypeAction.registerCustomShortcutSet(ActionManager.getInstance().getAction(IdeActions.ACTION_TYPE_HIERARCHY).getShortcutSet(), tree1);
		trees.put(TYPE_HIERARCHY_TYPE, tree1);

		final JTree tree2 = createTree(true);
		PopupHandler.installPopupHandler(tree2, group, ActionPlaces.TYPE_HIERARCHY_VIEW_POPUP, ActionManager.getInstance());
		baseOnThisTypeAction.registerCustomShortcutSet(ActionManager.getInstance().getAction(IdeActions.ACTION_TYPE_HIERARCHY).getShortcutSet(), tree2);
		trees.put(SUPERTYPES_HIERARCHY_TYPE, tree2);

		final JTree tree3 = createTree(true);
		PopupHandler.installPopupHandler(tree3, group, ActionPlaces.TYPE_HIERARCHY_VIEW_POPUP, ActionManager.getInstance());
		baseOnThisTypeAction.registerCustomShortcutSet(ActionManager.getInstance().getAction(IdeActions.ACTION_TYPE_HIERARCHY).getShortcutSet(), tree3);
		trees.put(SUBTYPES_HIERARCHY_TYPE, tree3);
	}

	@Nullable
	@Override
	protected JPanel createLegendPanel()
	{
		return null;
	}

	@Override
	protected boolean isApplicableElement(@NotNull PsiElement element)
	{
		return element instanceof NapileClass;
	}

	@Nullable
	@Override
	protected HierarchyTreeStructure createHierarchyTreeStructure(@NotNull String type, @NotNull PsiElement psiElement)
	{
		if(SUPERTYPES_HIERARCHY_TYPE.equals(type))
		{
			return new NapileSupertypesHierarchyTreeStructure(myProject, (NapileClass) psiElement);
		}
		else if(TYPE_HIERARCHY_TYPE.equals(type))
		{
			return new NapileTypeHierarchyTreeStructure(myProject, (NapileClass) psiElement);
		}
		else if(SUBTYPES_HIERARCHY_TYPE.equals(type))
		{
			return new NapileSubtypesHierarchyTreeStructure(myProject, (NapileClass) psiElement);
		}
		else
		{
			throw new IllegalArgumentException(type);
		}
	}

	@Nullable
	@Override
	protected Comparator<NodeDescriptor> getComparator()
	{
		if(HierarchyBrowserManager.getInstance(myProject).getState().SORT_ALPHABETICALLY)
		{
			return AlphaComparator.INSTANCE;
		}
		else
		{
			return NapileSourceComparator.INSTANCE;
		}
	}
}
