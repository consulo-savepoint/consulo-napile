package org.napile.idea.plugin.module.extension;

import javax.swing.Icon;

import org.consulo.module.extension.ModuleExtensionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.napile.idea.plugin.NapileIcons;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 12:25/27.05.13
 */
public class NapileModuleExtensionProvider implements ModuleExtensionProvider<NapileModuleExtension, NapileMutableModuleExtension>
{
	@Nullable
	@Override
	public Icon getIcon()
	{
		return NapileIcons.LOGO_16_16;
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Napile";
	}

	@NotNull
	@Override
	public Class<NapileModuleExtension> getImmutableClass()
	{
		return NapileModuleExtension.class;
	}

	@NotNull
	@Override
	public NapileModuleExtension createImmutable(@NotNull String s, @NotNull Module module)
	{
		return new NapileModuleExtension(s, module);
	}

	@NotNull
	@Override
	public NapileMutableModuleExtension createMutable(@NotNull String s, @NotNull Module module, @NotNull NapileModuleExtension moduleExtension)
	{
		return new NapileMutableModuleExtension(s, module, moduleExtension);
	}
}
