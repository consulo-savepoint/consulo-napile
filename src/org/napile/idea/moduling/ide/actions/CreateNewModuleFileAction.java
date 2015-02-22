package org.napile.idea.moduling.ide.actions;

import org.apache.velocity.runtime.parser.ParseException;
import org.napile.idea.moduling.util.NapileModulingConstants;
import com.intellij.ide.IdeView;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;

/**
 * @author VISTALL
 * @since 19:28/23.10.12
 * @see com.intellij.ide.actions.CreateFromTemplateAction
 */
@org.consulo.lombok.annotations.Logger
public class CreateNewModuleFileAction extends AnAction
{
	private static final String MODULE_FILE = "Napile Module File";

	public CreateNewModuleFileAction()
	{
		super(MODULE_FILE, MODULE_FILE, NapileModulingConstants.FILE_TYPE);
	}

	@Override
	public void actionPerformed(AnActionEvent e)
	{
		final DataContext dataContext = e.getDataContext();

		final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
		if(view == null)
			return;

		final Project project = PlatformDataKeys.PROJECT.getData(dataContext);

		final PsiDirectory dir = view.getOrChooseDirectory();
		if(dir == null || project == null)
			return;

		createFileFromTemplate(dir);
	}

	protected void createFileFromTemplate(PsiDirectory dir)
	{
		FileTemplate template = FileTemplateManager.getInstance().getInternalTemplate(NapileModulingConstants.MODULE_FILE_TEMPLATE_NAME);
		if(template == null)
		{
			Messages.showErrorDialog(dir.getProject(), "Module file template not found", MODULE_FILE);
			return;
		}

		PsiFile oldFile = dir.findFile(NapileModulingConstants.MODULE_FILE_NAME);
		if(oldFile != null)
		{
			Messages.showErrorDialog(dir.getProject(), "File exists", MODULE_FILE);
			return;
		}

		PsiElement element;
		Project project = dir.getProject();
		try
		{
			element = FileTemplateUtil.createFromTemplate(template, NapileModulingConstants.MODULE_FILE_TEMPLATE_NAME, FileTemplateManager.getInstance().getDefaultProperties(project), dir);
			final PsiFile psiFile = element.getContainingFile();

			final VirtualFile virtualFile = psiFile.getVirtualFile();
			if(virtualFile != null)
				FileEditorManager.getInstance(project).openFile(virtualFile, true);
		}
		catch(ParseException e)
		{
			Messages.showErrorDialog(project, "Error parsing Velocity template: " + e.getMessage(), MODULE_FILE);
		}
		catch(IncorrectOperationException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			LOGGER.error(e);
		}
	}
}
