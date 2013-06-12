package org.napile.idea.plugin.codeInsight.toolWindow;

import org.napile.idea.plugin.module.extension.NapileModuleExtension;
import com.intellij.openapi.wm.ToolWindowModuleExtensionCondition;

/**
 * @author VISTALL
 * @since 16:48/12.06.13
 */
public class NapileToolWindowCondition extends ToolWindowModuleExtensionCondition
{
	public NapileToolWindowCondition()
	{
		super(NapileModuleExtension.class);
	}
}
