package org.napile.idea.plugin.editor.hierarchy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.compiler.lang.psi.NapileClass;
import org.napile.compiler.lang.psi.NapileFile;
import com.intellij.codeInsight.TargetElementUtilBase;
import com.intellij.ide.hierarchy.HierarchyBrowser;
import com.intellij.ide.hierarchy.HierarchyProvider;
import com.intellij.ide.hierarchy.TypeHierarchyBrowserBase;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * Base from IDEA CE
 */
public class NapileTypeHierarchyProvider implements HierarchyProvider
{
	@Nullable
	@Override
	public PsiElement getTarget(@NotNull DataContext dataContext)
	{
		final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
		if(project == null)
			return null;

		final Editor editor = PlatformDataKeys.EDITOR.getData(dataContext);
		if(editor != null)
		{
			final PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
			if(file == null)
				return null;

			final PsiElement targetElement = TargetElementUtilBase.findTargetElement(editor, TargetElementUtilBase.ELEMENT_NAME_ACCEPTED |
					TargetElementUtilBase.REFERENCED_ELEMENT_ACCEPTED |
					TargetElementUtilBase.LOOKUP_ITEM_ACCEPTED);
			if(targetElement instanceof NapileClass)
			{
				return targetElement;
			}

			final int offset = editor.getCaretModel().getOffset();
			PsiElement element = file.findElementAt(offset);
			while(element != null)
			{
				if(element instanceof NapileFile)
				{
					final NapileClass[] classes = ((NapileFile) element).getDeclarations();
					return classes.length == 1 ? classes[0] : null;
				}
				if(element instanceof NapileClass)
				{
					return element;
				}
				element = element.getParent();
			}

			return null;
		}
		else
		{
			final PsiElement element = LangDataKeys.PSI_ELEMENT.getData(dataContext);
			return element instanceof NapileClass ? (NapileClass) element : null;
		}
	}

	@NotNull
	@Override
	public HierarchyBrowser createHierarchyBrowser(PsiElement target)
	{
		return new NapileTypeHierarchyBrowser(target.getProject(), target);
	}

	@Override
	public void browserActivated(@NotNull HierarchyBrowser hierarchyBrowser)
	{
		final NapileTypeHierarchyBrowser browser = (NapileTypeHierarchyBrowser)hierarchyBrowser;

		browser.changeView(TypeHierarchyBrowserBase.TYPE_HIERARCHY_TYPE);
	}
}
