package org.napile.idea.plugin.module.extension;

import org.consulo.module.extension.impl.ModuleExtensionImpl;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 12:24/27.05.13
 */
public class NapileModuleExtension extends ModuleExtensionImpl<NapileModuleExtension>
{
	public NapileModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}
}
